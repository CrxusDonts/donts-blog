package com.donts.blog.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.donts.blog.entity.Job;
import com.donts.blog.mapper.JobMapper;
import com.donts.blog.service.JobService;
import org.springframework.stereotype.Service;

/**
 * @author djy12
 * @description 针对表【t_job(定时任务调度表)】的数据库操作Service实现
 * @createDate 2024-03-10 20:18:55
 */
@Service
public class JobServiceImpl extends ServiceImpl<JobMapper, Job>
        implements JobService {

}




