package search.engine.crawler.service;

import search.engine.model.Page;
import search.engine.model.Site;

import java.io.IOException;
import java.util.Set;

public interface IPagesChildrenParserService {
    Set<Page> parsePageAndGetChildrenPages(Page page, Site site) throws IOException;
}
