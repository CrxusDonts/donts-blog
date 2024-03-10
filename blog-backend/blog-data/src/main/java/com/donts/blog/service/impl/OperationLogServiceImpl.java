package com.donts.blog.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.donts.blog.entity.OperationLog;
import com.donts.blog.mapper.OperationLogMapper;
import com.donts.blog.service.OperationLogService;
import org.springframework.stereotype.Service;

/**
 * @author djy12
 * @description 针对表【t_operation_log】的数据库操作Service实现
 * @createDate 2024-03-10 20:18:55
 */
@Service
public class OperationLogServiceImpl extends ServiceImpl<OperationLogMapper, OperationLog>
        implements OperationLogService {

}




