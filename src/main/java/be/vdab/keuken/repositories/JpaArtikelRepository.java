package be.vdab.keuken.repositories;

import be.vdab.keuken.domain.Artikel;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
class JpaArtikelRepository implements ArtikelRepository {
    private final EntityManager manager;

    JpaArtikelRepository(EntityManager manager) {
        this.manager = manager;
    }

    @Override
    public Optional<Artikel> findById(long id) {
        return Optional.ofNullable(manager.find(Artikel.class, id));
    }

    @Override
    public void create(Artikel artikel) {
        manager.persist(artikel);
    }

    //findByNaamContains is betere naamgeving
    @Override
    public List<Artikel> findByBevatWoord(String woord) {
        return manager.createNamedQuery("Artikel.findByBevatWoord", Artikel.class)
                //1ste parameter is de :zoals uit je XML file JPQL query, 2de parameter is hetgeen die je erin steekt
                //gebruik verschillende naamgevingen ipv overal "woord" om dit te verduidelijken
                //String, object
                .setParameter("zoals","%" + woord + "%")
                .setHint("javax.persistence.loadgraph", manager.createEntityGraph(Artikel.MET_ARTIKELGROEP))
                .getResultList();

        //Hans voorstel:
        //.setParameterWithName("woord")toValue("%" + woord + "%");
        //overschrijven("12332131", "456465456", 100);
        //overschrijvenVan("12332131")naar("456465456")bedrag(100);
    }

    @Override
    public int algemenePrijsVerhoging(BigDecimal percentage) {
        //return type moet niet meegegeven worden in query
        return manager.createNamedQuery("Artikel.algemenePrijsVerhoging")
                .setParameter("percentage", percentage)
                .executeUpdate();
    }
}
