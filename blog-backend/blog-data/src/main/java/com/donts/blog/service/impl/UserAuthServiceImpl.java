package com.donts.blog.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.donts.blog.entity.UserAuth;
import com.donts.blog.mapper.UserAuthMapper;
import com.donts.blog.service.UserAuthService;
import org.springframework.stereotype.Service;

/**
 * @author djy12
 * @description 针对表【t_user_auth】的数据库操作Service实现
 * @createDate 2024-03-10 20:18:55
 */
@Service
public class UserAuthServiceImpl extends ServiceImpl<UserAuthMapper, UserAuth>
        implements UserAuthService {

}




