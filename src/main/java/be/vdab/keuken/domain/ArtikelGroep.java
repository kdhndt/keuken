package be.vdab.keuken.domain;

import javax.persistence.*;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Objects;
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
//        return artikels.add(artikel);

        var toegevoegd = artikels.add(artikel);
        var oudeArtikelGroep = artikel.getArtikelGroep();
        if (oudeArtikelGroep != null && oudeArtikelGroep != this) {
            oudeArtikelGroep.artikels.remove(artikel);
        }
        if (this != oudeArtikelGroep) {
            artikel.setArtikelGroep(this);
        }
        return toegevoegd;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ArtikelGroep)) return false;
        ArtikelGroep that = (ArtikelGroep) o;
        return naam.equalsIgnoreCase(that.naam);
    }

    @Override
    public int hashCode() {
        return Objects.hash(naam.toLowerCase());
    }
}
