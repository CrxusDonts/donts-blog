package com.donts.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "TagVO", description = "标签")
public class TagVO implements Serializable {
    @Schema(name = "tagId", description = "标签id")
    private Long tagId;

    @Schema(name = "tagName", description = "标签名")
    private String tagName;

    @Schema(name = "count", description = "标签下文章数")
    private Long count;

}
