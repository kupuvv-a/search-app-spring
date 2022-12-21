package search.engine.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import search.engine.model.Site;
import search.engine.repository.SiteRepository;

import javax.transaction.Transactional;

@Service
public class IDaoSiteServiceImpl implements IDaoSiteService {

    private final SiteRepository siteRepository;

    public IDaoSiteServiceImpl( SiteRepository siteRepository) {
        this.siteRepository = siteRepository;
    }

    @Override
    @Transactional
    public long saveSite(Site aSite) {
        siteRepository.save(aSite);
        siteRepository.flush();
        return aSite.getId();
    }

    @Override
    public void deleteAllSites() {
        siteRepository.deleteAll();
    }
}
