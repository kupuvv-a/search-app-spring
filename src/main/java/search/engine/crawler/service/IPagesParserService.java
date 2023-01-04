package search.engine.crawler.service;

import search.engine.model.Page;
import search.engine.model.Site;

import java.io.IOException;
import java.util.Set;

public interface IPagesParserService {

    Set<Page> getPageChildren(Page page, Site site) throws IOException;
}
