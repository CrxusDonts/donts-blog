package com.donts.dto;

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
public class RoleVO implements Serializable {

    private Long roleId;

    private String roleName;

    private LocalDateTime createTime;

    private Boolean isDisable;
    /**
     * 资源列表
     */
    private List<Long> resourceIds;
    /**
     * 菜单列表
     */
    private List<Long> menuIds;

}
