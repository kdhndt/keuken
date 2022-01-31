package be.vdab.keuken.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNullPointerException;

class ArtikelGroepTest {
    private Artikel artikel1;
    private ArtikelGroep artikelGroep1;
    private ArtikelGroep artikelGroep2;

    @BeforeEach
    void beforeEach() {
        artikelGroep1 = new ArtikelGroep("test");
        artikelGroep2 = new ArtikelGroep("test2");
        artikel1 = new FoodArtikel("test", BigDecimal.ONE, BigDecimal.TEN, 12, artikelGroep1);
    }

    @Test
    void ArtikelGroep1HoortBijArtikel1() {
        assertThat(artikel1.getArtikelGroep()).isEqualTo(artikelGroep1);
        assertThat(artikelGroep1.getArtikels()).containsOnly(artikel1);
    }

    @Test void artikel1VerhuistVanArtikelGroep1NaarArtikelGroep2() {
//        artikel1.setArtikelGroep(artikelGroep2);
        assertThat(artikelGroep2.add(artikel1)).isTrue();
        assertThat(artikel1.getArtikelGroep()).isEqualTo(artikelGroep2);
        assertThat(artikelGroep2.getArtikels()).containsOnly(artikel1);
        assertThat(artikelGroep1.getArtikels()).doesNotContain(artikel1);
    }

    @Test void eenNullArtikelToevoegenMislukt() {
        assertThatNullPointerException().isThrownBy(() -> artikelGroep1.add(null));
    }

}