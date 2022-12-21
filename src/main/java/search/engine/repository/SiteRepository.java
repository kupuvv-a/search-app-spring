package search.engine.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import search.engine.model.Site;

import java.util.List;

public interface SiteRepository  extends JpaRepository<Site, Long> {

    @Query(value = "SELECT * from site", nativeQuery = true)
    List<Site> findAllContains();

}
