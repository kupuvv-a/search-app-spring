package search.engine.dao;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import search.engine.repository.PageRepository;

@SpringBootTest
public class DaoPageServiceImplTest {

    @Autowired
    PageRepository pageRepository;

    @Test
    void teatDeleteSitesTest() {
        IDaoPageService service = new DaoPageServiceImpl(pageRepository);
        service.deleteAllPages();
    }

}