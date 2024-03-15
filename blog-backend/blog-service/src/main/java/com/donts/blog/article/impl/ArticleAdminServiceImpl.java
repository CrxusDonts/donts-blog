package com.donts.blog.article.impl;

import com.alicp.jetcache.anno.CacheInvalidate;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.donts.blog.article.*;
import com.donts.blog.entity.Article;
import com.donts.blog.entity.ArticleTag;
import com.donts.blog.entity.Tag;
import com.donts.dto.ArticleTopFeaturedDTO;
import com.donts.dto.ConditionDTO;
import com.donts.response.PageResult;
import com.donts.response.UnifiedResp;
import com.donts.vo.ArticleAdminVO;
import com.donts.vo.TagVO;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

import static com.donts.consts.CacheNameConst.*;

@Service
@Slf4j
public class ArticleAdminServiceImpl implements ArticleAdminService {
    private static final String LOG_PREFIX = "ArticleAdminServiceImpl";

    @Resource
    private ArticleService articleService;

    @Resource
    private ArticleTagService articleTagService;

    @Resource
    private TagService tagService;

    @Resource
    private CategoryService categoryService;

    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    @Override
    public UnifiedResp<PageResult<ArticleAdminVO>> listArticlesForAdmin(ConditionDTO conditionDTO) {
        Page<Article> page = new Page<>(conditionDTO.getPage(), conditionDTO.getPageSize());
        LambdaQueryWrapper<Article> queryWrapper = new LambdaQueryWrapper<Article>()
                .eq(Objects.nonNull(conditionDTO.getIsDelete()), Article::getIsDelete, conditionDTO.getIsDelete())
                .like(StringUtils.isNotBlank(conditionDTO.getKeywords()), Article::getArticleTitle,
                        conditionDTO.getKeywords())
                .eq(Objects.nonNull(conditionDTO.getStatus()), Article::getStatus, conditionDTO.getStatus())
                .eq(Objects.nonNull(conditionDTO.getCategoryId()), Article::getCategoryId, conditionDTO.getCategoryId())
                .eq(Objects.nonNull(conditionDTO.getType()), Article::getType, conditionDTO.getType())
                //startTime and endTime are not null, and the creation time of the article is between startTime and
                // endTime
                .between(Objects.nonNull(conditionDTO.getStartTime()) && Objects.nonNull(conditionDTO.getEndTime()),
                        Article::getCreateTime, conditionDTO.getStartTime(), conditionDTO.getEndTime())
                .orderByDesc(Article::getIsTop, Article::getIsFeatured, Article::getId);
        // If tagId is not null, you need to perform a subquery to get the article ids associated with the tag
        if (Objects.nonNull(conditionDTO.getTagId())) {
            List<Long> articleIds = articleTagService.list(new LambdaQueryWrapper<ArticleTag>()
                            .select(ArticleTag::getArticleId)
                            .eq(ArticleTag::getTagId, conditionDTO.getTagId()))
                    .stream().map(ArticleTag::getArticleId).toList();
            if (!articleIds.isEmpty()) {
                queryWrapper.in(Article::getId, articleIds);
            } else {
                return UnifiedResp.success(PageResult.of(Collections.emptyList(), 0L, 0L, page.getCurrent(),
                        page.getSize()));
            }
        }
        log.info("{} -- listArticlesForAdmin target SQL: {}", LOG_PREFIX, queryWrapper.getTargetSql());
        articleService.page(page, queryWrapper);
        // Convert the result to ArticleAdminVO and return
        List<ArticleAdminVO> articleAdminVOList = page.getRecords().stream()
                .map(this::convertToArticleAdminVO) // You need to implement this method
                .toList();

        updateArticleViewsCount(articleAdminVOList);
        return UnifiedResp.success(PageResult.of(articleAdminVOList, page.getTotal(), page.getPages(),
                page.getCurrent(), page.getSize()));
    }

    @Override
    @CacheInvalidate(name = INDEX_TOP_AND_RECOMMEND_ARTICLE_CACHE_NAME, key = DEFAULT_KEY
            , condition = "#result.status == 0")
    public UnifiedResp<String> updateArticleTopAndFeatured(ArticleTopFeaturedDTO articleTopFeaturedDTO) {
        Article article = Article.builder()
                .id(articleTopFeaturedDTO.getArticleId())
                .isTop(articleTopFeaturedDTO.getIsTop())
                .isFeatured(articleTopFeaturedDTO.getIsFeatured())
                .build();
        boolean isSuccess = articleService.updateById(article);
        return isSuccess ? UnifiedResp.success("修改成功") : UnifiedResp.fail("修改失败");
    }

    /**
     * 填充文章浏览量
     *
     * @param articleAdminVOList 文章列表
     */
    private void updateArticleViewsCount(List<ArticleAdminVO> articleAdminVOList) {
        Set<ZSetOperations.TypedTuple<Object>> rangeWithScores =
                redisTemplate.opsForZSet().rangeWithScores(ARTICLE_VIEWS_COUNT, 0, -1);

        Map<Long, Double> viewsCountMap = (rangeWithScores != null) ? rangeWithScores.stream()
                .collect(Collectors.toMap(
                        item -> (Long) item.getValue(),
                        item -> Optional.ofNullable(item.getScore()).orElse(0.0)
                )) : new HashMap<>();
        articleAdminVOList.forEach(item ->
        {
            Double viewsCount = viewsCountMap.get(item.getArticleId());
            if (Objects.nonNull(viewsCount)) {
                item.setViewsCount(viewsCount.intValue());
            }
        });
    }

    private ArticleAdminVO convertToArticleAdminVO(Article article) {
        return ArticleAdminVO.builder()
                .articleId(article.getId())
                .articleTitle(article.getArticleTitle())
                .articleCover(article.getArticleCover())
                .createTime(article.getCreateTime())
                .isTop(article.getIsTop())
                .isFeatured(article.getIsFeatured())
                .status(article.getStatus())
                .category(categoryService.getById(article.getCategoryId()))
                .type(article.getType())
                .tagVOS(tagService.listTagsByArticleId(article.getId()).stream()
                        .map(this::convertToTagVO)
                        .toList())
                .isDelete(article.getIsDelete())
                .build();

    }

    private TagVO convertToTagVO(Tag tag) {
        return TagVO.builder()
                .tagId(tag.getId())
                .tagName(tag.getTagName())
                .build();
    }
}
