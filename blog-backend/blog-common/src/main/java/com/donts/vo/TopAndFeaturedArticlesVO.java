package com.donts.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "首页置顶和推荐文章")
public class TopAndFeaturedArticlesVO implements java.io.Serializable {
    @Schema(name = "topArticle", description = "置顶文章", type = "ArticleCardVO")
    private ArticleCardVO topArticle;
    @Schema(name = "featuredArticles", description = "推荐文章", type = "List<ArticleCardVO>")
    private List<ArticleCardVO> featuredArticles;
}
