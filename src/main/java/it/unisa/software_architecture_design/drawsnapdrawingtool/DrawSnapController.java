package it.unisa.software_architecture_design.drawsnapdrawingtool;

import it.unisa.software_architecture_design.drawsnapdrawingtool.commands.*;
import it.unisa.software_architecture_design.drawsnapdrawingtool.forme.*;
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
    private MenuItem composeButton;
    private ImageView imageCopy;
    private ImageView imageCut;
    private ImageView imagePaste;
    private ImageView imageCompose;

    /*
     * Attributi per lo zoom
     */
    @FXML
    private ComboBox<Double> zoom;

    @FXML
    private Slider gridSlider;

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
    private final Double[] zoomLevels = {1.25, 1.5, 1.75, 2.0};
    private int currentZoomIndex = 1;
    private boolean dragged = false;
    private final double baseCanvasWidth = 2048;
    private final double baseCanvasHeight = 2048;
    private boolean gridVisible = false;
    private double currentGridSize = 20.0;



    /**
     * Metodo di Inizializzazione dopo il caricamento del foglio fxml
     */
    @FXML
    void initialize() {
        canvas.setHeight(baseCanvasHeight);
        canvas.setWidth(baseCanvasWidth);
        gc = canvas.getGraphicsContext2D();
        drawingContext = new DrawingContext(new SelectState(toolBarFX, changeFillColorButton, composeButton));
        invoker = new Invoker();
        history = new DrawSnapHistory();

        scrollPane.setContent(canvas);

        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);


        // Ritarda l'applicazione della posizione iniziale del canvas al momento successivo al caricamento della UI
        Platform.runLater(() -> {
            invoker.setCommand(new ZoomCommand(canvas, scrollPane, baseCanvasWidth, baseCanvasHeight, zoomLevels[currentZoomIndex]));
            invoker.executeCommand();

            // Imposta la posizione di scorrimento iniziale al centro del canvas grande.
            // Questo è utile per iniziare la visualizzazione al centro dell'area di disegno.
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

        initializeGridSlider();
    }

    /**
     * Inizializza gli elementi grafici per lo {@code slider}
     */
    private void initializeGridSlider(){
        gridSlider.setValue(currentGridSize);
        gridSlider.valueProperty().addListener((observable, oldValue, newValue) -> {
            currentGridSize = newValue.doubleValue();
            redrawAll();
        });
        gridSlider.setVisible(false);
        gridSlider.setManaged(false);
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
                    setText(String.format("%.0f%%", (item-0.5) * 100));
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
                    setText(String.format("%.0f%%", (item-0.5) * 100));
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
        composeButton = new MenuItem("Componi");
        pasteButton.setOnAction(this::onPastePressed);
        copyButton.setOnAction(this::onCopyPressed);
        cutButton.setOnAction(this::onCutPressed);
        composeButton.setOnAction(this::onComposePressed);
        imageCopy = new ImageView(new Image(getClass().getResourceAsStream("/it/unisa/software_architecture_design/drawsnapdrawingtool/images/copia.png")));
        imageCut = new ImageView(new Image(getClass().getResourceAsStream("/it/unisa/software_architecture_design/drawsnapdrawingtool/images/taglia.png")));
        imagePaste = new ImageView(new Image(getClass().getResourceAsStream("/it/unisa/software_architecture_design/drawsnapdrawingtool/images/incolla.png")));
        imageCompose = new ImageView(new Image(getClass().getResourceAsStream("/it/unisa/software_architecture_design/drawsnapdrawingtool/images/incolla.png")));
        imageCopy.setFitWidth(16);
        imageCut.setFitWidth(16);
        imagePaste.setFitWidth(16);
        imageCompose.setFitWidth(16);
        imageCopy.setFitHeight(16);
        imageCut.setFitHeight(16);
        imagePaste.setFitHeight(16);
        imageCompose.setFitHeight(16);
        copyButton.setGraphic(imageCopy);
        cutButton.setGraphic(imageCut);
        pasteButton.setGraphic(imagePaste);
        composeButton.setGraphic(imageCompose);
        contextMenu.getItems().addAll(copyButton, cutButton, pasteButton, composeButton);
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void setModel(DrawSnapModel model) {
        this.forme = model;
    }

    /**
     * Inizializza gli event handler per gli eventi di interesse relativi al {@link Canvas}.
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
        double scale = canvas.getScaleX();

        if (contextMenu.isShowing()) {
            contextMenu.hide();
        }
        if (mouseEvent.getButton() == MouseButton.PRIMARY) { //Click Sinistro
            lastClickX = mouseEvent.getX() / zoomLevels[currentZoomIndex];
            lastClickY = mouseEvent.getY() / zoomLevels[currentZoomIndex];

            boolean stateChanged = drawingContext.handleMousePressed(mouseEvent, forme, lastClickX, lastClickY);
            updateState(stateChanged);
        } else if(mouseEvent.getButton() == MouseButton.SECONDARY) {//Click destro
            lastClickX = mouseEvent.getX() / zoomLevels[currentZoomIndex];
            lastClickY = mouseEvent.getY() / zoomLevels[currentZoomIndex];

            boolean hasSelection = forme.thereIsFormaSelezionata();
            boolean hasClipboard = !forme.isEmptyFormeCopiate();
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

            if(forme.countFormeSelezionate() > 1) {
                pasteButton.setDisable(true);
                copyButton.setDisable(true);
                cutButton.setDisable(true);
                composeButton.setDisable(false);
            }else{
                composeButton.setDisable(true);
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
        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());

        gc.save();
        gc.scale(zoomLevels[currentZoomIndex], zoomLevels[currentZoomIndex]);

        if(gridVisible){
            drawGrid(zoomLevels[currentZoomIndex], canvas.getWidth(), canvas.getHeight());
        }

        Iterator<Forma> it = forme.getIteratorForme();
        while (it.hasNext()) {
            Forma f = it.next();
            f.disegna(gc);
        }
        gc.restore();
    }

    /**
     * Metodo per disegnare la griglia all'interno del canvas
     * Nota: La griglia viene disegnata usando coordinate logiche,
     * la scala del GraphicsContext la renderà correttamente.
     */
    private void drawGrid(double currentZoomLevel, double canvasPhysicalWidth, double canvasPhysicalHeight) {
        gc.setStroke(Color.LIGHTGRAY);

        gc.setLineWidth(1.0 / currentZoomLevel);

        double maxLogicalX = canvasPhysicalWidth / currentZoomLevel;
        double maxLogicalY = canvasPhysicalHeight / currentZoomLevel;

        for (double x = 0; x <= maxLogicalX; x += currentGridSize) {
            gc.strokeLine(x, 0, x, maxLogicalY);
        }

        for (double y = 0; y <= maxLogicalY; y += currentGridSize) {
            gc.strokeLine(0, y, maxLogicalX, y);
        }
    }

    /**
     * Gestisce l'evento di mouse trascinato delegandone la gestione all'omonimo metodo di {@codeDrawingContext} che
     * a sua volta delega all'omonimo metodo della classe corrispondente allo stato attuale.
     * @param mouseEvent -> l'evento scatenante
     */
    private void handleMouseDragged(MouseEvent mouseEvent) {
        double logicalCurrentX = mouseEvent.getX() / zoomLevels[currentZoomIndex];
        double logicalCurrentY = mouseEvent.getY() / zoomLevels[currentZoomIndex];

        dragged = drawingContext.handleMouseDragged(mouseEvent, forme, logicalCurrentX, logicalCurrentY);
        updateState(false);
    }

    /**
     * Aggiornare il canvas durante il dragging è corretto ma il salvataggio del memento va effettuato solo al termine
     * dell'evento di drag, da cui la necessità di questo metodo e dell'attributo dragged che per ogni interazione con
     * il canvas conserva l'informazioni che dice se si tratta di un drag o meno
     * @param mouseEvent
     */
    private void handleMouseReleased(MouseEvent mouseEvent) {
        double logicalCurrentX = mouseEvent.getX() / zoomLevels[currentZoomIndex];
        double logicalCurrentY = mouseEvent.getY() / zoomLevels[currentZoomIndex];

        drawingContext.handleMouseReleased(mouseEvent, logicalCurrentX, logicalCurrentY);
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
        drawingContext.setCurrentState(new SelectState(toolBarFX, changeFillColorButton, composeButton), forme);
        canvas.setCursor(Cursor.DEFAULT);
        updateState(false);
    }

    /**
     * Metodo per passare alla modalità di panning (scorrimento)
     */
    void setMoveCanvasMode() {
        drawingContext.setCurrentState(new MoveCanvasState(canvas, scrollPane), forme);
        canvas.setCursor(Cursor.OPEN_HAND);
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
        toolBarFX.setDisable(true);
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
            invoker.setCommand(new ZoomCommand(canvas, scrollPane, baseCanvasWidth, baseCanvasHeight, zoomLevels[currentZoomIndex]));
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
            invoker.setCommand(new ZoomCommand(canvas, scrollPane, baseCanvasWidth, baseCanvasHeight, zoomLevels[currentZoomIndex]));
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
            invoker.setCommand(new ZoomCommand(canvas, scrollPane, baseCanvasWidth, baseCanvasHeight, zoomLevels[currentZoomIndex]));
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
        Forma tipoForma = forme.getFormaSelezionata();
        Forma forma = ((FormaSelezionataDecorator)tipoForma ).getForma();
        Label colorLabel = new Label("Colore di riempimento:");
        colorLabel.setStyle("-fx-font-size: 16px;");
        Color coloreAttuale = Color.BLACK;
        if ( forma instanceof Rettangolo ) {
            coloreAttuale = ((Rettangolo)forma).getColoreInterno();
        } else if ( forma instanceof Ellisse) {
            coloreAttuale = ((Ellisse)forma).getColoreInterno();
        }
        ColorPicker colorPicker = new ColorPicker(coloreAttuale); // Imposta colore di attuale

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
     * Metodo per invocare il comando di modifica del colore di contorno della figura
     * @param event -> evento di pressione del mouse sul tasto changeOutlineColor
     */
    @FXML
    void onChangeOutlineColorPressed(ActionEvent event) {
        // Creazione del Dialog
        Dialog<Color> dialog = new Dialog<>();
        dialog.setTitle("Seleziona Colore");
        dialog.setHeaderText("Vuoi cambiare il colore di contorno?");

        // Impostazione dell'interfaccia del dialog
        Forma tipoForma = forme.getFormaSelezionata();
        Forma forma = ((FormaSelezionataDecorator)tipoForma ).getForma();
        Label colorLabel = new Label("Colore di contorno:");
        colorLabel.setStyle("-fx-font-size: 16px;");
        ColorPicker colorPicker = new ColorPicker(forma.getColore()); // Imposta colore attuale

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
     * Metodo per invocare il comando di cambio delle dimensioni della figura
     * @param event -> evento di pressione del mouse sul tasto resize
     */
    @FXML
    public void onResizePressed(ActionEvent event) {
        Forma tipoForma = forme.getFormaSelezionata();
        Dialog<Map<String, Double>> dialog = new Dialog<>();
        dialog.setTitle("Ridimensiona Figura");
        dialog.setHeaderText("Vuoi cambiare le dimensioni della figura?");

        VBox contentBox = new VBox(15);
        contentBox.setPadding(new Insets(20));

        // Spinner per dimensioni
        Forma forma = ((FormaSelezionataDecorator)tipoForma ).getForma();
        double altezzaDefault = 0;
        double larghezzaDefault = forma.getLarghezza();;
        if ( forma instanceof Rettangolo ) {
            altezzaDefault = ((Rettangolo)forma).getAltezza();
        } else if ( forma instanceof Ellisse) {
            altezzaDefault = ((Ellisse)forma).getAltezza();
        }
        Spinner<Double> spinnerLarghezza = new Spinner<>(10.0, 500.0, larghezzaDefault, 1.0); //imposta dimensioni attuali
        Spinner<Double>  spinnerAltezza = new Spinner<>(10.0, 500.0, altezzaDefault, 1.0); //imposta dimensioni attuali

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
                Map<String, Double> map = new HashMap<>();
                if (( (FormaSelezionataDecorator)tipoForma ).getForma() instanceof Linea) {
                    double lunghezza = spinnerLarghezza.getValue();
                    map.put("Lunghezza", lunghezza);
                    System.out.println("Nuova Lunghezza: " + lunghezza);
                } else {
                    double altezza = spinnerAltezza.getValue();
                    double larghezza = spinnerLarghezza.getValue();
                    map.put("Larghezza", larghezza);
                    map.put("Altezza", altezza);
                    System.out.println("Nuova Altezza: " + altezza);
                    System.out.println("Nuova Larghezza: " + larghezza);
                }
                return map;
            }
            return null;
        });

        // Mostra il dialog e gestisce il risultato
        Optional<Map<String, Double>> result = dialog.showAndWait();
        result.ifPresent(nuoveDimensioni -> {
            if (( (FormaSelezionataDecorator)tipoForma ).getForma() instanceof Linea) {
                invoker.setCommand(new ResizeCommand(forme, nuoveDimensioni.get("Lunghezza"), 0));
                invoker.executeCommand();
                updateState(true);
            } else {
                invoker.setCommand(new ResizeCommand(forme, nuoveDimensioni.get("Larghezza"), nuoveDimensioni.get("Altezza")));
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
        gridSlider.setVisible(gridVisible);
        gridSlider.setManaged(gridVisible);
        updateState(false);
    }

    @FXML
    public void onComposePressed(ActionEvent event) {
        System.out.println("Composizione di forme selezionate");
        return ;
        /*
        if (forme.thereIsFormaSelezionata()) {
            invoker.setCommand(new ComposeCommand(forme));
            invoker.executeCommand();
            updateState(true);
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Attenzione");
            alert.setHeaderText("Nessuna forma selezionata");
            alert.setContentText("Per comporre una forma, seleziona almeno due forme.");
            alert.showAndWait();
        }*/
    }

    /**
     * Metodo per invocare il comando di cambio dell'angolo di inclinazione della figura
     * @param event -> evento di pressione del mouse sul tasto Rotation
     */
    @FXML
    void onRotationPressed(ActionEvent event){
        // Creazione del Dialog
        Dialog<Double> dialog = new Dialog<>();
        dialog.setTitle("Modifica Angolo di Inclinazione");
        dialog.setHeaderText("Vuoi cambiare l'angolo di inclinazione?");

        // Impostazione dell'interfaccia del dialog
        Forma tipoForma = forme.getFormaSelezionata();
        Forma forma = ((FormaSelezionataDecorator) tipoForma).getForma();
        Label angleLabel = new Label("Angolo di inclinazione:");
        angleLabel.setStyle("-fx-font-size: 16px;");

        // Recupera l'angolo attuale
        double angoloAttuale = forma.getAngoloInclinazione();

        // Creazione dello Spinner
        Spinner<Double> angleSpinner = new Spinner<>();
        SpinnerValueFactory<Double> valueFactory = new SpinnerValueFactory.DoubleSpinnerValueFactory(0, 360, angoloAttuale, 1);
        angleSpinner.setValueFactory(valueFactory);
        angleSpinner.setEditable(true); // Permette l'input da tastiera

        VBox dialogContent = new VBox(10, angleLabel, angleSpinner);
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
                double angoloAggiornato = angleSpinner.getValue();
                System.out.println("Angolo scelto: " + angoloAggiornato);
                return angoloAggiornato; // Restituisce l'angolo scelto
            }
            System.out.println("Angolo non scelto");
            return null; // Nessun angolo scelto
        });

        // Gestione del risultato del dialog
        Optional<Double> result = dialog.showAndWait();
        result.ifPresent(angoloAggiornato -> {
            invoker.setCommand(new RotationCommand(forme, angoloAggiornato));
            invoker.executeCommand();
            updateState(true);
        });
    }


}