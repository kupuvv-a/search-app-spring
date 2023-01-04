package search.engine.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import search.engine.config.SitesFromConfig;
import search.engine.dao.IDaoPageService;
import search.engine.dao.IDaoSiteService;
import search.engine.model.Site;
import search.engine.model.statistics.DetailedStatisticsItem;
import search.engine.model.statistics.StatisticsData;
import search.engine.model.statistics.StatisticsResponse;
import search.engine.model.statistics.TotalStatistics;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

@Service
@RequiredArgsConstructor
public class StatisticsServiceImpl implements IStatisticsService {

    private final Random random = new Random();
    private final SitesFromConfig sitesFromConfig;

    private final IDaoSiteService daoSiteService;
    private final IDaoPageService daoPageService;


    @Override
    public StatisticsResponse getStatistics() {
        String[] statuses = {"INDEXED", "FAILED", "INDEXING"};
        String[] errors = {
                "Ошибка индексации: главная страница сайта не доступна",
                "Ошибка индексации: сайт не доступен",
                ""
        };

        List<Site> sites = daoSiteService.getAllSites();

        TotalStatistics total = new TotalStatistics();
        total.setSites(sites.size());
        total.setIndexing(true);
        List<DetailedStatisticsItem> detailed = new ArrayList<>();

        AtomicInteger pages = new AtomicInteger();
        sites.forEach(site -> {

            DetailedStatisticsItem item = new DetailedStatisticsItem();
            item.setName(site.getName());
            item.setUrl(site.getUrl());
            item.setPages(daoPageService.getPagesBySiteId(site.getId()).size());//разница?
            item.setLemmas(0);//пока0
            item.setStatus(site.getStatus());
            item.setError(site.getLastError());
            item.setStatusTime(site.getStatusTime().getTime());

            pages.addAndGet(daoPageService.getPagesBySiteId(site.getId()).size());
            total.setPages(pages.get());//разница?
            total.setLemmas(total.getLemmas());//пока0
            detailed.add(item);

        });

        StatisticsData data = new StatisticsData();
        data.setTotal(total);
        data.setDetailed(detailed);

        StatisticsResponse response = new StatisticsResponse();
        response.setStatistics(data);
        response.setResult(true);

        return response;
    }
}
