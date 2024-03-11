package com.donts.blog.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.donts.blog.entity.Tag;

import java.util.List;

/**
 * @author djy12
 * @description 针对表【t_tag】的数据库操作Service
 * @createDate 2024-03-10 20:18:55
 */
public interface TagService extends IService<Tag> {

    /**
     * 根据文章id查询标签
     *
     * @param articleId 文章id
     * @return 标签列表
     */
    List<Tag> listTagsByArticleId(Long articleId);
}
