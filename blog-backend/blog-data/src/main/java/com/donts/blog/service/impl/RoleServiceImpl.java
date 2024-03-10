package com.donts.blog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.donts.blog.entity.Role;
import com.donts.blog.entity.RoleMenu;
import com.donts.blog.entity.RoleResource;
import com.donts.blog.entity.UserRole;
import com.donts.blog.mapper.RoleMapper;
import com.donts.blog.mapper.UserRoleMapper;
import com.donts.blog.service.RoleMenuService;
import com.donts.blog.service.RoleResourceService;
import com.donts.blog.service.RoleService;
import com.donts.dto.ConditionDTO;
import com.donts.dto.RoleDTO;
import com.donts.dto.RoleVO;
import com.donts.exception.BlogBizException;
import com.donts.response.PageResult;
import jakarta.annotation.Resource;
import lombok.SneakyThrows;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;


@Service
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role>
        implements RoleService {

    @Resource
    private RoleMapper roleMapper;

    @Resource
    private UserRoleMapper userRoleMapper;

    @Resource
    private RoleResourceService roleResourceService;

    @Resource
    private RoleMenuService roleMenuService;

    @Override
    public List<Role> listUserRoles() {
        return roleMapper.selectList(new LambdaQueryWrapper<Role>()
                .select(Role::getId, Role::getRoleName));
    }

    @SneakyThrows
    @Override
    public PageResult<RoleVO> listRoles(ConditionDTO conditionDTO) {
        Page<Role> page = new Page<>(conditionDTO.getCurrent(), conditionDTO.getSize());
        LambdaQueryWrapper<Role> queryWrapper = new LambdaQueryWrapper<Role>()
                .like(StringUtils.isNotBlank(conditionDTO.getKeywords()), Role::getRoleName, conditionDTO.getKeywords())
                .orderByDesc(Role::getCreateTime);
        roleMapper.selectPage(page, queryWrapper);
        List<RoleVO> roleVOList = CompletableFuture.supplyAsync(() -> page.getRecords().stream()
                        .map(role -> RoleVO.builder()
                                .roleId(role.getId())
                                .roleName(role.getRoleName())
                                .createTime(role.getCreateTime())
                                .build())
                        .collect(Collectors.toList()))
                .get();
        return PageResult.of(roleVOList, page.getTotal(), page.getPages(), page.getCurrent(), page.getSize());
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void saveOrUpdateRole(RoleDTO roleDTO) {
        Role roleCheck = roleMapper.selectOne(new LambdaQueryWrapper<Role>()
                .select(Role::getId)
                .eq(Role::getRoleName, roleDTO.getRoleName()));
        if (Objects.nonNull(roleCheck) && !(roleCheck.getId().equals(roleDTO.getRoleId()))) {
            throw new BlogBizException("该角色已存在");
        }
        Role role = Role.builder()
                .id(roleDTO.getRoleId())
                .roleName(roleDTO.getRoleName())
                .isDisable(Boolean.FALSE)
                .build();
        this.saveOrUpdate(role);
        if (Objects.nonNull(roleDTO.getResourceIds())) {
            if (Objects.nonNull(roleDTO.getRoleId())) {
                roleResourceService.remove(new LambdaQueryWrapper<RoleResource>()
                        .eq(RoleResource::getRoleId, roleDTO.getRoleId()));
            }
            List<RoleResource> roleResourceList = roleDTO.getResourceIds().stream()
                    .map(resourceId -> RoleResource.builder()
                            .roleId(role.getId())
                            .resourceId(resourceId)
                            .build())
                    .toList();
            roleResourceService.saveBatch(roleResourceList);
        }
        if (Objects.nonNull(roleDTO.getMenuIds())) {
            if (Objects.nonNull(roleDTO.getRoleId())) {
                roleMenuService.remove(new LambdaQueryWrapper<RoleMenu>().eq(RoleMenu::getRoleId, roleDTO.getRoleId()));
            }
            List<RoleMenu> roleMenuList = roleDTO.getMenuIds().stream()
                    .map(menuId -> RoleMenu.builder()
                            .roleId(role.getId())
                            .menuId(menuId)
                            .build())
                    .toList();
            roleMenuService.saveBatch(roleMenuList);
        }
    }

    @Override
    public void deleteRoles(List<Integer> roleIdList) {
        Long count = userRoleMapper.selectCount(new LambdaQueryWrapper<UserRole>()
                .in(UserRole::getRoleId, roleIdList));
        if (count > 0) {
            throw new BlogBizException("该角色下存在用户");
        }
        roleMapper.deleteBatchIds(roleIdList);
    }
}




