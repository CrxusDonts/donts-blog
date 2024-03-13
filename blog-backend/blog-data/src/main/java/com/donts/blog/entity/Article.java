package com.donts.blog.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @TableName t_article
 */
@TableName(value = "t_article")
@Data
public class Article implements Serializable {
    /**
     *
     */
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 作者
     */
    private Long userId;

    /**
     * 文章分类
     */
    private Long categoryId;

    /**
     * 文章缩略图
     */
    private String articleCover;

    /**
     * 标题
     */
    private String articleTitle;

    /**
     * 文章摘要，如果该字段为空，默认取文章的前500个字符作为摘要
     */
    private String articleAbstract;

    /**
     * 内容
     */
    private String articleContent;

    /**
     * 是否置顶 0否 1是
     */
    private Boolean isTop;

    /**
     * 是否推荐 0否 1是
     */
    private Boolean isFeatured;

    /**
     * 是否删除  0否 1是
     */
    private Boolean isDelete;

    /**
     * 状态值 1公开 2私密 3草稿
     * @see com.donts.enums.ArticleStatusEnum
     */
    private Integer status;

    /**
     * 文章类型 1原创 2转载 3翻译
     * @see com.donts.enums.ArticleTypeEnum
     */
    private Integer type;

    /**
     * 访问密码
     */
    private String password;

    /**
     * 原文链接
     */
    private String originalUrl;

    /**
     * 发表时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;


}