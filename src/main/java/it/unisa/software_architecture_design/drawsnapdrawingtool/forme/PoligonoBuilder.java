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
    private boolean puntoAggiunto = true;

    public PoligonoBuilder() {
        this.puntiX = new ArrayList<>();
        this.puntiY = new ArrayList<>();
        this.angoloInclinazione = 0.0;
        this.colore = Color.BLACK;
        this.coloreInterno = Color.WHITE;
    }

    /**
     * Aggiunge un nuovo punto al poligono in costruzione.
     * Prima di aggiungere il punto, verifica che non sia troppo vicino a punti già esistenti
     * per evitare sovrapposizioni o problemi di selezione.
     *
     * @param x La coordinata X del punto.
     * @param y La coordinata Y del punto.
     * @return Questa istanza di {@code PoligonoBuilder} per consentire il concatenamento dei metodi.
     */
    public PoligonoBuilder addPunto(double x, double y) {

        for(int i = 0; i< puntiX.size(); i++) {
            double existingX = puntiX.get(i);
            double existingY = puntiY.get(i);
            // Calcola la distanza euclidea tra il nuovo punto e i punti esistenti
            double distanza = Math.sqrt(
                    Math.pow(existingX - x, 2) + Math.pow(existingY - y, 2)
            );

            if(distanza < 5){
                puntoAggiunto = false;
                return this;
            }
            puntoAggiunto = true;
        }
        this.puntiX.add(x);
        this.puntiY.add(y);
        return this;
    }

    /**
     * Controlla se l'ultimo tentativo di aggiunta di un punto è avvenuto con successo.
     * Utile per verificare se un punto è stato scartato perché troppo vicino
     * a un punto esistente.
     *
     * @return {@code true} se l'ultimo punto è stato aggiunto con successo, {@code false} altrimenti.
     */
    public boolean wasPointAddedSuccess(){return puntoAggiunto;}

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
        this.puntiX = new ArrayList<>(puntiX);
        return this;
    }

    public PoligonoBuilder setPuntiY(List<Double> puntiY) {
        this.puntiY = new ArrayList<>(puntiY);
        return this;
    }

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

    /**
     * Calcola la coordinata X media dei vertici del poligono,
     * determinando effettivamente il suo centro orizzontale.
     *
     * @return La coordinata X del centro calcolata, o 0.0 se non sono definiti punti.
     */
    private double calcolaCentroX() {
        if (puntiX.isEmpty()) return 0.0;
        return puntiX.stream().mapToDouble(Double::doubleValue).average().orElse(0.0);
    }

    /**
     * Calcola la coordinata Y media dei vertici del poligono,
     * determinando effettivamente il suo centro verticale.
     *
     * @return La coordinata Y del centro calcolata, o 0.0 se non sono definiti punti.
     */
    private double calcolaCentroY() {
        if (puntiY.isEmpty()) return 0.0;
        return puntiY.stream().mapToDouble(Double::doubleValue).average().orElse(0.0);
    }

    /**
     * Calcola la larghezza effettiva del poligono basata sulla differenza
     * tra le sue coordinate X massime e minime.
     *
     * @return La larghezza calcolata, o 0.0 se sono definiti meno di due punti.
     */
    private double calcolaLarghezzaPoligono() {
        if (puntiX.size() < 2) return 0.0;
        double minX = puntiX.stream().mapToDouble(Double::doubleValue).min().orElse(0.0);
        double maxX = puntiX.stream().mapToDouble(Double::doubleValue).max().orElse(0.0);
        return maxX - minX;
    }

    /**
     * Calcola l'altezza effettiva del poligono basata sulla differenza
     * tra le sue coordinate Y massime e minime.
     *
     * @return L'altezza calcolata, o 0.0 se sono definiti meno di due punti.
     */
    private double calcolaAltezzaPoligono() {
        if (puntiY.size() < 2) return 0.0;
        double minY = puntiY.stream().mapToDouble(Double::doubleValue).min().orElse(0.0);
        double maxY = puntiY.stream().mapToDouble(Double::doubleValue).max().orElse(0.0);
        return maxY - minY;
    }

    public int getNumeroPunti() {
        return this.puntiX.size();
    }

    /**
     * Costruisce e restituisce un nuovo oggetto {@link Poligono} basato sui parametri attualmente configurati.
     *
     * @return Una nuova istanza di {@link Poligono}.
     * @throws IllegalStateException Se i punti sono mancanti, hanno dimensioni non corrispondenti,
     * o se vengono forniti meno di 3 punti.
     */
    public Poligono build() {
        if (puntiX.isEmpty() || puntiY.isEmpty() || puntiX.size() != puntiY.size()) {
            throw new IllegalStateException("Punti X e Y devono essere forniti e avere la stessa dimensione.");
        }
        if (puntiX.size() < 3) {
            throw new IllegalStateException("Un poligono deve avere almeno 3 punti per essere costruito.");
        }

        double centroX = calcolaCentroX();
        double centroY = calcolaCentroY();
        double larghezzaEffettiva = calcolaLarghezzaPoligono();
        double altezzaEffettiva = calcolaAltezzaPoligono();

        return new Poligono(
                centroX,
                centroY,
                larghezzaEffettiva,
                altezzaEffettiva,
                this.angoloInclinazione,
                this.colore,
                new ArrayList<>(this.puntiX),
                new ArrayList<>(this.puntiY),
                this.coloreInterno
        );
    }
}


