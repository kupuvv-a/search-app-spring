package search.engine.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import search.engine.config.SitesList;
import search.engine.crawler.CrawlerTask;
import search.engine.dao.IDaoPageService;
import search.engine.dao.IDaoSiteService;
import search.engine.message.ResultResponse;
import search.engine.model.Site;
import search.engine.model.StatusType;

import java.sql.Date;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.atomic.AtomicLong;

@Service
@RequiredArgsConstructor
public class ServiceIndexImpl implements IServiceIndex {

    private final SitesList configSites;
    private final IDaoPageService daoPageService;
    private final IDaoSiteService daoSiteService;

    @Override
    public ResultResponse runIndexing() {

        AtomicLong id = new AtomicLong();

        configSites.getSites().forEach(s -> {
            Site site = new Site();
            site.setUrl(s.getUrl());
            site.setName(s.getName());
            site.setStatusTime(new Date(System.currentTimeMillis()));
            site.setLastError("null");
            site.setStatus(StatusType.INDEXING.name());
            id.set(daoSiteService.saveSite(site));
        });

        ForkJoinPool forkJoinPool = new ForkJoinPool(4);
        forkJoinPool.invoke(new CrawlerTask(configSites.getSites().get(0).getUrl(), id.get(), daoPageService));

        return new ResultResponse("true", null);
    }
}
