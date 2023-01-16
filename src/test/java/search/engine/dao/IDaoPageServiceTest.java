package search.engine.dao;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import search.engine.model.Page;
import search.engine.model.Site;
import search.engine.model.StatusType;
import search.engine.repository.PageRepository;
import search.engine.repository.SiteRepository;

import java.sql.Timestamp;
import java.util.List;
import java.util.UUID;

@SpringBootTest
public class IDaoPageServiceTest {


    @Autowired
    SiteRepository siteRepository;

    @Autowired
    PageRepository pageRepository;


    @Test
    void deletePages() {
        IDaoPageService daoPageService = new DaoPageServiceImpl(pageRepository);
        daoPageService.deleteAllPages();
    }

    @Test
    void getPagesBySiteId() {
        IDaoPageService daoPageService = new DaoPageServiceImpl(pageRepository);
        List<Page> pages = daoPageService.getPagesBySiteId(8417);
        pages.forEach(page -> System.out.println(page.getPath()));
    }

    @Test
    void updatePage() throws InterruptedException {

        IDaoSiteService daoSiteService = new DaoSiteServiceImpl(siteRepository);
        final Site site = new Site();
        final String url = "http://test.example";
        site.setName("test");
        site.setUrl(url);
//        site.setStatusTime(new Date(System.currentTimeMillis()));
        site.setStatusTime(new Timestamp(System.currentTimeMillis()));
        site.setLastError("null");
        site.setStatus(StatusType.INDEXING.name());

        Site siteFromDb = siteRepository.getSiteByUrl(url);
        if (siteFromDb == null) {
            daoSiteService.saveSite(site);
        }

        IDaoPageService daoPageService = new DaoPageServiceImpl(pageRepository);
        final Page page = new Page();
        final String path = "http://test.example/test";
        page.setSite(site);
        page.setPath(path);
        page.setContent("test");
        page.setCode(HttpStatus.OK.value());

        Page pageByPath = daoPageService.getPageByPath(path);
        if (pageByPath == null) {
            daoPageService.savePage(page);
        }

        Thread.sleep(1000);

        final Page pageFromDb = daoPageService.getPageByPath(path);
        Assertions.assertNotNull(pageFromDb);

        String newContent = UUID.randomUUID().toString();
        daoPageService.updatePageContentByPath(path, newContent, 200);
        Thread.sleep(1000);
        final Page updatedPage = daoPageService.getPageByPath(path);
        Assertions.assertNotNull(updatedPage);
        Assertions.assertEquals(updatedPage.getContent(), newContent);
    }
}