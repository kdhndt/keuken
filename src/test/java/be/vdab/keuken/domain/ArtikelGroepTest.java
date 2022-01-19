package be.vdab.keuken.domain;

import org.junit.jupiter.api.BeforeEach;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

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
}