package it.unisa.software_architecture_design.drawsnapdrawingtool.forma;


import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;


public abstract class Forma {
    /**
     * Attributi
     */
    private double coordinataY;
    private double coordinataX;
    private double larghezza;
    private double angoloInclinazione;
    private Color colore;

    /**
     * Costruttore, getter e setter
     */
    public Forma(double coordinataX, double coordinataY, double larghezza, double angoloInclinazione, Color colore) {
        this.coordinataX = coordinataX;
        this.coordinataY = coordinataY;
        this.larghezza = larghezza;
        this.angoloInclinazione = angoloInclinazione;
        this.colore = colore;
    }

    public double getCoordinataY() {
        return coordinataY;
    }

    public double getCoordinataX() {
        return coordinataX;
    }

    public double getLarghezza() {
        return larghezza;
    }

    public double getAngoloInclinazione() {
        return angoloInclinazione;
    }

    public Color getColore() {
        return colore;
    }

    public void setCoordinataY(double coordinataY) {
        this.coordinataY = coordinataY;
    }

    public void setCoordinataX(double coordinataX) {
        this.coordinataX = coordinataX;
    }

    public void setLarghezza(double larghezza) {
        this.larghezza = larghezza;
    }

    public void setColore(Color colore) {
        this.colore = colore;
    }

    public void setAngoloInclinazione(double angoloInclidazione) {
        this.angoloInclinazione = angoloInclidazione;
    }

    /**
     * Logica della classe
     */

    /**
     *
     * @param gc
     */
    public abstract void disegna(GraphicsContext gc);

    /**
     * Determina se la forma contiene un punto specifico nello spazio.
     *
     * @param px La coordinata X del punto da verificare.
     * @param py La coordinata Y del punto da verificare.
     * @return {@code true} se il punto specificato (px, py) si trova all'interno della forma,
     *         altrimenti {@code false}.
     */
    public abstract boolean contiene(double px, double py);
}
