package search.engine.crawler;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import search.engine.crawler.service.IPagesParserService;
import search.engine.model.Page;
import search.engine.model.Site;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.RecursiveAction;

@Slf4j
public class CrawlerTask extends RecursiveAction {

    private final String url;
    private final Site site;
    private final IPagesParserService pageParserService;

    public CrawlerTask(String url, Site site, IPagesParserService pageParserService) {
        this.url = url;
        this.site = site;
        this.pageParserService = pageParserService;
    }

    @SneakyThrows
    @Override
    protected void compute() {
        log.info("checking url: {}, on site url: {}", url, site.getUrl());
        Set<Page> children = pageParserService.getPageChildren(createPage(this.site, this.url), this.site);
        if (children != null && !children.isEmpty()) {
            Set<CrawlerTask> actions = new HashSet<>();
            children.forEach(child -> actions.add(new CrawlerTask(child.getPath(), this.site, pageParserService)));
            invokeAll(actions);
        }
    }


    private Page createPage(Site site, String path) {
        final Page newPage = new Page();
        newPage.setSite(site);
        newPage.setPath(path);
        return newPage;
    }
}
