package search.engine.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Index;
import javax.persistence.*;

@Entity
@Table(name = "page", indexes = @Index(name = "pathPage", columnList = "path"))
@NoArgsConstructor
@Getter
@Setter
public class Page {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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
