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
@Schema(description = "文章卡片")
public class ArticleCardVO implements java.io.Serializable {
    @Schema(name = "id", description = "文章Id", type = "Long")
    private Long id;
    @Schema(name = "articleCover", description = "文章封面", type = "String")
    private String articleCover;
    @Schema(name = "articleTitle", description = "文章标题", type = "String")
    private String articleTitle;
    @Schema(name = "articleContent", description = "文章内容", type = "String")
    private String articleContent;
    @Schema(name = "isTop", description = "是否置顶", type = "Integer")
    private Integer isTop;
    @Schema(name = "isFeatured", description = "是否推荐", type = "Integer")
    private Integer isFeatured;
    @Schema(name = "author", description = "作者", type = "UserInfo")
    private UserInfo author;
    @Schema(name = "categoryName", description = "分类名", type = "String")
    private String categoryName;
    @Schema(name = "tags", description = "标签列表", type = "List<Tag>")
    private List<Tag> tags;
    @Schema(name = "status", description = "状态，1公开 2私密 3草稿", type = "Integer")
    /**
     * 状态值 1公开 2私密 3草稿
     * @see com.donts.enums.ArticleStatusEnum
     */
    private Integer status;
    @Schema(name = "viewCount", description = "浏览量", type = "Integer")
    private LocalDateTime createTime;
    @Schema(name = "updateTime", description = "更新时间", type = "LocalDateTime")
    private LocalDateTime updateTime;

}
