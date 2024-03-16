package com.donts.blog.article.impl;

import cn.dev33.satoken.stp.StpUtil;
import com.alicp.jetcache.anno.CachePenetrationProtect;
import com.alicp.jetcache.anno.Cached;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.donts.blog.article.ArticleService;
import com.donts.blog.article.ArticleTagService;
import com.donts.blog.article.CategoryService;
import com.donts.blog.article.TagService;
import com.donts.blog.entity.*;
import com.donts.blog.mapper.ArticleMapper;
import com.donts.blog.role.ArticleAccessTokenGenerator;
import com.donts.blog.service.UserInfoService;
import com.donts.dto.ArticlePasswordDTO;
import com.donts.enums.ArticleStatusEnum;
import com.donts.exception.BlogBizException;
import com.donts.helper.DtoAndVoHelper;
import com.donts.response.PageResult;
import com.donts.response.UnifiedResp;
import com.donts.vo.ArticleCardVO;
import com.donts.vo.ArticleVO;
import com.donts.vo.TimeLineArticleVO;
import com.donts.vo.TopAndFeaturedArticlesVO;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

import static com.donts.consts.CacheNameConst.*;
import static com.donts.enums.RespStatus.*;
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

    @Resource(name = "stringObjectRedisTemplate")
    private RedisTemplate<String, Object> redisTemplate;
    @Resource
    private ArticleTagService articleTagService;

    @Resource
    @Lazy
    private ArticleServiceImpl articleServiceProxy;

    @Override
    @Cached(name = INDEX_TOP_AND_RECOMMEND_ARTICLE_CACHE_NAME, key = DEFAULT_KEY,
            expire = 24 * 60 * 60)
    @CachePenetrationProtect(timeout = 3)
    public TopAndFeaturedArticlesVO listTopAndFeaturedArticles() {
        List<Article> articles = articleMapper.listTopAndFeaturedArticles();
        List<Long> userIds = articles.stream().map(Article::getUserId).toList();
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
        Page<Article> articlePage = pageUnDeleteArticles(page, size);
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
        Article articleForCheck = getArticleForCheck(articleId);
        if (articleForCheck == null) {
            return UnifiedResp.with(NOT_FOUND, "文章不存在");
        }
        //是否是私密文章
        if (ArticleStatusEnum.PRIVATE.getCode().equals(articleForCheck.getStatus())) {
            String accessToken = generateArticleAccessToken(articleId, articleForCheck.getPassword());
            if (Objects.isNull(redisTemplate.opsForValue().get(accessToken))) {
                return UnifiedResp.fail(ARTICLE_ACCESS_FAIL);
            }
        }

        updateArticleViewsCount(articleId);

        ArticleVO article = getArticleDetails(articleId);
        if (article == null) {
            return UnifiedResp.fail(ARTICLE_DETAIL_FAIL);
        }

        return UnifiedResp.success(article);
    }

    private Article getArticleForCheck(Long articleId) {
        return articleMapper.selectOne(new LambdaQueryWrapper<Article>()
                .eq(Article::getId, articleId)
                .eq(Article::getIsDelete, false));
    }


    private ArticleVO getArticleDetails(Long articleId) {
        CompletableFuture<ArticleVO> asyncArticle =
                CompletableFuture.supplyAsync(() -> articleServiceProxy.getArticleVOFromDB(articleId));
        CompletableFuture<ArticleCardVO> asyncPreArticle = getAsyncPreArticle(articleId);
        CompletableFuture<ArticleCardVO> asyncNextArticle = getAsyncNextArticle(articleId);

        CompletableFuture.allOf(asyncArticle, asyncPreArticle, asyncNextArticle)
                .exceptionally(e ->
                {
                    log.error("{} 获取文章详情失败", LOG_PREFIX, e);
                    throw new BlogBizException(ARTICLE_DETAIL_FAIL);
                }).join();

        ArticleVO article = asyncArticle.join();
        if (article != null) {
            setArticleDetails(article, articleId, asyncPreArticle, asyncNextArticle);
        }
        return article;
    }

    private CompletableFuture<ArticleCardVO> getAsyncPreArticle(Long articleId) {
        return CompletableFuture.supplyAsync(() ->
        {
            Long preArticleId = articleMapper.getPreArticleId(articleId);
            return preArticleId == null ? null : articleServiceProxy.getArticleCardVOFromDB(preArticleId);
        });
    }

    private CompletableFuture<ArticleCardVO> getAsyncNextArticle(Long articleId) {
        return CompletableFuture.supplyAsync(() ->
        {
            Long nextArticleId = articleMapper.getNextArticleId(articleId);
            return nextArticleId == null ? null : articleServiceProxy.getArticleCardVOFromDB(nextArticleId);
        });
    }

    private void setArticleDetails(ArticleVO article, Long articleId,
                                   CompletableFuture<ArticleCardVO> asyncPreArticle,
                                   CompletableFuture<ArticleCardVO> asyncNextArticle) {
        Double score = redisTemplate.opsForZSet().score(ARTICLE_VIEWS_COUNT, articleId);
        if (score != null) {
            article.setViewCount(score.intValue());
        }
        article.setPreArticleCard(asyncPreArticle.join());
        article.setNextArticleCard(asyncNextArticle.join());
    }

    @Override
    public UnifiedResp<String> accessArticle(ArticlePasswordDTO articlePasswordDTO) {
        String accessToken = null;
        Article article = articleMapper.selectOne(new LambdaQueryWrapper<Article>().eq(Article::getId,
                articlePasswordDTO.getArticleId()));
        if (Objects.isNull(article)) {
            return UnifiedResp.fail("文章不存在");
        }
        if (article.getPassword().equals(articlePasswordDTO.getArticlePassword())) {
            //下发一个文章访问令牌
            accessToken = generateArticleAccessToken(article.getId(), article.getPassword());
            redisTemplate.opsForValue().set(accessToken, article.getId(), 60L * 60L, TimeUnit.SECONDS);
            return UnifiedResp.success(accessToken);
        } else {
            return UnifiedResp.fail("密码错误");
        }
    }

    private String generateArticleAccessToken(Long articleId, String articlePassword) {
        String loginToken;
        if (StpUtil.isLogin()) {
            loginToken = StpUtil.getLoginIdAsString();
        } else {
            loginToken = StpUtil.getAnonTokenSession().getToken();
        }
        return ArticleAccessTokenGenerator.generateToken(articleId.toString(),
                articlePassword, loginToken);
    }

    @Override
    public UnifiedResp<PageResult<ArticleCardVO>> pageArticlesByTagId(Long tagId, Integer page, Integer size) {
        Page<Article> articlePage = new Page<>(page, size);
        articlePage = articleMapper.selectPage(articlePage, new LambdaQueryWrapper<Article>()
                .in(Article::getStatus, ArticleStatusEnum.PUBLIC.getCode(), ArticleStatusEnum.PRIVATE.getCode())
                .eq(Article::getId, tagId)
                .orderByDesc(Article::getCreateTime));
        return UnifiedResp.success(convertArticlePageToPageResult(articlePage));
    }

    private Page<Article> pageUnDeleteArticles(Integer page, Integer size) {
        Page<Article> articlePage = new Page<>(page, size);
        return articleMapper.selectPage(articlePage, new LambdaQueryWrapper<Article>()
                .in(Article::getStatus, ArticleStatusEnum.PUBLIC.getCode(), ArticleStatusEnum.PRIVATE.getCode())
                .eq(Article::getIsDelete, false)
                .orderByDesc(Article::getCreateTime));
    }

    @Override
    @Transactional(readOnly = true)
    public UnifiedResp<LinkedList<TimeLineArticleVO>> listArticlesByTimeline(Integer page, Integer size) {
        Page<Article> articlePage = pageUnDeleteArticles(page, size);
        List<Article> articles = articlePage.getRecords();
        List<ArticleCardVO> articleCardVOList =
                articles.stream().map(DtoAndVoHelper::convertArticleToArticleCardVO).toList();
        Map<String, List<ArticleCardVO>> timeLineMap = new LinkedHashMap<>();
        articleCardVOList.forEach(articleCardVO ->
        {
            LocalDateTime createTime = articleCardVO.getCreateTime();
            String yearMonth = createTime.getYear() + "-" + createTime.getMonthValue();
            List<ArticleCardVO> articleCardVOS = timeLineMap.computeIfAbsent(yearMonth, k -> new ArrayList<>());
            articleCardVOS.add(articleCardVO);
        });
        LinkedList<TimeLineArticleVO> timeLineArticleVOS = new LinkedList<>();
        timeLineMap.forEach((time, articlesInTime) ->
        {
            TimeLineArticleVO timeLineArticleVO = TimeLineArticleVO.builder()
                    .time(time)
                    .articles(articlesInTime)
                    .build();
            timeLineArticleVOS.add(timeLineArticleVO);
        });
        return UnifiedResp.success(timeLineArticleVOS);
    }


    private void updateArticleViewsCount(Long articleId) {
        redisTemplate.opsForZSet().incrementScore(ARTICLE_VIEWS_COUNT, articleId, 1D);
    }

    @Transactional(readOnly = true)
    @Cached(name = ARTICLE_VO_CACHE_NAME, key = ARTICLE_ID_SPEL_KEY, expire = 24 * 60 * 60)
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
        List<Long> tagIds = articleTags.stream().map(ArticleTag::getTagId).toList();
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
    @Cached(name = ARTICLE_CARD_VO_CACHE_NAME, key = ARTICLE_ID_SPEL_KEY, expire = 24 * 60 * 60)
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




