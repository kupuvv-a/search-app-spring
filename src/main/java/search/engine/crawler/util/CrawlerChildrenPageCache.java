package search.engine.crawler.util;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import lombok.extern.slf4j.Slf4j;

import java.util.Set;
import java.util.concurrent.TimeUnit;

@Slf4j
public class CrawlerChildrenPageCache {

    private static final long MAX_CACHE_SIZE = 1000 * 1000;
    private static final long LIFETIME_IN_MINUTES = 35;

    private static final Cache<String, Set<String>> PAGES_CHILDREN_CACHE;

    static {
        PAGES_CHILDREN_CACHE = CacheBuilder.newBuilder()
                .maximumSize(MAX_CACHE_SIZE)
                .expireAfterWrite(LIFETIME_IN_MINUTES, TimeUnit.MINUTES)
                .build();
    }

    public static void putPageChildrenIntoCache(String pageUrl, Set<String> children) {
//        log.debug("{}: Path {} was added into cache", pageUrl, children);
        PAGES_CHILDREN_CACHE.put(pageUrl, children);
    }

    public static Set<String> getChildrenFromCache(String pageUrl) {
//        log.debug("{}: Try to get children from cache", pageUrl);
        return PAGES_CHILDREN_CACHE.getIfPresent(pageUrl);
    }

}
