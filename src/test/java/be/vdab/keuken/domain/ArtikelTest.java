package be.vdab.keuken.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.*;

class ArtikelTest {
    private Artikel artikel1;
    private ArtikelGroep artikelGroep1;
    private ArtikelGroep artikelGroep2;

    @BeforeEach
    void beforeEach() {
        artikelGroep1 = new ArtikelGroep("test");
        artikelGroep2 = new ArtikelGroep("test2");
        artikel1 = new FoodArtikel("test", BigDecimal.TEN, BigDecimal.valueOf(12), 12, artikelGroep1);
    }

    @Test void artikel1KomtVoorInArtikelGroep1() {
        assertThat(artikel1.getArtikelGroep()).isEqualTo(artikelGroep1);
        assertThat(artikelGroep1.getArtikels()).containsOnly(artikel1);
    }

    @Test void artikel1VerhuistVanArtikelGroep1NaarArtikelGroep2() {
        artikel1.setArtikelGroep(artikelGroep2);
        assertThat(artikel1.getArtikelGroep()).isEqualTo(artikelGroep2);
        assertThat(artikelGroep2.getArtikels()).containsOnly(artikel1);
        assertThat(artikelGroep1.getArtikels()).doesNotContain(artikel1);
    }

    @Test void eenNullArtikelGroepInDeSetterMislukt() {
        assertThatNullPointerException().isThrownBy(() -> artikel1.setArtikelGroep(null));
    }

    @Test void nullArtikelGroepInDeConstructorMislukt() {
        assertThatNullPointerException().isThrownBy(() -> new FoodArtikel("test", BigDecimal.TEN, BigDecimal.valueOf(12), 12, null));
    }

    @Test
    void verhoogVerkoopPrijs() {
        artikel1.verhoogVerkoopPrijs(BigDecimal.ONE);
        assertThat(artikel1.getVerkoopprijs()).isEqualByComparingTo("13");
    }

    @Test
    void verhoogVerkoopPrijsMetNull() {
        assertThatNullPointerException().isThrownBy(() -> artikel1.verhoogVerkoopPrijs(null));
    }

    @Test
    void verhoogVerkoopPrijsMet0() {
        assertThatIllegalArgumentException().isThrownBy(() -> artikel1.verhoogVerkoopPrijs(BigDecimal.ZERO));
    }

    @Test
    void verhoogVerkoopPrijsMetNegatiefGetal() {
        assertThatIllegalArgumentException().isThrownBy(() -> artikel1.verhoogVerkoopPrijs(BigDecimal.valueOf(-1)));
    }
}