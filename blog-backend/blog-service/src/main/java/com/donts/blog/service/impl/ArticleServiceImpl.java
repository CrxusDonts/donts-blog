package com.donts.blog.service.impl;

import com.alicp.jetcache.anno.Cached;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.donts.blog.entity.Article;
import com.donts.blog.entity.Category;
import com.donts.blog.entity.Tag;
import com.donts.blog.entity.UserInfo;
import com.donts.blog.mapper.ArticleMapper;
import com.donts.blog.service.ArticleService;
import com.donts.blog.service.CategoryService;
import com.donts.blog.service.TagService;
import com.donts.blog.service.UserInfoService;
import com.donts.enums.ArticleStatusEnum;
import com.donts.helper.DtoAndVoHelper;
import com.donts.response.PageResult;
import com.donts.vo.ArticleCardVO;
import com.donts.vo.TopAndFeaturedArticlesVO;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.donts.consts.JetCacheNameConst.INDEX_TOP_AND_RECOMMEND_ARTICLE_CACHE_NAME;
import static com.donts.helper.DtoAndVoHelper.convertArticleToArticleCardVO;

/**
 * @author djy12
 * @description 针对表【t_article】的数据库操作Service实现
 * @createDate 2024-03-10 20:18:55
 */
@Service
@Slf4j
public class ArticleServiceImpl extends ServiceImpl<ArticleMapper, Article>
        implements ArticleService {

    @Resource
    private ArticleMapper articleMapper;

    @Resource
    private UserInfoService userInfoService;
    @Resource
    private CategoryService categoryService;
    @Resource
    private TagService tagService;


    @Override
    @Cached(name = INDEX_TOP_AND_RECOMMEND_ARTICLE_CACHE_NAME, expire = 24 * 60 * 60)
    public TopAndFeaturedArticlesVO listTopAndFeaturedArticles() {
        List<Article> articles = articleMapper.listTopAndFeaturedArticles();
        List<Long> userIds = articles.stream().map(Article::getUserId).toList();
        Map<Long, UserInfo> userInfoMap = userInfoService.listUserInfoMapByUserIds(userIds);
        List<ArticleCardVO> articleCardVOList = articles.stream()
                .map(article ->
                {
                    ArticleCardVO articleCardVO = DtoAndVoHelper.convertArticleToArticleCardVO(article);
                    articleCardVO.setAuthor(userInfoMap.get(article.getUserId()));
                    return articleCardVO;
                })
                .collect(Collectors.toList());

        Optional<ArticleCardVO> topArticleOptional = articleCardVOList.stream()
                .filter(articleCardVO -> Boolean.TRUE.equals(articleCardVO.getIsTop()))
                .findFirst();

        topArticleOptional.ifPresent(articleCardVOList::remove);
        return new TopAndFeaturedArticlesVO(topArticleOptional.orElse(null), articleCardVOList);
    }

    @Override
    public PageResult<ArticleCardVO> pageArticles(Integer page, Integer size) {
        Page<Article> articlePage = new Page<>(page, size);
        articlePage = articleMapper.selectPage(articlePage, new LambdaQueryWrapper<Article>()
                .in(Article::getStatus, ArticleStatusEnum.PUBLIC.getCode(), ArticleStatusEnum.PRIVATE.getCode())
                .orderByDesc(Article::getCreateTime));
        // 第一步：获取文章基本信息
        List<Article> articles = articlePage.getRecords();

        // 第二步：构造ArticleCardVO列表，遍历文章集合并填充相关联的数据
        List<ArticleCardVO> list = articles.stream().map(article ->
        {
            ArticleCardVO dto = convertArticleToArticleCardVO(article);

            // 查询并设置作者信息
            UserInfo user = userInfoService.getById(article.getUserId());
            if (user != null) {
                dto.setAuthor(user);
            }
            // 查询并设置分类信息
            Category category = categoryService.getById(article.getCategoryId());
            if (category != null) {
                dto.setCategoryName(category.getCategoryName());
            }

            // 查询并设置标签信息（假设一篇文章可能有多个标签）
            List<Tag> tags = tagService.listTagsByArticleId(article.getId());
            dto.setTags(tags);
            return dto;
        }).toList();
        return PageResult.of(list,
                articlePage.getTotal(),
                articlePage.getPages(),
                articlePage.getCurrent(),
                articlePage.getSize()
        );
    }


}




