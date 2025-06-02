package it.unisa.software_architecture_design.drawsnapdrawingtool.forme;

import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.List;

/**
 * Builder per la classe Poligono.
 * Facilita la creazione di oggetti Poligono complessi passo dopo passo.
 */
public class PoligonoBuilder {

    private List<Double> puntiX;
    private List<Double> puntiY;
    private double angoloInclinazione;
    private Color colore;
    private Color coloreInterno;
    private boolean pointAddedSucces = true;

    public PoligonoBuilder() {
        this.puntiX = new ArrayList<>();
        this.puntiY = new ArrayList<>();
        this.angoloInclinazione = 0.0;
        this.colore = Color.BLACK; // Default border color
        this.coloreInterno = Color.TRANSPARENT; // Default fill color
    }

    public PoligonoBuilder addPunto(double x, double y) {

        for(int i = 0; i< puntiX.size(); i++) {
            double existingX = puntiX.get(i);
            double existingY = puntiY.get(i);

            double distanza = Math.sqrt(
                    Math.pow(existingX - x, 2) + Math.pow(existingY - y, 2)
            );

            if(distanza < 5){
                pointAddedSucces = false;
                return this;
            }
            pointAddedSucces = true;
        }
        this.puntiX.add(x);
        this.puntiY.add(y);
        return this;
    }

    public boolean wasPointAddedSuccess(){return pointAddedSucces;}

    public PoligonoBuilder setAngoloInclinazione(double angoloInclinazione) {
        this.angoloInclinazione = angoloInclinazione;
        return this;
    }

    public PoligonoBuilder setColore(Color colore) {
        if (colore != null) {
            this.colore = colore;
        }
        return this;
    }

    public PoligonoBuilder setColoreInterno(Color coloreInterno) {
        if (coloreInterno != null) {
            this.coloreInterno = coloreInterno;
        }
        return this;
    }

    public PoligonoBuilder setPuntiX(List<Double> puntiX) {
        this.puntiX = new ArrayList<>(puntiX); // Defensive copy
        return this;
    }

    public PoligonoBuilder setPuntiY(List<Double> puntiY) {
        this.puntiY = new ArrayList<>(puntiY); // Defensive copy
        return this;
    }

    // Metodi per ottenere copie dei punti (utili per la preview)
    public List<Double> getPuntiX() {
        return new ArrayList<>(this.puntiX);
    }

    public List<Double> getPuntiY() {
        return new ArrayList<>(this.puntiY);
    }

    public double getAngoloInclinazione() {
        return angoloInclinazione;
    }

    public Color getColore() {
        return colore;
    }

    public Color getColoreInterno() {
        return coloreInterno;
    }

    private double calculateCentroX() {
        if (puntiX.isEmpty()) return 0.0;
        return puntiX.stream().mapToDouble(Double::doubleValue).average().orElse(0.0);
    }

    private double calculateCentroY() {
        if (puntiY.isEmpty()) return 0.0;
        return puntiY.stream().mapToDouble(Double::doubleValue).average().orElse(0.0);
    }

    private double calculateLarghezzaPoligono() {
        if (puntiX.size() < 2) return 0.0;
        double minX = puntiX.stream().mapToDouble(Double::doubleValue).min().orElse(0.0);
        double maxX = puntiX.stream().mapToDouble(Double::doubleValue).max().orElse(0.0);
        return maxX - minX;
    }

    private double calculateAltezzaPoligono() {
        if (puntiY.size() < 2) return 0.0;
        double minY = puntiY.stream().mapToDouble(Double::doubleValue).min().orElse(0.0);
        double maxY = puntiY.stream().mapToDouble(Double::doubleValue).max().orElse(0.0);
        return maxY - minY;
    }

    public int getNumeroPunti() {
        return this.puntiX.size();
    }

    public Poligono build() {
        if (puntiX.isEmpty() || puntiY.isEmpty() || puntiX.size() != puntiY.size()) {
            throw new IllegalStateException("Punti X e Y devono essere forniti e avere la stessa dimensione.");
        }
        if (puntiX.size() < 3) {
            throw new IllegalStateException("Un poligono deve avere almeno 3 punti per essere costruito.");
        }

        double centroX = calculateCentroX();
        double centroY = calculateCentroY();
        double larghezzaEffettiva = calculateLarghezzaPoligono();
        double altezzaEffettiva = calculateAltezzaPoligono();

        return new Poligono(
                centroX,
                centroY,
                altezzaEffettiva,    // Original logic: pass height as width parameter
                larghezzaEffettiva,  // Original logic: pass width as height parameter
                this.angoloInclinazione,
                this.colore,
                new ArrayList<>(this.puntiX),
                new ArrayList<>(this.puntiY),
                this.coloreInterno
        );
    }
}


