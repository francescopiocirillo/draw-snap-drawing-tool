package it.unisa.software_architecture_design.drawsnapdrawingtool.forme;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.awt.geom.Line2D;

/**
 * La classe {@link Linea} rappresenta la {@link Forma} Linea e presenta
 * tutte le caratteristiche ereditate da {@link Forma1D}.
 */
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
     * Gestisce il disegno di una {@link Linea} sul {@link GraphicsContext} specificato.
     * Questo metodo disegna una {@link Linea} utilizzando le coordinate di inizio e
     * fine fornite dagli attributi dell'oggetto usando il {@link Color} della {@link Forma}.
     * @param gc il {@link GraphicsContext} su cui disegnare la {@link Linea}.
     *           Deve essere già inizializzato e associato a un {@link javafx.scene.canvas.Canvas} valido.
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
