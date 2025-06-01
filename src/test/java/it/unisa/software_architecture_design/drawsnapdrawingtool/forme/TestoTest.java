package it.unisa.software_architecture_design.drawsnapdrawingtool.forme;

import javafx.scene.paint.Color;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class TestoTest {

    @Test
    void testContiene_puntoNelTestoOrizzontale() {
        Testo testo = new Testo(50, 50, 100, 0, Color.BLACK, 50, Color.BLACK, "Hello");
        assertTrue(testo.contiene(50, 50)); // Punto nel centro
        assertTrue(testo.contiene(40, 45)); // Punto interno vicino al bordo
        assertTrue(testo.contiene(60, 55));
    }


    @Test
    void testContiene_puntoSulBordoTestoOrizzontale() {
        Testo testo = new Testo(50, 50, 100, 0, Color.BLACK, 50, Color.BLACK, "Hello");
        assertTrue(testo.contiene(testo.getVerticeAX(), testo.getVerticeAY())); // Vertice Top-Left
        assertTrue(testo.contiene(testo.getVerticeBX(), testo.getVerticeBY())); // Vertice Top-Right
        assertTrue(testo.contiene(testo.getVerticeCX(), testo.getVerticeCY())); // Vertice Bottom-Right
        assertTrue(testo.contiene(testo.getVerticeDX(), testo.getVerticeDY())); // Vertice Bottom-Left
    }

    @Test
    void testContiene_puntoNelTestoInclinato() {
        Testo testo = new Testo(100, 100, 80, 45, Color.BLACK, 40, Color.BLACK, "World");
        assertTrue(testo.contiene(100, 100)); // Punto nel centro
        assertTrue(testo.contiene(95, 105)); // Punti interni dopo rotazione
    }


    @Test
    void testSpecchia_testoVerticale() {
        Testo testo = new Testo(100, 100, 50, 0, Color.BLUE, 20, Color.RED, "pepi");
        assertEquals("pepi", testo.getTesto()); // Verifica stato iniziale stringa
        assertEquals(0.0, testo.getAngoloInclinazione(), 0.001); // Verifica stato iniziale angolo

        testo.specchiaInVerticale();

        assertEquals("ipep", testo.getTesto()); // Verifica inversione stringa
        assertEquals(0.0, testo.getAngoloInclinazione(), 0.001); // Verifica angolo (0 gradi specchiato rimane 0)
        assertTrue(testo.contiene(100, 100)); // Verifica che il punto centrale sia ancora contenuto
        assertTrue(testo.contiene(100 + 10, 100 + 5)); // Punto interno originale
        assertTrue(testo.contiene(100 - 10, 100 + 5)); // Punto interno originale
    }


    @Test
    void testSpecchia_doppiaSpecchiaturaVerticale() {
        Testo testo = new Testo(100, 100, 50, 0, Color.BLUE, 20, Color.RED, "test");

        testo.specchiaInVerticale(); // Prima specchiatura: "tset", angolo 0
        assertEquals("tset", testo.getTesto());
        assertEquals(0.0, testo.getAngoloInclinazione(), 0.001);

        testo.specchiaInVerticale(); // Seconda specchiatura: "test", angolo 0
        assertEquals("test", testo.getTesto());
        assertEquals(0.0, testo.getAngoloInclinazione(), 0.001);
    }

    @Test
    void testSpecchia_testoOrizzontale() {
        String stringaIniziale = "orizzontale";
        double angoloIniziale = 30.0;
        Testo testo = new Testo(200, 150, 120, angoloIniziale, Color.DARKCYAN, 30, Color.CYAN, stringaIniziale);

        assertEquals(stringaIniziale, testo.getTesto(), "Verifica stato iniziale stringa.");
        assertEquals(angoloIniziale, testo.getAngoloInclinazione(), 0.001, "Verifica stato iniziale angolo.");

        testo.specchiaInOrizzontale();

        assertEquals(stringaIniziale, testo.getTesto(), "La stringa NON deve cambiare dopo specchiaInOrizzontale.");
        assertEquals(-angoloIniziale, testo.getAngoloInclinazione(), 0.001, "L'angolo deve essere negato.");
    }

    @Test
    void testSpecchia_doppiaSpecchiaturaOrizzontale() {
        String stringaIniziale = "doppia";
        double angoloIniziale = -25.0; // Inizia con un angolo negativo
        Testo testo = new Testo(50, 80, 90, angoloIniziale, Color.MAGENTA, 25, Color.PINK, stringaIniziale);

        assertEquals(stringaIniziale, testo.getTesto());
        assertEquals(angoloIniziale, testo.getAngoloInclinazione(), 0.001);

        // Prima specchiatura orizzontale
        testo.specchiaInOrizzontale();
        assertEquals(stringaIniziale, testo.getTesto(), "Stringa dopo la prima specchiatura.");
        assertEquals(-angoloIniziale, testo.getAngoloInclinazione(), 0.001, "Angolo dopo la prima specchiatura.");

        // Seconda specchiatura orizzontale
        testo.specchiaInOrizzontale();
        assertEquals(stringaIniziale, testo.getTesto(), "Stringa dopo la seconda specchiatura.");
        assertEquals(angoloIniziale, testo.getAngoloInclinazione(), 0.001, "Angolo dopo la seconda specchiatura (ripristinato).");
    }
}