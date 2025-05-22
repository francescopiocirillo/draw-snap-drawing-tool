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
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.util.Iterator;
import java.util.List;


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
    private ImageView imageCopy;
    private ImageView imageCut;
    private ImageView imagePaste;

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
    private List<Button> bottoniBarraPrincipale = null;

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
        bottoniBarraPrincipale = List.of(ellipseButton, rectangleButton, lineButton, selectButton);
        ellipseButton.setOnAction(event -> {
            bottoniBarraPrincipale.forEach(btn -> btn.getStyleClass().remove("selected"));
            setDrawMode(event, Forme.ELLISSE);
            ellipseButton.getStyleClass().add("selected");
        });
        rectangleButton.setOnAction(event -> {
            bottoniBarraPrincipale.forEach(btn -> btn.getStyleClass().remove("selected"));
            setDrawMode(event, Forme.RETTANGOLO);
            rectangleButton.getStyleClass().add("selected");
        });
        lineButton.setOnAction(event -> {
            bottoniBarraPrincipale.forEach(btn -> btn.getStyleClass().remove("selected"));
            setDrawMode(event, Forme.LINEA);
            lineButton.getStyleClass().add("selected");
        });
        selectButton.setOnAction(event -> {
            bottoniBarraPrincipale.forEach(btn -> btn.getStyleClass().remove("selected"));
            setSelectMode();
            selectButton.getStyleClass().add("selected");
        });

        //inizializzazione menu contestuale per l'operazione di incolla
        contextMenu = new ContextMenu();
        copyButton = new MenuItem("Copia");
        cutButton = new MenuItem("Taglia");
        pasteButton = new MenuItem("Incolla");
        pasteButton.setOnAction(this::onPastePressed);
        copyButton.setOnAction(this::onCopyPressed);
        cutButton.setOnAction(this::onCutPressed);
        imageCopy = new ImageView(new Image(getClass().getResourceAsStream("/it/unisa/software_architecture_design/drawsnapdrawingtool/images/copy3.png")));
        imageCut = new ImageView(new Image(getClass().getResourceAsStream("/it/unisa/software_architecture_design/drawsnapdrawingtool/images/taglia.png")));
        imagePaste = new ImageView(new Image(getClass().getResourceAsStream("/it/unisa/software_architecture_design/drawsnapdrawingtool/images/paste.png")));
        imageCopy.setFitWidth(16);
        imageCut.setFitWidth(16);
        imagePaste.setFitWidth(16);
        imageCopy.setFitHeight(16);
        imageCut.setFitHeight(16);
        imagePaste.setFitHeight(16);
        copyButton.setGraphic(imageCopy);
        cutButton.setGraphic(imageCut);
        pasteButton.setGraphic(imagePaste);
        contextMenu.getItems().addAll(copyButton, cutButton, pasteButton);


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
     * Controlla se viene cliccato il tasto destro o il sinistro e si comporta di conseguenza
     * Se cliccato il tasto sinistro chiama {@code handleMousePressed()} del {@code drawingContext}
     * e ridisegna tutto richiamando {@code redrawAll()}.
     * Se cliccato il tasto destro mostra il menu contestuale con i bottoni di {@code pasteButton}
     * {@code copyButton} e {@code cutButton} a seconda delle casistiche
     * @param mouseEvent -> evento generato dalla pressione del mouse sul canvas
     */
    private void handleMousePressed(MouseEvent mouseEvent) {
        if (contextMenu.isShowing()) {
            contextMenu.hide();
        }
        if (mouseEvent.getButton() == MouseButton.PRIMARY) { //Click Sinistro
            lastClickX = mouseEvent.getX();
            lastClickY = mouseEvent.getY();
            drawingContext.handleMousePressed(mouseEvent, forme);
            redrawAll();
        } else if(mouseEvent.getButton() == MouseButton.SECONDARY) {//Click destro
            lastClickX = mouseEvent.getX();
            lastClickY = mouseEvent.getY();

            boolean hasSelection = forme.thereIsFormaSelezionata();//controlla se c'è una forma selezionata
            boolean hasClipboard = !forme.isEmptyFormeCopiate();//controlla se ci sono forme copiate precedentemente
            boolean clickInterno = false;
            copyButton.setDisable(false);
            cutButton.setDisable(false);
            pasteButton.setDisable(false);

            //Se c'è una forma selezionata controlla se il click è avvenuto all'interno di essa
            if(hasSelection){
                Forma formaSelezionata = forme.getFormaSelezionata();
                if(formaSelezionata!= null){
                    clickInterno = formaSelezionata.contiene(lastClickX, lastClickY);
                }
            }

            //Se vi è una forma selezionata e il click è avvenuto al suo interno mostra copia e taglia
            if(hasSelection && clickInterno){
                pasteButton.setDisable(!hasClipboard);
                copyButton.setDisable(false);
                cutButton.setDisable(false);

            }else{
                copyButton.setDisable(true);
                cutButton.setDisable(true);
                pasteButton.setDisable(!hasClipboard);
            }

            if(!contextMenu.getItems().isEmpty()){
                contextMenu.show(canvas, mouseEvent.getScreenX(), mouseEvent.getScreenY());
            }
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