/**
 * Sample Skeleton for 'DrawSnapView.fxml' Controller Class
 */

package it.unisa.software_architecture_design.drawsnapdrawingtool;

import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;

public class DrawSnapController {

    //Oggetti per il foglio di disegno
    @FXML
    private Canvas canvas;
    private GraphicsContext gc;

    @FXML //questo metodo viene chiamato quando il file FXML viene caricato per inizializzarlo
    void initialize() {
        gc = canvas.getGraphicsContext2D();
    }
}