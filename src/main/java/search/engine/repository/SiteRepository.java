package search.engine.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import search.engine.model.Site;

import java.util.List;

@Repository
public interface SiteRepository extends JpaRepository<Site, Long> {

    @Query(value = "SELECT * FROM site", nativeQuery = true)
    List<Site> findAllContains();

}
