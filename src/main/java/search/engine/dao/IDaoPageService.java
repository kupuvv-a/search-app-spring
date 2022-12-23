package search.engine.dao;

import search.engine.model.Page;

public interface IDaoPageService {

    void savePage(Page aPage);

    Page getPageByPath(String path);

    void updatePageContentByPath(String path, String content, int code);

    void deleteAllPages();
}
