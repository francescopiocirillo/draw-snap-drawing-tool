package it.unisa.software_architecture_design.drawsnapdrawingtool.forme;


import it.unisa.software_architecture_design.drawsnapdrawingtool.utils.ColorUtils;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.io.*;

/**
 * La classe {@link Forma} rappresenta una forma generica. Si tratta di una
 * classe astratta che verrà estesa da delle classi concrete.
 */
public abstract class Forma implements Serializable, Cloneable{
    /*
     * Attributi
     */
    @Serial
    private static final long serialVersionUID = 1L;
    private double coordinataY;
    private double coordinataX;
    private double larghezza;
    private double angoloInclinazione;
    private transient Color colore;

    private double offsetX;
    private double offsetY;

    private static final double MIN_DIMENSION = 5.0;
    private static final double MAX_DIMENSION = 1000.0;

    /*
     * Costruttore, getter e setter
     */
    public Forma(){} //usato da FormaDecorator

    public Forma(double coordinataX, double coordinataY, double larghezza, double angoloInclinazione, Color colore) {
        this.coordinataX = coordinataX;
        this.coordinataY = coordinataY;
        this.larghezza = larghezza;
        this.angoloInclinazione = angoloInclinazione;
        this.colore = colore;
    }

    public double getCoordinataY() {
        return coordinataY;
    }

    public double getCoordinataX() {
        return coordinataX;
    }

    public double getLarghezza() {
        return larghezza;
    }

    public double getAngoloInclinazione() {
        return angoloInclinazione;
    }

    public Color getColore() {
        return colore;
    }

    public void setCoordinataY(double coordinataY) {
        this.coordinataY = coordinataY;
    }

    public void setCoordinataX(double coordinataX) {
        this.coordinataX = coordinataX;
    }

    public void setLarghezza(double larghezza) {
        this.larghezza = larghezza;
    }

    public void setColore(Color colore) {
        this.colore = colore;
    }

    public void setAngoloInclinazione(double angoloInclinazione) {
        this.angoloInclinazione = angoloInclinazione;
    }

    public void setOffsetX(double coordinataXPressed) {
        this.offsetX = coordinataXPressed - getCoordinataX();
    }

    public double getOffsetX() {
        return offsetX;
    }

    public void setOffsetY(double coordinataYPressed) {
        this.offsetY = coordinataYPressed - getCoordinataY();
    }

    public double getOffsetY() {
        return offsetY;
    }

    /**
     * Setter della coordinata X secondo la logica necessaria per l'operazione di Drag.
     * La coordinata deve essere impostata alla differenza tra la coordinata x dell'evento e il valore di offset
     * lungo l'asse x che viene impostato in seguito all'evento di mouse pressed che precede il drag.
     * @param coordinataXMouseDragged -> Coordinata X dell'evento mouse dragged
     */
    public void setCoordinataXForDrag(double coordinataXMouseDragged){
        setCoordinataX(coordinataXMouseDragged-getOffsetX());
    }

    /**
     * Setter della coordinata Y secondo la logica necessaria per l'operazione di Drag.
     * La coordinata deve essere impostata alla differenza tra la coordinata x dell'evento e il valore di offset
     * lungo l'asse x che viene impostato in seguito all'evento di mouse pressed che precede il drag.
     * @param coordinataYMouseDragged -> Coordinata X dell'evento mouse dragged
     */
    public void setCoordinataYForDrag(double coordinataYMouseDragged){
        setCoordinataY(coordinataYMouseDragged-getOffsetY());
    }

    /*
     * Logica della classe
     */

    /**
     * Gestisce il disegno di una {@link Forma} sul {@link GraphicsContext} specificato.
     *
     * @param gc il {@link GraphicsContext} su cui disegnare la {@link Forma}.
     *           Deve essere già inizializzato e associato a un {@link javafx.scene.canvas.Canvas} valido.
     */
    public abstract void disegna(GraphicsContext gc);

    /**
     * Verifica se la {@link Forma} contiene un punto specifico nello spazio.
     *
     * @param puntoDaValutareX La coordinata X del punto da verificare.
     * @param puntoDaValutareY La coordinata Y del punto da verificare.
     * @return {@code true} se il punto specificato (puntoDaValutareX, puntoDaValutareY) si trova all'interno della forma,
     *         altrimenti {@code false}.
     */
    public abstract boolean contiene(double puntoDaValutareX, double puntoDaValutareY);

    /**
     * Gestisce la ridistribuzione dei valori della {@link Forma} per specchiarla
     * lungo l'asse verticale che passa per il centro della {@link Forma} stessa
     */
    public abstract void specchiaInVerticale();

    /**
     * Gestisce la ridistribuzione dei valori della {@link Forma} per specchiarla
     * lungo l'asse orizzontale che passa per il centro della {@link Forma} stessa
     */
    public abstract void specchiaInOrizzontale();

    /**
     * Gestisce il ridimensionamento della {@link Forma} in modo proporzionale applicando un
     * fattore di scala uniforme a tutti i suoi punti intrinseci, sempre rispettando i limiti di dimensione 5-1000
     *
     * @param proporzione La percentuale di ridimensionamento (es. 100 per nessuna modifica, 50 per metà dimensione).
     */
    public void proportionalResize(double proporzione){
        double nuovaLarghezza = getLarghezza() * proporzione / 100;

        if (nuovaLarghezza < MIN_DIMENSION) {
            setLarghezza(MIN_DIMENSION);
        } else if (nuovaLarghezza > MAX_DIMENSION) {
            setLarghezza(MAX_DIMENSION);
        } else {
            setLarghezza(nuovaLarghezza);
        }
    }

    /**
     * Gestisce la clonazione dell'oggetto creandone una nuova istanza
     * Permette di modificare l'elemento clonato senza intaccare quello originale
     * @return la {@link Forma} clonata.
     */
    @Override
    public Forma clone(){
        try{
            return (Forma) super.clone();
        }catch(CloneNotSupportedException ex){
            ex.printStackTrace();
            return null;
        }
    }

    /**
     * Verifica se la {@link Forma} corrente è uguale ad un altra {@link Forma}
     * @param forma è la {@link Forma} con cui fare il confronto
     * @return {@code true} se gli attributi sono uguali, altrimenti {@code false}
     */
    public boolean confrontaAttributi(Forma forma){
        if (forma instanceof FormaDecorator){
            FormaDecorator decorator = (FormaDecorator) forma;
            forma = decorator.getForma();
        }
        return this.angoloInclinazione == forma.getAngoloInclinazione() &&
                this.colore == forma.getColore() &&
                this.coordinataX == forma.getCoordinataX() &&
                this.coordinataY == forma.getCoordinataY() &&
                this.larghezza == forma.getLarghezza();
    }

    /*
     * Logica per la serializzazione e deserializzazione
     */

    /**
     * Gestisce la serializzazione dell'oggetto nel complesso e
     * poi salva come {@link String} l'informazione sul {@code colore}
     * visto che {@link Color} non è {@link Serializable}.
     * @param out è l' {@link ObjectOutputStream} sul quale salvare le informazioni, sarà il
     *            {@link java.io.File} scelto dall'utente
     * @throws IOException se si verifica un errore di I/O durante la scrittura dell'oggetto
     */
    @Serial
    private void writeObject(ObjectOutputStream out) throws IOException {
        out.defaultWriteObject();
        // salva il colore come stringa HEX
        out.writeUTF(ColorUtils.toHexString(colore));
    }

    /**
     * Gestisce la deserializzazione dell'oggetto nel complesso e
     * poi recupera le informazioni sul {@code colore}
     * visto che {@link Color} non è {@link Serializable}.
     * @param in è l' {@link ObjectInputStream} dal quale caricare le informazioni, sarà il
     *            {@link java.io.File} scelto dall'utente
     * @throws IOException se si verifica un errore di I/O durante la scrittura dell'oggetto
     * @throws ClassNotFoundException se la classe dell'oggetto serializzato non è trovata
     */
    @Serial
    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        in.defaultReadObject();
        // ricostruisci il colore da stringa HEX
        String colorHex = in.readUTF();
        colore = Color.web(colorHex);
    }
}
