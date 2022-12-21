package search.engine.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import search.engine.model.Page;
import search.engine.repository.PageRepository;

import javax.transaction.Transactional;

@Service
public class DaoPageServiceImpl implements IDaoPageService {

    @Autowired
    private final PageRepository pageRepository;

    public DaoPageServiceImpl(PageRepository pageRepository) {
        this.pageRepository = pageRepository;
    }

    @Override
    @Transactional
    public void savePage(Page aPage) {
        pageRepository.save(aPage);
    }
}
