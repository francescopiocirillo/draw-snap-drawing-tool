package it.unisa.software_architecture_design.drawsnapdrawingtool;

import it.unisa.software_architecture_design.drawsnapdrawingtool.commands.Invoker;
import it.unisa.software_architecture_design.drawsnapdrawingtool.commands.SaveCommand;
import it.unisa.software_architecture_design.drawsnapdrawingtool.forme.Forma;
import it.unisa.software_architecture_design.drawsnapdrawingtool.interactionstate.DrawState;
import it.unisa.software_architecture_design.drawsnapdrawingtool.interactionstate.DrawingContext;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import it.unisa.software_architecture_design.drawsnapdrawingtool.enumeration.*;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;

public class DrawSnapController {

    /*
     * Attributi per il foglio di disegno
     */
    @FXML
    private Canvas canvas;
    private GraphicsContext gc;
    private DrawingContext drawingContext;
    private List<Forma> forme = null;
    private Stage stage;

    /*
     * Attributi per i bottoni
     */
    @FXML
    private Button ellipseButton;
    @FXML
    private Button rectangleButton;
    @FXML
    private Button lineButton;

    /*
     * Attributi per la logica
     */
    private Invoker invoker = null;


    /**
     * Metodo di Inizializzazione dopo il caricamento del foglio fxml
     */
    @FXML
    void initialize() {
        forme = new ArrayList<>();
        gc = canvas.getGraphicsContext2D();
        drawingContext = new DrawingContext(new DrawState(forme, Forme.LINEA)); // stato di default, sarà cambiato quando avremo lo stato sposta o seleziona
        invoker = new Invoker();

        // inizializzazione bottoni per la selezione forma
        ellipseButton.setOnAction(event -> setDrawMode(event, Forme.ELLISSE));
        rectangleButton.setOnAction(event -> setDrawMode(event, Forme.RETTANGOLO));
        lineButton.setOnAction(event -> setDrawMode(event, Forme.LINEA));

        initializeCanvasEventHandlers();
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    /**
     * Inizializza gli event handler per gli eventi di interesse relativi al {@link Canvas}
     */
    private void initializeCanvasEventHandlers() {
        canvas.setOnMousePressed(this::handleMousePressed);
        canvas.setOnMouseDragged(this::handleMouseDragged);
        canvas.setOnMouseReleased(this::handleMouseReleased);
    }

    private void handleMousePressed(MouseEvent mouseEvent) {
        System.out.println("Mouse pressed");
        /*
         * handleMousePressed del controller deve aprire la finestra di dialogo per inserire i dati
         * della figura, dopodiché solo quando l'utente clicca il tasto di conferma deve prendere
         * i parametri inseriti dall'utente e passarli all'handleMousePressed di DrawingContext
         */
        //drawingContext.handleMousePressed(mouseEvent, forme /*aggiungere parametri che vengono dalla finestra di dialogo*/);
        /* drawingContext passa i parametri a handleMousePressed di DrawState, che crea la figura e la aggiunge
         * alla lista figure, dopodiché il controller deve ricaricare il canvas (tipo con una funzione redrawAll())
         * per mostrare anche la figura aggiornata
         */

    }

    private void handleMouseDragged(MouseEvent mouseEvent) {

    }

    private void handleMouseReleased(MouseEvent mouseEvent) {

    }

    /**
     * Metodo per passare alla modalità di disegno
     * @param event -> evento che ha causato la chiamata alla funzione
     * @param forma -> forma corrispondente al bottone che è stato premuto
     */
    void setDrawMode(ActionEvent event, Forme forma) {
        drawingContext.setCurrentState(new DrawState(forme, forma));
    }

    @FXML
    void onSavePressed(ActionEvent event) {
        invoker.setCommand(new SaveCommand(forme, stage));
        invoker.executeCommand();
    }

}