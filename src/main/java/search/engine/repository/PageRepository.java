package search.engine.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import search.engine.model.Page;

import java.util.List;

@Repository
public interface PageRepository extends JpaRepository<Page, Long> {
    @Query(value = "SELECT * FROM page", nativeQuery = true)
    List<Page> findAllPages();
}
