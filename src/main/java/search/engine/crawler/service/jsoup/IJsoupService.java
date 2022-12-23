package search.engine.crawler.service.jsoup;

import org.jsoup.Connection;
import org.jsoup.nodes.Document;


public interface IJsoupService {

    Connection.Response executeJsoupResponse(String pageUrl);

    Document getDocument(Connection.Response aResponse);

}
