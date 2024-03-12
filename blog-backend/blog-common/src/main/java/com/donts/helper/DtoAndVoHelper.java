package com.donts.helper;

import com.donts.blog.entity.Article;
import com.donts.vo.ArticleCardVO;
import org.apache.commons.lang3.StringUtils;

public class DtoAndVoHelper {
    public static ArticleCardVO convertArticleToArticleCardVO(Article article) {
        ArticleCardVO dto = new ArticleCardVO();
        dto.setId(article.getId());
        dto.setArticleCover(article.getArticleCover());
        dto.setArticleTitle(article.getArticleTitle());
        dto.setArticleContent(getTruncatedContent(article.getArticleAbstract(), article.getArticleContent()));
        dto.setIsTop(article.getIsTop());
        dto.setIsFeatured(article.getIsFeatured());
        dto.setStatus(article.getStatus());
        dto.setCreateTime(article.getCreateTime());
        dto.setUpdateTime(article.getUpdateTime());
        return dto;
    }

    /**
     * 辅助方法，用于截取文章摘要或内容
     *
     * @param articleAbstract 文章摘要
     * @param articleContent  文章内容
     * @return 截取后的文章内容
     */
    private static String getTruncatedContent(String articleAbstract, String articleContent) {
        return StringUtils.isBlank(articleAbstract) ? StringUtils.substring(articleContent, 0, 500) : articleAbstract;
    }
}
