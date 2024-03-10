package com.donts.blog.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.donts.blog.entity.Photo;
import com.donts.blog.mapper.PhotoMapper;
import com.donts.blog.service.PhotoService;
import org.springframework.stereotype.Service;

/**
 * @author djy12
 * @description 针对表【t_photo(照片)】的数据库操作Service实现
 * @createDate 2024-03-10 20:18:55
 */
@Service
public class PhotoServiceImpl extends ServiceImpl<PhotoMapper, Photo>
        implements PhotoService {

}




