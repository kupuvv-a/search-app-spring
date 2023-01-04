package search.engine.dao;

import org.springframework.stereotype.Service;
import search.engine.model.Page;
import search.engine.repository.PageRepository;

import javax.transaction.Transactional;
import java.util.List;

@Service
public class DaoPageServiceImpl implements IDaoPageService {

    private final PageRepository pageRepository;

    public DaoPageServiceImpl(PageRepository pageRepository) {
        this.pageRepository = pageRepository;
    }

    @Override
    @Transactional
    public Page savePage(Page aPage) {
       return pageRepository.save(aPage);
    }

    @Override
    public void deleteAllPages() {
        pageRepository.deleteAll();
    }

    @Override
    public List<Page> getPagesBySiteId(long siteId) {
        return pageRepository.findPagesBySiteId(siteId);
    }

    @Override
    public Page getPageByPath(String url) {
        return pageRepository.findPageByUrl(url);
    }

    @Override
    public void updatePageContentByPath(String path, String content, int code) {
        pageRepository.updatePageContent(path, content, code);
    }

    @Override
    public void updatePage(Page page) {
        pageRepository.updatePage(page.getId(), page.getContent(), page.getCode());
    }
}
