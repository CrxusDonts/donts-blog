package com.donts.blog.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.donts.blog.entity.Article;

import java.util.List;

/**
 * @author djy12
 * @description 针对表【t_article】的数据库操作Mapper
 * @createDate 2024-03-10 20:18:55
 * @Entity com.donts.blog.entity.Article
 */
public interface ArticleMapper extends BaseMapper<Article> {

    List<Article> listTopAndFeaturedArticles();

    Long getPreArticleId(Long articleId);

    Long getNextArticleId(Long articleId);

    Long selectLatestArchiveArticleId();
}




