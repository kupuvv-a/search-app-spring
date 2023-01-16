package search.engine.crawler.service;

import org.jsoup.Connection;
import org.jsoup.nodes.Document;

import java.io.IOException;


public interface IJsoupService {

    Connection.Response executeJsoupResponse(String pageUrl) throws IOException;

    Document getDocument(Connection.Response aResponse);
}
