package it.unisa.software_architecture_design.drawsnapdrawingtool.forme;

import javafx.scene.paint.Color;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.List;

class PoligonoTest {

    private List<Double> quadratoPuntiX_raw = Arrays.asList(90.0, 110.0, 110.0, 90.0);
    private List<Double> quadratoPuntiY_raw = Arrays.asList(90.0, 90.0, 110.0, 110.0);

    private List<Double> triangoloPuntiX_raw = Arrays.asList(100.0, 120.0, 80.0);
    private List<Double> triangoloPuntiY_raw = Arrays.asList(80.0, 120.0, 120.0);

    private List<Double> lShapePuntiX_raw = Arrays.asList(80.0, 80.0, 120.0, 120.0, 100.0, 100.0);
    private List<Double> lShapePuntiY_raw = Arrays.asList(80.0, 120.0, 120.0, 100.0, 100.0, 80.0);


    @Test
    void testContiene_puntoNelQuadratoOrizzontale() {
        Poligono quadrato = new Poligono(100, 100, 20, 20, 0, Color.BLACK, quadratoPuntiX_raw, quadratoPuntiY_raw, Color.WHITE);
        assertTrue(quadrato.contiene(100, 100));
        assertTrue(quadrato.contiene(95, 95));
        assertTrue(quadrato.contiene(105, 105));
    }

    @Test
    void testContiene_puntoNelQuadratoInclinato45Gradi() {
        Poligono quadrato = new Poligono(100, 100, 20, 20, 45, Color.BLACK, quadratoPuntiX_raw, quadratoPuntiY_raw, Color.WHITE);
        assertTrue(quadrato.contiene(100, 100));
        assertTrue(quadrato.contiene(100 + 5, 100 - 5));
    }

    @Test
    void testContiene_puntoNelTriangoloInclinato() {
        Poligono triangolo = new Poligono(100, 100, 40, 40, 30, Color.RED, triangoloPuntiX_raw, triangoloPuntiY_raw, Color.PINK);
        assertTrue(triangolo.contiene(100, 100));
        assertTrue(triangolo.contiene(100, 105));
    }

    @Test
    void testContiene_puntoSulBordoDelQuadrato() {
        Poligono quadrato = new Poligono(100, 100, 20, 20, 0, Color.BLACK, quadratoPuntiX_raw, quadratoPuntiY_raw, Color.WHITE);
        assertTrue(quadrato.contiene(100, 90));
        assertTrue(quadrato.contiene(90, 90));
        assertTrue(quadrato.contiene(110, 110));
    }


    @Test
    void testContiene_puntoFuoriDalQuadratoInclinato() {
        Poligono quadrato = new Poligono(100, 100, 20, 20, 45, Color.BLACK, quadratoPuntiX_raw, quadratoPuntiY_raw, Color.WHITE);
        assertFalse(quadrato.contiene(50, 50));
        assertFalse(quadrato.contiene(150, 150));
        assertFalse(quadrato.contiene(90, 90));
    }

    @Test
    void testContiene_puntoNelPoligonoComplessoLShape() {
        Poligono lShape = new Poligono(100, 100, 40, 40, 0, Color.BLACK, lShapePuntiX_raw, lShapePuntiY_raw, Color.WHITE);
        assertTrue(lShape.contiene(90, 90));
        assertTrue(lShape.contiene(110, 110));
        assertTrue(lShape.contiene(95, 115));
    }


}