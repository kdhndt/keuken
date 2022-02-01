package be.vdab.keuken.domain;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@Table(name = "artikels")
@DiscriminatorColumn(name = "soort")
//gebruik het artikelGroep attribuut als knooppunt om de bijbehorende ArtikelGroep Entity te lezen (verbonden via associatie)
@NamedEntityGraph(name = Artikel.MET_ARTIKELGROEP, attributeNodes = @NamedAttributeNode("artikelGroep"))
public abstract class Artikel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String naam;
    private BigDecimal aankoopprijs;
    private BigDecimal verkoopprijs;
    @ElementCollection @OrderBy("vanafAantal")
    @CollectionTable(name = "kortingen", joinColumns = @JoinColumn(name = "artikelId"))
    private Set<Korting> kortingen;
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "artikelgroepId")
    private ArtikelGroep artikelGroep;
    //public visibility, we hebben deze bv. nodig in onze repo
    public static final String MET_ARTIKELGROEP = "Artikel.metArtikelGroep";

    public Artikel(String naam, BigDecimal aankoopprijs, BigDecimal verkoopprijs, ArtikelGroep artikelGroep) {
        this.naam = naam;
        this.aankoopprijs = aankoopprijs;
        this.verkoopprijs = verkoopprijs;
        this.kortingen = new LinkedHashSet<>();
        setArtikelGroep(artikelGroep);
    }

    protected Artikel() {}

    public ArtikelGroep getArtikelGroep() {
        return artikelGroep;
    }

    public void setArtikelGroep(ArtikelGroep artikelGroep) {
        if (!artikelGroep.getArtikels().contains(this)) {
            artikelGroep.add(this);
        }
        this.artikelGroep = artikelGroep;
    }

    public Set<Korting> getKortingen() {
        return Collections.unmodifiableSet(kortingen);
    }

    public void verhoogVerkoopPrijs(BigDecimal bedrag) {
        if (bedrag.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException();
        }
        //niet vergeten verkoopprijs = verkoopprijs + ... te doen!
        verkoopprijs = verkoopprijs.add(bedrag);
    }

    public long getId() {
        return id;
    }

    public String getNaam() {
        return naam;
    }

    public BigDecimal getAankoopprijs() {
        return aankoopprijs;
    }

    public BigDecimal getVerkoopprijs() {
        return verkoopprijs;
    }

    //equals en hashCode op naam is hier voldoende, een unieke naam per artikelGroep, niet overcompliceren door op verschillende variabelen te baseren
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Artikel)) return false;
        Artikel artikel = (Artikel) o;
        return naam.equalsIgnoreCase(artikel.naam)/* && Objects.equals(artikelGroep, artikel.artikelGroep)*/;
    }

    @Override
    public int hashCode() {
        return Objects.hash(naam.toLowerCase()/*, artikelGroep*/);
    }
}
