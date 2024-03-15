package com.donts.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "ArticleTopFeaturedDTO", description = "文章置顶推荐修改DTO")
public class ArticleTopFeaturedDTO {
    @Schema(description = "文章id")
    @NotNull(message = "id不能为空")
    private Long articleId;
    @Schema(description = "是否置顶")
    @NotNull(message = "是否置顶不能为空")
    private Boolean isTop;

    @Schema(description = "是否推荐")
    @NotNull(message = "是否推荐不能为空")
    private Boolean isFeatured;
}