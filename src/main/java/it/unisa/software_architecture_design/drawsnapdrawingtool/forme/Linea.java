package it.unisa.software_architecture_design.drawsnapdrawingtool.forme;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.awt.geom.Line2D;

public class Linea extends Forma  {
    /*
     * Attributi
     */
    private double xInizio;
    private double yInizio;
    private double xFine;
    private double yFine;

    /*
     * Costruttore, getter e setter
     */
    public Linea(double coordinataX, double coordinataY, double larghezza, double angoloInclinazione, Color colore) {
        super(coordinataX, coordinataY, larghezza, angoloInclinazione, colore); // Chiamata al costruttore della classe estesa Forma
        updateCoordinateYInizioFine();
        updateCoordinateXInizioFine();
    }

    private void updateCoordinateYInizioFine() {
        this.yInizio = this.getCoordinataY() - (this.getLarghezza() / 2) * Math.sin(Math.toRadians(getAngoloInclinazione()));
        this.yFine = this.getCoordinataY() + (this.getLarghezza() / 2) * Math.sin(Math.toRadians(getAngoloInclinazione()));
    }

    private void updateCoordinateXInizioFine() {
        this.xInizio = this.getCoordinataX() - (this.getLarghezza() / 2) * Math.cos(Math.toRadians(getAngoloInclinazione()));
        this.xFine = this.getCoordinataX() + (this.getLarghezza() / 2) * Math.cos(Math.toRadians(getAngoloInclinazione()));
    }

    public double getXInizio() {
        return xInizio;
    }

    public double getYInizio() {
        return yInizio;
    }

    public double getXFine() {
        return xFine;
    }

    public double getYFine() {
        return yFine;
    }

    @Override
    public void setCoordinataY(double coordinataY) {
        super.setCoordinataY(coordinataY);
        updateCoordinateYInizioFine();
    }

    @Override
    public void setCoordinataX(double coordinataX) {
        super.setCoordinataX(coordinataX);
        updateCoordinateXInizioFine();
    }

    /*
      Logica della classe
     */

    /**
     * Disegna una linea sul {@link GraphicsContext} specificato.
     * Questo metodo disegna una linea utilizzando le coordinate di inizio e fine fornite dagli attributi
     * dell'oggetto usando il colore della forma.
     *
     * @param gc il {@code GraphicsContext} su cui disegnare la linea.
     *           Deve essere già inizializzato e associato a un {@code Canvas} valido.
     */
    @Override
    public void disegna(GraphicsContext gc) {
        // Salva lo stato iniziale del foglio di disegno
        gc.save();

        // Imposta il colore del GraphicsContext
        gc.setStroke(getColore());

        // Calcola i punti iniziali e finali della linea
        double xInizio = getXInizio();
        double yInizio = getYInizio();
        double xFine = getXFine();
        double yFine = getYFine();

        // Disegna la linea
        gc.strokeLine(xInizio, yInizio, xFine, yFine);

        // Ripristina lo stato iniziale del foglio di disegno
        gc.restore();
    }

    /**
     * Determina se la linea contiene un punto specifico nello spazio.
     *
     * @param puntoDaValutareX La coordinata X del punto da verificare.
     * @param puntoDaValutareY La coordinata Y del punto da verificare.
     * @return {@code true} se il punto specificato (puntoDaValutareX, puntoDaValutareY) si trova sulla linea,
     *         altrimenti {@code false}.
     */
    @Override
    public boolean contiene(double puntoDaValutareX, double puntoDaValutareY) {
        //Punti alle estremità della linea
        double xInizio = getXInizio();
        double yInizio = getYInizio();
        double xFine = getXFine();
        double yFine = getYFine();
        final double TOLLERANZA = 10; //si considerano anche i punti nell'intorno della linea

        Line2D line = new Line2D.Double(xInizio, yInizio, xFine, yFine);
        return line.ptSegDist(puntoDaValutareX, puntoDaValutareY) <= TOLLERANZA;
    }
}
