package it.unisa.software_architecture_design.drawsnapdrawingtool;

import it.unisa.software_architecture_design.drawsnapdrawingtool.commands.*;
import it.unisa.software_architecture_design.drawsnapdrawingtool.forme.Forma;
import it.unisa.software_architecture_design.drawsnapdrawingtool.interactionstate.DrawState;
import it.unisa.software_architecture_design.drawsnapdrawingtool.interactionstate.DrawingContext;
import it.unisa.software_architecture_design.drawsnapdrawingtool.interactionstate.SelectState;
import it.unisa.software_architecture_design.drawsnapdrawingtool.memento.DrawSnapHistory;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import it.unisa.software_architecture_design.drawsnapdrawingtool.enumeration.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.util.*;


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

    //Attributi barra secondaria
    @FXML
    private ComboBox<Double> zoom;

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
    private DrawSnapHistory history = null;
    private Invoker invoker = null;
    private double lastClickX = -1;
    private double lastClickY = -1;
    private final Double[] zoomLevels = {0.5, 1.0, 1.5, 2.0};
    private int currentZoomIndex = 1;
    private boolean dragged = false;



    /**
     * Metodo di Inizializzazione dopo il caricamento del foglio fxml
     */
    @FXML
    void initialize() {
        gc = canvas.getGraphicsContext2D();
        drawingContext = new DrawingContext(new SelectState(toolBarFX)); // stato di default, sarà cambiato quando avremo lo stato sposta o seleziona
        invoker = new Invoker();
        history = new DrawSnapHistory();

        double canvasWidth = 4096;
        double canvasHeight = 4096;
        canvasContainer.setPrefSize(canvasWidth, canvasHeight);
        // Ritarda l'applicazione ella posizione iniziale del canvas al momento successivo al caricamento della UI
        Platform.runLater(() -> {
            scrollPane.setHvalue(0.5);
            scrollPane.setVvalue(0.5);
            // in questo modo si può scegliere manualmente quale bottone deve essere in focus al caricamento
            // dell'app, altrimenti JavaFX mette in focus il primo controllo che rileva
            canvas.requestFocus();
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
        selectButton.getStyleClass().add("selected");

        initializeContextMenu();

        initializeZoom();

        initializeCanvasEventHandlers();
    }

    /**
     * Inizializza gli elementi grafici per lo {@code zoom}
     */
    private void initializeZoom(){
        zoom.getItems().addAll(zoomLevels);
        zoom.setValue(zoomLevels[currentZoomIndex]);
        zoom.setButtonCell(new ListCell<>(){
            private final ImageView zoomImage = new ImageView(new Image(getClass().getResourceAsStream("/it/unisa/software_architecture_design/drawsnapdrawingtool/images/zoom.png")));
            {
                zoomImage.setFitWidth(16);
                zoomImage.setFitHeight(16);
            }

            @Override
            protected void updateItem(Double item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                    setGraphic(null);
                }else{
                    setText((int) (item*100) + "%");
                    setGraphic(zoomImage);
                }
            }
        });

        zoom.setCellFactory(lv -> new ListCell<Double>(){
            @Override
            protected void updateItem(Double item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                }else{
                    setText(String.format("%.0f%%", item*100));
                }
            }
        });
    }

    /**
     * Inizializza gli elementi grafici del menu contestuale
     */
    private void initializeContextMenu() {
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
        // alla pressione del mouse si suppone sempre che non si tratta di un drag, solo all'interno del metodo
        // di drag la flag viene asserita
        dragged = false;

        if (contextMenu.isShowing()) {
            contextMenu.hide();
        }
        if (mouseEvent.getButton() == MouseButton.PRIMARY) { //Click Sinistro
            lastClickX = mouseEvent.getX();
            lastClickY = mouseEvent.getY();
            boolean stateChanged = drawingContext.handleMousePressed(mouseEvent, forme);
            updateState(stateChanged);
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
     * Metodo che salva lo stato dopo un cambiamento e poi ricarica il canvas
     * @param stateChanged boolean che decide se lo stato va salvato o meno
     */
    void updateState(boolean stateChanged){
        if(stateChanged){
            history.saveState(forme.saveToMemento());
        }
        redrawAll();
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
        dragged = drawingContext.handleMouseDragged(mouseEvent, forme); // passa la forma da creare al DrawStateù
        updateState(false);
    }

    /**
     * Aggiornare il canvas durante il dragging è corretto ma il salvataggio del memento va effettuato solo al termine
     * dell'evento di drag, da cui la necessità di questo metodo e dell'attributo dragged che per ogni interazione con
     * il canvas conserva l'informazioni che dice se si tratta di un drag o meno
     * @param mouseEvent
     */
    private void handleMouseReleased(MouseEvent mouseEvent) {
        if(dragged){
            updateState(dragged);
        }
    }

    /**
     * Metodo per passare alla modalità di disegno
     * @param event -> evento che ha causato la chiamata alla funzione
     * @param forma -> forma corrispondente al bottone che è stato premuto
     */
    void setDrawMode(ActionEvent event, Forme forma) {
        drawingContext.setCurrentState(new DrawState(forma), forme);
        updateState(false);
    }

    /**
     * Metodo per passare alla modalità di selezione
     */
    void setSelectMode() {
        drawingContext.setCurrentState(new SelectState(toolBarFX), forme);
        updateState(false);
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
        updateState(true);
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
        updateState(true);
    }

    /**
     * Metodo per tagliare una figura selezionata
     * @param event -> evento che causa l'operazione di taglia
     */
    void onCutPressed(ActionEvent event) {
        invoker.setCommand(new CutCommand(forme));
        invoker.executeCommand();
        toolBarFX.setDisable(true);
        updateState(true);
    }

    /**
     * Metodo per copiare una figura selezionata
     * @param event -> evento che causa l'operazione di copia
     */
    void onCopyPressed(ActionEvent event) {
        invoker.setCommand(new CopyCommand(forme));
        invoker.executeCommand();
        toolBarFX.setDisable(true);
        updateState(false);
    }

    /**
     * Metodo per incollare una figura selezionata
     * @param event -> evento che causa l'operazione di incolla
     */
    void onPastePressed(ActionEvent event) {
        invoker.setCommand(new PasteCommand(forme, lastClickX, lastClickY));
        invoker.executeCommand();
        toolBarFX.setDisable(true);
        updateState(true);
    }

    /**
     * Metodo per mettere in primo piano la figura selezionata
     * @param event -> evento che causa l'operazione di incolla
     */
    @FXML
    void onBackToFrontPressed(ActionEvent event) {
        invoker.setCommand(new BackToFrontCommand(forme));
        invoker.executeCommand();
        updateState(true);
    }

    /**
     * Metodo per mettere in secondo piano la figura selezionata
     * @param event -> evento che causa l'operazione di messa in secondo piano
     */
    @FXML
    void onFrontToBackPressed(ActionEvent event) {
        invoker.setCommand(new FrontToBackCommand(forme));
        invoker.executeCommand();
        updateState(true);
    }

    /**
     * Metodo per modificare lo zoom con il livello selezionato
     * @param event -> evento che causa il cambio di zoom
     */
    @FXML
    void zoomChangePressed(ActionEvent event) {
        int selectedIndex = zoom.getSelectionModel().getSelectedIndex();
        if(selectedIndex >= 0){
            currentZoomIndex = selectedIndex;
            invoker.setCommand(new ZoomCommand(canvas, zoomLevels, currentZoomIndex));
            invoker.executeCommand();
            updateState(false);
        }
    }

    /**
     * Metodo per aumentare lo zoom di un livello
     * @param event -> evento che causa l'aumento di zoom
     */
    @FXML
    void zoomInPressed(ActionEvent event) {
        if(currentZoomIndex < zoomLevels.length - 1 ){
            currentZoomIndex++;
            invoker.setCommand(new ZoomCommand(canvas, zoomLevels, currentZoomIndex));
            invoker.executeCommand();
            zoom.getSelectionModel().select(currentZoomIndex);
            updateState(false);
        }
    }

    /**
     * Metodo per diminuire lo zoom di un livello
     * @param event -> evento che causa la riduzione di zoom
     */
    @FXML
    void zoomOutPressed(ActionEvent event) {
        if(currentZoomIndex > 0){
            currentZoomIndex--;
            invoker.setCommand(new ZoomCommand(canvas, zoomLevels, currentZoomIndex));
            invoker.executeCommand();
            zoom.getSelectionModel().select(currentZoomIndex);
            updateState(false);
        }
    }

    /**
     * Metodo per invocare il comando di ripristino dello stato precedente dell'applicazione
     * @param event -> evento di pressione del mouse sul tasto undo
     */
    @FXML
    void onUndoPressed(ActionEvent event) {
        invoker.setCommand(new UndoCommand(forme, history));
        invoker.executeCommand();
        updateState(false);
        canvas.requestFocus();
    }

    /**
     * Metodo per invocare il comando di ripristino dello stato precedente dell'applicazione
     * @param event -> evento di pressione del mouse sul tasto undo
     */
    @FXML
    void changeFillColorPressed(ActionEvent event) {
        // Creazione del Dialog
        Dialog<Color> dialog = new Dialog<>();
        dialog.setTitle("Seleziona Colore");
        dialog.setHeaderText("Scegli un colore per il riempimento:");

        // Impostazione dell'interfaccia del dialog
        Label colorLabel = new Label("Colore di riempimento:");
        colorLabel.setStyle("-fx-font-size: 16px;");
        ColorPicker colorPicker = new ColorPicker(Color.WHITE); // Imposta colore di default

        VBox dialogContent = new VBox(10, colorLabel, colorPicker);
        dialogContent.setAlignment(Pos.CENTER);
        dialogContent.setPadding(new Insets(20));

        dialog.getDialogPane().setContent(dialogContent);
        dialog.getDialogPane().setMinWidth(300);
        dialog.getDialogPane().setMinHeight(200);

        // Aggiunta dei pulsanti OK e Annulla
        ButtonType confirmButton = new ButtonType("Conferma", ButtonBar.ButtonData.OK_DONE);
        ButtonType cancelButton = new ButtonType("Annulla", ButtonBar.ButtonData.CANCEL_CLOSE);
        dialog.getDialogPane().getButtonTypes().addAll(confirmButton, cancelButton);

        // Impostazione del comportamento alla conferma
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == confirmButton) {
                System.out.println("colore scelto: " + colorPicker.getValue());
                return colorPicker.getValue(); // Restituisce il colore scelto
            }
            System.out.println("colore non scelto");
            return null; // Nessun colore scelto
        });

        // Mostra il dialog e gestisce il risultato
        Optional<Color> result = dialog.showAndWait();
        result.ifPresent(coloreSelezionato -> {
            System.out.println("azione colore scelto");
            invoker.setCommand(new ChangeFillColorCommand(forme, coloreSelezionato));
            invoker.executeCommand();
            updateState(true);
            System.out.println("Colore selezionato: " + coloreSelezionato.toString());
            // Esempio: applica il colore a una figura
            // figura.setFill(coloreSelezionato);
            /**
             * il Model deve avere un metodo che prende un colore e lo assegna alla figura selezionata
             * un Command tipo changeFillColorCommand, alla creazione si prende il colore nuovo e il Model
             * e quando viene chiamato execute chiama il metodo del Model dandogli il colore
             */
        });
    }

}