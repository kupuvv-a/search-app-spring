package search.engine.crawler.util;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import search.engine.model.Page;

import java.util.concurrent.TimeUnit;

public class CrawlerPageCache {
    private static final long MAX_CACHE_SIZE = 1000;
    private static final long LIFETIME_IN_MINUTES = 5;

    private static final Cache<String, Page> PAGES;

    static {
        PAGES = CacheBuilder.newBuilder()
                .maximumSize(MAX_CACHE_SIZE)
                .expireAfterWrite(LIFETIME_IN_MINUTES, TimeUnit.MINUTES)
                .build();
    }

    public static void putPageChildrenIntoCache(String url, Page aPage) {
//        log.debug("{}: Page {} was added into cache", url, aPage);
        PAGES.put(url, aPage);
    }

    public static Page getChildrenFromCache(String url) {
//        log.debug("{}: Try to get Page from cache", url);
        return PAGES.getIfPresent(url);
    }
}
