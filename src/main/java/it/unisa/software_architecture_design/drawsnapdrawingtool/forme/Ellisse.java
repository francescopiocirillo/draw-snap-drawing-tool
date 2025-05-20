package it.unisa.software_architecture_design.drawsnapdrawingtool.forme;

import it.unisa.software_architecture_design.drawsnapdrawingtool.utils.ColorUtils;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serial;

public class Ellisse extends Forma {
    /*
     * Attributi
     */
    private double altezza;
    private transient Color coloreInterno;

    /**
     * Costruttore, Getter e Setter
     */
    public Ellisse(double coordinataX, double coordinataY, double larghezza, double angoloInclinazione, Color colore, double altezza, Color coloreInterno) {
        super(coordinataX, coordinataY, larghezza, angoloInclinazione, colore);
        this.altezza = altezza;
        this.coloreInterno = coloreInterno;
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

    /*
      Logica della classe
     */
    
    /**
     * Disegna l'Ellisse sul {@link GraphicsContext} specificato.
     *
     * @param gc il {@code GraphicsContext} su cui disegnare l'Ellisse.
     *           Deve essere già inizializzato e associato a un {@code Canvas} valido.
     */
    @Override
    public void disegna(GraphicsContext gc) {
        // Salva lo stato iniziale del foglio di disegno
        gc.save();

        // Traslazione al centro dell'ellisse
        gc.translate(getCoordinataX(), getCoordinataY());

        // rotate inclina l'ellisse secondo l'angoloInclinazione indicato
        gc.rotate(getAngoloInclinazione());

        // fill disegna l'area interna dell'ellisse
        gc.setFill(coloreInterno);
        gc.fillOval(-getLarghezza()/ 2, -altezza / 2, getLarghezza(), altezza);

        // stroke disegna il contorno
        gc.setStroke(getColore());
        gc.setLineWidth(2);
        gc.strokeOval(-getLarghezza() / 2, -altezza / 2, getLarghezza(), altezza);

        // Ripristina lo stato iniziale del foglio di disegno
        gc.restore();
    }

    /**
     * Determina se l'Ellisse contiene un punto specifico nello spazio.
     * L'equazione per verificare se un punto si trova all'interno dell'ellisse è
     * \frac{(puntoDaValutareX-coordinataX)^2}{mezzaLarghezza^2}+\frac{(puntoDaValutareY-coordinataY)^2}{mezzaAltezza^2}<=1
     *
     *
     * @param puntoDaValutareX la coordinata X del punto da valutare
     * @param puntoDaValutareY la coordinata Y del punto da valutare
     * @return {@code true} se il punto specificato (puntoDaValutareX, puntoDaValutareY) si trova all'interno dell'Ellisse,
     *          altrimenti {@code false}.
     */
    @Override
    public boolean contiene(double puntoDaValutareX, double puntoDaValutareY) {
        double mezzaLarghezza = getLarghezza() / 2;
        double mezzaAltezza = altezza / 2;

        // Traslazione rispetto al centro dell'ellisse
        double dx = puntoDaValutareX - getCoordinataX();
        double dy = puntoDaValutareY - getCoordinataY();

        // Rotazione inversa per annullare l'inclinazione
        double radians = Math.toRadians(-getAngoloInclinazione());
        double rotatedX = dx * Math.cos(radians) - dy * Math.sin(radians);
        double rotatedY = dx * Math.sin(radians) + dy * Math.cos(radians);

        // Verifica se il punto trasformato soddisfa l'equazione dell'ellisse
        return (rotatedX * rotatedX) / (mezzaLarghezza * mezzaLarghezza) +
                (rotatedY * rotatedY) / (mezzaAltezza * mezzaAltezza) <= 1;
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
        Ellisse ellisse = (Ellisse) forma;
        return super.confrontaAttributi(ellisse) &&
                this.altezza == ellisse.getAltezza() &&
                this.coloreInterno == ellisse.getColoreInterno();
    }
}
