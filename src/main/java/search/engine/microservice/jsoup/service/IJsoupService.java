package search.engine.microservice.jsoup.service;

import org.jsoup.Connection;
import org.jsoup.nodes.Document;

import java.io.IOException;


public interface IJsoupService {

    Connection.Response executeJsoupResponse(String pageUrl) throws IOException;

    Document getDocument(Connection.Response aResponse);

    String cleanHtmlTags(String htmlDocument);
}
