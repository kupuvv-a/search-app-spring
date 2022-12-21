package search.engine.crawler.service;

import lombok.extern.slf4j.Slf4j;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import search.engine.crawler.util.DomainUtil;
import search.engine.dao.IDaoPageService;
import search.engine.model.Page;

import java.io.IOException;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
public class PagesChildrenParserServiceImpl implements IPagesChildrenParserService {

    private final String domain;

    public PagesChildrenParserServiceImpl(String pageUrl) {
        this.domain = DomainUtil.getDomainFromUrl(pageUrl);

    }

    @Override
    public Set<String> parsePageAndGetChildren(String pageUrl) throws IOException {
        Connection.Response response = executeJsoupResponse(pageUrl);
        log.info("url: {} ; code: {}", pageUrl, response.statusCode());



        return getPageChildren(response.parse(), pageUrl);
    }

    private Connection.Response executeJsoupResponse(String pageUrl) throws IOException {
        return Jsoup.connect(pageUrl)
                .ignoreHttpErrors(true)
                .userAgent(CrawlerConstant.USER_AGENT)
                .timeout(CrawlerConstant.TIMEOUT)
                .referrer("https://google.com")
                .validateTLSCertificates(false)//deprecate!?
                .execute();
    }

    private Set<String> getPageChildren(Document aDocument, String url) {
        return aDocument.select("a").stream()
                .map(element -> element.attr("href"))
                .filter(s -> s.matches(url + CrawlerConstant.REG_FOR_URL) || s.matches("^" + CrawlerConstant.REG_FOR_URL))
                .filter(s -> !s.matches(url + CrawlerConstant.REG_FOR_HTML) || !s.matches("^" + CrawlerConstant.REG_FOR_HTML))
                .map(s -> {
                    if (!s.startsWith(domain))
                        return domain + s;
                    else return s;
                })
                .collect(Collectors.toSet());
    }

}
