package it.unisa.software_architecture_design.drawsnapdrawingtool.forme;


import it.unisa.software_architecture_design.drawsnapdrawingtool.utils.ColorUtils;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serial;

/**
 * La classe {@link Testo} rappresenta la {@link Forma} Testo e presenta tutte le caratteristiche
 * ereditate da {@link Forma2D} in più alla {@link String} {@code testo} che rappresenterà.
 */
public class Testo extends Forma2D {

    /*
     * Attributi
     */
    private transient String testo;
    private final String FONT_NAME = "Arial";
    private double currentFontSize = 12.0;
    private boolean specchiataVerticale = false;
    private boolean specchiataOrizzontale = false;
    private double verticeAX;
    private double verticeAY;
    private double verticeBX;
    private double verticeBY;
    private double verticeCX;
    private double verticeCY;
    private double verticeDX;
    private double verticeDY;
    private double scaleX = 1.0;
    private double scaleY = 1.0;


    /**
     * Costruttore, Getter e Setter
     */
    public Testo(double coordinataX, double coordinataY, double larghezza, double angoloInclinazione, Color colore, double altezza, Color coloreInterno, String testo) {
        super(coordinataX, coordinataY, larghezza, angoloInclinazione, colore, altezza, coloreInterno);
        this.testo = testo;
        calculateFontSize();
        updateVertici();
    }

    @Override
    public double getAltezza() {
        return super.getAltezza();
    }

    @Override
    public void setAltezza(double altezza) {
        super.setAltezza(altezza);
        calculateFontSize();
        updateVertici();
    }

    @Override
    public double getLarghezza() {
        return super.getLarghezza();
    }

    @Override
    public void setLarghezza(double larghezza) {
        super.setLarghezza(larghezza);
        calculateFontSize();
        updateVertici();
    }

    @Override
    public double getAngoloInclinazione() {
        return super.getAngoloInclinazione();
    }

    @Override
    public void setAngoloInclinazione(double angoloInclinazione) {
        super.setAngoloInclinazione(angoloInclinazione);
        updateVertici();
    }

    @Override
    public Color getColoreInterno() {
        return super.getColoreInterno();
    }

    @Override
    public void setColoreInterno(Color coloreInterno) {
        super.setColoreInterno(coloreInterno);
    }

    public String getTesto() {
        return testo;
    }

    public void setTesto(String testo) {
        this.testo = testo;
    }

    public double getCurrentFontSize(){
        return currentFontSize;
    }

    public void setCurrentFontSize(double currentFontSize) {
        this.currentFontSize = currentFontSize;
    }

    public double getVerticeAY() {
        return verticeAY;
    }

    public double getVerticeAX() {
        return verticeAX;
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
     * Logica della Classe
     */

    /**
     * Gestisce l'aggiornamento delle coordinate dei vertici del bounding box del {@link Testo}
     * Questo metodo calcola i 4 vertici del rettangolo delimitatore del {@link Testo}
     * tenendo conto della sua posizione (centro), larghezza, altezza e angolo di inclinazione.
     * È simile a {@code updateVertici()} del {@link Rettangolo}.
     */
    private void updateVertici() {

        double centroX = getCoordinataX();
        double centroY = getCoordinataY();
        double mezzaLarghezza = getLarghezza() / 2;
        double mezzaAltezza = getAltezza() / 2;

        double angoloRadianti = Math.toRadians(getAngoloInclinazione());
        double cosAngolo = Math.cos(angoloRadianti);
        double sinAngolo = Math.sin(angoloRadianti);

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
     * Gestisce il calcolo dei fattori di scala ({@code scaleX}, {@code scaleY}) necessari per
     * stirare il {@link Testo} in modo che riempia la larghezza e l'altezza della bounding box.
     * Imposta anche un {@code currentFontSize} di base per la misurazione del {@code testo}.
     */
    private void calculateFontSize() {
        if (getLarghezza() <= 0 || getAltezza() <= 0 || testo == null || testo.isEmpty()) {
            this.scaleX = 1.0;
            this.scaleY = 1.0;
            this.currentFontSize = 12.0;
            return;
        }

        // Usa una dimensione del font di base per misurare le dimensioni naturali del testo.
        double baseMeasurementFontSize = 100.0;
        Text tempText = new Text(testo);
        tempText.setFont(Font.font(FONT_NAME, baseMeasurementFontSize));

        double naturalTextWidthAtBaseSize = tempText.getLayoutBounds().getWidth();
        double naturalTextHeightAtBaseSize = tempText.getLayoutBounds().getHeight();

        // Calcola i fattori di scala necessari per stirare il testo
        // dalle sue dimensioni naturali alle dimensioni target della bounding box.
        this.scaleX = naturalTextWidthAtBaseSize > 0 ? getLarghezza() / naturalTextWidthAtBaseSize : 1.0;
        this.scaleY = naturalTextHeightAtBaseSize > 0 ? getAltezza() / naturalTextHeightAtBaseSize : 1.0;

        this.currentFontSize = baseMeasurementFontSize;
    }

    /**
     * Gestisce il disegno della {@link Forma} {@link Testo} sul {@link GraphicsContext}
     * del {@link javafx.scene.canvas.Canvas}
     * @param gc il {@code GraphicsContext} su cui disegnare la Forma.
     *           Deve essere già inizializzato e associato a un {@link javafx.scene.canvas.Canvas} valido.
     */
    @Override
    public void disegna(GraphicsContext gc) {
        gc.save(); // Salva lo stato iniziale del contesto grafico

        double centroX = getCoordinataX();
        double centroY = getCoordinataY();

        // Applica le trasformazioni della forma (traslazione e rotazione)
        gc.translate(centroX, centroY);
        gc.rotate(getAngoloInclinazione());
        gc.scale(this.scaleX, this.scaleY);

        if(this.specchiataOrizzontale){
            gc.scale(1, -1);
        }


        gc.setFont(Font.font(FONT_NAME, currentFontSize));
        gc.setFill(getColoreInterno());
        gc.setStroke(getColore());


        if (testo == null || testo.isEmpty()) {
            gc.restore();
            return;
        }

        Text tempMeasureText = new Text(testo);
        tempMeasureText.setFont(Font.font(FONT_NAME, currentFontSize));
        double totalTextWidth = tempMeasureText.getLayoutBounds().getWidth();
        double textHeight = tempMeasureText.getLayoutBounds().getHeight();
        double baselineOffset = -tempMeasureText.getLayoutBounds().getMinY(); // Offset per allineamento baseline


        double currentX = -totalTextWidth / 2;

        double charOffsetY = -textHeight / 2 + baselineOffset;


        for (int i = 0; i < testo.length(); i++) {
            String currentChar = String.valueOf(testo.charAt(i));

            Text charMeasure = new Text(currentChar);
            charMeasure.setFont(Font.font(FONT_NAME, currentFontSize));
            double charWidth = charMeasure.getLayoutBounds().getWidth();

            gc.save();

            gc.translate(currentX + charWidth / 2, charOffsetY);

            if(specchiataVerticale) {
                gc.scale(-1, 1);
            }
            gc.fillText(currentChar, -charWidth / 2, 0);
            gc.strokeText(currentChar, -charWidth / 2, 0);

            gc.restore();

            currentX += charWidth;
        }

        gc.restore();
    }

    /**
     * Verifica se il {@link Testo} contiene un punto specifico nello spazio.
     * @param puntoDaValutareX La coordinata X del punto da verificare.
     * @param puntoDaValutareY La coordinata Y del punto da verificare.
     * @return {@code true} se il punto specificato (puntoDaValutareX, puntoDaValutareY) si trova
     * all'interno del rettangolo di contenimento, altrimenti {@code false}.
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
        String testoCorrente = getTesto();

        if (testoCorrente != null && !testoCorrente.isEmpty()) {
            String testoSpecchiato = new StringBuilder(testoCorrente).reverse().toString();
            setTesto(testoSpecchiato);
            setAngoloInclinazione(-getAngoloInclinazione());
        }
        specchiataVerticale = !specchiataVerticale;
    }

    /**
     * Gestisce la ridistribuzione dei valori della {@link Forma} per specchiarla
     * lungo l'asse orizzontale che passa per il centro della {@link Forma} stessa
     */
    @Override
    public void specchiaInOrizzontale() {
        setAngoloInclinazione(-getAngoloInclinazione());
        this.specchiataOrizzontale = !specchiataOrizzontale;
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
        out.writeUTF(ColorUtils.toHexString(super.getColore()));
        // Serializza il colore interno specifico della sottoclasse
        out.writeUTF(ColorUtils.toHexString(getColoreInterno()));
        out.writeUTF(testo);
        out.writeDouble(currentFontSize);
        out.writeBoolean(specchiataVerticale);
        out.writeBoolean(specchiataOrizzontale);
    }

    /**
     * Gestsce la deserializzazione dell'oggetto nel complesso con il metodo della superclasse e
     * poi ricava anche il {@link Color} di riempimento che non è {@link java.io.Serializable}.
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
        String testo = in.readUTF();
        this.setTesto(testo);
        this.currentFontSize = in.readDouble();
        specchiataVerticale = in.readBoolean();
        specchiataOrizzontale = in.readBoolean();
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