package it.unisa.software_architecture_design.drawsnapdrawingtool;

import it.unisa.software_architecture_design.drawsnapdrawingtool.commands.*;
import it.unisa.software_architecture_design.drawsnapdrawingtool.forme.Forma;
import it.unisa.software_architecture_design.drawsnapdrawingtool.interactionstate.DrawState;
import it.unisa.software_architecture_design.drawsnapdrawingtool.interactionstate.DrawingContext;
import it.unisa.software_architecture_design.drawsnapdrawingtool.interactionstate.SelectState;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import it.unisa.software_architecture_design.drawsnapdrawingtool.enumeration.*;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.util.Iterator;


public class DrawSnapController {

    /*
     * Attributi per il foglio di disegno
     */
    @FXML
    private Canvas canvas;
    @FXML
    private AnchorPane canvasContainer;
    @FXML
    private ScrollPane scrollPane;
    private GraphicsContext gc;
    private DrawingContext drawingContext;
    private DrawSnapModel forme = null;
    private Stage stage;

    //Menù contestuale
    private ContextMenu contextMenu;
    private MenuItem pasteButton;
    private MenuItem copyButton;
    private MenuItem cutButton;

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
    private double lastClickX = -1;
    private double lastClickY = -1;


    /**
     * Metodo di Inizializzazione dopo il caricamento del foglio fxml
     */
    @FXML
    void initialize() {
        gc = canvas.getGraphicsContext2D();
        drawingContext = new DrawingContext(new SelectState(toolBarFX)); // stato di default, sarà cambiato quando avremo lo stato sposta o seleziona
        invoker = new Invoker();

        double canvasWidth = 4096;
        double canvasHeight = 4096;
        canvasContainer.setPrefSize(canvasWidth, canvasHeight);
        // Ritarda l'applicazione ella posizione iniziale del canvas al momento successivo al caricamento della UI
        Platform.runLater(() -> {
            scrollPane.setHvalue(0.5);
            scrollPane.setVvalue(0.5);
        });

        // inizializzazione bottoni per la selezione forma
        ellipseButton.setOnAction(event -> setDrawMode(event, Forme.ELLISSE));
        rectangleButton.setOnAction(event -> setDrawMode(event, Forme.RETTANGOLO));
        lineButton.setOnAction(event -> setDrawMode(event, Forme.LINEA));
        selectButton.setOnAction(event -> setSelectMode());

        //inizializzazione menu contestuale per l'operazione di incolla
        contextMenu = new ContextMenu();
        copyButton = new MenuItem("Copia");
        cutButton = new MenuItem("Taglia");
        pasteButton = new MenuItem("Incolla");
        pasteButton.setOnAction(this::onPastePressed);
        copyButton.setOnAction(this::onCopyPressed);
        cutButton.setOnAction(this::onCutPressed);


        initializeCanvasEventHandlers();
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void setModel(DrawSnapModel model) {
        this.forme = model;
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
        if (contextMenu.isShowing()) {
            contextMenu.hide();
        }
        if (mouseEvent.getButton() == MouseButton.PRIMARY) { //tasto sinistro
            System.out.println("Clic sinistro");
            lastClickX = mouseEvent.getX();
            lastClickY = mouseEvent.getY();
            drawingContext.handleMousePressed(mouseEvent, forme);
            redrawAll();
        } else if (mouseEvent.getButton() == MouseButton.SECONDARY && !forme.isEmptyFormeCopiate() && !forme.thereIsFormaSelezionata()) { //tasto destro
            System.out.println("Clic destro");
            lastClickX = mouseEvent.getX();
            lastClickY = mouseEvent.getY();
            contextMenu.getItems().clear();
            contextMenu.getItems().add(pasteButton);
            contextMenu.show(canvas, mouseEvent.getScreenX(), mouseEvent.getScreenY());
        }else if (mouseEvent.getButton() == MouseButton.SECONDARY && forme.thereIsFormaSelezionata()){
            System.out.println("Clic destro");
            lastClickX = mouseEvent.getX();
            lastClickY = mouseEvent.getY();
            contextMenu.getItems().clear();
            contextMenu.getItems().addAll(copyButton, cutButton);
            contextMenu.show(canvas, mouseEvent.getScreenX(), mouseEvent.getScreenY());
        }
    }

    /**
     * Metodo che cancella il contenuto del {@link Canvas} e ridisegna tutte le forme presenti
     * nella lista {@code forme} utilizzando il {@link GraphicsContext}.
     * Chiamato dopo una modifica nel canvas per aggiornarne la visualizzazione.
     */
    void redrawAll() {
        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight()); // l'area da ripulire è tutto il canvas
        Iterator<Forma> it = forme.getIteratorForme();
        while (it.hasNext()) {
            Forma f = it.next();
            f.disegna(gc);
        }
    }


    private void handleMouseDragged(MouseEvent mouseEvent) {
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
        invoker.setCommand(new LoadCommand(forme, stage));
        invoker.executeCommand();
        redrawAll();
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
    void onCutPressed(ActionEvent event) {
        invoker.setCommand(new CutCommand(forme));
        invoker.executeCommand();
        toolBarFX.setDisable(true);
        redrawAll();
    }

    /**
     * Metodo per copiare una figura selezionata
     * @param event -> evento che causa l'operazione di copia
     */
    void onCopyPressed(ActionEvent event) {
        invoker.setCommand(new CopyCommand(forme));
        invoker.executeCommand();
        toolBarFX.setDisable(true);
        redrawAll();
    }

    /**
     * Metodo per incollare una figura selezionata
     * @param event -> evento che causa l'operazione di incolla
     */
    void onPastePressed(ActionEvent event) {
        invoker.setCommand(new PasteCommand(forme, lastClickX, lastClickY));
        invoker.executeCommand();
        toolBarFX.setDisable(true);
        redrawAll();
    }



}