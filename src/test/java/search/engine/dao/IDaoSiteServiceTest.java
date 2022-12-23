package search.engine.dao;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.internal.matchers.Equals;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.util.Assert;
import search.engine.model.Page;
import search.engine.model.Site;
import search.engine.model.StatusType;
import search.engine.repository.SiteRepository;

import java.sql.Date;
import java.util.UUID;

@SpringBootTest
public class IDaoSiteServiceTest {

    @Autowired
    SiteRepository siteRepository;

    @Test
    void deleteSites() {
        IDaoSiteService service = new DaoSiteServiceImpl(siteRepository);
        service.deleteAllSites();
    }

    @Test
    void getSiteByName() {
        IDaoSiteService service = new DaoSiteServiceImpl(siteRepository);
//        Assert.notNull(service.getSiteByName("Лента.ру").getName(), "find site");
        Assert.isNull(service.getSiteByName("123"), "null");
    }


    @Test
    void updatePage() throws InterruptedException {

        IDaoSiteService daoSiteService = new DaoSiteServiceImpl(siteRepository);
        final Site site = new Site();
        final String url = "http://test.example";
        site.setName("test");
        site.setUrl(url);
        site.setStatusTime(new Date(System.currentTimeMillis()));
        site.setLastError("null");
        site.setStatus(StatusType.INDEXING.name());

        Site siteFromDb = siteRepository.getSiteByUrl(url);
        if (siteFromDb == null) {
            daoSiteService.saveSite(site);
        }

        Thread.sleep(1000);

        daoSiteService.updateSiteStatus(url, StatusType.INDEXED.name());

        Thread.sleep(1000);

        final Site updatedSite = daoSiteService.getSiteByUrl(url);
        Assertions.assertNotNull(updatedSite);
        Assertions.assertEquals(updatedSite.getStatus(), StatusType.INDEXED.name());
    }

}