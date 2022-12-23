package search.engine.crawler;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import search.engine.crawler.service.IPagesChildrenParserService;
import search.engine.dao.IDaoPageService;
import search.engine.model.Page;
import search.engine.model.Site;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.RecursiveAction;

@Slf4j
public class CrawlerTask extends RecursiveAction {

    private final String url;
    private final Site site;
    private final IDaoPageService daoPageService;
    private final IPagesChildrenParserService pageParserService;

    public CrawlerTask(String url, Site site, IDaoPageService daoPageService, IPagesChildrenParserService pageParserService) {
        this.url = url;
        this.site = site;
        this.daoPageService = daoPageService;
        this.pageParserService = pageParserService;
    }

    @SneakyThrows
    @Override
    protected void compute() {

        final Page newPage = new Page();
        newPage.setSite(this.site);
        newPage.setPath(this.url);

        Set<Page> children = pageParserService.parsePageAndGetChildrenPages(newPage, this.site);
        if (children != null && !children.isEmpty()) {
            Set<CrawlerTask> actions = new HashSet<>();
            children.forEach(child -> actions.add(new CrawlerTask(child.getPath(), this.site, daoPageService, pageParserService)));
            invokeAll(actions);
        }
    }
}
