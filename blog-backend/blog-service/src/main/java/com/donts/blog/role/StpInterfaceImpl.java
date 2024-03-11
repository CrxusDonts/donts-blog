package com.donts.blog.role;

import cn.dev33.satoken.stp.StpInterface;
import com.donts.blog.service.RoleService;
import com.donts.dto.RoleVO;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * 自定义权限加载接口实现类
 */
@Component
public class StpInterfaceImpl implements StpInterface {

    @Resource
    private RoleService roleService;

    /**
     * 返回一个账号所拥有的权限码集合
     */
    @Override
    public List<String> getPermissionList(Object loginId, String loginType) {
        //resourceNameList and menuNameList
        List<RoleVO> roleVOList = roleService.listRolesWithMenuAndResource(loginId.toString());
        List<String> permissionList = new ArrayList<>();
        roleVOList.stream().filter(roleVO -> !roleVO.getIsDisable()).forEach(roleVO ->
        {
            permissionList.addAll(roleVO.getResourceIds().stream().map(String::valueOf).toList());
            permissionList.addAll(roleVO.getMenuIds().stream().map(String::valueOf).toList());
        });
        return permissionList;
    }

    /**
     * 返回一个账号所拥有的角色标识集合 (权限与角色可分开校验)
     */
    @Override
    public List<String> getRoleList(Object loginId, String loginType) {
        String loginIdStr = loginId.toString();
        List<RoleVO> roleVOList = roleService.listRoles(loginIdStr);
        return roleVOList.stream().filter(roleVO -> !roleVO.getIsDisable())
                .map(RoleVO::getRoleId).map(String::valueOf).toList();
    }

}
