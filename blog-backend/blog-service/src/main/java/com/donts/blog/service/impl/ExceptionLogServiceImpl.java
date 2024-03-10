package com.donts.blog.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.donts.blog.entity.ExceptionLog;
import com.donts.blog.mapper.ExceptionLogMapper;
import com.donts.blog.service.ExceptionLogService;
import org.springframework.stereotype.Service;

/**
 * @author djy12
 * @description 针对表【t_exception_log】的数据库操作Service实现
 * @createDate 2024-03-10 20:18:55
 */
@Service
public class ExceptionLogServiceImpl extends ServiceImpl<ExceptionLogMapper, ExceptionLog>
        implements ExceptionLogService {

}




