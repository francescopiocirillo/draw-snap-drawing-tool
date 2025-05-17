package it.unisa.software_architecture_design.drawsnapdrawingtool.forme;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

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

    private void updateCoordinateYInizioFine() {
        this.yInizio = this.getCoordinataY() - (this.getLarghezza() / 2) * Math.sin(Math.toRadians(getAngoloInclinazione()));
        this.yFine = this.getCoordinataY() + (this.getLarghezza() / 2) * Math.sin(Math.toRadians(getAngoloInclinazione()));
    }

    private void updateCoordinateXInizioFine() {
        this.xInizio = this.getCoordinataX() - (this.getLarghezza() / 2) * Math.cos(Math.toRadians(getAngoloInclinazione()));
        this.xFine = this.getCoordinataX() + (this.getLarghezza() / 2) * Math.cos(Math.toRadians(getAngoloInclinazione()));
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
        // Imposta il colore del GraphicsContext
        gc.setStroke(getColore());

        // Calcola i punti iniziali e finali della linea
        double xInizio = getXInizio();
        double yInizio = getYInizio();
        double xFine = getXFine();
        double yFine = getYFine();

        // Disegna la linea
        gc.strokeLine(xInizio, yInizio, xFine, yFine);

    }

    /**
     * Determina se la linea contiene un punto specifico nello spazio.
     *
     * @param px La coordinata X del punto da verificare.
     * @param py La coordinata Y del punto da verificare.
     * @return {@code true} se il punto specificato (px, py) si trova sulla linea,
     *         altrimenti {@code false}.
     */
    @Override
    public boolean contiene(double px, double py) {
        //Punti alle estremità della linea
        double xInizio = getXInizio();
        double yInizio = getYInizio();
        double xFine = getXFine();
        double yFine = getYFine();
        final double TOLLERANZA = 0.1; //si considerano anche i punti nell'intorno della linea

        //Calcolo appartenenza alla retta che passa per i punti di inizio e di fine
        double dx = xFine - xInizio;
        double dy = yFine - yInizio;
        double determinante = (px - xInizio) * dy - (py - yInizio) * dx;

        if (Math.abs(determinante) > TOLLERANZA) {
            return false; // Il punto non appartiene alla retta
        }

        //Calcolo appartenenza al segmento compreso tra i punti di inizio e di fine
        return (px >= Math.min(xInizio, xFine) && px <= Math.max(xInizio, xFine)) &&
                (py >= Math.min(yInizio, yFine) && py <= Math.max(yInizio, yFine));
    }
}
