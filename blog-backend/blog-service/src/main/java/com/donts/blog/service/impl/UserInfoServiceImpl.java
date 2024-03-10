package com.donts.blog.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.donts.blog.entity.UserInfo;
import com.donts.blog.mapper.UserInfoMapper;
import com.donts.blog.service.UserInfoService;
import org.springframework.stereotype.Service;

/**
 * @author djy12
 * @description 针对表【t_user_info】的数据库操作Service实现
 * @createDate 2024-03-10 20:18:55
 */
@Service
public class UserInfoServiceImpl extends ServiceImpl<UserInfoMapper, UserInfo>
        implements UserInfoService {

}




