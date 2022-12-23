package search.engine.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import search.engine.model.Site;

import java.util.List;

@Repository
@Transactional
public interface SiteRepository extends JpaRepository<Site, Long> {

    @Query(value = "SELECT * FROM site", nativeQuery = true)
    List<Site> findAllSites();

    @Query(value = "SELECT * FROM site WHERE search_engine.site.name = ?1", nativeQuery = true)
    Site getSiteByName(String name);

    @Query(value = "SELECT * FROM site WHERE search_engine.site.url = ?1", nativeQuery = true)
    Site getSiteByUrl(String url);

    @Modifying
    @Query(value = "UPDATE search_engine.site SET status = ?2 WHERE url = ?1", nativeQuery = true)
    void updateSiteStatus(String url, String statusType);


}
