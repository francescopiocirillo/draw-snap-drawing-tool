package it.unisa.software_architecture_design.drawsnapdrawingtool.forme;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.List;

public class Poligono extends Forma {

    private List<Double> puntiX;
    private List<Double> puntiY;
    private transient Color coloreInterno;
    private double altezza;



    public Poligono(double coordinataX, double coordinataY,double altezza, double larghezza, double angoloInclinazione, Color colore, List<Double> puntiX, List<Double> puntiY, Color coloreInterno) {
        super(coordinataX, coordinataY, larghezza, angoloInclinazione, colore);
        this.altezza = altezza;
        this.puntiX = puntiX;
        this.puntiY = puntiY;
        this.coloreInterno = coloreInterno;
    }

    @Override
    public void setCoordinataX(double coordinataX) {
        double deltaX = coordinataX - super.getCoordinataX();
        trasla(deltaX, 0); // Sposta il poligono in orizzontale
    }

    @Override
    public void setCoordinataY(double coordinataY) {
        double deltaY = coordinataY - super.getCoordinataY();
        trasla(0, deltaY); // Sposta il poligono in verticale
    }

    public List<Double> getPuntiY() {
        return puntiY;
    }

    public void setPuntiY(List<Double> puntiY) {
        this.puntiY = puntiY;
        calcolaBoundingBox();
    }

    public List<Double> getPuntiX() {
        return puntiX;
    }

    public void setPuntiX(List<Double> puntiX) {
        this.puntiX = puntiX;
        calcolaBoundingBox();

    }

    public Color getColoreInterno() {
        return coloreInterno;
    }

    public void setColoreInterno(Color coloreInterno) {
        this.coloreInterno = coloreInterno;
    }

    public double getAltezza() {
        return altezza;
    }

    public void setAltezza(double altezza) {
        this.altezza = altezza;
    }



    @Override
    public void disegna(GraphicsContext gc) {
        // Salva lo stato iniziale del foglio di disegno
        gc.save();
        double[] xArray = puntiX.stream().mapToDouble(Double::doubleValue).toArray();
        double[] yArray = puntiY.stream().mapToDouble(Double::doubleValue).toArray();


        // Imposta il colore del contorno e dell'interno
        gc.setFill(getColoreInterno());
        gc.setStroke(getColore());
        gc.setLineWidth(2);

        // fill disegna l'area interna del rettangolo
        gc.fillPolygon(
                xArray,
                yArray,
                puntiX.size() // Numero di punti
        );

        // stroke disegna il contorno
        gc.strokePolygon(
                xArray,
                yArray,
                puntiX.size() // Numero di punti
        );

        // Ripristina lo stato iniziale del foglio di disegno
        gc.restore();
    }


    @Override
    public boolean contiene(double x, double y) {
        int n = puntiX.size();
        boolean dentro = false;

        for (int i = 0, j = n - 1; i < n; j = i++) {
            double xi = puntiX.get(i), yi = puntiY.get(i);
            double xj = puntiX.get(j), yj = puntiY.get(j);
            boolean interseca = ((yi > y) != (yj > y)) &&
                    (x < (xj - xi) * (y - yi) / (yj - yi + 1e-10) + xi);
            if (interseca) dentro = !dentro;
        }

        return dentro;
    }

    @Override
    public void specchia() {

    }

    /**
     * Calcola la bounding box del poligono (min/max X/Y) e il suo centro.
     * Aggiorna anche le proprietà `coordinataX`, `coordinataY`, `larghezza` e `altezza`
     * ereditate da Forma per riflettere il centro e le dimensioni della bounding box.
     */
    private void calcolaBoundingBox() {
        if (puntiX == null || puntiX.isEmpty()) {
            super.setCoordinataX(0);
            super.setCoordinataY(0);
            super.setLarghezza(0);
            setAltezza(0);
            return;
        }

        double minX = puntiX.get(0);
        double maxX = puntiX.get(0);
        double minY = puntiY.get(0);
        double maxY = puntiY.get(0);

        for (int i = 1; i < puntiX.size(); i++) {
            minX = Math.min(minX, puntiX.get(i));
            maxX = Math.max(maxX, puntiX.get(i));
            minY = Math.min(minY, puntiY.get(i));
            maxY = Math.max(maxY, puntiY.get(i));
        }

        // Imposta il centro della bounding box direttamente negli attributi della superclasse
        super.setCoordinataX((minX + maxX) / 2.0);
        super.setCoordinataY((minY + maxY) / 2.0);
        super.setLarghezza(maxX - minX);
        setAltezza(maxY - minY); // Aggiorna l'altezza della superclasse
    }

    // Metodo per traslare tutti i punti del poligono
    // Questo è il metodo che dovrebbe essere chiamato dal handleMouseDragged
    public void trasla(double deltaX, double deltaY) {
        for (int i = 0; i < puntiX.size(); i++) {
            puntiX.set(i, puntiX.get(i) + deltaX);
            puntiY.set(i, puntiY.get(i) + deltaY);
        }
        // Dopo aver traslato i punti, ricalcola la bounding box e aggiorna
        // la posizione del centro nella superclasse Forma.
        calcolaBoundingBox();
    }

    @Override
    public Poligono clone() { // Nota il tipo di ritorno specifico per una migliore tipizzazione
        Poligono cloned = (Poligono) super.clone(); // Clona gli attributi di Forma

        // *** Copia profonda delle liste di punti ***
        // Questo è il punto chiave per i poligoni
        cloned.puntiX = new ArrayList<>(this.puntiX);
        cloned.puntiY = new ArrayList<>(this.puntiY);

        // Gli oggetti Color sono immutabili, quindi un riferimento va bene per coloreInterno
        cloned.coloreInterno = this.coloreInterno;

        // Ricalcola la bounding box della copia, in caso ci siano dipendenze interne
        cloned.calcolaBoundingBox();

        return cloned;
    }

    // Metodo per scalare il poligono
    // Questo metodo è più robusto se prende fattori di scala
    public void scala(double scalaX, double scalaY) {
        if (puntiX.isEmpty()) return;

        List<Double> nuoviPuntiX = new ArrayList<>();
        List<Double> nuoviPuntiY = new ArrayList<>();

        for (int i = 0; i < puntiX.size(); i++) {
            double oldX = puntiX.get(i);
            double oldY = puntiY.get(i);

            // Sposta il punto all'origine (rispetto al centro interno del poligono)
            double translatedX = oldX - super.getCoordinataX();
            double translatedY = oldY - super.getCoordinataY();

            // Scala il punto
            double scaledX = translatedX * scalaX;
            double scaledY = translatedY * scalaY;

            // Riporta il punto alla sua posizione originale (rispetto al centro interno)
            nuoviPuntiX.add(scaledX + super.getCoordinataX());
            nuoviPuntiY.add(scaledY + super.getCoordinataY());
        }
        this.puntiX = nuoviPuntiX; // Aggiorna le liste di punti
        this.puntiY = nuoviPuntiY;
        calcolaBoundingBox(); // Ricalcola il centro e la bounding box dopo la scala
    }

}
