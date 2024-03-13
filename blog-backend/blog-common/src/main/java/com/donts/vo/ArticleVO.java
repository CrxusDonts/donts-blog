package com.donts.vo;


import com.donts.blog.entity.Tag;
import com.donts.blog.entity.UserInfo;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "文章")
public class ArticleVO implements java.io.Serializable {
    @Schema(name = "id", description = "文章Id", type = "Long")
    private Long id;
    @Schema(name = "author", description = "作者", type = "UserInfo")
    private UserInfo author;
    @Schema(name = "categoryName", description = "分类名", type = "String")
    private String categoryName;
    @Schema(name = "articleCover", description = "文章封面", type = "String")
    private String articleCover;
    @Schema(name = "articleTitle", description = "文章标题", type = "String")
    private String articleTitle;
    @Schema(name = "articleContent", description = "文章内容", type = "String")
    private String articleContent;
    @Schema(name = "isTop", description = "是否置顶", type = "Integer")
    private Boolean isTop;
    @Schema(name = "isFeatured", description = "是否推荐", type = "Integer")
    private Boolean isFeatured;
    @Schema(name = "isDelete", description = "是否删除", type = "Integer")
    private Boolean isDelete;
    /**
     * 状态值 1公开 2私密 3草稿
     *
     * @see com.donts.enums.ArticleStatusEnum
     */
    @Schema(name = "status", description = "状态 1公开 2私密 3草稿", type = "Integer")
    private Integer status;
    @Schema(name = "tags", description = "标签列表", type = "List<Tag>")
    private List<Tag> tags;
    @Schema(name = "viewCount", description = "浏览量", type = "Integer")
    private Integer viewCount;
    /**
     * 类型，1原创 2转载
     *
     * @see com.donts.enums.ArticleTypeEnum
     */
    @Schema(name = "type", description = "文章类型 1原创 2转载 3翻译", type = "Integer")
    private Integer type;
    @Schema(name = "originalUrl", description = "原文链接", type = "String")
    private String originalUrl;
    @Schema(name = "createTime", description = "创建时间", type = "LocalDateTime")
    private LocalDateTime createTime;
    @Schema(name = "updateTime", description = "更新时间", type = "LocalDateTime")
    private LocalDateTime updateTime;
    @Schema(name = "preArticleCard", description = "上一篇文章卡片", type = "ArticleCardVO")
    private ArticleCardVO preArticleCard;
    @Schema(name = "nextArticleCard", description = "下一篇文章卡片", type = "ArticleCardVO")
    private ArticleCardVO nextArticleCard;

}
