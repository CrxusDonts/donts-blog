package com.donts.blog.fe.controller;

import com.donts.blog.service.ArticleService;
import com.donts.dto.ArticlePasswordDTO;
import com.donts.response.PageResult;
import com.donts.response.UnifiedResp;
import com.donts.vo.ArticleCardVO;
import com.donts.vo.ArticleVO;
import com.donts.vo.TimeLineArticleVO;
import com.donts.vo.TopAndFeaturedArticlesVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedList;

@RestController
@Tag(name = "ArticleController", description = "文章模块")
@RequestMapping("/fe/api")
public class ArticleController {

    @Resource
    private ArticleService articleService;


    @Operation(summary = "获取置顶和推荐文章")
    @GetMapping("/article/top-and-featured")
    public UnifiedResp<TopAndFeaturedArticlesVO> listTopAndFeaturedArticles() {
        return UnifiedResp.success(articleService.listTopAndFeaturedArticles());
    }

    //articles?page=2&size=10
    @Operation(summary = "分页获取文章列表")
    @GetMapping("/articles")
    public UnifiedResp<PageResult<ArticleCardVO>> pageArticles(@RequestParam(value = "page", defaultValue = "1") Integer page, @RequestParam(value = "size", defaultValue = "10") Integer size) {
        return UnifiedResp.success(articleService.pageArticles(page, size));

    }

    @Operation(summary = "根据分类id分页获取文章")
    @GetMapping("/articles/category/{categoryId}")
    public UnifiedResp<PageResult<ArticleCardVO>> listArticlesByCategoryId(@PathVariable("categoryId") Long categoryId, @RequestParam(value = "page", defaultValue = "1") Integer page, @RequestParam(value = "size", defaultValue = "10") Integer size) {
        return UnifiedResp.success(articleService.pageArticlesByCategoryId(categoryId, page, size));
    }

    @Operation(summary = "根据id获取文章")
    @GetMapping("/articles/{articleId}")
    public UnifiedResp<ArticleVO> findArticleById(@PathVariable("articleId") Long articleId) {
        return articleService.findArticleById(articleId);
    }

    @Operation(summary = "校验文章访问密码")
    @PostMapping("/articles/access")
    public UnifiedResp<String> accessArticle(@Valid @RequestBody ArticlePasswordDTO articlePasswordDTO) {
        return articleService.accessArticle(articlePasswordDTO);
    }

    @Operation(summary = "根据标签id获取文章")
    @GetMapping("/articles/tag/{tagId}")
    public UnifiedResp<PageResult<ArticleCardVO>> pageArticlesByTagId(@PathVariable("tagId") Long tagId,
                                                                      @RequestParam(value = "page", defaultValue = "1"
                                                                      ) Integer page, @RequestParam(value = "size",
            defaultValue = "10") Integer size) {
        return articleService.pageArticlesByTagId(tagId, page, size);
    }

    @Operation(summary = "按照时间线获取所有文章")
    @GetMapping("/articles/timeline")
    public UnifiedResp<LinkedList<TimeLineArticleVO>> pageArticlesByTimeline(@RequestParam(value = "page",
            defaultValue = "1") Integer page, @RequestParam(value = "size", defaultValue = "10") Integer size) {
        return articleService.listArticlesByTimeline(page, size);
    }

}
