package it.unisa.software_architecture_design.drawsnapdrawingtool.forme;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class Linea extends Forma  {
    private double xInizio;
    private double yInizio;
    private double xFine;
    private double yFine;
    /**
     * Costruttore, Setter
     */
    public Linea(double coordinataX, double coordinataY, double larghezza, double angoloInclinazione, Color colore) {
        super(coordinataX, coordinataY, larghezza, angoloInclinazione, colore); // Chiamata al costruttore della classe estesa Forma
    }

    public double getxInizio() {
        return xInizio;
    }

    public double getyInizio() {
        return yInizio;
    }

    public double getxFine() {
        return xFine;
    }

    public double getyFine() {
        return yFine;
    }
    @Override
    public void setCoordinataY(double coordinataY) {
        double larghezza = getLarghezza();
        this.yInizio = coordinataY - (larghezza / 2) * Math.sin(Math.toRadians(getAngoloInclinazione()));
        this.yFine = coordinataY + (larghezza / 2) * Math.sin(Math.toRadians(getAngoloInclinazione()));
        super.setCoordinataY(coordinataY);
    }

    @Override
    public void setCoordinataX(double coordinataX) {
        double larghezza = getLarghezza();
        this.xInizio = coordinataX - (larghezza / 2) * Math.cos(Math.toRadians(getAngoloInclinazione()));
        this.xFine = coordinataX + (larghezza/2) * Math.cos(Math.toRadians(getAngoloInclinazione()));
        super.setCoordinataX(coordinataX);
    }
    /**
     *
     * @param gc
     */
    @Override
    public void disegna(GraphicsContext gc) {
        // Imposta il colore del GraphicsContext
        gc.setStroke(getColore());

        // Calcola i punti iniziali e finali della linea
        double xInizio = getxInizio();
        double yInizio = getyInizio();
        double xFine = getxFine();
        double yFine = getyFine();

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
        double xInizio = getxInizio();
        double yInizio = getyInizio();
        double xFine = getxFine();
        double yFine = getyFine();
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
