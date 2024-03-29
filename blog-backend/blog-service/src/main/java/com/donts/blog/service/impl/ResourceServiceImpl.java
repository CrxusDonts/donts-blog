package com.donts.blog.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.donts.blog.entity.Resource;
import com.donts.blog.mapper.ResourceMapper;
import com.donts.blog.service.ResourceService;
import org.springframework.stereotype.Service;

/**
 * @author djy12
 * @description 针对表【t_resource】的数据库操作Service实现
 * @createDate 2024-03-10 20:18:55
 */
@Service
public class ResourceServiceImpl extends ServiceImpl<ResourceMapper, Resource>
        implements ResourceService {

}




