package it.unisa.software_architecture_design.drawsnapdrawingtool;

import it.unisa.software_architecture_design.drawsnapdrawingtool.commands.*;
import it.unisa.software_architecture_design.drawsnapdrawingtool.forme.Forma;
import it.unisa.software_architecture_design.drawsnapdrawingtool.forme.FormaSelezionataDecorator;
import it.unisa.software_architecture_design.drawsnapdrawingtool.forme.Linea;
import it.unisa.software_architecture_design.drawsnapdrawingtool.interactionstate.DrawState;
import it.unisa.software_architecture_design.drawsnapdrawingtool.interactionstate.DrawingContext;
import it.unisa.software_architecture_design.drawsnapdrawingtool.interactionstate.MoveCanvasState;
import it.unisa.software_architecture_design.drawsnapdrawingtool.interactionstate.SelectState;
import it.unisa.software_architecture_design.drawsnapdrawingtool.memento.DrawSnapHistory;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
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
    private ScrollBar scrollBarH;
    @FXML
    private ScrollBar scrollBarV;
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
    private Button handButton;
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
    @FXML
    private Button changeFillColorButton;
    @FXML
    private Button undoButton;
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
    private final double canvasWidth = 4096;
    private final double canvasHeight = 4096;
    private boolean gridVisible = false;


    /**
     * Metodo di Inizializzazione dopo il caricamento del foglio fxml
     */
    @FXML
    void initialize() {
        gc = canvas.getGraphicsContext2D();
        drawingContext = new DrawingContext(new SelectState(toolBarFX, changeFillColorButton)); // stato di default, sarà cambiato quando avremo lo stato sposta o seleziona
        invoker = new Invoker();
        history = new DrawSnapHistory();


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
        bottoniBarraPrincipale = List.of(handButton, ellipseButton, rectangleButton, lineButton, selectButton);
        handButton.setOnAction(e -> {
            bottoniBarraPrincipale.forEach(btn -> btn.getStyleClass().remove("selected"));
            setMoveCanvasMode();
            handButton.getStyleClass().add("selected");
        });
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

        undoButton.setDisable(true);

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
                zoomImage.setFitWidth(24);
                zoomImage.setFitHeight(24);
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
        imageCopy = new ImageView(new Image(getClass().getResourceAsStream("/it/unisa/software_architecture_design/drawsnapdrawingtool/images/copia.png")));
        imageCut = new ImageView(new Image(getClass().getResourceAsStream("/it/unisa/software_architecture_design/drawsnapdrawingtool/images/taglia.png")));
        imagePaste = new ImageView(new Image(getClass().getResourceAsStream("/it/unisa/software_architecture_design/drawsnapdrawingtool/images/incolla.png")));
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
            undoButton.setDisable(false);
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

        if(gridVisible){
            drawGrid();
        }

        Iterator<Forma> it = forme.getIteratorForme();
        while (it.hasNext()) {
            Forma f = it.next();
            f.disegna(gc);
        }
    }

    /**
     * Metodo per disegnare la griglia all'interno del canvas
     */
    void drawGrid(){
        gc.setStroke(Color.LIGHTGRAY);
        gc.setLineWidth(1);

        int spaziatura = 20;

        for(int i = 0; i < canvasWidth; i+=spaziatura){
            gc.strokeLine(i, 0, i, canvasHeight);
        }

        for(int i = 0; i<canvasHeight; i+=spaziatura){
            gc.strokeLine(0, i, canvasWidth, i);
        }
    }

    /**
     * Gestisce l'evento di mouse trascinato delegandone la gestione all'omonimo metodo di {@codeDrawingContext} che
     * a sua volta delega all'omonimo metodo della classe corrispondente allo stato attuale.
     * @param mouseEvent -> l'evento scatenante
     */
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
        canvas.setCursor(Cursor.DEFAULT);
        updateState(false);
    }

    /**
     * Metodo per passare alla modalità di selezione
     */
    void setSelectMode() {
        drawingContext.setCurrentState(new SelectState(toolBarFX, changeFillColorButton), forme);
        canvas.setCursor(Cursor.DEFAULT);
        updateState(false);
    }

    /**
     * Metodo per passare alla modalità di panning (scorrimento)
     */
    void setMoveCanvasMode() {
        drawingContext.setCurrentState(new MoveCanvasState(scrollPane), forme);
        canvas.setCursor(Cursor.HAND);
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
    void onZoomChangePressed(ActionEvent event) {
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
    void onZoomInPressed(ActionEvent event) {
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
    void onZoomOutPressed(ActionEvent event) {
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
        toolBarFX.setDisable(true);
        if(history.isEmpty()){
            undoButton.setDisable(true);
        }
        canvas.requestFocus();
    }

    /**
     * Metodo per invocare il comando di cambio del colore interno della figura
     * @param event -> evento di pressione del mouse sul tasto changeFillColor
     */
    @FXML
    void onChangeFillColorPressed(ActionEvent event) {
        // Creazione del Dialog
        Dialog<Color> dialog = new Dialog<>();
        dialog.setTitle("Seleziona Colore");
        dialog.setHeaderText("Vuoi cambiare il colore di riempimento?");

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
        });
    }

    /**
     * Metodo per invocare il comando di ripristino dello stato precedente dell'applicazione
     * @param event -> evento di pressione del mouse sul tasto undo
     */
    @FXML
    void onChangeOutlineColorPressed(ActionEvent event) {
        // Creazione del Dialog
        Dialog<Color> dialog = new Dialog<>();
        dialog.setTitle("Seleziona Colore");
        dialog.setHeaderText("Vuoi cambiare il colore di contorno?");

        // Impostazione dell'interfaccia del dialog
        Label colorLabel = new Label("Colore di contorno:");
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
            invoker.setCommand(new ChangeOutlineColorCommand(forme, coloreSelezionato));
            invoker.executeCommand();
            updateState(true);
            System.out.println("Colore selezionato: " + coloreSelezionato.toString());
        });
    }

    /**
     *
     * @param event
     */
    @FXML
    public void onResizePressed(ActionEvent event) {
        Forma tipoForma = forme.getFormaSelezionata();
        Dialog<Void> dialog = new Dialog<>();
        dialog.setTitle("Ridimensiona Figura");
        dialog.setHeaderText("Vuoi cambiare le dimensioni della figura?");

        VBox contentBox = new VBox(15);
        contentBox.setPadding(new Insets(20));

        // Spinner per dimensioni
        Spinner<Double> spinnerAltezza = new Spinner<>(10.0, 500.0, 100.0, 1.0);
        Spinner<Double> spinnerLarghezza = new Spinner<>(10.0, 500.0, 100.0, 1.0);

        spinnerAltezza.setEditable(true);
        spinnerLarghezza.setEditable(true);

        VBox dimensioniBox = new VBox(10);
        dimensioniBox.setAlignment(Pos.CENTER);

        if ( ( (FormaSelezionataDecorator)tipoForma ).getForma() instanceof Linea ) {
            System.out.println("tipoForma: " + tipoForma);
            dimensioniBox.getChildren().addAll(
                    new Label("Lunghezza:"), spinnerLarghezza
            );
        } else {
            dimensioniBox.getChildren().addAll(
                    new Label("Altezza:"), spinnerAltezza,
                    new Label("Larghezza:"), spinnerLarghezza
            );
        }

        contentBox.getChildren().add(dimensioniBox);
        dialog.getDialogPane().setContent(contentBox);
        dialog.getDialogPane().setMinWidth(400);

        // Pulsanti di conferma e annullamento
        ButtonType confirmButton = new ButtonType("Conferma", ButtonBar.ButtonData.OK_DONE);
        ButtonType cancelButton = new ButtonType("Annulla", ButtonBar.ButtonData.CANCEL_CLOSE);
        dialog.getDialogPane().getButtonTypes().addAll(confirmButton, cancelButton);

        // Impostazione del comportamento alla conferma
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == confirmButton) {
                if (tipoForma instanceof Linea) {
                    double lunghezza = spinnerLarghezza.getValue();
                    invoker.setCommand(new ResizeCommand(forme, spinnerLarghezza.getValue(), 0));
                    invoker.executeCommand();
                    updateState(true);
                    System.out.println("Nuova Lunghezza: " + lunghezza);
                } else {
                    double altezza = spinnerAltezza.getValue();
                    double larghezza = spinnerLarghezza.getValue();
                    invoker.setCommand(new ResizeCommand(forme, spinnerLarghezza.getValue(), spinnerAltezza.getValue()));
                    invoker.executeCommand();
                    updateState(true);
                    System.out.println("Nuova Altezza: " + altezza);
                    System.out.println("Nuova Larghezza: " + larghezza);
                }
            }
            return null;
        });

        // Mostra il dialog e gestisce il risultato
        Optional<Void> result = dialog.showAndWait();
        result.ifPresent(ignored -> {
            if (tipoForma instanceof Linea) {
                invoker.setCommand(new ResizeCommand(forme, spinnerLarghezza.getValue(), 0));
                invoker.executeCommand();
                updateState(true);
            } else {
                invoker.setCommand(new ResizeCommand(forme, spinnerLarghezza.getValue(), spinnerAltezza.getValue()));
                invoker.executeCommand();
                updateState(true);
            }
        });
    }

    /**
     * Metodo perrendere la griglia visibile e permetterne il disegno
     * @param event -> evento che ne causa la visualizzazione
     */
    @FXML
    public void onGridPressed(ActionEvent event) {
        gridVisible = !gridVisible;
        updateState(false);
    }
}