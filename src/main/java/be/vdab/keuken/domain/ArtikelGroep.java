package be.vdab.keuken.domain;

import javax.persistence.*;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Table(name = "artikelgroepen")
public class ArtikelGroep {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String naam;

    @OneToMany(mappedBy = "artikelGroep")
    @OrderBy("naam")
    private Set<Artikel> artikels;

    public ArtikelGroep(String naam) {
        this.naam = naam;
        //!!!!!!!!!!!!!! VERGEET JE SET OOK NIET IN CONSTRUCTOR TE INITIALISEREN
        this.artikels = new LinkedHashSet<>();
    }

    protected ArtikelGroep() {
    }

    public long getId() {
        return id;
    }

    public String getNaam() {
        return naam;
    }

    public Set<Artikel> getArtikels() {
        return Collections.unmodifiableSet(artikels);
    }

    public boolean add(Artikel artikel) {
        var toegevoegd = artikels.add(artikel);
        var oudeArtikelGroep = artikel.getArtikelGroep();
        if (oudeArtikelGroep != null && oudeArtikelGroep != this) {
            oudeArtikelGroep.artikels.remove(artikel);
        }
        if (oudeArtikelGroep != this) {
            artikel.setArtikelGroep(this);
        }
        return toegevoegd;
    }

    //equals en hashCode is hier niet nodig, er is nl. geen verzameling van artikelGroepen, wel van artikels, obv equals en hashCode wordt nl. gecontroleerd op duplicaten?

}
