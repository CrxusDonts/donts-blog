package com.donts.blog.article;

import com.baomidou.mybatisplus.extension.service.IService;
import com.donts.blog.entity.Article;
import com.donts.dto.ArticlePasswordDTO;
import com.donts.response.PageResult;
import com.donts.response.UnifiedResp;
import com.donts.vo.ArticleCardVO;
import com.donts.vo.ArticleVO;
import com.donts.vo.TimeLineArticleVO;
import com.donts.vo.TopAndFeaturedArticlesVO;

import java.util.LinkedList;

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
     *
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

    /**
     * 校验文章访问密码
     *
     * @param articlePasswordDTO 文章密码
     * @return 校验结果
     */
    UnifiedResp<String> accessArticle(ArticlePasswordDTO articlePasswordDTO);

    /**
     * 根据标签id获取文章
     *
     * @param tagId 标签id
     * @param page  页码
     * @param size  每页大小
     * @return 文章列表
     */
    UnifiedResp<PageResult<ArticleCardVO>> pageArticlesByTagId(Long tagId, Integer page, Integer size);

    /**
     * 按照时间线获取文章
     *
     * @param page 页码
     * @param size 每页大小
     * @return 文章归档
     */
    UnifiedResp<LinkedList<TimeLineArticleVO>> listArticlesByTimeline(Integer page, Integer size);
}
