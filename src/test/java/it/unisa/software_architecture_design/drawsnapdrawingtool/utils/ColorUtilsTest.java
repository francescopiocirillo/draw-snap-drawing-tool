package it.unisa.software_architecture_design.drawsnapdrawingtool.utils;

import javafx.scene.paint.Color;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ColorUtilsTest {

    /**
     * Verifica che il metodo {@code toHexString} trasformi correttamente un colore
     * completamente opaco, cioè con alpha=1.
     * Nel caso specifico si testa il Rosso.
     */
    @Test
    void testToHexString_rossoOpaco() {
        String hex = ColorUtils.toHexString(Color.rgb(255, 0, 0));
        assertEquals("#FF0000", hex);
    }

    /**
     * Verifica che il metodo {@code toHexString} trasformi correttamente un colore
     * parzialmente trasparente, cioè con 0 < alpha < 1.
     * Nel caso specifico si testa il Verde con alpha = 0.5.
     */
    @Test
    public void testToHexString_verdeParzialmenteTrasparente() {
        String hex = ColorUtils.toHexString(Color.rgb(0, 255, 0, 0.5));
        assertEquals("#00FF0080", hex);
    }

    /**
     * Verifica che il metodo {@code toHexString} trasformi correttamente un colore
     * totalmente trasparente, cioè con alpha = 0.
     * Nel caso specifico si testa il Blu.
     */
    @Test
    public void testToHexString_bluTrasparente() {
        String hex = ColorUtils.toHexString(Color.rgb(0, 0, 255, 0.0));
        assertEquals("#0000FF00", hex);
    }

    /**
     * Verifica che il metodo {@code toHexString} trasformi correttamente il colore
     * con tutti i canali a 0, cioè il nero opaco.
     */
    @Test
    public void testToHexString_neroOpaco() {
        String hex = ColorUtils.toHexString(Color.BLACK);
        assertEquals("#000000", hex);
    }

    /**
     * Verifica che il metodo {@code toHexString} trasformi correttamente il colore
     * con tutti i canali a 1, cioè il bianco opaco.
     */
    @Test
    public void testToHexString_biancoOpaco() {
        String hex = ColorUtils.toHexString(Color.WHITE);
        assertEquals("#FFFFFF", hex);
    }

    /**
     * Verifica che il metodo {@code toHexString} trasformi correttamente il colore
     * quando il valore dei canali è sottoposto ad arrotondamento.
     * Nel caso specifico tutti i valori sono al 50% più 0.001 e il risultato è un grigio
     * parzialmente trasparente
     */
    @Test
    public void testToHexString_conArrotondamento() {
        String hex = ColorUtils.toHexString(new Color(0.501, 0.501, 0.501, 0.501));
        assertEquals("#80808080", hex);  // 0.501 * 255 ≈ 128 (0x80)
    }

    /**
     * Verifica che il metodo {@code toHexString} trasformi correttamente il colore
     * quando il valore dei canali è tale da produrre un generico colore.
     */
    @Test
    public void testToHexString_coloreGenerico1() {
        String hex = ColorUtils.toHexString(new Color(0.8, 0.2, 0.2, 0.75));
        assertEquals("#CC3333BF", hex);  // 0.501 * 255 ≈ 128 (0x80)
    }

    /**
     * Verifica che il metodo {@code toHexString} trasformi correttamente il colore
     * quando il valore dei canali è tale da produrre un generico colore.
     */
    @Test
    public void testToHexString_coloreGenerico2() {
        String hex = ColorUtils.toHexString(new Color(0.0, 1.0, 1.0, 0.6));
        assertEquals("#00FFFF99", hex);
    }

    /**
     * Verifica che il metodo {@code toHexString} trasformi correttamente il colore
     * quando il valore dei canali è tale da produrre un generico colore.
     */
    @Test
    public void testToHexString_coloreGenerico3() {
        String hex = ColorUtils.toHexString(new Color(0.5, 0.0, 0.5, 1.0));
        assertEquals("#800080", hex);
    }

    /**
     * Verifica che il metodo {@code toHexString} trasformi correttamente il colore
     * quando il valore dei canali è tale da produrre un generico colore.
     */
    @Test
    public void testToHexString_coloreGenerico4() {
        String hex = ColorUtils.toHexString(new Color(0.2, 0.4, 0.6, 0.3));
        assertEquals("#3366994D", hex);
    }
}