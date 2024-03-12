package com.donts.blog.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.donts.blog.entity.UserInfo;

import java.util.List;
import java.util.Map;

/**
 * @author djy12
 * @description 针对表【t_user_info】的数据库操作Service
 * @createDate 2024-03-10 20:18:55
 */
public interface UserInfoService extends IService<UserInfo> {
    /**
     * 根据用户id列表查询用户信息
     *
     * @param userIds 用户id列表
     * @return 用户信息列表(以用户id为key)
     */
    Map<Long, UserInfo> listUserInfoMapByUserIds(List<Long> userIds);
}
