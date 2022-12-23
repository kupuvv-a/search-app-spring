package search.engine.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import search.engine.model.Page;

@Repository
@Transactional
public interface PageRepository extends JpaRepository<Page, Long> {

    @Query(value = "SELECT * FROM page WHERE search_engine.page.path = ?1", nativeQuery = true)
    Page findPageByUrl(String url);

    @Modifying
    @Query(value = "UPDATE  page SET content = ?2, code = ?3 WHERE path = ?1", nativeQuery = true)
    void updatePageContent(String path, String content, int code);

}
