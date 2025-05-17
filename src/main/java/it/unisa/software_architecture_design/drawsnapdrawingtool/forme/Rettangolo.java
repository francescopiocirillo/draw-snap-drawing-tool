package it.unisa.software_architecture_design.drawsnapdrawingtool.forme;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class Rettangolo extends Forma  {
    /*
     * Attributi
     */
    private double altezza;
    private Color coloreInterno;
    private double verticeAX;
    private double verticeAY;
    private double verticeBX;
    private double verticeBY;
    private double verticeCX;
    private double verticeCY;
    private double verticeDX;
    private double verticeDY;

    /**
     * Costruttore, Getter e Setter
     */
    public Rettangolo(double coordinataX, double coordinataY, double larghezza, double angoloInclinazione, Color colore, double altezza, Color coloreInterno) {
        super(coordinataX, coordinataY, larghezza, angoloInclinazione, colore);
        this.altezza = altezza;
        this.coloreInterno = coloreInterno;
        updateVertici();
    }

    private void updateVertici() {
        double centroX = getCoordinataX();
        double centroY = getCoordinataY();
        double mezzaAltezza = getAltezza() / 2;
        double mezzaLarghezza = getLarghezza() / 2;

        double angoloRad = Math.toRadians(getAngoloInclinazione());
        double cosAngolo = Math.cos(angoloRad);
        double sinAngolo = Math.sin(angoloRad);

        this.verticeAX = centroX - mezzaAltezza * cosAngolo + mezzaLarghezza * sinAngolo;
        this.verticeAY = centroY - mezzaAltezza * sinAngolo - mezzaLarghezza * cosAngolo;

        this.verticeBX = centroX + mezzaAltezza * cosAngolo + mezzaLarghezza * sinAngolo;
        this.verticeBY = centroY + mezzaAltezza * sinAngolo - mezzaLarghezza * cosAngolo;

        this.verticeCX = centroX + mezzaAltezza * cosAngolo - mezzaLarghezza * sinAngolo;
        this.verticeCY = centroY + mezzaAltezza * sinAngolo + mezzaLarghezza * cosAngolo;

        this.verticeDX = centroX - mezzaAltezza * cosAngolo - mezzaLarghezza * sinAngolo;
        this.verticeDY = centroY - mezzaAltezza * sinAngolo + mezzaLarghezza * cosAngolo;
    }

    public double getAltezza() {
        return altezza;
    }

    public void setAltezza(double altezza) {
        this.altezza = altezza;
    }

    public Color getColoreInterno() {
        return coloreInterno;
    }

    public void setColoreInterno(Color coloreInterno) {
        this.coloreInterno = coloreInterno;
    }

    public double getVerticeAY() {
        return verticeAY;
    }

    public double getVerticeDY() {
        return verticeDY;
    }

    public double getVerticeDX() {
        return verticeDX;
    }

    public double getVerticeCY() {
        return verticeCY;
    }

    public double getVerticeCX() {
        return verticeCX;
    }

    public double getVerticeBY() {
        return verticeBY;
    }

    public double getVerticeBX() {
        return verticeBX;
    }

    public double getVerticeAX() {
        return verticeAX;
    }

    @Override
    public void setCoordinataY(double coordinataY) {
        super.setCoordinataY(coordinataY);
        updateVertici();
    }

    @Override
    public void setCoordinataX(double coordinataX) {
        super.setCoordinataX(coordinataX);
        updateVertici();
    }

    /*
      Logica della classe
     */

    /**
     * Disegna un rettangolo sul {@link GraphicsContext} specificato.
     * Questo metodo disegna un rettangolo utilizzando le coordinate dei vertici fornite dagli attributi
     * dell'oggetto usando il colore della forma e il colore interno del rettangolo.
     *
     * @param gc il {@code GraphicsContext} su cui disegnare il rettangolo.
     *           Deve essere già inizializzato e associato a un {@code Canvas} valido.
     */
    @Override
    public void disegna(GraphicsContext gc) {
        // Salva lo stato iniziale del foglio di disegno
        gc.save();

        // Vertici del rettangolo
        verticeAX = getVerticeAX();
        verticeAY = getVerticeAY();
        verticeBX = getVerticeBX();
        verticeBY = getVerticeBY();
        verticeCX = getVerticeCX();
        verticeCY = getVerticeCY();
        verticeDX = getVerticeDX();
        verticeDY = getVerticeDY();

        // Imposta il colore del contorno e dell'interno
        gc.setFill(getColoreInterno());
        gc.setStroke(getColore());
        gc.setLineWidth(2);

        // fill disegna l'area interna del rettangolo
        gc.fillPolygon(
                new double[]{verticeAX, verticeBX, verticeCX, verticeDX}, // Coordinate X dei vertici
                new double[]{verticeAY, verticeBY, verticeCY, verticeDY}, // Coordinate Y dei vertici
                4 // Numero di punti
        );

        // stroke disegna il contorno
        gc.strokePolygon(
                new double[]{verticeAX, verticeBX, verticeCX, verticeDX},
                new double[]{verticeAY, verticeBY, verticeCY, verticeDY},
                4
        );

        // Ripristina lo stato iniziale del foglio di disegno
        gc.restore();
    }

    /**
     * Determina se il rettangolo contiene un punto specifico nello spazio.
     *
     * @param puntoDaValutareX La coordinata X del punto da verificare.
     * @param puntoDaValutareY La coordinata Y del punto da verificare.
     * @return {@code true} se il punto specificato (puntoDaValutareX, puntoDaValutareY) si trova all'interno del rettangolo,
     * altrimenti {@code false}.
     */
    @Override
    public boolean contiene(double puntoDaValutareX, double puntoDaValutareY) {
        // Vertici del rettangolo
        verticeAX = getVerticeAX();
        verticeAY = getVerticeAY();
        verticeBX = getVerticeBX();
        verticeBY = getVerticeBY();
        verticeCX = getVerticeCX();
        verticeCY = getVerticeCY();
        verticeDX = getVerticeDX();
        verticeDY = getVerticeDY();

        // Se il punto è "alla sinistra" di tutti i lati consecutivi allora è contenuto del rettangolo.
        // Nota: se il punto è "alla sinistra" di CD allora è "alla destra" di DC secondo la regola del prodotto vettoriale
        return isToTheLeft(puntoDaValutareX, puntoDaValutareY, verticeAX, verticeAY, verticeBX, verticeBY) &&
                isToTheLeft(puntoDaValutareX, puntoDaValutareY, verticeBX, verticeBY, verticeCX, verticeCY) &&
                isToTheLeft(puntoDaValutareX, puntoDaValutareY, verticeCX, verticeCY, verticeDX, verticeDY) &&
                isToTheLeft(puntoDaValutareX, puntoDaValutareY, verticeDX, verticeDY, verticeAX, verticeAY);
    }

    /**
     * Determina se il punto (puntoDaValutareX, puntoDaValutareY) è "alla sinistra" del segmento tra (inizioVettoreCoordinataX, y1) e (x2, y2) tramite il prodotto vettoriale.
     *
     * @param puntoDaValutareX
     * @param puntoDaValutareY
     * @param inizioVettoreCoordinataX
     * @param inizioVettoreCoordinataY
     * @param fineVettoreCoordinataX
     * @param fineVettoreCoordinataY
     * @return {@code true} se il punto specificato (puntoDaValutareX, puntoDaValutareY) si trova "alla sinistra" del segmento,
     *          altrimenti {@code false}.
     */
    private boolean isToTheLeft(double puntoDaValutareX, double puntoDaValutareY, double inizioVettoreCoordinataX, double inizioVettoreCoordinataY, double fineVettoreCoordinataX, double fineVettoreCoordinataY) {
        double crossProduct = (fineVettoreCoordinataX - inizioVettoreCoordinataX) * (puntoDaValutareY - inizioVettoreCoordinataY) - (fineVettoreCoordinataY - inizioVettoreCoordinataY) * (puntoDaValutareX - inizioVettoreCoordinataX);
        return crossProduct >= 0 ;
    }
}
