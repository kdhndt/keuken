package be.vdab.keuken.repositories;

import be.vdab.keuken.domain.Artikel;

import javax.persistence.EntityManager;
import java.util.Optional;

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
}