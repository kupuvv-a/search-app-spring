package search.engine.model;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity(name = "site")
@NoArgsConstructor
@Getter
@Setter
public class Site {

    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Id
    Long id;

    @Column(nullable = false, unique = true, columnDefinition = "ENUM('INDEXING', 'INDEXED', 'FAILED')")
    StatusType status;

    @Column(name = "last_error", nullable = false, unique = true)
    String lastError;

    @Column(nullable = false)
    String url;

    @Column(nullable = false, unique = true)
    String name;

}
