package it.unisa.software_architecture_design.drawsnapdrawingtool.forme;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.awt.geom.Line2D;

public class Linea extends Forma1D  {

    /*
     * Costruttore
     */
    public Linea(double coordinataX, double coordinataY, double larghezza, double angoloInclinazione, Color colore) {
        super(coordinataX, coordinataY, larghezza, angoloInclinazione, colore); // Chiamata al costruttore della classe estesa Forma
    }

    /*
      Logica della classe
     */

    /**
     * Disegna una linea sul {@link GraphicsContext} specificato.
     * Questo metodo disegna una linea utilizzando le coordinate di inizio e fine fornite dagli attributi
     * dell'oggetto usando il colore della forma.
     *
     * @param gc il {@code GraphicsContext} su cui disegnare la linea.
     *           Deve essere già inizializzato e associato a un {@code Canvas} valido.
     */
    @Override
    public void disegna(GraphicsContext gc) {
        // Salva lo stato iniziale del foglio di disegno
        gc.save();

        // Imposta il colore del GraphicsContext
        gc.setStroke(getColore());

        // Calcola i punti iniziali e finali della linea
        double xInizio = getXInizio();
        double yInizio = getYInizio();
        double xFine = getXFine();
        double yFine = getYFine();

        // Disegna la linea
        gc.strokeLine(xInizio, yInizio, xFine, yFine);

        // Ripristina lo stato iniziale del foglio di disegno
        gc.restore();
    }

}
