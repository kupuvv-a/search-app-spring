package search.engine.dao;

import org.springframework.stereotype.Service;
import search.engine.model.Site;
import search.engine.repository.SiteRepository;

import javax.transaction.Transactional;
import java.util.List;

@Service
public class DaoSiteServiceImpl implements IDaoSiteService {

    private final SiteRepository siteRepository;

    public DaoSiteServiceImpl(SiteRepository siteRepository) {
        this.siteRepository = siteRepository;
    }

    @Override
    public Site getSiteByName(String aName) {
        return siteRepository.getSiteByName(aName);
    }

    @Override
    public Site getSiteByUrl(String url) {
        return siteRepository.getSiteByUrl(url);
    }

    @Override
    public void updateSiteStatus(String url, String statusType) {
        siteRepository.updateSiteStatus(url, statusType);
    }

    @Override
    public List<Site> getAllSites() {
        return siteRepository.findAllSites();
    }

    @Override
    @Transactional
    public void saveSite(Site aSite) {
        siteRepository.save(aSite);
    }

    @Override
    public void deleteAllSites() {
        siteRepository.deleteAll();
    }
}