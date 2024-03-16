package com.donts.blog.admin.controller;

import com.donts.blog.aop.anno.OptLog;
import com.donts.blog.article.ArticleAdminService;
import com.donts.dto.ArticleTopFeaturedDTO;
import com.donts.dto.ConditionDTO;
import com.donts.dto.LogicDeleteStatusDTO;
import com.donts.response.PageResult;
import com.donts.response.UnifiedResp;
import com.donts.vo.ArticleAdminVO;
import com.donts.vo.ArticleAdminViewVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.donts.enums.OptTypeEnum.DELETE;
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

    @Operation(summary = "删除或者恢复文章")
    @PutMapping("/logicDelete")
    public UnifiedResp<String> updateArticleDeleteLogically(@Valid @RequestBody LogicDeleteStatusDTO logicDeleteStatusDTO) {
        return articleAdminService.updateArticleDeleteLogically(logicDeleteStatusDTO);
    }

    @OptLog(optType = DELETE)
    @Operation(summary = "物理删除文章")
    @DeleteMapping
    public UnifiedResp<String> deleteArticles(@RequestBody List<Long> articleIds) {
        return articleAdminService.deleteArticles(articleIds);
    }

    @Operation(summary = "根据id查看后台文章")
    @GetMapping("/{articleId}")
    public UnifiedResp<ArticleAdminViewVO> getArticleBackById(@PathVariable("articleId") Long articleId) {
        return articleAdminService.getArticleByIdForAdmin(articleId);
    }

}
