package com.donts.blog.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.donts.blog.entity.UserInfo;
import com.donts.blog.mapper.UserInfoMapper;
import com.donts.blog.service.UserInfoService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author djy12
 * @description 针对表【t_user_info】的数据库操作Service实现
 * @createDate 2024-03-10 20:18:55
 */
@Service
public class UserInfoServiceImpl extends ServiceImpl<UserInfoMapper, UserInfo>
        implements UserInfoService {
    @Resource
    private UserInfoMapper userInfoMapper;

    @Override
    public Map<Long, UserInfo> listUserInfoMapByUserIds(List<Long> userIds) {
        if (userIds.isEmpty()) {
            return Map.of();
        }
        List<UserInfo> userInfos = userInfoMapper.selectBatchIds(userIds);
        return userInfos.stream()
                .collect(Collectors.toMap(UserInfo::getId, userInfo -> userInfo, (k1, k2) -> k1));

    }
}




