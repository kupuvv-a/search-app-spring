package search.engine.crawler.service;


import java.io.IOException;
import java.util.Set;

public interface IPagesChildrenParserService {

    Set<String> parsePageAndGetChildren(String pageUrl) throws IOException;

}
