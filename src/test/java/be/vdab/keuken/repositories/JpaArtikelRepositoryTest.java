package be.vdab.keuken.repositories;

import be.vdab.keuken.domain.*;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;

import javax.persistence.EntityManager;
import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest(showSql = false)
@Sql({"/insertArtikelGroep.sql", "/insertArtikel.sql"})
@Import(JpaArtikelRepository.class)
class JpaArtikelRepositoryTest extends AbstractTransactionalJUnit4SpringContextTests {
    private static final String ARTIKELS = "artikels";
    private final JpaArtikelRepository repository;
    private final EntityManager manager;

    public JpaArtikelRepositoryTest(JpaArtikelRepository repository, EntityManager manager) {
        this.repository = repository;
        this.manager = manager;
    }

    private long idVanTestFoodArtikel() {
        return jdbcTemplate.queryForObject(
                "select id from artikels where naam = 'testfood'", Long.class);
    }

    private long idVanTestNonFoodArtikel() {
        return jdbcTemplate.queryForObject("select id from artikels where naam = 'testnonfood'", Long.class);
    }

    @Test void artikelGroepLazyLoaded() {
        assertThat(repository.findById(idVanTestFoodArtikel()))
                .hasValueSatisfying(artikel -> assertThat(artikel.getArtikelGroep().getNaam()).isEqualTo("test"));
    }

    @Test
    void findFoodArtikelById() {
        assertThat(repository.findById(idVanTestFoodArtikel()))
                .containsInstanceOf(FoodArtikel.class)
                .hasValueSatisfying(
                        artikel -> assertThat(artikel.getNaam()).isEqualTo("testfood"));
    }

    @Test
    void findNonFoodArtikelById() {
        assertThat(repository.findById(idVanTestNonFoodArtikel()))
                .containsInstanceOf(NonFoodArtikel.class)
                .hasValueSatisfying(
                        artikel -> assertThat(artikel.getNaam()).isEqualTo("testnonfood"));
    }

    @Test
    void findByOnbestaandeId() {
        assertThat(repository.findById(-1)).isNotPresent();
    }

    @Test
    void createFoodArtikel() {
        var artikelGroep = new ArtikelGroep("test");
        var artikel = new FoodArtikel("testfood2", BigDecimal.TEN, BigDecimal.valueOf(12), 5, artikelGroep);
        manager.persist(artikelGroep);
        repository.create(artikel);
//        manager.flush();
        assertThat(artikel.getId()).isPositive();
        assertThat(countRowsInTableWhere(ARTIKELS, "id=" + artikel.getId())).isOne();
    }

    @Test
    void createNonFoodArtikel() {
        var artikelGroep = new ArtikelGroep("test");
        var artikel = new NonFoodArtikel("testnonfood2", BigDecimal.TEN, BigDecimal.valueOf(12), 5, artikelGroep);
        manager.persist(artikelGroep);
        repository.create(artikel);
//        manager.flush();
        assertThat(artikel.getId()).isPositive();
        assertThat(countRowsInTableWhere(ARTIKELS, "id=" + artikel.getId())).isOne();
    }

    @Test
    void findByNaamContains() {
        var artikels = repository.findByBevatWoord("a");
        manager.clear();
        assertThat(artikels)
                .hasSize(countRowsInTableWhere(ARTIKELS, "naam like '%ap%'"))
                .extracting(Artikel::getNaam)
                .allSatisfy(naam -> assertThat(naam).containsIgnoringCase("ap"))
                .isSortedAccordingTo(String::compareToIgnoreCase);
        assertThat(artikels)
                //zoek eerst artikelGroep om de naam ervan te vinden
                .extracting(Artikel::getArtikelGroep)
                .extracting(ArtikelGroep::getNaam)
                .isNotNull();
    }

    @Test
    void algemenePrijsVerhoging() {
        assertThat(repository.algemenePrijsVerhoging(BigDecimal.TEN))
                //vergelijk de gereturnde int waarde (aantal gewijzigd) met het aantal rijen in tabel
                .isEqualTo(countRowsInTable(ARTIKELS));
        assertThat(countRowsInTableWhere(ARTIKELS, "verkoopprijs = 132 and id = " + idVanTestFoodArtikel())).isOne();
    }

    @Test void kortingenLezen() {
        assertThat(repository.findById(idVanTestFoodArtikel()))
                .hasValueSatisfying(artikel -> assertThat(artikel.getKortingen()).containsOnly(new Korting(2, BigDecimal.valueOf(5))));
    }
}