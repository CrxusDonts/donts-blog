package com.donts.blog.article;

import com.donts.dto.ArticleTopFeaturedDTO;
import com.donts.dto.ConditionDTO;
import com.donts.dto.LogicDeleteStatusDTO;
import com.donts.response.PageResult;
import com.donts.response.UnifiedResp;
import com.donts.vo.ArticleAdminVO;
import com.donts.vo.ArticleAdminViewVO;

import java.util.List;

public interface ArticleAdminService {
    /**
     * 获取后台文章
     *
     * @param conditionDTO 查询条件
     * @return 文章列表
     */
    UnifiedResp<PageResult<ArticleAdminVO>> listArticlesForAdmin(ConditionDTO conditionDTO);

    /**
     * 修改文章是否置顶和推荐
     */
    UnifiedResp<String> updateArticleTopAndFeatured(ArticleTopFeaturedDTO articleTopFeaturedDTO);

    /**
     * 删除或者恢复文章
     *
     * @param logicDeleteStatusDTO 逻辑删除状态
     * @return 操作结果
     */
    UnifiedResp<String> updateArticleDeleteLogically(LogicDeleteStatusDTO logicDeleteStatusDTO);

    /**
     * 物理删除文章
     *
     * @param articleIds 文章id列表
     * @return 操作结果
     */
    UnifiedResp<String> deleteArticles(List<Long> articleIds);

    /**
     * 根据id查看后台文章
     *
     * @param articleId 文章id
     * @return 文章详情
     */
    UnifiedResp<ArticleAdminViewVO> getArticleByIdForAdmin(Long articleId);
}
