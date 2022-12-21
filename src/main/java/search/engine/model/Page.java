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

    @Column(name = "site_id", nullable = false, unique = true)
    Long siteId;

    @Column(nullable = false, unique = true)
    String path;

    @Column(nullable = false)
    int code;

    @Column(nullable = false, unique = true)
    String content;

}
