package be.vdab.keuken.repositories;

import be.vdab.keuken.domain.Artikel;
import be.vdab.keuken.domain.FoodArtikel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;

import javax.persistence.EntityManager;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest(showSql = false)
@Sql("/insertArtikel.sql")
@Import(JpaArtikelRepository.class)
class JpaArtikelRepositoryTest extends AbstractTransactionalJUnit4SpringContextTests {

    private final JpaArtikelRepository repository;
    private static final String ARTIKELS = "artikels";
    //overbodig?
    private final EntityManager manager;
//    private Artikel artikel;

/*    @BeforeEach
    void beforeEach() {
//        artikel = new Artikel("testArtikel2", BigDecimal.TEN, BigDecimal.valueOf(12));
    }*/

    public JpaArtikelRepositoryTest(JpaArtikelRepository repository, EntityManager manager) {
        this.repository = repository;
        this.manager = manager;
    }

    /*    private long idVanTestArtikel() {
        return jdbcTemplate.queryForObject("select id from artikels where naam = 'testArtikel'", Long.class);
    }*/

    private long idVanTestFoodArtikel() {
        return jdbcTemplate.queryForObject(
                "select id from artikels where naam = 'testfood'", Long.class);
    }


    private long idVanTestNonFoodArtikel() {
        return jdbcTemplate.queryForObject("select id from artikels where naam = 'testnonfood'", Long.class);
    }

    @Test
    void findFoodArtikelById() {
        assertThat(repository.findById(idVanTestFoodArtikel()))
                .containsInstanceOf(FoodArtikel.class)
                .hasValueSatisfying(
                        artikel -> assertThat(artikel.getNaam()).isEqualTo("testfood"));
    }

/*    @Test
    void findById() {
        assertThat(repository.findById(idVanTestArtikel()))
                .hasValueSatisfying(artikel -> assertThat(artikel.getNaam()).isEqualTo("testArtikel"));
    }*/

    @Test
    void findByOnbestaandeId() {
        assertThat(repository.findById(-1L)).isNotPresent();
    }

/*
    @Test
    void create() {
        repository.create(artikel);
        assertThat(artikel.getId()).isPositive();
        assertThat(countRowsInTableWhere(ARTIKELS, "id=" + artikel.getId())).isOne();
    }
*/

    @Test
    void findByBevatWoord() {
        assertThat(repository.findByBevatWoord("ap"))
//                .hasSize(jdbcTemplate.queryForObject("select count(*) from artikels where naam like '%ap%'", Integer.class))
                .hasSize(countRowsInTableWhere(ARTIKELS, "naam like '%ap%'"))
                .extracting(Artikel::getNaam)
                .allSatisfy(naam -> assertThat(naam).containsIgnoringCase("ap"))
                .isSortedAccordingTo(String::compareToIgnoreCase);
    }

/*    @Test
    void algemenePrijsVerhoging() {
        assertThat(repository.algemenePrijsVerhoging(BigDecimal.TEN))
                //vergelijk de gereturnde int waarde (aantal gewijzigd) met het aantal rijen in tabel
                .isEqualTo(countRowsInTable(ARTIKELS));
        //beide onderstaande tests kunnen en komen op hetzelfde neer
        assertThat(repository.findById(idVanTestArtikel()))
                .hasValueSatisfying(artikel -> assertThat(artikel.getVerkoopprijs()).isEqualByComparingTo("22"));
        assertThat(countRowsInTableWhere(ARTIKELS, "verkoopprijs = 22 and id = " + idVanTestArtikel())).isOne();
    }*/
}