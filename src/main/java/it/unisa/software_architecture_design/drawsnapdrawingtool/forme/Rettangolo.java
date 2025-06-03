package it.unisa.software_architecture_design.drawsnapdrawingtool.forme;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

/**
 * La classe {@link Rettangolo} rappresenta la {@link Forma} Rettangolo e presenta
 * tutte le caratteristiche ereditate da {@link Forma2D}.
 */
public class Rettangolo extends Forma2D{
    /*
     * Attributi
     */
    private double verticeAX;
    private double verticeAY;
    private double verticeBX;
    private double verticeBY;
    private double verticeCX;
    private double verticeCY;
    private double verticeDX;
    private double verticeDY;

    /*
     * Costruttore, Getter e Setter
     */
    public Rettangolo(double coordinataX, double coordinataY, double larghezza, double angoloInclinazione, Color colore, double altezza, Color coloreInterno) {
        super(coordinataX, coordinataY, larghezza, angoloInclinazione, colore, altezza, coloreInterno);
        updateVertici();
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
    public void setAltezza(double altezza) {
        super.setAltezza(altezza);
        updateVertici();
    }

    @Override
    public void setLarghezza(double larghezza) {
        super.setLarghezza(larghezza);
        updateVertici();
    }

    @Override
    public void setAngoloInclinazione( double angoloInclinazione ) {
        super.setAngoloInclinazione( angoloInclinazione );
        updateVertici();
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
     * Gestisce l'ggiornamento dei vertici secondo le informazioni:
     * - coordinataX del centro;
     * - coordinataY del centro;
     * - larghezza;
     * - altezza;
     * - angolo di inclinazione.
     * Il metodo va invocato ogni volta che avviene una modifica agli attributi sopra elencati.
     */
    private void updateVertici() {
        double centroX = getCoordinataX();
        double centroY = getCoordinataY();
        double mezzaLarghezza = getLarghezza() / 2;
        double mezzaAltezza = getAltezza() / 2;

        double angoloRad = Math.toRadians(getAngoloInclinazione());
        double cosAngolo = Math.cos(angoloRad);
        double sinAngolo = Math.sin(angoloRad);

        this.verticeAX = centroX - mezzaLarghezza * cosAngolo + mezzaAltezza * sinAngolo;
        this.verticeAY = centroY - mezzaLarghezza * sinAngolo - mezzaAltezza * cosAngolo;

        this.verticeBX = centroX + mezzaLarghezza * cosAngolo + mezzaAltezza * sinAngolo;
        this.verticeBY = centroY + mezzaLarghezza * sinAngolo - mezzaAltezza * cosAngolo;

        this.verticeCX = centroX + mezzaLarghezza * cosAngolo - mezzaAltezza * sinAngolo;
        this.verticeCY = centroY + mezzaLarghezza * sinAngolo + mezzaAltezza * cosAngolo;

        this.verticeDX = centroX - mezzaLarghezza * cosAngolo - mezzaAltezza * sinAngolo;
        this.verticeDY = centroY - mezzaLarghezza * sinAngolo + mezzaAltezza * cosAngolo;
    }

    /**
     * Gestisce il disegno di un {@link Rettangolo} sul {@link GraphicsContext} specificato.
     * Questo metodo disegna un {@link Rettangolo} utilizzando le coordinate dei vertici
     * fornite dagli attributi dell'oggetto usando il {@link Color} del bordo della {@link Forma} e
     * il {link Color} interno del rettangolo.
     * @param gc il {@link GraphicsContext} su cui disegnare il {@link Rettangolo}.
     *           Deve essere già inizializzato e associato a un {@link javafx.scene.canvas.Canvas} valido.
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
     * Verifica se il {@link Rettangolo} contiene un punto specifico nello spazio.
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
     * Gestisce la ridistribuzione dei valori della {@link Forma} per specchiarla
     * lungo l'asse verticale che passa per il centro della {@link Forma} stessa
     */
    @Override
    public void specchiaInVerticale() {
        double centroX = getCoordinataX();

        // Inverti le coordinate X dei vertici rispetto al centro
        this.verticeAX = 2 * centroX - this.verticeAX;
        this.verticeBX = 2 * centroX - this.verticeBX;
        this.verticeCX = 2 * centroX - this.verticeCX;
        this.verticeDX = 2 * centroX - this.verticeDX;

        // Aggiorna l'angolo di inclinazione per mantenere la figura correttamente orientata
        setAngoloInclinazione((360 - getAngoloInclinazione()) % 360);

        // Aggiorna i vertici per riflettere i cambiamenti
        updateVertici();
    }

    /**
     * Gestisce la ridistribuzione dei valori della {@link Forma} per specchiarla
     * lungo l'asse orizzontale che passa per il centro della {@link Forma} stessa
     */
    @Override
    public void specchiaInOrizzontale() {
        double centroY = getCoordinataY();

        // Inverti le coordinate Y dei vertici rispetto al centro
        this.verticeAY = 2 * centroY - this.verticeAY;
        this.verticeBY = 2 * centroY - this.verticeBY;
        this.verticeCY = 2 * centroY - this.verticeCY;
        this.verticeDY = 2 * centroY - this.verticeDY;

        // Aggiorna l'angolo di inclinazione per mantenere la figura correttamente orientata
        setAngoloInclinazione((360 - getAngoloInclinazione()) % 360);

        // Aggiorna i vertici per riflettere i cambiamenti
        updateVertici();
    }

    /**
     * Verifica se il {@link Rettangolo} corrente è uguale ad un altra {@link Forma}
     * @param forma è la {@link Forma} con cui fare il confronto
     * @return {@code true} se gli attributi sono uguali, altrimenti {@code false}
     */
    @Override
    public boolean confrontaAttributi(Forma forma){
        if(!(forma instanceof Rettangolo rettangolo)) return false;
        return super.confrontaAttributi(rettangolo) &&
                this.verticeAX == rettangolo.getVerticeAX() &&
                this.verticeAY == rettangolo.getVerticeAY() &&
                this.verticeBX == rettangolo.getVerticeBX() &&
                this.verticeCX == rettangolo.getVerticeCX() &&
                this.verticeCY == rettangolo.getVerticeCY() &&
                this.verticeDX == rettangolo.getVerticeDX() &&
                this.verticeDY == rettangolo.getVerticeDY();
    }

    /*
     * Metodi Ausiliari
     */

    /**
     * Verifica se il punto (puntoDaValutareX, puntoDaValutareY) è "alla sinistra" del segmento tra
     * (inizioVettoreCoordinataX, y1) e (x2, y2) tramite il prodotto vettoriale.
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
