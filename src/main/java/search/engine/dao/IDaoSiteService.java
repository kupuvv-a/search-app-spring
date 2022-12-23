package search.engine.dao;

import search.engine.model.Site;

import java.util.List;

public interface IDaoSiteService {

    void saveSite(Site aSite);

    Site getSiteByName(String aName);

    Site getSiteByUrl(String url);

    void updateSiteStatus(String url, String statusType);

    List<Site> getAllSites();

    void deleteAllSites();


}
