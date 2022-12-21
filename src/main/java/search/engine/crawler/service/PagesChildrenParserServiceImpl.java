package search.engine.crawler.service;

import lombok.extern.slf4j.Slf4j;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import search.engine.crawler.util.DomainUtil;
import search.engine.dao.IDaoPageService;
import search.engine.model.Page;

import java.io.IOException;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
public class PagesChildrenParserServiceImpl implements IPagesChildrenParserService {

    private final String domain;
    private final Page page;
    private final long siteId;
    private final IDaoPageService daoPageService;

    public PagesChildrenParserServiceImpl(String pageUrl, long siteId, IDaoPageService daoPageService) {
        this.siteId = siteId;
        this.domain = DomainUtil.getDomainFromUrl(pageUrl);
        this.daoPageService = daoPageService;
        this.page = new Page();
        page.setPath(pageUrl);
    }

    @Override
    public Set<String> parsePageAndGetChildren(String pageUrl) throws IOException {

        final Connection.Response response = executeJsoupResponse(pageUrl);

        page.setSiteId(this.siteId);
        page.setCode(response.statusCode());

        final Document document = getPageDocument(response);
        page.setContent(document.html());
        daoPageService.savePage(page);

        log.info("url: {} ; code: {}", pageUrl, response.statusCode());

        return getPageChildren(document, pageUrl);
    }

    private Connection.Response executeJsoupResponse(String pageUrl) throws IOException {
        return Jsoup.connect(pageUrl)
                .ignoreHttpErrors(true)
                .userAgent(CrawlerConstant.USER_AGENT)
                .timeout(CrawlerConstant.TIMEOUT)
                .referrer("https://google.com")
                .execute();
    }

    private Document getPageDocument(Connection.Response aResponse) throws IOException {
        return aResponse.parse();
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
