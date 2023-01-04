package search.engine.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity(name = "page")
@NoArgsConstructor
@Getter
@Setter
public class Page {

    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Id
    Long id;

    @ManyToOne
    @JoinColumn(name = "site_id", nullable = false)
    Site site;

    @Column(nullable = false, unique = true)
    String path;

    @Column(nullable = false)
    int code;

    @Column(nullable = false, columnDefinition = "MEDIUMTEXT")
    String content;

}
