/**
 * Sample Skeleton for 'DrawSnapView.fxml' Controller Class
 */

package it.unisa.software_architecture_design.drawsnapdrawingtool;

import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;

public class DrawSnapController {

    /**
     * Attributi per il foglio di disegno
     **/
    @FXML
    private Canvas canvas;
    private GraphicsContext gc;

    /**
     * Metodo di Inizializzazione dopo il caricamento del foglio fxml
     */
    @FXML
    void initialize() {
        gc = canvas.getGraphicsContext2D();
    }
}