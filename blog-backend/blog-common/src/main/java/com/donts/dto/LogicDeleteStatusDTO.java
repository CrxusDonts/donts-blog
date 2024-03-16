package com.donts.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "LogicDeleteStatusDTO", description = "逻辑删除状态DTO")
public class LogicDeleteStatusDTO implements java.io.Serializable {

    @NotEmpty(message = "id列表不能为空")
    @Schema(name = "ids", description = "要删除的id", type = "array")
    private List<Long> ids;

    @NotNull(message = "状态值不能为空")
    @Schema(name = "isDelete", description = "删除状态", type = "boolean")
    private Boolean isDelete;
}
