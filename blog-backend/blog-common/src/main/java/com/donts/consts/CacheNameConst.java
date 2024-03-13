package com.donts.consts;

public final class CacheNameConst {
    private CacheNameConst() {
    }

    /**
     * 用户角色缓存
     */
    public static final String USER_ROLE_CACHE_NAME = "userRoleCache";

    /**
     * 用户角色缓存 with Resource and Menu
     */
    public static final String USER_ROLE_CACHE_NAME_WITH_RESOURCE_AND_MENU = "userRoleCacheWithResourceAndMenu";

    /**
     * 首页置顶和推荐文章缓存
     */
    public static final String INDEX_TOP_AND_RECOMMEND_ARTICLE_CACHE_NAME = "indexTopAndRecommendArticleCache";
    /**
     * 私密文章的可访问人员缓存
     */
    public static final String PRIVATE_ARTICLE_ACCESSIBLE_USER_CACHE_NAME = "privateArticleAccessibleUserCache";
    /**
     * 文章阅读量缓存
     */
    public static final String ARTICLE_VIEWS_COUNT = "articleViewsCount";

    /**
     * 文章vo缓存
     */
    public static final String ARTICLE_VO_CACHE_NAME = "articleVoCache";
    /**
     * 文章CardVO缓存
     */
    public static final String ARTICLE_CARD_VO_CACHE_NAME = "articleCardVoCache";
}
