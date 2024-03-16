package com.donts.consts;

public final class CacheNameConst {
    /**
     * 用户角色缓存
     */
    public static final String USER_ROLE_CACHE_NAME = "userRoleCache_";
    /**
     * 默认KEY，SpEL表达式
     */
    public static final String DEFAULT_KEY = "'DEFAULT_KEY'";
    /**
     * 用户角色缓存 with Resource and Menu
     */
    public static final String USER_ROLE_CACHE_NAME_WITH_RESOURCE_AND_MENU = "userRoleCacheWithResourceAndMenu_";
    /**
     * 首页置顶和推荐文章缓存
     */
    public static final String INDEX_TOP_AND_RECOMMEND_ARTICLE_CACHE_NAME = "indexTopAndRecommendArticleCache";
    /**
     * 文章阅读量缓存
     */
    public static final String ARTICLE_VIEWS_COUNT = "articleViewsCount_";
    /**
     * 文章vo缓存
     */
    public static final String ARTICLE_VO_CACHE_NAME = "articleVoCache_";
    /**
     * #articleId SPEl表达式
     */
    public static final String ARTICLE_ID_SPEL_KEY = "#articleId";
    /**
     * 文章CardVO缓存
     */
    public static final String ARTICLE_CARD_VO_CACHE_NAME = "articleCardVoCache_";

    private CacheNameConst() {
    }
}
