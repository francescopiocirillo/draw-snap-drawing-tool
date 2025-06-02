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

/**
 * La classe {@link Poligono} rappresenta la {@link Forma} Poligono e presenta
 * tutte le caratteristiche ereditate da {@link Forma2D}.
 */
public class Poligono extends Forma2D {
    /*
     * Attributi
     */
    private List<Double> puntiX; // Punti intrinseci, relativi al centro del poligono
    private List<Double> puntiY; // Punti intrinseci, relativi al centro del poligono
    private double intrinsicAltezza; // Altezza della bounding box del poligono.
    private double intrinsicLarghezza; // Larghezza della bounding box del poligono.
    private double intrinsicCenterX; // Centro X della bounding box
    private double intrinsicCenterY; // Centro Y della bounding box

    /*
     * Costruttore, getter e setter
     */
    public Poligono(double initialRefX, double initialRefY, double larghezza, double altezza, double angoloInclinazione, Color colore, List<Double> rawPuntiX, List<Double> rawPuntiY, Color coloreInterno) {
        // Imposta la posizione globale del poligono direttamente al punto di riferimento iniziale.
        super(initialRefX, initialRefY, larghezza, angoloInclinazione, colore, altezza, coloreInterno);

        this.puntiX = new ArrayList<>();
        this.puntiY = new ArrayList<>();

        // Converte i punti globali in punti relativi
        for (int i = 0; i < rawPuntiX.size(); i++) {
            this.puntiX.add(rawPuntiX.get(i) - initialRefX);
            this.puntiY.add(rawPuntiY.get(i) - initialRefY);
        }

        // Calcola la bounding box dei punti relativi.
        calcolaBoundingBox();
    }

    @Override
    public double getLarghezza() {
        return super.getLarghezza();
    }

    public double getAltezza() {
        return super.getAltezza();
    }

    @Override
    public void setCoordinataX(double coordinataX) {
        super.setCoordinataX(coordinataX);
    }

    @Override
    public void setCoordinataY(double coordinataY) {
        super.setCoordinataY(coordinataY);
    }

    @Override
    public Color getColoreInterno() {
        return super.getColoreInterno();
    }

    @Override
    public void setColoreInterno(Color coloreInterno) {
        super.setColoreInterno(coloreInterno);
    }

    public double getIntrinsicCenterX() {
        return intrinsicCenterX;
    }

    public double getIntrinsicCenterY() {
        return intrinsicCenterY;
    }

    @Override
    public double getAngoloInclinazione() { return super.getAngoloInclinazione();}

    @Override
    public void setAngoloInclinazione(double nuovoAngolo) {
        super.setAngoloInclinazione(nuovoAngolo);
    }

    /*
     Logica della classe
    */

    /**
     * Gestisce il disegno di un {@link Poligono} sul {@link GraphicsContext} specificato.
     * Applica le trasformazioni (traslazione e rotazione) e poi disegna il {@link Poligono}
     * utilizzando i suoi punti relativi.
     * @param gc è il {@link GraphicsContext} su cui disegnare il {@link Poligono}.
     */
    @Override
    public void disegna(GraphicsContext gc) {
        gc.save();

        gc.translate(super.getCoordinataX(), super.getCoordinataY());

        gc.rotate(super.getAngoloInclinazione());

        //converte la List<Double> in un array double
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
     * Verifica se un punto specificato si trova all'interno del {@link Poligono}.
     * Considera anche una piccola tolleranza per la selezione del bordo.
     *
     * @param x La coordinata X del punto da valutare.
     * @param y La coordinata Y del punto da valutare.
     * @return {@code true} se il punto si trova all'interno o sul bordo del {@link Poligono},
     * altrimenti {@code false}.
     */
    @Override
    public boolean contiene(double x, double y) {
        // I punti del poligono (puntiX, puntiY) sono relativi al suo centro e non ruotati.
        // Per verificare il contenimento, bisogna portare il punto di input (x, y) nello stesso sistema.
        // Prima, si trasla il punto in modo che sia relativo al centro globale del poligono.
        double translatedX = x - super.getCoordinataX();
        double translatedY = y - super.getCoordinataY();
        // Poi, ruota il punto traslato all'indietro (con l'angolo inverso del poligono).
        // Questo annulla la rotazione del poligono rispetto al punto di input.
        double angleRad = Math.toRadians(-super.getAngoloInclinazione());
        double rotatedX = translatedX * Math.cos(angleRad) - translatedY * Math.sin(angleRad);
        double rotatedY = translatedX * Math.sin(angleRad) + translatedY * Math.cos(angleRad);

        double tolleranzaClick = 3.0;
        if (puntoVicinoAlContorno(rotatedX, rotatedY, tolleranzaClick)) { //se il punto cliccato è vicino al bordo
            return true;
        }

        //controllo se il punto è all'interno del poligono
        int n = puntiX.size();
        if (n < 3) return false; // Non c'è poligono con meno di 3 punti

        boolean dentro = false;
        //algortimo Ray Casting:
        // Si traccia un raggio orizzontale dal punto (rotatedX, rotatedY) verso destra.
        // Si conta quante volte questo raggio interseca i lati del poligono.
        // Se il numero di intersezioni è dispari, il punto è dentro; se è pari, è fuori.

        //itera sui segmenti del poligono
        for (int i = 0, j = n - 1; i < n; j = i++) {
            double xi = puntiX.get(i), yi = puntiY.get(i);
            double xj = puntiX.get(j), yj = puntiY.get(j);

            double yDiff = yj - yi;
            if (Math.abs(yDiff) < 1e-9) continue; //se il segmento è orizzontale viene saltato

            // L'intersezione è valida solo se:
            // 1. Il raggio attraversa verticalmente il segmento: un vertice è sopra il raggio e l'altro è sotto (o viceversa).
            // 2. L'intersezione avviene a destra del punto di partenza del raggio.
            //    La seconda parte calcola la X di intersezione e verifica se è maggiore della rotatedX del punto.
            boolean interseca = ((yi > rotatedY) != (yj > rotatedY)) &&
                    (rotatedX < (xj - xi) * (rotatedY - yi) / yDiff + xi);
            // Se il raggio interseca il segmento, si inverte lo stato di 'dentro'.
            if (interseca) dentro = !dentro;
        }
        return dentro;
    }

    /**
     * Gestisce la ridistribuzione dei valori della {@link Forma} per specchiarla
     * lungo l'asse verticale che passa per il centro della {@link Forma} stessa
     */
    @Override
    public void specchiaInVerticale() {
        // Specchia i punti lungo l'asse Y (negando le coordinate X)
        List<Double> nuoviPuntiX = new ArrayList<>();
        for (double px : puntiX) {
            nuoviPuntiX.add(-px); // Nega la coordinata X
        }
        this.puntiX = nuoviPuntiX;

        // Le coordinate Y rimangono invariate per lo specchiamento lungo l'asse Y

        calcolaBoundingBox();
        // Inverte l'angolo di inclinazione.
        super.setAngoloInclinazione(-super.getAngoloInclinazione());
    }

    /**
     * Gestisce la ridistribuzione dei valori della {@link Forma} per specchiarla
     * lungo l'asse orizzontale che passa per il centro della {@link Forma} stessa
     */
    @Override
    public void specchiaInOrizzontale() {
        // Specchia i punti lungo l'asse X (negando le coordinate Y)
        List<Double> nuoviPuntiY = new ArrayList<>();
        for (double py : puntiY) {
            nuoviPuntiY.add(-py); // Nega la coordinata Y
        }
        this.puntiY = nuoviPuntiY;

        // Le coordinate X rimangono invariate per lo specchiamento lungo l'asse X

        calcolaBoundingBox();

        super.setAngoloInclinazione(-super.getAngoloInclinazione());
    }

    /**
     * Gestisce il ridimensionamento del {@link Poligono} in modo proporzionale applicando un
     * fattore di scala uniforme a tutti i suoi punti intrinseci.
     *
     * @param proporzione La percentuale di ridimensionamento (es. 100 per nessuna modifica, 50 per metà dimensione).
     */
    @Override
    public void proportionalResize(double proporzione) {
        double fattoreScala = proporzione / 100.0;
        // Applica il fattore di scala sia sull'asse X che sull'asse Y
        scala(fattoreScala, fattoreScala);

    }

    /**
     * Gestisce la scala dei punti intrinseci del {@link Poligono} rispetto al suo centro locale (0,0).
     * Questo metodo modifica le coordinate dei vertici, ridimensionando la {@link Forma}.
     *
     * @param scalaX Il fattore di scala sull'asse X.
     * @param scalaY Il fattore di scala sull'asse Y.
     */
    public void scala(double scalaX, double scalaY) {
        if (puntiX.isEmpty()) return;

        List<Double> nuoviPuntiX = new ArrayList<>();
        List<Double> nuoviPuntiY = new ArrayList<>();

        // Itera su ogni punto e applica i fattori di scala.
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

    /*
     * Logica di serializzazione e deserializzazione
     */

    /**
     * Gestisce la serializzazione dell'oggetto nel complesso con il metodo della superclasse e
     * poi salva anche il {@link Color} di riempimento che non è {@link java.io.Serializable}.
     * @param out è l' {@link ObjectOutputStream} sul quale salvare le informazioni, sarà il
     *            {@link java.io.File} scelto dall'utente
     * @throws IOException se si verifica un errore di I/O durante la scrittura dell'oggetto
     */
    @Serial
    private void writeObject(ObjectOutputStream out) throws IOException {
        out.defaultWriteObject();
        out.writeUTF(ColorUtils.toHexString(getColore()));
        // Serializza il colore interno specifico della sottoclasse
        out.writeUTF(ColorUtils.toHexString(getColoreInterno()));
    }

    /**
     * Gestisce la deserializzazione dell'oggetto nel complesso con il metodo della superclasse e
     * poi ricava anche il {@link Color} di riempimento che non è {@link java.io.Serializable}
     * @param in è l' {@link ObjectInputStream} dal quale caricare le informazioni, sarà il
     *           {@link java.io.File} scelto dall'utente
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

    /*
     * Metodi Ausiliari
     */

    /**
     * Metodo helper che calcola la bounding box del {@link Poligono} (min/max X/Y) e il suo centro
     * basandosi sui punti intrinseci (non ruotati) del {@link Poligono}.
     * Aggiorna le proprietà `intrinsicLarghezza`, `intrinsicAltezza`, `intrinsicCenterX`, `intrinsicCenterY`
     * e le dimensioni della superclasse
     */
    private void calcolaBoundingBox() {
        if (puntiX == null || puntiX.isEmpty()) {
            this.intrinsicLarghezza = 0;
            this.intrinsicAltezza = 0;
            this.intrinsicCenterX = 0;
            this.intrinsicCenterY = 0;
            super.setLarghezza(0);
            super.setAltezza(0);
            return;
        }
        // Inizializza min/max con il primo punto.
        double minX = puntiX.get(0);
        double maxX = puntiX.get(0);
        double minY = puntiY.get(0);
        double maxY = puntiY.get(0);

        // Itera sui restanti punti per trovare i valori min/max X e Y.
        for (int i = 1; i < puntiX.size(); i++) {
            minX = Math.min(minX, puntiX.get(i));
            maxX = Math.max(maxX, puntiX.get(i));
            minY = Math.min(minY, puntiY.get(i));
            maxY = Math.max(maxY, puntiY.get(i));
        }
        // Calcola la larghezza e l'altezza intrinseche basate sui valori min/max trovati.
        this.intrinsicLarghezza = maxX - minX;
        this.intrinsicAltezza = maxY - minY;

        // Calcola e salva il centro della bounding box intrinseca
        this.intrinsicCenterX = (minX + maxX) / 2.0;
        this.intrinsicCenterY = (minY + maxY) / 2.0;

        // Aggiorna anche le proprietà di larghezza e altezza nella superclasse Forma2D.
        super.setLarghezza(this.intrinsicLarghezza);
        super.setAltezza(this.intrinsicAltezza);
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
     * Verifica se un punto (px, py) è sufficientemente vicino al contorno del {@link Poligono}.
     *
     * @param px La coordinata X del punto da verificare.
     * @param py La coordinata Y del punto da verificare.
     * @param tolleranza La distanza massima per considerare il punto "vicino" al contorno.
     * @return {@code true} se il punto è vicino al contorno, altrimenti {@code false}.
     */
    private boolean puntoVicinoAlContorno(double px, double py, double tolleranza) {
        int n = puntiX.size();
        if (n < 3) return false; // Non c'è poligono con meno di 3 punti

        double tolleranzaQuadrata = tolleranza * tolleranza;
        //iterazione sui segmenti del poligono
        for (int i = 0, j = n - 1; i < n; j = i++) {
            double ax = puntiX.get(j); //coordinate punto di inizio del segmento corrente
            double ay = puntiY.get(j);
            double bx = puntiX.get(i); //coordinate punto di fine del segmento corrente
            double by = puntiY.get(i);

            // Calcola la distanza dal punto (px, py) al segmento (ax, ay) - (bx, by)
            double lunghezzaSegmentoQuadrata = distanzaQuadrata(ax, ay, bx, by);

            if (lunghezzaSegmentoQuadrata == 0.0) { //il segmento è un punto
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
}
