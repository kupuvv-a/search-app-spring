package search.engine.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.sql.Date;

@Entity(name = "site")
@NoArgsConstructor
@Getter
@Setter
public class Site {

    @GeneratedValue(strategy = GenerationType.AUTO)
    @Id
    Long id;

    @Column(nullable = false, columnDefinition = "ENUM('INDEXING', 'INDEXED', 'FAILED')")
    String status;

    @Column(name = "status_time", nullable = false)
    Date statusTime;

    @Column(name = "last_error", nullable = false)
    String lastError;

    @Column(nullable = false, unique = true)
    String url;

    @Column(nullable = false, unique = true)
    String name;

}