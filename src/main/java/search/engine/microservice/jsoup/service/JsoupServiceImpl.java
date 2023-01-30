package search.engine.microservice.jsoup.service;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import search.engine.microservice.jsoup.Constant;

import java.io.IOException;

@Service
public class JsoupServiceImpl implements IJsoupService {

    @Override
    public Connection.Response executeJsoupResponse(String pageUrl) throws IOException {
        return Jsoup.connect(pageUrl)
                .ignoreHttpErrors(true)
                .userAgent(Constant.USER_AGENT)
                .timeout(Constant.TIMEOUT)
                .referrer(Constant.REFERER)
                .execute();
    }

    @Override
    public Document getDocument(Connection.Response aResponse) {
        try {
            return aResponse.parse();
        } catch (IOException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Cannot parse page : " + e.getMessage());
        }
    }

    @Override
    public String cleanHtmlTags(String htmlDocument) {
        return Jsoup.parse(htmlDocument).text();
    }
}
