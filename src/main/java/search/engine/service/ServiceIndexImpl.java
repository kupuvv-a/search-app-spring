package search.engine.service;

import lombok.RequiredArgsConstructor;
import org.jsoup.Connection;
import org.springframework.stereotype.Service;
import search.engine.config.ConfigSite;
import search.engine.config.SitesFromConfig;
import search.engine.crawler.CrawlerTask;
import search.engine.crawler.service.IPagesChildrenParserService;
import search.engine.crawler.service.jsoup.IJsoupService;
import search.engine.dao.IDaoPageService;
import search.engine.dao.IDaoSiteService;
import search.engine.message.ResultResponse;
import search.engine.model.Site;
import search.engine.model.StatusType;

import java.sql.Date;
import java.util.concurrent.ForkJoinPool;

@Service
@RequiredArgsConstructor
public class ServiceIndexImpl implements IServiceIndex {

    private final SitesFromConfig configSites;
    private final IDaoPageService daoPageService;
    private final IDaoSiteService daoSiteService;
    private final IPagesChildrenParserService pagesChildrenParserService;
    private final IJsoupService jsoupService;

    @Override
    public ResultResponse runIndexing() {

        checkPropertyAndDbUpdate();

        daoSiteService.getAllSites().forEach(site -> {

            Connection.Response response = jsoupService.executeJsoupResponse(site.getUrl());
            if (response.statusCode() == 200) {
                ForkJoinPool forkJoinPool = new ForkJoinPool(10);
                forkJoinPool.invoke(new CrawlerTask(site.getUrl(), site, daoPageService, pagesChildrenParserService));
                daoSiteService.updateSiteStatus(site.getUrl(), StatusType.INDEXED.name());
            }
        });

        return new ResultResponse("true", null);
    }

    @Override
    public ResultResponse runIndexing(String url) {
        Site site = daoSiteService.getSiteByUrl(url);
        if (site == null) {
            site = createSiteFromUrl(url);
            daoSiteService.saveSite(site);
        }
        ForkJoinPool forkJoinPool = new ForkJoinPool(4);
        forkJoinPool.invoke(new CrawlerTask(
                daoSiteService.getSiteByUrl(url).getUrl()
                , daoSiteService.getSiteByUrl(url)
                , daoPageService
                , pagesChildrenParserService));


        return new ResultResponse("true", null);
    }

    private Site createSiteFromUrl(String url) {
        Site site = new Site();
        site.setUrl(url);
        site.setName(url.substring(7, 17));//todo replace this shit
        site.setStatusTime(new Date(System.currentTimeMillis()));
        site.setLastError("null");
        site.setStatus(StatusType.INDEXING.name());
        return site;
    }

    private void checkPropertyAndDbUpdate() {
        configSites.getConfigSites().forEach(s -> {
            if (daoSiteService.getSiteByName(s.getName()) == null)
                daoSiteService.saveSite(createSiteFromConfig(s, StatusType.INDEXING));
        });
    }

    private Site createSiteFromConfig(ConfigSite s, StatusType statusType) {
        Site site = new Site();
        site.setUrl(s.getUrl());
        site.setName(s.getName());
        site.setStatusTime(new Date(System.currentTimeMillis()));
        site.setLastError("null");
        site.setStatus(statusType.name());
        return site;
    }
}
