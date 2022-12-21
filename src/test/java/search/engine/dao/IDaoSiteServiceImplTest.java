package search.engine.dao;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import search.engine.repository.SiteRepository;

@SpringBootTest
public class IDaoSiteServiceImplTest {

    @Autowired
    SiteRepository siteRepository;

    @Test
    void teatDeleteSitesTest() {
        IDaoSiteService service = new IDaoSiteServiceImpl(siteRepository);
        service.deleteAllSites();
    }

}