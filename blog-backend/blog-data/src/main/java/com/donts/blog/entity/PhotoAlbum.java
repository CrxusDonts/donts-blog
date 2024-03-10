package com.donts.blog.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

;

/**
 * 相册
 *
 * @TableName t_photo_album
 */
@TableName(value = "t_photo_album")
@Data
public class PhotoAlbum implements Serializable {
    /**
     * 主键
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 相册名
     */
    private String albumName;

    /**
     * 相册描述
     */
    private String albumDesc;

    /**
     * 相册封面
     */
    private String albumCover;

    /**
     * 是否删除
     */
    private Integer isDelete;

    /**
     * 状态值 1公开 2私密
     */
    private Integer status;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;


}