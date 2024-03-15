package com.donts.blog.article.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.donts.blog.article.ArticleTagService;
import com.donts.blog.entity.ArticleTag;
import com.donts.blog.mapper.ArticleTagMapper;
import org.springframework.stereotype.Service;

/**
 * @author djy12
 * @description 针对表【t_article_tag】的数据库操作Service实现
 * @createDate 2024-03-10 20:18:55
 */
@Service
public class ArticleTagServiceImpl extends ServiceImpl<ArticleTagMapper, ArticleTag>
        implements ArticleTagService {

}




