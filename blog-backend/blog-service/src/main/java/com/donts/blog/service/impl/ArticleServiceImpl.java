package com.donts.blog.service.impl;

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
import com.donts.response.PageResult;
import com.donts.vo.ArticleCardVO;
import com.donts.vo.TopAndFeaturedArticlesVO;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.List;

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
    public TopAndFeaturedArticlesVO listTopAndFeaturedArticles() {
        return null;
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
            ArticleCardVO dto = new ArticleCardVO();

            // 设置基本属性
            dto.setId(article.getId());
            dto.setArticleCover(article.getArticleCover());
            dto.setArticleTitle(article.getArticleTitle());
            dto.setArticleContent(getTruncatedContent(article.getArticleAbstract(), article.getArticleContent()));
            dto.setIsTop(article.getIsTop());
            dto.setIsFeatured(article.getIsFeatured());
            dto.setStatus(article.getStatus());
            dto.setCreateTime(article.getCreateTime());
            dto.setUpdateTime(article.getUpdateTime());

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


    /**
     * 辅助方法，用于截取文章摘要或内容
     *
     * @param articleAbstract 文章摘要
     * @param articleContent  文章内容
     * @return 截取后的文章内容
     */
    private String getTruncatedContent(String articleAbstract, String articleContent) {
        return StringUtils.isBlank(articleAbstract) ? StringUtils.substring(articleContent, 0, 500) : articleAbstract;
    }
}




