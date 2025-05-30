package it.unisa.software_architecture_design.drawsnapdrawingtool.forme;

import javafx.scene.paint.Color;

public class FactoryTesto implements FactoryForma{

    private String testo = "Default";

    public void setTesto(String testo) {
        this.testo = testo;
    }
    /**
     * Crea una forma con i parametri specificati.
     *
     * @param coordinataX        La coordinata X della posizione della forma.
     * @param coordinataY        La coordinata Y della posizione della forma.
     * @param altezza            L'altezza della forma.
     * @param larghezza          La larghezza della forma.
     * @param angoloInclinazione L'angolo di inclinazione della forma (in gradi).
     * @param colore             Il colore della forma.
     * @param coloreInterno      Il colore di riempimento interno della forma.
     * @return Un'istanza di una classe che implementa l'interfaccia `Forma`.
     */
    @Override
    public Forma creaForma(double coordinataX, double coordinataY, double altezza, double larghezza, double angoloInclinazione, Color colore, Color coloreInterno) {
        return new Testo(coordinataX, coordinataY, larghezza, angoloInclinazione, colore, altezza, coloreInterno, testo);
    }
}