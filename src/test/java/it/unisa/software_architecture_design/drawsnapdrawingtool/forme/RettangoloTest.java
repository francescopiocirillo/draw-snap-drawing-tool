package it.unisa.software_architecture_design.drawsnapdrawingtool.forme;

import org.junit.jupiter.api.Test;
import javafx.scene.paint.Color;
import static org.junit.jupiter.api.Assertions.*;

class RettangoloTest {

    @Test
    void tesContiene_puntoNelRettangoloOrizzontale() {
        Rettangolo rettangolo = new Rettangolo(50, 50, 100, 0, Color.YELLOW, 50, Color.DARKRED);

        assertTrue(rettangolo.contiene(60, 50));
    }

    @Test
    void testContiene_puntoNelRettangoloInclinato() {
        Rettangolo rettangolo = new Rettangolo(100, 100, 80, 45, Color.BLUE, 40, Color.GREEN);

        assertTrue(rettangolo.contiene(100, 100));
    }

    @Test
    void testContiene_puntoSulBordoDelRettangolo() {
        Rettangolo rettangolo = new Rettangolo(50, 50, 100, 0, Color.VIOLET, 50, Color.VIOLET);

        //utilizzando la trigonometria
        double puntoX = rettangolo.getCoordinataX() + rettangolo.getAltezza() / 2;
        double puntoY = rettangolo.getVerticeAY();

        assertTrue(rettangolo.contiene(puntoX, puntoY));
    }

    @Test
    void testContiene_puntoNellAngoloDelRettangolo() {
        Rettangolo rettangolo = new Rettangolo(50, 50, 60, 0, Color.AQUA, 40, Color.SALMON);
        double puntoX = rettangolo.getVerticeAX();
        double puntoY = rettangolo.getVerticeAY();

        assertTrue(rettangolo.contiene(puntoX, puntoY));
    }

    @Test
    void testContiene_puntoRettangoloMinimo() {
        Rettangolo rettangolo = new Rettangolo(50, 50, 0.01, 0, Color.BLACK, 0.01, Color.WHITE);

        assertTrue(rettangolo.contiene(50, 50));
    }

    @Test
    void testContiene_puntoSulBordoRettangoloMinimo() {
        Rettangolo rettangolo = new Rettangolo(50, 50, 0.01, 0, Color.BLUE, 0.01, Color.GREEN);
        // Punto sul bordo superiore
        assertTrue(rettangolo.contiene(50, 50.005));
    }

    @Test
    void testContiene_puntoSulBordoConInclinazione() {
        Rettangolo rettangolo = new Rettangolo(50, 50, 40, 45, Color.CORAL, 20, Color.CYAN);
        double verticeAX = rettangolo.getVerticeAX();
        double verticeAY = rettangolo.getVerticeAY();

        assertTrue(rettangolo.contiene(verticeAX, verticeAY));
    }

    @Test
    void testPuntoBordoRettangoloVerticale() {
        Rettangolo rettangolo = new Rettangolo(50, 50, 40, 90, Color.BLACK, 20, Color.LIGHTGRAY);
        double verticeAX = rettangolo.getVerticeAX();
        double verticeAY = rettangolo.getVerticeAY();

        assertTrue(rettangolo.contiene(verticeAX, verticeAY));
    }

    @Test
    void testPuntoVicinoDiagonaleEsterno() {
        Rettangolo rettangolo = new Rettangolo(100, 100, 60, 0, Color.PURPLE, 40, Color.LIGHTGRAY);
        double verticeAX = rettangolo.getVerticeAX();
        double verticeAY = rettangolo.getVerticeAY();

        assertFalse(rettangolo.contiene(verticeAX - 0.1, verticeAY - 0.1));
    }

    @Test
    void testPuntoInternoVicinoBordo() {
        Rettangolo rettangolo = new Rettangolo(50, 50, 40, 0, Color.ORANGE, 20, Color.PINK);
        // Punto interno vicino al bordo inferiore (y = 60)
        assertTrue(rettangolo.contiene(50, 59.9));
    }

    @Test
    void testContiene_puntoFuoriRettangoloOrizzontale() {
        Rettangolo rettangolo = new Rettangolo(50, 50, 100, 0, Color.RED, 50, Color.YELLOW);

        assertFalse(rettangolo.contiene(120, 50));
    }

    @Test
    void testContiene_puntoFuoriRettangoloInclinato() {
        Rettangolo rettangolo = new Rettangolo(100, 100, 80, 45, Color.BLUE, 40, Color.GREEN);

        assertFalse(rettangolo.contiene(200, 150));
    }

    @Test
    void testContiene_puntoEsternoVicinoAlRettangolo() {
        Rettangolo rettangolo = new Rettangolo(50, 50, 100, 0, Color.PURPLE, 50, Color.GRAY);

        //utilizzando la trigonometria
        double puntoX = rettangolo.getCoordinataX() + rettangolo.getLarghezza() / 2 + 0.1;
        double puntoY = rettangolo.getCoordinataY();

        assertFalse(rettangolo.contiene(puntoX, puntoY));
    }

    @Test
    void testContiene_puntoVicinoRettangoloMinimo() {
        Rettangolo rettangolo = new Rettangolo(50, 50, 0.01, 0, Color.ORANGE, 0.01, Color.CYAN);
        // Punto vicino al rettangolo ma esterno
        assertFalse(rettangolo.contiene(50.015, 50.015));
    }

    @Test
    void testContiene_puntoFuoriDalRettangoloMinimo() {
        Rettangolo rettangolo = new Rettangolo(50, 50, 0.01, 0, Color.RED, 0.01, Color.YELLOW);
        // Punto leggermente fuori dal rettangolo
        assertFalse(rettangolo.contiene(50.01, 50));
    }

    @Test
    void testContiene_puntoEsternoVicinoAlRettangoloInclinato() {
        Rettangolo rettangolo = new Rettangolo(50, 50, 40, 45, Color.LIGHTGREEN, 20, Color.LIGHTBLUE);
        // Punto appena fuori rispetto a un vertice
        double verticeAX = rettangolo.getVerticeAX();
        double verticeAY = rettangolo.getVerticeAY();
        // Sposto di poco fuori dal bordo
        double puntoX = verticeAX + 0.2;
        double puntoY = verticeAY + 0.2;
        assertFalse(rettangolo.contiene(puntoX, puntoY));
    }

}