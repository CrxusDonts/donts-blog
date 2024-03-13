package com.donts.blog.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.donts.blog.entity.Article;
import com.donts.response.PageResult;
import com.donts.response.UnifiedResp;
import com.donts.vo.ArticleCardVO;
import com.donts.vo.ArticleVO;
import com.donts.vo.TopAndFeaturedArticlesVO;

/**
 * @author djy12
 * @description 针对表【t_article】的数据库操作Service
 * @createDate 2024-03-10 20:18:55
 */
public interface ArticleService extends IService<Article> {
    /**
     * 获取首页置顶和推荐文章
     *
     * @return 首页置顶和推荐文章
     */
    TopAndFeaturedArticlesVO listTopAndFeaturedArticles();

    /**
     * 分页查询文章
     * @param page 页码
     * @param size 每页大小
     * @return 文章列表
     */

    PageResult<ArticleCardVO> pageArticles(Integer page, Integer size);

    /**
     * 根据分类id分页获取文章
     *
     * @param categoryId 分类id
     * @param page       页码
     * @param size       每页大小
     * @return 文章列表
     */
    PageResult<ArticleCardVO> pageArticlesByCategoryId(Long categoryId, Integer page, Integer size);

    /**
     * 根据文章id获取文章
     *
     * @param articleId 文章id
     * @return 文章
     */
    UnifiedResp<ArticleVO> findArticleById(Long articleId);

}
