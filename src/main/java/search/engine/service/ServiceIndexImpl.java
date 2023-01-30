package search.engine.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Connection;
import org.springframework.stereotype.Service;
import search.engine.config.ConfigSite;
import search.engine.config.SitesFromConfig;
import search.engine.microservice.crawler.CrawlerTask;
import search.engine.microservice.crawler.service.IPagesParserService;
import search.engine.dao.IDaoSiteService;
import search.engine.microservice.jsoup.service.IJsoupService;
import search.engine.message.ResultResponse;
import search.engine.model.Site;
import search.engine.model.StatusType;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.Locale;
import java.util.concurrent.ForkJoinPool;

@Service
@RequiredArgsConstructor
@Slf4j
public class ServiceIndexImpl implements IServiceIndex {

    private final IPagesParserService pagesChildrenParserService;
    private final SitesFromConfig configSites;
    private final IDaoSiteService daoSiteService;
    private final IJsoupService jsoupService;

    @Override
    public ResultResponse runIndexing() {

        checkPropertyAndDbUpdate();
        //todo check indexing start
        daoSiteService.getAllSites().forEach(site -> {

            log.info("run indexing site url : {}", site.getUrl());
            site.setStatus(StatusType.INDEXING.name());
            site.setStatusTime(new Timestamp(System.currentTimeMillis()));
            daoSiteService.updateSiteStatus(site.getUrl(), StatusType.INDEXING.name());

            try {
                final Connection.Response response = jsoupService.executeJsoupResponse(site.getUrl());
                if (response.statusCode() == 200) { // может и не надо, подумай !
                    final ForkJoinPool forkJoinPool = new ForkJoinPool(10);
                    forkJoinPool.invoke(new CrawlerTask(site.getUrl(), site, pagesChildrenParserService));
                    daoSiteService.updateSiteStatus(site.getUrl(), StatusType.INDEXED.name());
                }
            } catch (IOException e) {
                site.setStatusTime(new Timestamp(System.currentTimeMillis()));
                site.setStatus(StatusType.FAILED.name());
                site.setLastError(e.getMessage().toLowerCase(Locale.ROOT));
                daoSiteService.updateSite(site);
            }
        });
        return new ResultResponse("true", null);
    }

    @Override
    public ResultResponse runIndexing(final String url) {
        Site site = daoSiteService.getSiteByUrl(url);
        if (site == null) {
            log.info("site url: {} was not found", url);
            try {
                final Connection.Response response = jsoupService.executeJsoupResponse(url);
                if (response.statusCode() == 200) {
                    site = createSiteFromUrl(url);
                    daoSiteService.saveSite(site);
                    log.info("new site was create : {}", site.getUrl());
                } else return new ResultResponse("false", "Bad site url: " + url);

            } catch (IOException e) {
                return new ResultResponse("false", e.getMessage());
            }
        }

        log.info("run indexing site url : {}", site.getUrl());
        if (!site.getStatus().equals(StatusType.INDEXING.name()))
            daoSiteService.updateSiteStatus(site.getUrl(), StatusType.INDEXING.name());

        final ForkJoinPool forkJoinPool = new ForkJoinPool(4);
        forkJoinPool.invoke(new CrawlerTask(site.getUrl(), site, pagesChildrenParserService));
        daoSiteService.updateSiteStatus(site.getUrl(), StatusType.INDEXED.name());
        return new ResultResponse("true", null);
    }


    private Site createSiteFromUrl(final String url) {
        final Site site = new Site();
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
                daoSiteService.saveSite(createNewSiteFromConfig(s));
        });
    }

    private Site createNewSiteFromConfig(final ConfigSite aSite) {
        final Site site = new Site();
        site.setUrl(aSite.getUrl());
        site.setName(aSite.getName());
        site.setStatusTime(new Timestamp(System.currentTimeMillis()));
        site.setLastError("null");
        site.setStatus(StatusType.INDEXING.name());
        return site;
    }
}
