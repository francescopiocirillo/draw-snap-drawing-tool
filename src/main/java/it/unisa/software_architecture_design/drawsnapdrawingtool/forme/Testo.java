package it.unisa.software_architecture_design.drawsnapdrawingtool.forme;


import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class Testo extends Forma {

    private double altezza;
    private transient Color coloreInterno;
    private transient String testo;
    private final String FONT_NAME = "Arial";
    private double currentFontSize = 12.0;
    private boolean specchiata = false;
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
    public Testo(double coordinataX, double coordinataY, double larghezza, double angoloInclinazione, Color colore, double altezza, Color coloreInterno, String testo) {
        super(coordinataX, coordinataY, larghezza, angoloInclinazione, colore);
        this.altezza = altezza;
        this.coloreInterno = coloreInterno;
        this.testo = testo;
        calculateFontSize();
    }

    /**
     * Aggiorna le coordinate dei vertici del bounding box del testo.
     * Questo metodo calcola i 4 vertici del rettangolo delimitatore del testo
     * tenendo conto della sua posizione (centro), larghezza, altezza e angolo di inclinazione.
     * È simile a updateVertici() del Rettangolo.
     */
    private void updateVertici() {
        // getCoordinataX() e getCoordinataY() sono il centro della forma (come per Rettangolo)
        double centroX = getCoordinataX();
        double centroY = getCoordinataY();
        double mezzaLarghezza = getLarghezza() / 2;
        double mezzaAltezza = this.altezza / 2;

        double angoloRadianti = Math.toRadians(getAngoloInclinazione());
        double cosAngolo = Math.cos(angoloRadianti);
        double sinAngolo = Math.sin(angoloRadianti);

        // Calcolo dei vertici del rettangolo ruotato attorno al centro
        // Vertice A (Top-Left)
        this.verticeAX = centroX - mezzaLarghezza * cosAngolo + mezzaAltezza * sinAngolo;
        this.verticeAY = centroY - mezzaLarghezza * sinAngolo - mezzaAltezza * cosAngolo;

        // Vertice B (Top-Right)
        this.verticeBX = centroX + mezzaLarghezza * cosAngolo + mezzaAltezza * sinAngolo;
        this.verticeBY = centroY + mezzaLarghezza * sinAngolo - mezzaAltezza * cosAngolo;

        // Vertice C (Bottom-Right)
        this.verticeCX = centroX + mezzaLarghezza * cosAngolo - mezzaAltezza * sinAngolo;
        this.verticeCY = centroY + mezzaLarghezza * sinAngolo + mezzaAltezza * cosAngolo;

        // Vertice D (Bottom-Left)
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
     * Restituisce la larghezza effettiva del testo renderizzato con il font calcolato.
     * Questo è utile per il decorator di selezione.
     * @return La larghezza renderizzata del testo.
     */
    public double getRenderedWidth() {
        if (testo == null || testo.isEmpty()) {
            return 0;
        }
        Text tempText = new Text(testo);
        tempText.setFont(Font.font(FONT_NAME, currentFontSize));
        return tempText.getLayoutBounds().getWidth();
    }

    /**
     * Restituisce l'altezza effettiva del testo renderizzato con il font calcolato.
     * Questo è utile per il decorator di selezione.
     * @return L'altezza renderizzata del testo.
     */
    public double getRenderedHeight() {
        if (testo == null || testo.isEmpty()) {
            return 0;
        }
        Text tempText = new Text(testo);
        tempText.setFont(Font.font(FONT_NAME, currentFontSize));
        return tempText.getLayoutBounds().getHeight();
    }



    private void calculateFontSize(){

        if(!(getLarghezza() <=0 || getAltezza() <= 0 || testo == null || testo.isEmpty())){
            double targetFontSize = 100.0;

            Text tempText = new Text(testo);
            tempText.setFont(Font.font(FONT_NAME, targetFontSize));

            double scaleX = getLarghezza() / tempText.getLayoutBounds().getWidth();
            double scaleY = getAltezza() / tempText.getLayoutBounds().getHeight();

            this.currentFontSize = targetFontSize * Math.min(scaleX, scaleY);
        }
    }


    @Override
    public void disegna(GraphicsContext gc) {
        gc.save(); // Salva lo stato iniziale del contesto grafico

        double centroX = getCoordinataX();
        double centroY = getCoordinataY();

        // Applica le trasformazioni della forma (traslazione e rotazione)
        gc.translate(centroX, centroY);
        gc.rotate(getAngoloInclinazione());

        // Imposta il font, i colori e lo spessore del bordo
        gc.setFont(Font.font(FONT_NAME, currentFontSize));
        gc.setFill(getColoreInterno());
        gc.setStroke(getColore());

        // Se il testo è nullo o vuoto, non disegnare nulla e esci
        if (testo == null || testo.isEmpty()) {
            gc.restore();
            return;
        }

        // --- CALCOLO PER POSIZIONARE I CARATTERI ---
        // Misura la larghezza totale della stringa per centrarla
        // (usando un temporaneo Text object con il font calcolato)
        Text tempMeasureText = new Text(testo);
        tempMeasureText.setFont(Font.font(FONT_NAME, currentFontSize));
        double totalTextWidth = tempMeasureText.getLayoutBounds().getWidth();
        double textHeight = tempMeasureText.getLayoutBounds().getHeight();
        double baselineOffset = -tempMeasureText.getLayoutBounds().getMinY(); // Offset per allineamento baseline

        // Calcola la posizione X di partenza per il primo carattere, in modo che l'intera stringa sia centrata
        double currentX = -totalTextWidth / 2;
        // Calcola l'offset Y per centrare verticalmente il testo nel suo bounding box, allineando alla baseline
        double charOffsetY = -textHeight / 2 + baselineOffset;

        // --- CICLO PER DISEGNARE OGNI CARATTERE INDIVIDUALMENTE ---
        for (int i = 0; i < testo.length(); i++) {
            String currentChar = String.valueOf(testo.charAt(i));

            // Misura il singolo carattere per ottenere la sua larghezza esatta
            Text charMeasure = new Text(currentChar);
            charMeasure.setFont(Font.font(FONT_NAME, currentFontSize));
            double charWidth = charMeasure.getLayoutBounds().getWidth();

            gc.save(); // Salva lo stato del GC per applicare trasformazioni al singolo carattere

            // 1. Trasla l'origine del GC al centro del carattere corrente.
            // currentX è l'inizio del carattere, aggiungiamo charWidth / 2 per arrivare al centro.
            gc.translate(currentX + charWidth / 2, charOffsetY);

            if(specchiata) {
                gc.scale(-1, 1);
            }
            // 3. Disegna il carattere.
            // Dato che l'origine è stata traslata al centro del carattere e poi scalata,
            // disegniamo il carattere con un offset negativo della sua metà larghezza per farlo apparire centrato.
            // La coordinata Y è 0 perché charOffsetY è già stata inclusa nella traslazione.
            gc.fillText(currentChar, -charWidth / 2, 0);
            gc.strokeText(currentChar, -charWidth / 2, 0);

            gc.restore(); // Ripristina lo stato del GC per il carattere successivo

            // Avanza la posizione X per il carattere successivo
            currentX += charWidth;
        }

        gc.restore(); // Ripristina lo stato iniziale del GC dopo aver disegnato tutti i caratteri
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

    @Override
    public void specchia() {

        String testoCorrente = getTesto();

        if (testoCorrente != null && !testoCorrente.isEmpty()) {
            String testoSpecchiato = new StringBuilder(testoCorrente).reverse().toString();
            setTesto(testoSpecchiato);
            setAngoloInclinazione(-getAngoloInclinazione());
        }
        specchiata = !specchiata;
    }
}
