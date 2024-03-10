package com.donts.blog.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

;

/**
 * @TableName t_exception_log
 */
@TableName(value = "t_exception_log")
@Data
public class ExceptionLog implements Serializable {
    /**
     *
     */
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 请求接口
     */
    private String optUri;

    /**
     * 请求方式
     */
    private String optMethod;

    /**
     * 请求方式
     */
    private String requestMethod;

    /**
     * 请求参数
     */
    private String requestParam;

    /**
     * 操作描述
     */
    private String optDesc;

    /**
     * 异常信息
     */
    private String exceptionInfo;

    /**
     * ip
     */
    private String ipAddress;

    /**
     * ip来源
     */
    private String ipSource;

    /**
     * 操作时间
     */
    private LocalDateTime createTime;


}