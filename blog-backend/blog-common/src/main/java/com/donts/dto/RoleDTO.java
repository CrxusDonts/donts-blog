package com.donts.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "角色")
public class RoleDTO {

    @Schema(name = "roleId", description = "角色Id", type = "Long")
    private Long roleId;

    @NotBlank(message = "角色名不能为空")
    @Schema(name = "roleName", description = "角色名", type = "String")
    private String roleName;

    @Schema(name = "resourceIdList", description = "资源列表", type = "List<Integer>")
    private List<Long> resourceIds;

    @Schema(name = "menuIdList", description = "菜单列表", type = "List<Integer>")
    private List<Long> menuIds;

}
