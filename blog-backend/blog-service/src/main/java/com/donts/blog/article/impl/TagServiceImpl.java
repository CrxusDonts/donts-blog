package com.donts.blog.article.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.donts.blog.article.ArticleTagService;
import com.donts.blog.article.TagService;
import com.donts.blog.entity.ArticleTag;
import com.donts.blog.entity.Tag;
import com.donts.blog.mapper.TagMapper;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

/**
 * @author djy12
 * @description 针对表【t_tag】的数据库操作Service实现
 * @createDate 2024-03-10 20:18:55
 */
@Service
public class TagServiceImpl extends ServiceImpl<TagMapper, Tag> implements TagService {

    @Resource
    private TagMapper tagMapper;

    @Resource
    private ArticleTagService articleTagService;

    @Override
    public List<Tag> listTagsByArticleId(Long articleId) {
        List<ArticleTag> articleTagRelations = articleTagService
                .list(new LambdaQueryWrapper<ArticleTag>().eq(ArticleTag::getArticleId, articleId));
        if (articleTagRelations.isEmpty()) {
            return Collections.emptyList();
        }
        return tagMapper.selectList(new LambdaQueryWrapper<Tag>().in(Tag::getId,
                articleTagRelations.stream().map(ArticleTag::getTagId).toList())
        );

    }

}




