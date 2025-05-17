package it.unisa.software_architecture_design.drawsnapdrawingtool.forme;

import javafx.scene.paint.Color;

public class AttributiForma {
    public double coordinataX;
    public double coordinataY;
    public double altezza;
    public double larghezza;
    public double angoloInclinazione;
    public Color colore;
    public Color coloreInterno;

    // Costruttore con valori di default
    public AttributiForma() {
        this.coordinataX = 0.0;
        this.coordinataY = 0.0;
        this.altezza = 50.0;
        this.larghezza = 100.0;
        this.angoloInclinazione = 0.0;
        this.colore = Color.BLUE;
        this.coloreInterno = Color.LIGHTBLUE;
    }

    // Costruttore con valori scelti
    public AttributiForma(double coordinataX, double coordinataY, double altezza, double larghezza,
                           double angoloInclinazione, Color colore, Color coloreInterno) {
        this.coordinataX = coordinataX;
        this.coordinataY = coordinataY;
        this.altezza = altezza;
        this.larghezza = larghezza;
        this.angoloInclinazione = angoloInclinazione;
        this.colore = colore;
        this.coloreInterno = coloreInterno;
    }
}
