package com.donts.blog.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

;

/**
 * @TableName t_comment
 */
@TableName(value = "t_comment")
@Data
public class Comment implements Serializable {
    /**
     * 主键
     */
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 评论用户Id
     */
    private Long userId;

    /**
     * 评论主题id
     */
    private Long topicId;

    /**
     * 评论内容
     */
    private String commentContent;

    /**
     * 回复用户id
     */
    private Long replyUserId;

    /**
     * 父评论id
     */
    private Long parentId;

    /**
     * 评论类型 1.文章 2.留言 3.关于我 4.友链 5.说说
     */
    private Integer type;

    /**
     * 是否删除  0否 1是
     */
    private Boolean isDelete;

    /**
     * 是否审核
     */
    private Boolean isReview;

    /**
     * 评论时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;


}