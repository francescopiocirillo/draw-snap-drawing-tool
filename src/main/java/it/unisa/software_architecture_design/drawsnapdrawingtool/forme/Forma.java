package it.unisa.software_architecture_design.drawsnapdrawingtool.forme;


import it.unisa.software_architecture_design.drawsnapdrawingtool.utils.ColorUtils;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;


public abstract class Forma implements Serializable {
    /*
     * Attributi
     */
    private static final long serialVersionUID = 1L;
    private double coordinataY;
    private double coordinataX;
    private double larghezza;
    private double angoloInclinazione;
    private transient Color colore;

    /*
     * Costruttore, getter e setter
     */
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

    public void setAngoloInclinazione(double angoloInclidazione) {
        this.angoloInclinazione = angoloInclidazione;
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

    /*
     * Metodi per la serializzazione/deserializzazione
     */

    /**
     * Serializza l'oggetto nel complesso e poi salva come Stringa l'informazione sul colore
     * visto che Color non è serializzabile
     * @param out è lo stream sul quale salvare le informazioni, sarà il File scelto dall'utente
     * @throws IOException se si verifica un errore di I/O durante la scrittura dell'oggetto
     */
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
    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        in.defaultReadObject();
        // ricostruisci il colore da stringa HEX
        String colorHex = in.readUTF();
        colore = Color.web(colorHex);
    }

}
