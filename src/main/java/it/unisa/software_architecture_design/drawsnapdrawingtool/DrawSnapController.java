package it.unisa.software_architecture_design.drawsnapdrawingtool;

import it.unisa.software_architecture_design.drawsnapdrawingtool.commands.CutCommand;
import it.unisa.software_architecture_design.drawsnapdrawingtool.commands.DeleteCommand;
import it.unisa.software_architecture_design.drawsnapdrawingtool.commands.Invoker;
import it.unisa.software_architecture_design.drawsnapdrawingtool.commands.SaveCommand;
import it.unisa.software_architecture_design.drawsnapdrawingtool.forme.Forma;
import it.unisa.software_architecture_design.drawsnapdrawingtool.interactionstate.DrawState;
import it.unisa.software_architecture_design.drawsnapdrawingtool.interactionstate.DrawingContext;
import it.unisa.software_architecture_design.drawsnapdrawingtool.interactionstate.SelectState;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
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
    @FXML
    private Button selectButton;
    @FXML
    private ToolBar toolBarFX; // barra in alto delle modifiche

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
        drawingContext = new DrawingContext(new SelectState(toolBarFX)); // stato di default, sarà cambiato quando avremo lo stato sposta o seleziona
        invoker = new Invoker();

        // inizializzazione bottoni per la selezione forma
        ellipseButton.setOnAction(event -> setDrawMode(event, Forme.ELLISSE));
        rectangleButton.setOnAction(event -> setDrawMode(event, Forme.RETTANGOLO));
        lineButton.setOnAction(event -> setDrawMode(event, Forme.LINEA));
        selectButton.setOnAction(event -> setSelectMode());

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

    /**
     * Gestisce l'evento di pressione del mouse sul {@link Canvas}.
     * Mostra una finestra di dialogo di conferma che chiede all'utente se desidera inserire una figura
     * nel punto selezionato. Se l'utente conferma, il metodo delega la gestione dell'evento al
     * {@code drawingContext}, passando anche la lista delle forme {@code forme}, e aggiorna il canvas
     * richiamando {@code redrawAll()}.
     * @param mouseEvent -> evento generato dalla pressione del mouse sul canvas
     */
    private void handleMousePressed(MouseEvent mouseEvent) {
        System.out.println("Mouse pressed");
        drawingContext.handleMousePressed(mouseEvent, forme); // passa la forma da creare al DrawState
        redrawAll();
    }

    /**
     * Metodo che cancella il contenuto del {@link Canvas} e ridisegna tutte le forme presenti
     * nella lista {@code forme} utilizzando il {@link GraphicsContext}.
     * Chiamato dopo una modifica nel canvas per aggiornarne la visualizzazione.
     */
    void redrawAll() {
        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight()); // l'area da ripulire è tutto il canvas
        for (Forma f : forme) {
            f.disegna(gc);
        }
    }

    private void handleMouseDragged(MouseEvent mouseEvent) {
        System.out.println("Mouse dragged");
        drawingContext.handleMouseDragged(mouseEvent, forme); // passa la forma da creare al DrawState
        redrawAll();
    }

    private void handleMouseReleased(MouseEvent mouseEvent) {

    }

    /**
     * Metodo per passare alla modalità di disegno
     * @param event -> evento che ha causato la chiamata alla funzione
     * @param forma -> forma corrispondente al bottone che è stato premuto
     */
    void setDrawMode(ActionEvent event, Forme forma) {
        drawingContext.setCurrentState(new DrawState(forma), forme);
        redrawAll();
    }

    /**
     * Metodo per passare alla modalità di selezione
     */
    void setSelectMode() {
        drawingContext.setCurrentState(new SelectState(toolBarFX), forme);
        redrawAll();
    }

    /**
     * Metodo per salvare su file il disegno corrente
     * @param event -> evento che causa l'operazione di salvataggio
     */
    @FXML
    void onSavePressed(ActionEvent event) {
        invoker.setCommand(new SaveCommand(forme, stage));
        invoker.executeCommand();
    }

    /**
     * Metodo per caricare da file un disegno precedentemente salvato
     * @param event -> evento che causa l'operazione di caricamento
     */
    @FXML
    void onLoadPressed(ActionEvent event) {

    }

    /**
     * Metodo per eliminare una figura selezionata
     * @param event -> evento che causa l'operazione di elimina
     */
    @FXML
    void onDeletePressed(ActionEvent event) {
        invoker.setCommand(new DeleteCommand(forme));
        invoker.executeCommand();
        toolBarFX.setDisable(true); // disattiva la toolBar visto che la figura non è più in focus essendo eliminata
        redrawAll();
    }

    /**
     * Metodo per tagliare una figura selezionata
     * @param event -> evento che causa l'operazione di taglia
     */
    @FXML
    void onCutPressed(ActionEvent event) {
        invoker.setCommand(new CutCommand(forme));
        invoker.executeCommand();
        toolBarFX.setDisable(true);
        redrawAll();
    }

}