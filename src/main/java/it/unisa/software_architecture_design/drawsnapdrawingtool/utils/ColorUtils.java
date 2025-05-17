package it.unisa.software_architecture_design.drawsnapdrawingtool.utils;

import javafx.scene.paint.Color;

public class ColorUtils {

    /**
     * Converte l'oggetto di tipo Color in una stringa HEX in modo da permettere la serializzazione
     * @param color è il colore da convertire
     * @return la stringa HEX corrispondente al Color di input
     */
    public static String toHexString(Color color) {
        int r = (int) Math.round(color.getRed() * 255);
        int g = (int) Math.round(color.getGreen() * 255);
        int b = (int) Math.round(color.getBlue() * 255);
        int a = (int) Math.round(color.getOpacity() * 255);

        if (a < 255) {
            return String.format("#%02X%02X%02X%02X", r, g, b, a);
        } else {
            return String.format("#%02X%02X%02X", r, g, b);
        }
    }
}
