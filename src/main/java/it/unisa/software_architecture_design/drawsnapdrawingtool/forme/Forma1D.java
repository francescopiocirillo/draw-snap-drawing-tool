package it.unisa.software_architecture_design.drawsnapdrawingtool.forme;

import javafx.scene.paint.Color;

import java.awt.geom.Line2D;

/**
 * La classe {@link Forma1D} eredita le caratteristiche della classe {@link Forma}
 * e viene usata per distinguere le {@link Forma} ad 1 Dimensione. Si tratta di una
 * classe astratta che verrà estesa da delle classi concrete.
 */
public abstract class Forma1D extends Forma{
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
    public Forma1D(double coordinataX, double coordinataY, double larghezza, double angoloInclinazione, Color colore) {
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

    @Override
    public void setLarghezza(double larghezza) {
        super.setLarghezza(larghezza);
        updateCoordinateYInizioFine();
        updateCoordinateXInizioFine();
    }

    @Override
    public void setAngoloInclinazione(double angoloInclinazione) {
        super.setAngoloInclinazione(angoloInclinazione);
        updateCoordinateYInizioFine();
        updateCoordinateXInizioFine();
    }

    /*
     * Logica della Classe
     */

    /**
     * Gestisce la ridistribuzione dei valori della {@link Forma} per specchiarla
     * lungo l'asse verticale che passa per il centro della {@link Forma} stessa
     */
    @Override
    public void specchiaInVerticale(){
        double centroX = getCoordinataX();

        // Inverti le coordinate X rispetto al centro
        this.xInizio = 2 * centroX - this.xInizio;
        this.xFine = 2 * centroX - this.xFine;
        this.setAngoloInclinazione(180 - this.getAngoloInclinazione());
    }

    /**
     * Gestisce la ridistribuzione dei valori della {@link Forma} per specchiarla
     * lungo l'asse orizzontale che passa per il centro della {@link Forma} stessa
     */
    @Override
    public void specchiaInOrizzontale(){
        double centroY = getCoordinataY();

        // Inverti le coordinate Y rispetto al centro
        this.yInizio = 2 * centroY - this.yInizio;
        this.yFine = 2 * centroY - this.yFine;
        this.setAngoloInclinazione(360 - this.getAngoloInclinazione());
    }

    /**
     * Verifica se la {@link Linea} contiene un punto specifico nello spazio.
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

    /*
     * Metodi Ausiliari
     */

    /**
     * Gestisce l'aggiornamento delle coordinate di inizio e fine lungo l'asse Y.
     */
    private void updateCoordinateYInizioFine() {
        this.yInizio = this.getCoordinataY() - (this.getLarghezza() / 2) * Math.sin(Math.toRadians(getAngoloInclinazione()));
        this.yFine = this.getCoordinataY() + (this.getLarghezza() / 2) * Math.sin(Math.toRadians(getAngoloInclinazione()));
    }

    /**
     * Gestisce l'aggiornamento delle coordinate di inizio e fine lungo l'asse X.
     */
    private void updateCoordinateXInizioFine() {
        this.xInizio = this.getCoordinataX() - (this.getLarghezza() / 2) * Math.cos(Math.toRadians(getAngoloInclinazione()));
        this.xFine = this.getCoordinataX() + (this.getLarghezza() / 2) * Math.cos(Math.toRadians(getAngoloInclinazione()));
    }
}
