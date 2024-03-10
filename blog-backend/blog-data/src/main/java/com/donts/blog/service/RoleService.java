package com.donts.blog.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.donts.blog.entity.Role;
import com.donts.dto.ConditionDTO;
import com.donts.dto.RoleDTO;
import com.donts.dto.RoleVO;
import com.donts.response.PageResult;

import java.util.List;


public interface RoleService extends IService<Role> {
    List<Role> listUserRoles();

    PageResult<RoleVO> listRoles(ConditionDTO conditionDTO);

    void saveOrUpdateRole(RoleDTO roleDTO);

    void deleteRoles(List<Integer> roleIdList);
}
