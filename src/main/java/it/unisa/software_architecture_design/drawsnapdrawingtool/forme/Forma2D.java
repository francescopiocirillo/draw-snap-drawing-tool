package it.unisa.software_architecture_design.drawsnapdrawingtool.forme;

import it.unisa.software_architecture_design.drawsnapdrawingtool.utils.ColorUtils;
import javafx.scene.paint.Color;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serial;

/**
 * La classe {@link Forma2D} eredita le caratteristiche della classe {@link Forma}
 * e viene usata per distinguere le {@link Forma} a 2 Dimensioni. Si tratta di una
 * classe astratta che verrà estesa da delle classi concrete.
 */
public abstract class Forma2D extends Forma{
    /*
     * Attributi
     */
    private double altezza;
    private transient Color coloreInterno;

    /*
     * Costruttore, Getter e Setter
     */
    public Forma2D(double coordinataX, double coordinataY, double larghezza, double angoloInclinazione, Color colore, double altezza, Color coloreInterno) {
        super(coordinataX, coordinataY, larghezza, angoloInclinazione, colore);
        this.altezza = altezza;
        this.coloreInterno = coloreInterno;
    }

    public Color getColoreInterno() {
        return coloreInterno;
    }

    public void setColoreInterno(Color coloreInterno) {
        this.coloreInterno = coloreInterno;
    }

    public double getAltezza() {
        return altezza;
    }

    public void setAltezza(double altezza) {
        this.altezza = altezza;
    }

    /*
     * Logica della classe
     */

    /**
     * Gestisce il ridimensionamento della {@link Forma2D} in modo proporzionale applicando un
     * fattore di scala uniforme a tutti i suoi punti intrinseci.
     *
     * @param proporzione La percentuale di ridimensionamento (es. 100 per nessuna modifica, 50 per metà dimensione).
     */
    @Override
    public void proportionalResize(double proporzione){
        setLarghezza(getLarghezza()*proporzione/100);
        setAltezza(getAltezza()*proporzione/100);
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
        out.writeUTF(ColorUtils.toHexString(coloreInterno));
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
}
