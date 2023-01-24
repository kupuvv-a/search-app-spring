package search.engine.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import search.engine.model.Lemma;
import search.engine.model.Site;

import java.util.List;

public interface LemmaRepository extends JpaRepository<Lemma, Long> {

    @Query(value = "SELECT * FROM lemma", nativeQuery = true)
    List<Site> findAllLemmas();

}
