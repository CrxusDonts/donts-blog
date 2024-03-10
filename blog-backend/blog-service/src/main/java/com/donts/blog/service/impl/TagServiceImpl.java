package com.donts.blog.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.donts.blog.entity.Tag;
import com.donts.blog.mapper.TagMapper;
import com.donts.blog.service.TagService;
import org.springframework.stereotype.Service;

/**
 * @author djy12
 * @description 针对表【t_tag】的数据库操作Service实现
 * @createDate 2024-03-10 20:18:55
 */
@Service
public class TagServiceImpl extends ServiceImpl<TagMapper, Tag>
        implements TagService {

}




