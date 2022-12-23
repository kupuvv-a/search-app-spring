package search.engine.crawler.service;

import lombok.extern.slf4j.Slf4j;
import org.jsoup.Connection;
import org.jsoup.nodes.Document;
import org.springframework.stereotype.Service;
import search.engine.crawler.service.jsoup.IJsoupService;
import search.engine.dao.IDaoPageService;
import search.engine.model.Page;
import search.engine.model.Site;

import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
public class PagesChildrenParserServiceImpl implements IPagesChildrenParserService {

    private final String REG_FOR_URL = "/.+/?";
    private final String REG_FOR_HTML = ".+.html";

    private final IDaoPageService daoPageService;
    private final IJsoupService jsoupService;

    public PagesChildrenParserServiceImpl(IDaoPageService daoPageService, IJsoupService jsoupService) {
        this.daoPageService = daoPageService;
        this.jsoupService = jsoupService;
    }

    @Override
    public synchronized Set<Page>  parsePageAndGetChildrenPages(Page page, Site site) {

        final Connection.Response response = jsoupService.executeJsoupResponse(page.getPath());

        final Document document = jsoupService.getDocument(response);
        final   String  content = document.html();

        savePageOrUpdateContent(page, response, content);
        return getChildrenPages(document, page.getPath(), site);
    }

    private void savePageOrUpdateContent(Page page, Connection.Response response, String content) {
        Page dbPage = daoPageService.getPageByPath(page.getPath());
        if (dbPage == null) {
            page.setContent(content);
            daoPageService.savePage(page);
            log.info("page saved : {}", page.getPath());
        } else {
            if (dbPage.getContent() == null || !dbPage.getContent().equals(content)) {
                daoPageService.updatePageContentByPath(dbPage.getPath(), content, response.statusCode());
                log.info("page updated : {}", page.getPath());
            }
        }
    }


    private Set<Page> getChildrenPages(Document document, String url, Site site) {
        return document.select("a").stream()
                .map(element -> element.attr("href"))
                .filter(link -> link.matches(url + REG_FOR_URL) || link.matches("^" + REG_FOR_URL))
                .filter(link -> !link.matches(url + REG_FOR_HTML) || !link.matches("^" + REG_FOR_HTML))
                .map(link -> {
                    if (!link.startsWith(site.getUrl()))
                        return site.getUrl() + link;
                    else return link;
                })
                .map(link -> createPageOrNull(link, site))
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());
    }

    private Page createPageOrNull(String path, Site site) {

        Page page = daoPageService.getPageByPath(path);
        if (page == null) {
            Page newPage = createPage(path, site);
            daoPageService.savePage(newPage);
            return newPage;
        }
        return null;
    }

    private Page createPage(String path, Site site) {
        Page page = new Page();
        page.setPath(path);
        page.setSite(site);
//        page.setCode(status);
//        page.setContent(content);
        return page;
    }

}
