package search.engine.dao;

import search.engine.model.Page;

import java.util.List;

public interface IDaoPageService {

    Page savePage(Page aPage);

    Page getPageByPath(String path);

    void updatePage(Page page);

    void updatePageContentByPath(String path, String content, int code);

    void deleteAllPages();

    List<Page> getPagesBySiteId(long siteId);
}
