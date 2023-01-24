package search.engine.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity(name = "aIndex")
@NoArgsConstructor
@Getter
@Setter
public class Index {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @ManyToOne
    @JoinColumn(name = "page_id")
    Page page;

    @ManyToOne
    @JoinColumn(name = "lemma_id")
    Lemma lemma;

    @Column(nullable = false)
    Float lemmaRank;

}
