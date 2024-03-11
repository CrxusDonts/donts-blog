package com.donts.blog.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.donts.blog.entity.Article;
import com.donts.response.PageResult;
import com.donts.vo.ArticleCardVO;
import com.donts.vo.TopAndFeaturedArticlesVO;

/**
 * @author djy12
 * @description 针对表【t_article】的数据库操作Service
 * @createDate 2024-03-10 20:18:55
 */
public interface ArticleService extends IService<Article> {

    TopAndFeaturedArticlesVO listTopAndFeaturedArticles();


    PageResult<ArticleCardVO> pageArticles(Integer page, Integer size);
}
