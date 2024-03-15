package com.donts.blog.admin.controller;

import com.donts.blog.aop.anno.OptLog;
import com.donts.blog.article.ArticleAdminService;
import com.donts.dto.ArticleTopFeaturedDTO;
import com.donts.dto.ConditionDTO;
import com.donts.response.PageResult;
import com.donts.response.UnifiedResp;
import com.donts.vo.ArticleAdminVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import static com.donts.enums.OptTypeEnum.UPDATE;

@RestController
@Tag(name = "AdminArticleController", description = "admin文章模块")
@RequestMapping("/admin/api/articles")
public class AdminArticleController {
    @Resource
    private ArticleAdminService articleAdminService;


    @Operation(summary = "获取后台文章")
    @GetMapping
    public UnifiedResp<PageResult<ArticleAdminVO>> listArticlesAdmin(ConditionDTO conditionDTO) {
        return articleAdminService.listArticlesForAdmin(conditionDTO);
    }

    @OptLog(optType = UPDATE)
    @Operation(summary = "修改文章是否置顶和推荐")
    @PutMapping("/topAndFeatured")
    public UnifiedResp<String> updateArticleTopAndFeatured(@Valid @RequestBody ArticleTopFeaturedDTO articleTopFeaturedDTO) {
        return articleAdminService.updateArticleTopAndFeatured(articleTopFeaturedDTO);
    }
}
