package com.donts.blog.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import com.alicp.jetcache.anno.Cached;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.donts.blog.entity.*;
import com.donts.blog.mapper.ArticleMapper;
import com.donts.blog.service.*;
import com.donts.enums.ArticleStatusEnum;
import com.donts.exception.BlogBizException;
import com.donts.helper.DtoAndVoHelper;
import com.donts.response.PageResult;
import com.donts.response.UnifiedResp;
import com.donts.vo.ArticleCardVO;
import com.donts.vo.ArticleVO;
import com.donts.vo.TopAndFeaturedArticlesVO;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

import static com.donts.consts.CacheNameConst.*;
import static com.donts.enums.RespStatus.ARTICLE_ACCESS_FAIL;
import static com.donts.enums.RespStatus.ARTICLE_DETAIL_FAIL;
import static com.donts.helper.DtoAndVoHelper.convertArticleToArticleCardVO;
import static com.donts.helper.DtoAndVoHelper.convertArticleToArticleVO;

/**
 * @author djy12
 * @description 针对表【t_article】的数据库操作Service实现
 * @createDate 2024-03-10 20:18:55
 */
@Service
@Slf4j
public class ArticleServiceImpl extends ServiceImpl<ArticleMapper, Article>
        implements ArticleService {
    private static final String LOG_PREFIX = "ArticleServiceImpl";

    @Resource
    private ArticleMapper articleMapper;

    @Resource
    private UserInfoService userInfoService;
    @Resource
    private CategoryService categoryService;
    @Resource
    private TagService tagService;

    @Resource
    private RedisTemplate<String, Object> redisTemplate;
    @Resource
    private ArticleTagService articleTagService;

    @Resource
    @Lazy
    private ArticleServiceImpl articleServiceProxy;

    @Override
    @Cached(name = INDEX_TOP_AND_RECOMMEND_ARTICLE_CACHE_NAME, expire = 24 * 60 * 60)
    public TopAndFeaturedArticlesVO listTopAndFeaturedArticles() {
        List<Article> articles = articleMapper.listTopAndFeaturedArticles();
        List<Long> userIds = articles.stream().map(Article::getUserId).collect(Collectors.toList());
        Map<Long, UserInfo> userInfoMap = userInfoService.listUserInfoMapByUserIds(userIds);

        List<ArticleCardVO> articleCardVOList = new ArrayList<>();
        // 使用原子引用类型，因为在lambda表达式中无法直接修改局部变量
        AtomicReference<ArticleCardVO> topArticle = new AtomicReference<>();

        articles.forEach(article ->
        {
            ArticleCardVO articleCardVO = DtoAndVoHelper.convertArticleToArticleCardVO(article);
            articleCardVO.setAuthor(userInfoMap.get(article.getUserId()));

            if (Boolean.TRUE.equals(articleCardVO.getIsTop())) {
                topArticle.set(articleCardVO);
            } else {
                articleCardVOList.add(articleCardVO);
            }
        });

        return new TopAndFeaturedArticlesVO(topArticle.get(), articleCardVOList);
    }

    @Override
    public PageResult<ArticleCardVO> pageArticles(Integer page, Integer size) {
        Page<Article> articlePage = new Page<>(page, size);
        articlePage = articleMapper.selectPage(articlePage, new LambdaQueryWrapper<Article>()
                .in(Article::getStatus, ArticleStatusEnum.PUBLIC.getCode(), ArticleStatusEnum.PRIVATE.getCode())
                .orderByDesc(Article::getCreateTime));
        // 第一步：获取文章基本信息
        return convertArticlePageToPageResult(articlePage);
    }

    @Override
    public PageResult<ArticleCardVO> pageArticlesByCategoryId(Long categoryId, Integer page, Integer size) {
        Page<Article> articlePage = new Page<>(page, size);
        articlePage = articleMapper.selectPage(articlePage, new LambdaQueryWrapper<Article>()
                .eq(Article::getCategoryId, categoryId)
                .in(Article::getStatus, ArticleStatusEnum.PUBLIC.getCode(), ArticleStatusEnum.PRIVATE.getCode())
                .orderByDesc(Article::getCreateTime));
        return convertArticlePageToPageResult(articlePage);
    }

    @Override
    public UnifiedResp<ArticleVO> findArticleById(Long articleId) {
        Article articleForCheck = articleMapper.selectById(articleId);
        if (Objects.isNull(articleForCheck)) {
            UnifiedResp.fail("文章不存在");
        }
        if (articleForCheck.getStatus().equals(ArticleStatusEnum.PRIVATE.getCode())) {
            Boolean isAccess;
            isAccess = redisTemplate.opsForSet().isMember(PRIVATE_ARTICLE_ACCESSIBLE_USER_CACHE_NAME + articleId,
                    StpUtil.getLoginIdAsString());
            if (Boolean.FALSE.equals(isAccess)) {
                return UnifiedResp.fail(ARTICLE_ACCESS_FAIL);
            }
        }
        updateArticleViewsCount(articleId);
        //TODO 配置线程池，避免使用commonPool
        CompletableFuture<ArticleVO> asyncArticle =
                CompletableFuture.supplyAsync(() -> articleServiceProxy.getArticleVOFromDB(articleId));
        CompletableFuture<ArticleCardVO> asyncPreArticle = CompletableFuture.supplyAsync(() ->
        {
            Long preArticleId = articleMapper.getPreArticleId(articleId);
            if (Objects.isNull(preArticleId)) {
                return null;
            }
            return articleServiceProxy.getArticleCardVOFromDB(preArticleId);
        });
        CompletableFuture<ArticleCardVO> asyncNextArticle = CompletableFuture.supplyAsync(() ->
        {
            Long nextArticleId = articleMapper.getNextArticleId(articleId);
            if (Objects.isNull(nextArticleId)) {
                return null;
            }
            return articleServiceProxy.getArticleCardVOFromDB(nextArticleId);
        });
        CompletableFuture.allOf(asyncArticle, asyncPreArticle, asyncNextArticle)
                .exceptionally(e ->
                {
                    log.error("{} 获取文章详情失败", LOG_PREFIX, e);
                    throw new BlogBizException(ARTICLE_DETAIL_FAIL);
                }).join();
        ArticleVO article = asyncArticle.join();
        if (Objects.isNull(article)) {
            return UnifiedResp.fail(ARTICLE_DETAIL_FAIL);
        }
        Double score = redisTemplate.opsForZSet().score(ARTICLE_VIEWS_COUNT, articleId);
        if (Objects.nonNull(score)) {
            article.setViewCount(score.intValue());
        }
        article.setPreArticleCard(asyncPreArticle.join());
        article.setNextArticleCard(asyncNextArticle.join());
        return UnifiedResp.success(article);
    }

    private void updateArticleViewsCount(Long articleId) {
        redisTemplate.opsForZSet().incrementScore(ARTICLE_VIEWS_COUNT, articleId, 1D);
    }

    @Transactional(readOnly = true)
    @Cached(name = ARTICLE_VO_CACHE_NAME, key = "#articleId", expire = 24 * 60 * 60)
    public ArticleVO getArticleVOFromDB(Long articleId) {
        // 查询文章信息
        Article article = articleMapper.selectOne(new LambdaQueryWrapper<Article>()
                .eq(Article::getId, articleId)
                .eq(Article::getIsDelete, 0)
                .in(Article::getStatus, 1, 2));
        if (article == null) {
            return null;
        }

        // 查询用户信息
        UserInfo userInfo = userInfoService.getById(article.getUserId());

        // 查询分类信息
        Category category = categoryService.getById(article.getCategoryId());

        // 查询标签信息
        List<ArticleTag> articleTags = articleTagService.list(new LambdaQueryWrapper<ArticleTag>()
                .eq(ArticleTag::getArticleId, articleId));
        List<Long> tagIds = articleTags.stream().map(ArticleTag::getTagId).collect(Collectors.toList());
        List<Tag> tags = tagService.listByIds(tagIds);

        // 构建VO
        ArticleVO articleVO = convertArticleToArticleVO(article);
        if (userInfo != null) {
            articleVO.setAuthor(userInfo);
        }
        if (category != null) {
            articleVO.setCategoryName(category.getCategoryName());
        }
        if (!CollectionUtils.isEmpty(tags)) {
            articleVO.setTags(tags);
        }

        return articleVO;
    }

    @Transactional(readOnly = true)
    @Cached(name = ARTICLE_CARD_VO_CACHE_NAME, key = "#articleId", expire = 24 * 60 * 60)
    public ArticleCardVO getArticleCardVOFromDB(Long articleId) {
        Article article = articleMapper.selectById(articleId);
        if (article == null) {
            return null;
        }
        ArticleCardVO articleCardVO = convertArticleToArticleCardVO(article);
        fillTagAndCategory(article, articleCardVO);
        return articleCardVO;
    }

    private PageResult<ArticleCardVO> convertArticlePageToPageResult(Page<Article> articlePage) {
        List<Article> articles = articlePage.getRecords();

        // 第二步：构造ArticleCardVO列表，遍历文章集合并填充相关联的数据
        List<ArticleCardVO> list = articles.stream().map(article ->
        {
            ArticleCardVO articleCardVO = convertArticleToArticleCardVO(article);
            fillTagAndCategory(article, articleCardVO);

            return articleCardVO;
        }).toList();
        return PageResult.of(list,
                articlePage.getTotal(),
                articlePage.getPages(),
                articlePage.getCurrent(),
                articlePage.getSize()
        );
    }

    private void fillTagAndCategory(Article article, ArticleCardVO articleCardVO) {
        // 查询并设置作者信息
        UserInfo user = userInfoService.getById(article.getUserId());
        if (user != null) {
            articleCardVO.setAuthor(user);
        }
        // 查询并设置分类信息
        Category category = categoryService.getById(article.getCategoryId());
        if (category != null) {
            articleCardVO.setCategoryName(category.getCategoryName());
        }

        // 查询并设置标签信息（假设一篇文章可能有多个标签）
        List<Tag> tags = tagService.listTagsByArticleId(article.getId());
        articleCardVO.setTags(tags);
    }

}




