package com.donts.blog.article;

import com.donts.dto.ArticleTopFeaturedDTO;
import com.donts.dto.ConditionDTO;
import com.donts.response.PageResult;
import com.donts.response.UnifiedResp;
import com.donts.vo.ArticleAdminVO;

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
}
