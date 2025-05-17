package it.unisa.software_architecture_design.drawsnapdrawingtool.forme;

import org.junit.jupiter.api.Test;
import javafx.scene.paint.Color;

import static org.junit.jupiter.api.Assertions.*;

class EllisseTest {

    @Test
    void testContiene_puntoAlCentro() {
        Ellisse ellisse = new Ellisse(10, 15, 40, 0, Color.VIOLET, 30, Color.YELLOW);
        // Punto esattamente al centro dell'ellisse
        assertTrue(ellisse.contiene(10, 15));
    }

    @Test
    void testContiene_puntoInternoOrizzontale() {
        Ellisse ellisse = new Ellisse(40, 40, 60, 0, Color.RED, 40, Color.GREEN);

        assertTrue(ellisse.contiene(45, 45));
    }

    @Test
    void testContiene_puntoInternoEllisseInclinata() {
        Ellisse ellisse = new Ellisse(30, 30, 70, 30, Color.DARKRED, 40, Color.LIGHTGREEN);

        assertTrue(ellisse.contiene(32, 28));
    }

    @Test
    void testContiene_puntoInternoEllisseAngoloOttuso() {
        Ellisse ellisse = new Ellisse(30, 30, 80, 120, Color.ORANGE, 40, Color.YELLOW);
        // Punto ben all'interno dopo la rotazione
        assertTrue(ellisse.contiene(20, 40));
    }

    @Test
    void testContiene_puntoSulBordoDellEllisseOrizzontale() {
        Ellisse ellisse = new Ellisse(30, 30, 50, 0, Color.BLUE, 30, Color.PURPLE);

        assertTrue(ellisse.contiene(55, 30));
    }

    @Test
    void testContiene_puntoSulBordoEllisseAngoloOttuso() {
        Ellisse ellisse = new Ellisse(50, 50, 100, 135, Color.RED, 60, Color.GREEN);

        //utilizzando trigonometria
        double x = 50 + (50 * Math.sqrt(2) / 2);
        double y = 50 - (30 * Math.sqrt(2) / 2);
        assertTrue(ellisse.contiene(x, y));
    }

    @Test
    void testContiene_puntoNellEllisseConAltezzaMinima() {
        Ellisse ellisse = new Ellisse(50, 50, 100, 135, Color.RED, 1, Color.GREEN); // Altezza quasi nulla
        // Punto allineato con l'asse maggiore
        assertTrue(ellisse.contiene(50, 50));
    }

    @Test
    void testContiene_puntoSulBordoEllisseInclinata() {
        Ellisse ellisse = new Ellisse(60, 60, 100, 45, Color.DARKBLUE, 50, Color.MAGENTA);

        //utilizzando trigonometria
        double x = 60 + Math.sqrt(25);
        double y = 60 - Math.sqrt(25);
        assertTrue(ellisse.contiene(x, y));
    }

    @Test
    void testContiene_puntoEsternoAllEllisseOrizzontale() {
        Ellisse ellisse = new Ellisse(50, 50, 80, 0, Color.BLACK, 50, Color.LIGHTGRAY);

        assertFalse(ellisse.contiene(100, 50));
    }

    @Test
    void testContiene_puntoEsternoEllisseInclinata() {
        Ellisse ellisse = new Ellisse(50, 50, 100, 60, Color.GRAY, 50, Color.PINK);

        assertFalse(ellisse.contiene(110, 50));
    }

    @Test
    void testContiene_puntoEsternoEllisseAngoloOttuso() {
        Ellisse ellisse = new Ellisse(70, 70, 120, 150, Color.BLACK, 80, Color.LIGHTGRAY);

        assertFalse(ellisse.contiene(150, 70));
    }

    @Test
    void testContiene_fuoriDallEllisseConAltezzaMinima() {
        Ellisse ellisse = new Ellisse(50, 50, 100, 135, Color.RED, 1, Color.GREEN); // Altezza quasi nulla

        assertFalse(ellisse.contiene(50, 55));
    }

    @Test
    void testContiene_puntoVicinoAlBordoEsternoOrizzontale() {
        Ellisse ellisse = new Ellisse(20, 20, 40, 0, Color.ORANGE, 20, Color.CYAN);

        assertFalse(ellisse.contiene(40.1, 20));
    }

    @Test
    void testContiene_puntoEsternoLungoLAsseMaggiore() {
        Ellisse ellisse = new Ellisse(30, 30, 60, 0, Color.DARKGREEN, 40, Color.LIGHTBLUE);

        assertFalse(ellisse.contiene(61, 30));
    }

    @Test
    void testContiene_puntoEsternoLungoLAsseMinore() {
        Ellisse ellisse = new Ellisse(30, 30, 60, 0, Color.DARKGREEN, 40, Color.LIGHTBLUE);

        assertFalse(ellisse.contiene(30, 51));
    }
}