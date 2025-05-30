package it.unisa.software_architecture_design.drawsnapdrawingtool.forme;

import javafx.scene.paint.Color;

public class AttributiForma {
    private double coordinataX;
    private double coordinataY;
    private double altezza;
    private double larghezza;
    private double angoloInclinazione;
    private Color colore;
    private Color coloreInterno;
    private String testo;

    // Costruttore con valori di default
    public AttributiForma() {
        this.coordinataX = 0.0;
        this.coordinataY = 0.0;
        this.altezza = 50.0;
        this.larghezza = 100.0;
        this.angoloInclinazione = 0.0;
        this.colore = Color.BLUE;
        this.coloreInterno = Color.LIGHTBLUE;
        this.testo = "default";
    }

    // Costruttore con valori scelti
    public AttributiForma(double coordinataX, double coordinataY, double altezza, double larghezza,
                           double angoloInclinazione, Color colore, Color coloreInterno, String testo) {
        this.coordinataX = coordinataX;
        this.coordinataY = coordinataY;
        this.altezza = altezza;
        this.larghezza = larghezza;
        this.angoloInclinazione = angoloInclinazione;
        this.colore = colore;
        this.coloreInterno = coloreInterno;
        this.testo = testo;
    }

    public double getCoordinataY() {
        return coordinataY;
    }

    public void setCoordinataY(double coordinataY) {
        this.coordinataY = coordinataY;
    }

    public double getAltezza() {
        return altezza;
    }

    public void setAltezza(double altezza) {
        this.altezza = altezza;
    }

    public double getLarghezza() {
        return larghezza;
    }

    public void setLarghezza(double larghezza) {
        this.larghezza = larghezza;
    }

    public double getAngoloInclinazione() {
        return angoloInclinazione;
    }

    public void setAngoloInclinazione(double angoloInclinazione) {
        this.angoloInclinazione = angoloInclinazione;
    }

    public Color getColore() {
        return colore;
    }

    public void setColore(Color colore) {
        this.colore = colore;
    }

    public Color getColoreInterno() {
        return coloreInterno;
    }

    public void setColoreInterno(Color coloreInterno) {
        this.coloreInterno = coloreInterno;
    }

    public double getCoordinataX() {
        return coordinataX;
    }

    public void setCoordinataX(double coordinataX) {
        this.coordinataX = coordinataX;
    }

    public String getTesto() {
        return testo;
    }

    public void setTesto(String testo) {
        this.testo = testo;
    }
}
