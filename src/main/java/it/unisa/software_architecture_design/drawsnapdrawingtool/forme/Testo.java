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

public class Testo extends Forma {

    private double altezza;
    private transient Color coloreInterno;
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
        super(coordinataX, coordinataY, larghezza, angoloInclinazione, colore);
        this.altezza = altezza;
        this.coloreInterno = coloreInterno;
        this.testo = testo;
        System.out.println("colore interno" + coloreInterno + "\n colore Bordo" + super.getColore());
        calculateFontSize();
        updateVertici();
    }

    /**
     * Aggiorna le coordinate dei vertici del bounding box del testo.
     * Questo metodo calcola i 4 vertici del rettangolo delimitatore del testo
     * tenendo conto della sua posizione (centro), larghezza, altezza e angolo di inclinazione.
     * È simile a updateVertici() del Rettangolo.
     */
    private void updateVertici() {

        double centroX = getCoordinataX();
        double centroY = getCoordinataY();
        double mezzaLarghezza = getLarghezza() / 2;
        double mezzaAltezza = this.altezza / 2;

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

    public double getAltezza() {
        return altezza;
    }

    public void setAltezza(double altezza) {
        this.altezza = altezza;
        System.out.println("Altezza: " + altezza);
        calculateFontSize();
        updateVertici();
    }

    @Override
    public void setLarghezza(double larghezza) {
        super.setLarghezza(larghezza);
        calculateFontSize();
        updateVertici();
    }

    public void setAngoloInclinazione(double angoloInclinazione) {
        super.setAngoloInclinazione(angoloInclinazione);
        updateVertici();
    }

    public Color getColoreInterno() {
        return coloreInterno;
    }

    public void setColoreInterno(Color coloreInterno) {
        this.coloreInterno = coloreInterno;
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

    /**
     * Restituisce la larghezza effettiva del testo renderizzato (corrisponde alla larghezza della bounding box).
     * Questo è utile per il decorator di selezione.
     * @return La larghezza della bounding box del testo.
     */
    public double getRenderedWidth() {
        return getLarghezza(); // La larghezza renderizzata è la larghezza della bounding box
    }

    /**
     * Restituisce l'altezza effettiva del testo renderizzato (corrisponde all'altezza della bounding box).
     * Questo è utile per il decorator di selezione.
     * @return L'altezza della bounding box del testo.
     */
    public double getRenderedHeight() {
        return getAltezza(); // L'altezza renderizzata è l'altezza della bounding box
    }

    /**
     * Calcola i fattori di scala (scaleX, scaleY) necessari per stirare il testo
     * in modo che riempia la larghezza e l'altezza della bounding box.
     * Imposta anche un `currentFontSize` di base per la misurazione del testo.
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
     * Ridistribuisce i valori della figura per specchiarla lungo l'asse verticale che passa per il
     * cetro della figura stessa
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
     * Ridistribuisce i valori della figura per specchiarla lungo l'asse orizzontale che passa per il
     * cetro della figura stessa
     */
    @Override
    public void specchiaInOrizzontale() {
        setAngoloInclinazione(-getAngoloInclinazione());
        this.specchiataOrizzontale = !specchiataOrizzontale;
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
        out.writeUTF(ColorUtils.toHexString(super.getColore()));
        // Serializza il colore interno specifico della sottoclasse
        out.writeUTF(ColorUtils.toHexString(coloreInterno));
        out.writeUTF(testo);
        out.writeDouble(currentFontSize);
        out.writeBoolean(specchiataVerticale);
        out.writeBoolean(specchiataOrizzontale);
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
        String testo = in.readUTF();
        this.setTesto(testo);
        this.currentFontSize = in.readDouble();
        specchiataVerticale = in.readBoolean();
        specchiataOrizzontale = in.readBoolean();
    }
}
