package com.donts.blog.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.donts.blog.entity.About;
import com.donts.blog.mapper.AboutMapper;
import com.donts.blog.service.AboutService;
import org.springframework.stereotype.Service;

/**
 * @author djy12
 * @description 针对表【t_about】的数据库操作Service实现
 * @createDate 2024-03-10 20:18:55
 */
@Service
public class AboutServiceImpl extends ServiceImpl<AboutMapper, About>
        implements AboutService {

}




