package com.donts.blog.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.donts.blog.entity.JobLog;
import com.donts.blog.mapper.JobLogMapper;
import com.donts.blog.service.JobLogService;
import org.springframework.stereotype.Service;

/**
 * @author djy12
 * @description 针对表【t_job_log(定时任务调度日志表)】的数据库操作Service实现
 * @createDate 2024-03-10 20:18:55
 */
@Service
public class JobLogServiceImpl extends ServiceImpl<JobLogMapper, JobLog>
        implements JobLogService {

}




