package it.unisa.software_architecture_design.drawsnapdrawingtool.forme;

import it.unisa.software_architecture_design.drawsnapdrawingtool.utils.ColorUtils;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serial;

public class Rettangolo extends Forma  {
    /*
     * Attributi
     */
    private double altezza;
    private transient Color coloreInterno;
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

    public double getAltezza() {
        return altezza;
    }

    public void setAltezza(double altezza) {
        this.altezza = altezza;
        updateVertici();
    }

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
    public void proportionalResize(double proporzione){
        setLarghezza(getLarghezza()*proporzione/100);
        setAltezza(getAltezza()*proporzione/100);
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
     * Determina se il punto (puntoDaValutareX, puntoDaValutareY) è "alla sinistra" del segmento tra
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
     * Metodo per il controllare se due forme sono uguali
     * @param forma -> forma con cui fare il confronto
     * @return {@code true} se gli attributi sono uguali, altrimenti {@code false}
     */
    @Override
    public boolean confrontaAttributi(Forma forma){
        Rettangolo rettangolo = (Rettangolo) forma;
        return super.confrontaAttributi(rettangolo) &&
                this.verticeAX == rettangolo.getVerticeAX() &&
                this.verticeAY == rettangolo.getVerticeAY() &&
                this.verticeBX == rettangolo.getVerticeBX() &&
                this.verticeCX == rettangolo.getVerticeCX() &&
                this.verticeCY == rettangolo.getVerticeCY() &&
                this.verticeDX == rettangolo.getVerticeDX() &&
                this.verticeDY == rettangolo.getVerticeDY();
    }

    /**
     * Ridistribuisce i valori della figura per specchiarla lungo l'asse verticale che passa per il
     * cetro della figura stessa
     */
    @Override
    public void specchia() {
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
}
