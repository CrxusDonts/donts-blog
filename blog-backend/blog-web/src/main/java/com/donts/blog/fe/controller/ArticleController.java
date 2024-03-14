package com.donts.blog.fe.controller;

import com.donts.blog.service.ArticleService;
import com.donts.dto.ArticlePasswordDTO;
import com.donts.response.PageResult;
import com.donts.response.UnifiedResp;
import com.donts.vo.ArticleCardVO;
import com.donts.vo.ArticleVO;
import com.donts.vo.TopAndFeaturedArticlesVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

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
    public UnifiedResp<PageResult<ArticleCardVO>> pageArticles(@RequestParam(value = "page", defaultValue = "1") Integer page,
                                                               @RequestParam(value = "size", defaultValue = "10") Integer size) {
        return UnifiedResp.success(articleService.pageArticles(page, size));

    }

    @Operation(summary = "根据分类id分页获取文章")
    @GetMapping("/articles/category/{categoryId}")
    public UnifiedResp<PageResult<ArticleCardVO>> listArticlesByCategoryId(@PathVariable("categoryId") Long categoryId,
                                                                           @RequestParam(value = "page",
                                                                                   defaultValue = "1") Integer page,
                                                                           @RequestParam(value = "size",
                                                                                   defaultValue = "10") Integer size) {
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
                                                                      @RequestParam(value = "page",
                                                                              defaultValue = "1") Integer page,
                                                                      @RequestParam(value = "size",
                                                                              defaultValue = "10") Integer size) {
        return articleService.pageArticlesByTagId(tagId, page, size);
    }

//    @Operation(summary = "根据id获取文章")
//    @GetMapping("/article/{articleId}")
//    public UnifiedResp<ArticleCardVO> getArticleById(@PathVariable("articleId") Long articleId) {
//        return UnifiedResp.success(articleService.getArticleById(articleId));
//    }
//
//    @Operation(summary = "根据分类id更新文章")
//    @PutMapping("/article/{articleId}")
//    public UnifiedResp<ArticleCardVO> updateArticleById(@PathVariable("articleId") Long articleId, @RequestBody
//    ArticleCardVO articleCardVO) {
//        return UnifiedResp.success(articleService.updateArticleById(articleId, articleCardVO));
//    }
//
//    @Operation(summary = "根据id删除文章")
//    @DeleteMapping("/article/{articleId}")
//    public UnifiedResp<ArticleCardVO> deleteArticleById(@PathVariable("articleId") Long articleId) {
//        return UnifiedResp.success(articleService.deleteArticleById(articleId));
//    }
//
//    @Operation(summary = "新增文章")
//    @PostMapping("/article")
//    public UnifiedResp<ArticleCardVO> addArticle(@RequestBody ArticleCardVO articleCardVO) {
//        return UnifiedResp.success(articleService.addArticle(articleCardVO));
//    }

    //    @ApiOperation("根据分类id获取文章")
//    @GetMapping("/articles/categoryId")
//    public ResultVO<PageResultDTO<ArticleCardDTO>> getArticlesByCategoryId(@RequestParam Integer categoryId) {
//        return ResultVO.ok(articleService.listArticlesByCategoryId(categoryId));
//    }
//
//    @ApiOperation("根据id获取文章")
//    @GetMapping("/articles/{articleId}")
//    public ResultVO<ArticleDTO> getArticleById(@PathVariable("articleId") Integer articleId) {
//        return ResultVO.ok(articleService.getArticleById(articleId));
//    }
//
//    @ApiOperation("校验文章访问密码")
//    @PostMapping("/articles/access")
//    public ResultVO<?> accessArticle(@Valid @RequestBody ArticlePasswordVO articlePasswordVO) {
//        articleService.accessArticle(articlePasswordVO);
//        return ResultVO.ok();
//    }
//
//    @ApiOperation("根据标签id获取文章")
//    @GetMapping("/articles/tagId")
//    public ResultVO<PageResultDTO<ArticleCardDTO>> listArticlesByTagId(@RequestParam Integer tagId) {
//        return ResultVO.ok(articleService.listArticlesByTagId(tagId));
//    }
//
//    @ApiOperation("获取所有文章归档")
//    @GetMapping("/archives/all")
//    public ResultVO<PageResultDTO<ArchiveDTO>> listArchives() {
//        return ResultVO.ok(articleService.listArchives());
//    }
//
//    @ApiOperation("获取后台文章")
//    @GetMapping("/admin/articles")
//    public ResultVO<PageResultDTO<ArticleAdminDTO>> listArticlesAdmin(ConditionVO conditionVO) {
//        return ResultVO.ok(articleService.listArticlesAdmin(conditionVO));
//    }
//
//    @OptLog(optType = SAVE_OR_UPDATE)
//    @ApiOperation("保存和修改文章")
//    @PostMapping("/admin/articles")
//    public ResultVO<?> saveOrUpdateArticle(@Valid @RequestBody ArticleVO articleVO) {
//        articleService.saveOrUpdateArticle(articleVO);
//        return ResultVO.ok();
//    }
//
//    @OptLog(optType = UPDATE)
//    @ApiOperation("修改文章是否置顶和推荐")
//    @PutMapping("/admin/articles/topAndFeatured")
//    public ResultVO<?> updateArticleTopAndFeatured(@Valid @RequestBody ArticleTopFeaturedVO articleTopFeaturedVO) {
//        articleService.updateArticleTopAndFeatured(articleTopFeaturedVO);
//        return ResultVO.ok();
//    }
//
//    @ApiOperation("删除或者恢复文章")
//    @PutMapping("/admin/articles")
//    public ResultVO<?> updateArticleDelete(@Valid @RequestBody DeleteVO deleteVO) {
//        articleService.updateArticleDelete(deleteVO);
//        return ResultVO.ok();
//    }
//
//    @OptLog(optType = DELETE)
//    @ApiOperation(value = "物理删除文章")
//    @DeleteMapping("/admin/articles/delete")
//    public ResultVO<?> deleteArticles(@RequestBody List<Integer> articleIds) {
//        articleService.deleteArticles(articleIds);
//        return ResultVO.ok();
//    }
//
//    @OptLog(optType = UPLOAD)
//    @ApiOperation("上传文章图片")
//    @ApiImplicitParam(name = "file", value = "文章图片", required = true, dataType = "MultipartFile")
//    @PostMapping("/admin/articles/images")
//    public ResultVO<String> saveArticleImages(MultipartFile file) {
//        return ResultVO.ok(uploadStrategyContext.executeUploadStrategy(file, FilePathEnum.ARTICLE.getPath()));
//    }
//
//    @ApiOperation("根据id查看后台文章")
//    @ApiImplicitParam(name = "articleId", value = "文章id", required = true, dataType = "Integer")
//    @GetMapping("/admin/articles/{articleId}")
//    public ResultVO<ArticleAdminViewDTO> getArticleBackById(@PathVariable("articleId") Integer articleId) {
//        return ResultVO.ok(articleService.getArticleByIdAdmin(articleId));
//    }
//
//    @OptLog(optType = UPLOAD)
//    @ApiOperation(value = "导入文章")
//    @PostMapping("/admin/articles/import")
//    public ResultVO<?> importArticles(MultipartFile file, @RequestParam(required = false) String type) {
//        articleImportStrategyContext.importArticles(file, type);
//        return ResultVO.ok();
//    }
//
//    @OptLog(optType = EXPORT)
//    @ApiOperation(value = "导出文章")
//    @ApiImplicitParam(name = "articleIdList", value = "文章id", required = true, dataType = "List<Integer>")
//    @PostMapping("/admin/articles/export")
//    public ResultVO<List<String>> exportArticles(@RequestBody List<Integer> articleIds) {
//        return ResultVO.ok(articleService.exportArticles(articleIds));
//    }
//
//    @ApiOperation(value = "搜索文章")
//    @GetMapping("/articles/search")
//    public ResultVO<List<ArticleSearchDTO>> listArticlesBySearch(ConditionVO condition) {
//        return ResultVO.ok(articleService.listArticlesBySearch(condition));
//    }


}
