package it.unisa.software_architecture_design.drawsnapdrawingtool.forme;

import org.junit.jupiter.api.Test;
import javafx.scene.paint.Color;

import static org.junit.jupiter.api.Assertions.*;

class LineaTest {

    @Test
    void testContiene_puntoSullaLineaOrizzontale() {
        Linea linea = new Linea(100, 200, 150, 0, Color.BLACK);

        assertTrue(linea.contiene(100, 200));
    }

    @Test
    void testContiene_puntoSullaLineaInclinata() {
        Linea linea = new Linea(200, 300, 200, 45, Color.BLACK);

        //utilizzando trigonometria
        double puntoX = linea.getXInizio() + (linea.getXFine() - linea.getXInizio()) / 2;
        double puntoY = linea.getYInizio() + (linea.getYFine() - linea.getYInizio()) / 2;

        assertTrue(linea.contiene(puntoX, puntoY));
    }
    @Test
    void testContiene_puntoSullaLineaInclinazioneNegativa() {
        Linea linea = new Linea(300, 300, 100, 210, Color.RED);

        //utilizzando trigonometria
        double puntoX = linea.getXInizio() + (linea.getXFine() - linea.getXInizio()) * 0.25;
        double puntoY = linea.getYInizio() + (linea.getYFine() - linea.getYInizio()) * 0.25;

        assertTrue(linea.contiene(puntoX, puntoY), "Un punto interno a una linea inclinata negativamente dovrebbe essere considerato sulla linea");
    }

    @Test
    void testContiene_puntoSullaLineaVerticale() {
        Linea linea = new Linea(150, 100, 200, 90, Color.RED);

        assertTrue(linea.contiene(150, 200));
    }

    @Test
    void testContiene_puntoEstremoFinaleDellaLinea() {
        Linea linea = new Linea(50, 50, 100, 0, Color.GREEN);

        assertTrue(linea.contiene(100, 50));
    }

    @Test
    void testContiene_puntoEstremoInizialeLineaOrizzontale() {
        Linea linea = new Linea(50, 50, 100, 0, Color.GREEN);

        assertTrue(linea.contiene(0, 50));
    }

    @Test
    void testContiene_puntoFuoriDallaLineaOrizzontale() {
        Linea linea = new Linea(100, 100, 300, 0, Color.BLUE);

        assertFalse(linea.contiene(200, 200));
    }

    @Test
    void testContiene_puntoFuoriDallaLineaInclinata() {
        Linea linea = new Linea(0, 30, 150, 20, Color.BLUE);

        assertFalse(linea.contiene(0, 0));
    }

    @Test
    void testContiene_puntoEsternoVicinoAllaLinea() {
        Linea linea = new Linea(100, 100, 200, 45, Color.GRAY);

        //utilizzando la trigonometria
        double puntoX = linea.getXInizio() + (linea.getXFine() - linea.getXInizio()) * 0.75;
        double puntoY = linea.getYInizio() + (linea.getYFine() - linea.getYInizio()) * 0.75;

        puntoX = puntoX +2;

        assertFalse(linea.contiene(puntoX, puntoY));
    }

    @Test
    void testContiene_puntoEsternoLontanoDallaLinea() {
        Linea linea = new Linea(100, 400, 400, 0, Color.YELLOW);

        assertFalse(linea.contiene(600, 600), "Un punto molto lontano dalla linea non dovrebbe essere considerato sulla linea");
    }

    @Test
    void testContiene_puntoSullaRettaMaNonSulSegmento() {
        Linea linea = new Linea(150, 150, 200, 90, Color.BLACK);

        assertFalse(linea.contiene(150, 400), "Un punto allineato ma fuori dai limiti del segmento non dovrebbe essere considerato sulla linea");
    }
}