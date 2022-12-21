package search.engine.dao;

import search.engine.model.Site;

public interface IDaoSiteService {
    //todo return long site_id
    long saveSite(Site aSite);

    void deleteAllSites();

}
