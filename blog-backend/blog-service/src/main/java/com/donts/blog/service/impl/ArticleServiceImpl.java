package com.donts.blog.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.donts.blog.entity.Article;
import com.donts.blog.mapper.ArticleMapper;
import com.donts.blog.service.ArticleService;
import org.springframework.stereotype.Service;

/**
 * @author djy12
 * @description 针对表【t_article】的数据库操作Service实现
 * @createDate 2024-03-10 20:18:55
 */
@Service
public class ArticleServiceImpl extends ServiceImpl<ArticleMapper, Article>
        implements ArticleService {

}




