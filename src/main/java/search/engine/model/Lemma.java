package search.engine.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;


@Entity(name = "lemma")
@NoArgsConstructor
@Getter
@Setter
public class Lemma implements Comparable<Lemma> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @ManyToOne
    @JoinColumn(name = "site_id")
    Site site;

    @Column(nullable = false, unique = true)
    String lemma;

    @Column(nullable = false)
    int frequency;


    @Override
    public int compareTo(Lemma aLemma) {
        if (frequency > aLemma.getFrequency()) {
            return 1;
        } else if (frequency < aLemma.getFrequency()) {
            return -1;
        }
        return 0;
    }
}
