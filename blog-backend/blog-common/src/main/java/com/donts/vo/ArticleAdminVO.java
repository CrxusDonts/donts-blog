package com.donts.vo;

import com.donts.blog.entity.Category;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(name = "ArticleAdminVO", description = "后台文章")
public class ArticleAdminVO implements Serializable {

    @Schema(description = "文章id")
    private Long articleId;

    @Schema(description = "文章封面")
    private String articleCover;

    @Schema(description = "文章标题")
    private String articleTitle;

    @Schema(description = "创建时间")
    private LocalDateTime createTime;

    @Schema(description = "浏览次数")
    private Integer viewsCount;

    @Schema(description = "分类")
    private Category category;

    @Schema(description = "标签")
    private List<TagVO> tagVOS;

    @Schema(description = "是否置顶")
    private Boolean isTop;

    @Schema(description = "是否推荐")
    private Boolean isFeatured;

    @Schema(description = "是否删除")
    private Boolean isDelete;
    /**
     * @see com.donts.enums.ArticleStatusEnum
     */
    @Schema(description = "状态")
    private Integer status;
    /**
     * @see com.donts.enums.ArticleTypeEnum
     */
    @Schema(description = "类型")
    private Integer type;

}