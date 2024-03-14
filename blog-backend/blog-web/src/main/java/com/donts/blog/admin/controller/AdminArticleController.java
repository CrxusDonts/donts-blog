package com.donts.blog.admin.controller;

import com.donts.blog.service.ArticleService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Tag(name = "AdminArticleController", description = "admin文章模块")
@RequestMapping("/admin/api")
public class AdminArticleController {
    @Resource
    private ArticleService articleService;


}
