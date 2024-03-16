package com.donts.vo;

import com.donts.blog.entity.Tag;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "文章")
public class ArticleAdminViewVO implements Serializable {
    @Schema(description = "文章id", name = "id", type = "Long")
    private Long id;
    @Schema(description = "文章标题", name = "articleTitle", type = "String")
    private String articleTitle;
    @Schema(description = "文章摘要", name = "articleAbstract", type = "String")
    private String articleAbstract;
    @Schema(description = "文章内容", name = "articleContent", type = "String")
    private String articleContent;
    @Schema(description = "文章封面", name = "articleCover", type = "String")
    private String articleCover;
    @Schema(description = "分类名称", name = "categoryName", type = "String")
    private String categoryName;
    @Schema(description = "标签", name = "tags", type = "List<Tag>")
    private List<Tag> tags;
    @Schema(description = "是否置顶", name = "isTop", type = "Boolean")
    private Boolean isTop;
    @Schema(description = "是否推荐", name = "isFeatured", type = "Boolean")
    private Boolean isFeatured;
    /**
     * @see com.donts.enums.ArticleStatusEnum
     */
    @Schema(description = "状态", name = "status", type = "Integer")
    private Integer status;
    /**
     * @see com.donts.enums.ArticleTypeEnum
     */
    @Schema(description = "类型", name = "type", type = "Integer")
    private Integer type;
    @Schema(description = "原始链接", name = "originalUrl", type = "String")
    private String originalUrl;
    @Schema(description = "密码", name = "password", type = "String")
    private String password;

}
