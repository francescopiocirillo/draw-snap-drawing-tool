package it.unisa.software_architecture_design.drawsnapdrawingtool.forme;


import it.unisa.software_architecture_design.drawsnapdrawingtool.utils.ColorUtils;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.io.*;


public abstract class Forma implements Serializable, Cloneable{
    /*
     * Attributi
     */
    @Serial
    private static final long serialVersionUID = 1L;
    private double coordinataY;
    private double coordinataX;
    private double angoloInclinazione;
    private transient Color colore;

    /*
     * Costruttore, getter e setter
     */
    public Forma(){} //usato da FormaDecorator

    public Forma(double coordinataX, double coordinataY, double angoloInclinazione, Color colore) {
        this.coordinataX = coordinataX;
        this.coordinataY = coordinataY;
        this.angoloInclinazione = angoloInclinazione;
        this.colore = colore;
    }

    public double getCoordinataY() {
        return coordinataY;
    }

    public double getCoordinataX() {
        return coordinataX;
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

    public void setColore(Color colore) {
        this.colore = colore;
    }

    public void setAngoloInclinazione(double angoloInclinazione) {
        this.angoloInclinazione = angoloInclinazione;
    }


    /*
     * Logica della classe
     */

    /**
     * Disegna la Forma sul {@link GraphicsContext} specificato.
     *
     * @param gc il {@code GraphicsContext} su cui disegnare la Forma.
     *           Deve essere già inizializzato e associato a un {@code Canvas} valido.
     */
    public abstract void disegna(GraphicsContext gc);

    /**
     * Determina se la forma contiene un punto specifico nello spazio.
     *
     * @param puntoDaValutareX La coordinata X del punto da verificare.
     * @param puntoDaValutareY La coordinata Y del punto da verificare.
     * @return {@code true} se il punto specificato (puntoDaValutareX, puntoDaValutareY) si trova all'interno della forma,
     *         altrimenti {@code false}.
     */
    public abstract boolean contiene(double puntoDaValutareX, double puntoDaValutareY);

    /**
     * Ridistribuisce i valori della figura per specchiarla lungo l'asse verticale che passa per il
     * cetro della figura stessa
     */
    public abstract void specchia();
    /*
     * Metodi per la serializzazione/deserializzazione
     */

    /**
     * Serializza l'oggetto nel complesso e poi salva come Stringa l'informazione sul colore
     * visto che Color non è serializzabile
     * @param out è lo stream sul quale salvare le informazioni, sarà il File scelto dall'utente
     * @throws IOException se si verifica un errore di I/O durante la scrittura dell'oggetto
     */
    @Serial
    private void writeObject(ObjectOutputStream out) throws IOException {
        out.defaultWriteObject();
        // salva il colore come stringa HEX
        out.writeUTF(ColorUtils.toHexString(colore));
    }

    /**
     * Deserializza l'oggetto nel complesso e poi recupera le informazioni sul colore
     * visto che Color non è serializzabile
     * @param in è lo stream dal quale prelevare le informazioni, sarà il File scelto dall'utente
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

    /**
     * Metodo per clonare l'oggetto creandone una nuova istanza
     * Permette di modificare l'elemento clonato senza intaccare quello originale
     * @return la forma clonata.
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
     * Metodo per il controllare se due forme sono uguali
     * @param forma -> forma con cui fare il confronto
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
                this.coordinataY == forma.getCoordinataY();
    }
}
