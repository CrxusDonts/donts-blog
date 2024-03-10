package com.donts.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "查询条件")
public class ConditionDTO implements Serializable {

    @Schema(name = "current", description = "页码", type = "Long")
    private Long current;

    @Schema(name = "size", description = "条数", type = "Long")
    private Long size;

    @Schema(name = "keywords", description = "搜索内容", type = "String")
    private String keywords;

    @Schema(name = "categoryId", description = "分类id", type = "Integer")
    private Long categoryId;

    @Schema(name = "tagId", description = "标签id", type = "Integer")
    private Long tagId;

    @Schema(name = "albumId", description = "相册id", type = "Integer")
    private Long albumId;

    @Schema(name = "loginType", description = "登录类型", type = "Integer")
    private Integer loginType;

    @Schema(name = "type", description = "类型", type = "Integer")
    private Integer type;

    @Schema(name = "status", description = "状态", type = "Integer")
    private Integer status;

    @Schema(name = "startTime", description = "开始时间", type = "LocalDateTime")
    private LocalDateTime startTime;

    @Schema(name = "endTime", description = "结束时间", type = "LocalDateTime")
    private LocalDateTime endTime;

    @Schema(name = "isDelete", description = "是否删除", type = "Boolean")
    private Boolean isDelete;

    @Schema(name = "isReview", description = "是否审核", type = "Boolean")
    private Boolean isReview;

    @Schema(name = "isTop", description = "是否置顶", type = "Boolean")
    private Boolean isTop;

    @Schema(name = "isFeatured", description = "是否推荐", type = "Boolean")
    private Boolean isFeatured;


}