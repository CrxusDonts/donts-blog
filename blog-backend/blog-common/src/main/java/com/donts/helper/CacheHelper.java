package com.donts.helper;

import com.alicp.jetcache.CacheManager;
import com.alicp.jetcache.SimpleCacheManager;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import static com.donts.consts.CacheNameConst.ARTICLE_CARD_VO_CACHE_NAME;
import static com.donts.consts.CacheNameConst.ARTICLE_VO_CACHE_NAME;


@Component
@Slf4j
public class CacheHelper {
    private static final String LOG_PREFIX = "CacheHelper";

    @Resource(name = "jcCacheManager", type = SimpleCacheManager.class)
    private CacheManager cacheManager;

    private CacheHelper() {
    }

    /**
     * 清除缓存By文章ID
     */
    public void clearCacheByArticleId(Long articleId) {
        List<String> cacheNames = List.of(
                ARTICLE_CARD_VO_CACHE_NAME,
                ARTICLE_VO_CACHE_NAME
        );
        log.info("{}: Clearing cache for article ID: {} using cache names: {}", LOG_PREFIX, articleId, cacheNames);
        cacheNames.stream()
                .map(cacheManager::getCache)
                .filter(Objects::nonNull)
                .forEach(cache -> cache.remove(articleId));

        log.info("{}: Cache cleared for article ID: {}", LOG_PREFIX, articleId);

    }


    /**
     * 清除缓存By文章IDs
     */
    public void clearCacheByArticleIds(List<Long> articleIds) {
        List<String> cacheNames = List.of(
                ARTICLE_CARD_VO_CACHE_NAME,
                ARTICLE_VO_CACHE_NAME
        );
        log.info("{}: Clearing cache for article IDs: {} using cache names: {}", LOG_PREFIX, articleIds, cacheNames);
        Set<Long> keySet = new HashSet<>(articleIds);
        cacheNames.stream()
                .map(cacheManager::getCache)
                .filter(Objects::nonNull)
                .forEach(cache -> cache.removeAll(keySet));

        log.info("{}: Cache cleared for article IDs: {}", LOG_PREFIX, keySet);
    }


}

