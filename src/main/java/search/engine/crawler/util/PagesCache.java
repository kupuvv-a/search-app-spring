package search.engine.crawler.util;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import lombok.extern.slf4j.Slf4j;
import search.engine.model.Page;

import java.util.concurrent.TimeUnit;

@Slf4j
public class PagesCache {

    private static final long MAX_CACHE_SIZE = 100 * 100;
    private static final long SESSIONS_LIFETIME_IN_MINUTES = 60;

    private static final Cache<String, Page> CHECKED_PAGES_CACHE;

    static {
        CHECKED_PAGES_CACHE = CacheBuilder.newBuilder()
                .maximumSize(MAX_CACHE_SIZE)
                .expireAfterWrite(SESSIONS_LIFETIME_IN_MINUTES, TimeUnit.MINUTES)
                .build();
    }

    public static void putIntoCache(String path, Page page) {
        log.debug("{}: Page {} was added into cache", path, page);
        CHECKED_PAGES_CACHE.put(path, page);
    }

    public static Page getFromCache(String path) {
        log.debug("{}: Try to get Page from cache", path);
        return CHECKED_PAGES_CACHE.getIfPresent(path);
    }
}
