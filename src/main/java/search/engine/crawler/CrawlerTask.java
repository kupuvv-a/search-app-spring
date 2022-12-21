package search.engine.crawler;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import search.engine.crawler.service.IPagesChildrenParserService;
import search.engine.crawler.service.PagesChildrenParserServiceImpl;
import search.engine.crawler.util.CrawlerChildrenPageCache;
import search.engine.dao.IDaoPageService;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.RecursiveAction;
import java.util.stream.Collectors;

@Slf4j
public class CrawlerTask extends RecursiveAction {

    private final String url;
    private final Set<String> checkedPage = new HashSet<>();
    private final IPagesChildrenParserService pageParserService;

    public CrawlerTask(String url) {
        this.url = url;
        this.pageParserService = new PagesChildrenParserServiceImpl(url);
    }

    @SneakyThrows
    @Override
    protected void compute() {

        Thread.sleep((int) (Math.random() * 100));

        Set<String> pages = pageParserService.parsePageAndGetChildren(url)
                .stream()
                .filter(s -> !checkedPage.contains(url))
                .filter(s -> s.startsWith(url) && (s.length() > url.length()))
                .collect(Collectors.toSet());

        checkedPage.addAll(pages);
        CrawlerChildrenPageCache.putPageChildrenIntoCache(url, pages);


        Set<String> children = CrawlerChildrenPageCache.getChildrenFromCache(url);
        if (!children.isEmpty() && !checkedPage.contains(url)) {

            Set<CrawlerTask> actions = new HashSet<>();
            children.forEach(url -> actions.add(new CrawlerTask(url)));

            invokeAll(actions);
        }
    }
}
