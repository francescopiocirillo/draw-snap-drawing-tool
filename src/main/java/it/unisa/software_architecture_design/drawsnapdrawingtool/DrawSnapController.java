package it.unisa.software_architecture_design.drawsnapdrawingtool;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import it.unisa.software_architecture_design.drawsnapdrawingtool.enumeration.*;

public class DrawSnapController {

    /*
     * Attributi per il foglio di disegno
     */
    @FXML
    private Canvas canvas;
    private GraphicsContext gc;

    /*
     * Attributi per i bottoni
     */
    @FXML
    private Button ellipseButton;
    @FXML
    private Button rectangleButton;
    @FXML
    private Button lineButton;

    /**
     * Metodo di Inizializzazione dopo il caricamento del foglio fxml
     */
    @FXML
    void initialize() {
        gc = canvas.getGraphicsContext2D();
        ellipseButton.setOnAction(event -> setDrawMode(event, Forme.ELLISSE));
        rectangleButton.setOnAction(event -> setDrawMode(event, Forme.RETTANGOLO));
        lineButton.setOnAction(event -> setDrawMode(event, Forme.LINEA));
    }

    /**
     * Metodo per passare alla modalità di disegno
     * @param event -> evento che ha causato la chiamata alla funzione
     * @param forme -> forma corrispondente al bottone che è stato premuto
     */
    void setDrawMode(ActionEvent event, Forme forme) {

    }
}