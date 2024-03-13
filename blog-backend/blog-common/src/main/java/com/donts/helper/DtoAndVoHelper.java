package com.donts.helper;

import com.donts.blog.entity.Article;
import com.donts.vo.ArticleCardVO;
import com.donts.vo.ArticleVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;

@Slf4j
public class DtoAndVoHelper {
    private static final String LOG_PREFIX = "DtoAndVoHelper";

    private DtoAndVoHelper() {
    }

    public static ArticleCardVO convertArticleToArticleCardVO(Article article) {
        ArticleCardVO dto = new ArticleCardVO();
        try {
            BeanUtils.copyProperties(article, dto);
            dto.setArticleContent(getTruncatedContent(article.getArticleAbstract(), article.getArticleContent()));
        } catch (Exception e) {
            log.error(" {} convertArticleToArticleCardVO ", LOG_PREFIX, e);
        }
        return dto;
    }

    public static ArticleVO convertArticleToArticleVO(Article article) {
        ArticleVO vo = new ArticleVO();
        try {
            BeanUtils.copyProperties(article, vo);
            vo.setArticleContent(getTruncatedContent(article.getArticleAbstract(), article.getArticleContent()));
        } catch (Exception e) {
            log.error(" {} convertArticleToArticleVO ", LOG_PREFIX, e);
        }
        return vo;
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
