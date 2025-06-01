package it.unisa.software_architecture_design.drawsnapdrawingtool.forme;

import it.unisa.software_architecture_design.drawsnapdrawingtool.utils.ColorUtils;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serial;
import java.util.ArrayList;
import java.util.List;

public class Poligono extends Forma {
    /*
     * Attributi
     */
    private List<Double> puntiX; // Punti intrinseci, relativi al centro del poligono (0,0)
    private List<Double> puntiY; // Punti intrinseci, relativi al centro del poligono (0,0)
    private transient Color coloreInterno;
    // Dimensioni intrinseche (non ruotate) del poligono.
    private double intrinsicLarghezza;
    private double intrinsicAltezza;
    private double intrinsicCenterX; // Centro X della bounding box intrinseca, relativo all'origine interna (0,0)
    private double intrinsicCenterY; // Centro Y della bounding box intrinseca, relativo all'origine interna (0,0)

    /**
     * Costruttore, Getter e Setter
     *
     *
     */
    public Poligono(double initialRefX, double initialRefY, double larghezza, double altezza, double angoloInclinazione, Color colore, List<Double> rawPuntiX, List<Double> rawPuntiY, Color coloreInterno) {
        // Imposta la posizione globale del poligono direttamente al punto di riferimento iniziale.
        super(initialRefX, initialRefY, 0, angoloInclinazione, colore);

        this.puntiX = new ArrayList<>();
        this.puntiY = new ArrayList<>();
        this.coloreInterno = coloreInterno;

        // Converte i punti globali (rawPuntiX, rawPuntiY) in punti relativi
        // sottraendo le coordinate del punto di riferimento iniziale (initialRefX, initialRefY).
        for (int i = 0; i < rawPuntiX.size(); i++) {
            this.puntiX.add(rawPuntiX.get(i) - initialRefX);
            this.puntiY.add(rawPuntiY.get(i) - initialRefY);
        }
        // Calcola la bounding box intrinseca (larghezze/altezze minime e massime) dei punti relativi.
        calcolaBoundingBox();
    }

    // Getter per le dimensioni intrinseche
    @Override
    public double getLarghezza() {
        return intrinsicLarghezza;
    }

    public double getAltezza() {
        return intrinsicAltezza;
    }

    @Override
    public void setCoordinataX(double coordinataX) {
        super.setCoordinataX(coordinataX);
    }

    @Override
    public void setCoordinataY(double coordinataY) {
        super.setCoordinataY(coordinataY);
    }


    public Color getColoreInterno() {
        return coloreInterno;
    }

    public void setColoreInterno(Color coloreInterno) {
        this.coloreInterno = coloreInterno;
    }

    // Aggiungi questi getter:
    public double getIntrinsicCenterX() {
        return intrinsicCenterX;
    }

    public double getIntrinsicCenterY() {
        return intrinsicCenterY;
    }

    /*
     Logica della classe
    */

    /**
     * Disegna il poligono sul contesto grafico specificato.
     * Applica le trasformazioni (traslazione e rotazione) e poi disegna il poligono
     * utilizzando i suoi punti relativi.
     *
     * @param gc Il contesto grafico su cui disegnare il poligono.
     */
    @Override
    public void disegna(GraphicsContext gc) {
        gc.save();

        gc.translate(super.getCoordinataX(), super.getCoordinataY());

        gc.rotate(super.getAngoloInclinazione());

        // I punti sono già relativi all'origine (0,0) a causa della traslazione iniziale nel costruttore.
        double[] xArray = puntiX.stream().mapToDouble(Double::doubleValue).toArray();
        double[] yArray = puntiY.stream().mapToDouble(Double::doubleValue).toArray();

        gc.setFill(getColoreInterno());
        gc.setStroke(getColore());
        gc.setLineWidth(2);

        // fill disegna l'area interna del poligono
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

        gc.restore();
    }

    /**
     * Metodo helper per calcolare la distanza euclidea al quadrato tra due punti.
     *
     * @param x1 Coordinata X del primo punto.
     * @param y1 Coordinata Y del primo punto.
     * @param x2 Coordinata X del secondo punto.
     * @param y2 Coordinata Y del secondo punto.
     * @return La distanza al quadrato tra i due punti.
     */
    private double distanzaQuadrata(double x1, double y1, double x2, double y2) {
        double dx = x1 - x2;
        double dy = y1 - y2;
        return dx * dx + dy * dy;
    }

    /**
     * Verifica se un punto (px, py) è sufficientemente vicino al contorno del poligono.
     * Questo metodo opera nel sistema di coordinate intrinseco del poligono (dopo traslazione e rotazione inversa).
     *
     * @param px La coordinata X del punto da verificare (nel sistema di coordinate intrinseco).
     * @param py La coordinata Y del punto da verificare (nel sistema di coordinate intrinseco).
     * @param tolleranza La distanza massima per considerare il punto "vicino" al contorno.
     * @return {@code true} se il punto è vicino al contorno, altrimenti {@code false}.
     */
    private boolean puntoVicinoAlContorno(double px, double py, double tolleranza) {
        int n = puntiX.size();
        if (n < 2) return false; // Non c'è contorno con meno di 2 punti

        double tolleranzaQuadrata = tolleranza * tolleranza;

        for (int i = 0, j = n - 1; i < n; j = i++) {
            double ax = puntiX.get(j); // Vertice precedente (o ultimo vertice per il primo lato)
            double ay = puntiY.get(j);
            double bx = puntiX.get(i); // Vertice corrente
            double by = puntiY.get(i);

            // Calcola la distanza dal punto (px, py) al segmento (ax, ay) - (bx, by)
            double lunghezzaSegmentoQuadrata = distanzaQuadrata(ax, ay, bx, by);

            if (lunghezzaSegmentoQuadrata == 0.0) {
                if (distanzaQuadrata(px, py, ax, ay) <= tolleranzaQuadrata) {
                    return true;
                }
                continue;
            }

            // Proiezione del punto P sul segmento AB. t = dot((P-A), (B-A)) / |B-A|^2
            double t = ((px - ax) * (bx - ax) + (py - ay) * (by - ay)) / lunghezzaSegmentoQuadrata;

            double distanzaPuntoSegmentoQuadrata;
            if (t < 0.0) { // Proiezione fuori dal segmento, il punto più vicino è A
                distanzaPuntoSegmentoQuadrata = distanzaQuadrata(px, py, ax, ay);
            } else if (t > 1.0) { // Proiezione fuori dal segmento, il punto più vicino è B
                distanzaPuntoSegmentoQuadrata = distanzaQuadrata(px, py, bx, by);
            } else { // Proiezione cade sul segmento
                // Calcola il punto di proiezione (qx, qy)
                double qx = ax + t * (bx - ax);
                double qy = ay + t * (by - ay);
                distanzaPuntoSegmentoQuadrata = distanzaQuadrata(px, py, qx, qy);
            }

            if (distanzaPuntoSegmentoQuadrata <= tolleranzaQuadrata) {
                return true; // Il punto è abbastanza vicino a questo segmento
            }
        }
        return false;
    }

    /**
     * Determina se un punto specificato si trova all'interno del poligono.
     * Considera anche una piccola tolleranza per la selezione del bordo.
     *
     * @param x La coordinata X del punto da valutare (in coordinate globali).
     * @param y La coordinata Y del punto da valutare (in coordinate globali).
     * @return {@code true} se il punto si trova all'interno o sul bordo del poligono, altrimenti {@code false}.
     */
    @Override
    public boolean contiene(double x, double y) {
        double translatedX = x - super.getCoordinataX();
        double translatedY = y - super.getCoordinataY();

        double angleRad = Math.toRadians(-super.getAngoloInclinazione());
        double rotatedX = translatedX * Math.cos(angleRad) - translatedY * Math.sin(angleRad);
        double rotatedY = translatedX * Math.sin(angleRad) + translatedY * Math.cos(angleRad);

        double tolleranzaClick = 3.0;
        if (puntoVicinoAlContorno(rotatedX, rotatedY, tolleranzaClick)) {
            return true;
        }

        int n = puntiX.size();
        if (n < 3) return false;

        boolean dentro = false;
        for (int i = 0, j = n - 1; i < n; j = i++) {
            double xi = puntiX.get(i), yi = puntiY.get(i);
            double xj = puntiX.get(j), yj = puntiY.get(j);

            double yDiff = yj - yi;
            if (Math.abs(yDiff) < 1e-9) continue;

            boolean interseca = ((yi > rotatedY) != (yj > rotatedY)) &&
                    (rotatedX < (xj - xi) * (rotatedY - yi) / yDiff + xi);
            if (interseca) dentro = !dentro;
        }
        return dentro;
    }


    /**
     * Specchia il poligono lungo l'asse Y del suo sistema di coordinate intrinseco.
     * Questo viene fatto negando le coordinate X di tutti i punti.
     */
    @Override
    public void specchiaInVerticale() {
        // Specchia i punti lungo l'asse Y (negando le coordinate X)
        List<Double> nuoviPuntiX = new ArrayList<>();
        for (double px : puntiX) {
            nuoviPuntiX.add(-px); // Negare la coordinata X
        }
        this.puntiX = nuoviPuntiX;

        // Le coordinate Y rimangono invariate per lo specchiamento lungo l'asse Y

        calcolaBoundingBox();

        super.setAngoloInclinazione(-super.getAngoloInclinazione());
    }

    /**
     * Specchia il poligono lungo l'asse X del suo sistema di coordinate intrinseco.
     * Questo viene fatto negando le coordinate Y di tutti i punti.
     */
    @Override
    public void specchiaInOrizzontale() {
        // Specchia i punti lungo l'asse X (negando le coordinate Y)
        List<Double> nuoviPuntiY = new ArrayList<>();
        for (double py : puntiY) {
            nuoviPuntiY.add(-py); // Negare la coordinata Y
        }
        this.puntiY = nuoviPuntiY;

        // Le coordinate X rimangono invariate per lo specchiamento lungo l'asse X

        calcolaBoundingBox();

        super.setAngoloInclinazione(-super.getAngoloInclinazione());
    }


    /**
     * Calcola la bounding box del poligono (min/max X/Y) e il suo centro
     * basandosi sui punti intrinseci (non ruotati) del poligono.
     * Aggiorna le proprietà `intrinsicLarghezza` e `intrinsicAltezza`.
     */
    private void calcolaBoundingBox() {
        if (puntiX == null || puntiX.isEmpty()) {
            this.intrinsicLarghezza = 0;
            this.intrinsicAltezza = 0;
            this.intrinsicCenterX = 0;
            this.intrinsicCenterY = 0;
            super.setLarghezza(0);
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

        this.intrinsicLarghezza = maxX - minX;
        this.intrinsicAltezza = maxY - minY;

        // Calcola e salva il centro della bounding box intrinseca
        this.intrinsicCenterX = (minX + maxX) / 2.0;
        this.intrinsicCenterY = (minY + maxY) / 2.0;

        super.setLarghezza(this.intrinsicLarghezza);
    }

    /**
     * Crea e restituisce una copia profonda di questo oggetto Poligono.
     * @return Un nuovo oggetto Poligono che è una copia esatta di questo.
     */
    @Override
    public Poligono clone() {
        Poligono cloned = (Poligono) super.clone();

        // Copia profonda delle liste di punti (sono i punti intrinseci)
        cloned.puntiX = new ArrayList<>(this.puntiX);
        cloned.puntiY = new ArrayList<>(this.puntiY);

        cloned.coloreInterno = this.coloreInterno;
        cloned.intrinsicLarghezza = this.intrinsicLarghezza;
        cloned.intrinsicAltezza = this.intrinsicAltezza;

        return cloned;
    }

    /**
     * Scala i punti intrinseci del poligono rispetto al suo centro (0,0).
     * Questo metodo modifica le coordinate dei vertici, ridimensionando la forma.
     *
     * @param scalaX Il fattore di scala sull'asse X.
     * @param scalaY Il fattore di scala sull'asse Y.
     */
    public void scala(double scalaX, double scalaY) {
        if (puntiX.isEmpty()) return;

        List<Double> nuoviPuntiX = new ArrayList<>();
        List<Double> nuoviPuntiY = new ArrayList<>();

        for (int i = 0; i < puntiX.size(); i++) {
            double oldX = puntiX.get(i);
            double oldY = puntiY.get(i);

            // Scala il punto
            nuoviPuntiX.add(oldX * scalaX);
            nuoviPuntiY.add(oldY * scalaY);
        }
        this.puntiX = nuoviPuntiX;
        this.puntiY = nuoviPuntiY;
        calcolaBoundingBox(); // Ricalcola le dimensioni intrinseche dopo la scala
    }


    @Override
    public void setAngoloInclinazione(double nuovoAngolo) {
        super.setAngoloInclinazione(nuovoAngolo);
    }


    /**
     * Serializza l'oggetto nel complesso con il metodo della superclasse e poi salva
     * anche il colore di riempimento che non è serializzabile.
     * @param out è lo stream sul quale salvare le informazioni, sarà il File scelto dall'utente
     * @throws IOException se si verifica un errore di I/O durante la scrittura dell'oggetto
     */
    @Serial
    private void writeObject(ObjectOutputStream out) throws IOException {
        out.defaultWriteObject();
        out.writeUTF(ColorUtils.toHexString(getColore()));
        // Serializza il colore interno specifico della sottoclasse
        out.writeUTF(ColorUtils.toHexString(coloreInterno));
    }

    /**
     * Deserializza l'oggetto nel complesso con il metodo della superclasse e poi ricava
     * anche il colore di riempimento che non è serializzabile.
     * @param in è lo stream dal quale caricare le informazioni, sarà il File scelto dall'utente
     * @throws IOException se si verifica un errore di I/O durante la scrittura dell'oggetto
     * @throws ClassNotFoundException se si verifica un errore nel caricare una classe
     */
    @Serial
    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        in.defaultReadObject();
        String colore = in.readUTF();
        this.setColore(Color.web(colore));
        String coloreInterno = in.readUTF();
        this.setColoreInterno(ColorUtils.fromHexString(coloreInterno));
    }

    /**
     * Ridimensiona il poligono in modo proporzionale applicando un fattore di scala uniforme
     * a tutti i suoi punti intrinseci.
     *
     * @param proporzione La percentuale di ridimensionamento (es. 100 per nessuna modifica, 50 per metà dimensione).
     */
    @Override
    public void proportionalResize(double proporzione) {
        double fattoreScala = proporzione / 100.0;
        // Applica il fattore di scala sia sull'asse X che sull'asse Y
        scala(fattoreScala, fattoreScala);

    }
}
