package search.engine.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Connection;
import org.springframework.stereotype.Service;
import search.engine.config.ConfigSite;
import search.engine.config.SitesFromConfig;
import search.engine.crawler.CrawlerTask;
import search.engine.crawler.service.IJsoupService;
import search.engine.crawler.service.IPagesParserService;
import search.engine.dao.IDaoSiteService;
import search.engine.message.ResultResponse;
import search.engine.model.Site;
import search.engine.model.StatusType;

import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.concurrent.ForkJoinPool;

@Service
@RequiredArgsConstructor
@Slf4j
public class ServiceIndexImpl implements IServiceIndex {

    private final SitesFromConfig configSites;
    private final IDaoSiteService daoSiteService;
    private final IPagesParserService pagesChildrenParserService;
    private final IJsoupService jsoupService;

    @Override
    public ResultResponse runIndexing() {

        checkPropertyAndDbUpdate();

        daoSiteService.getAllSites().forEach(site -> {

            log.info("run indexing site url : {}", site.getUrl());

            site.setStatus(StatusType.INDEXING.name());
            site.setStatusTime(new Timestamp(System.currentTimeMillis()));
            daoSiteService.updateSiteStatus(site.getUrl(), StatusType.INDEXING.name());

            Connection.Response response = jsoupService.executeJsoupResponse(site.getUrl());
            if (response.statusCode() == 200) {
                ForkJoinPool forkJoinPool = new ForkJoinPool(10);
                forkJoinPool.invoke(new CrawlerTask(site.getUrl(), site, pagesChildrenParserService));
                daoSiteService.updateSiteStatus(site.getUrl(), StatusType.INDEXED.name());
            }
        });
        return new ResultResponse("true");
    }

    @Override
    public ResultResponse runIndexing(String url) {
        Site site = daoSiteService.getSiteByUrl(url);
        if (site == null) {
            site = createSiteFromUrl(url);
            daoSiteService.saveSite(site);
        }

        log.info("run indexing site url : {}", site.getUrl());
        daoSiteService.updateSiteStatus(site.getUrl(), StatusType.INDEXING.name());

        ForkJoinPool forkJoinPool = new ForkJoinPool(4);
        forkJoinPool.invoke(new CrawlerTask(site.getUrl(), site, pagesChildrenParserService));

        daoSiteService.updateSiteStatus(site.getUrl(), StatusType.INDEXED.name());
        return new ResultResponse("true");
    }


    private Site createSiteFromUrl(String url) {
        Site site = new Site();
        site.setUrl(url);
        site.setName(url.substring(7, 17));//todo replace this shit
        site.setStatusTime(new Timestamp(System.currentTimeMillis()));
        site.setLastError("null");
        site.setStatus(StatusType.INDEXING.name());
        return site;
    }

    private void checkPropertyAndDbUpdate() {
        configSites.getConfigSites().forEach(s -> {
            if (daoSiteService.getSiteByName(s.getName()) == null)
                daoSiteService.saveSite(createNewSiteFromConfig(s, StatusType.INDEXING));
        });
    }

    private Site createNewSiteFromConfig(ConfigSite s, StatusType statusType) {
        Site site = new Site();
        site.setUrl(s.getUrl());
        site.setName(s.getName());
        site.setStatusTime(new Timestamp(System.currentTimeMillis()));
        site.setLastError("null");
        site.setStatus(statusType.name());
        return site;
    }
}
