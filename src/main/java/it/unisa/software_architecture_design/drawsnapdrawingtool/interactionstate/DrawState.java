package it.unisa.software_architecture_design.drawsnapdrawingtool.interactionstate;

import it.unisa.software_architecture_design.drawsnapdrawingtool.DrawSnapModel;
import it.unisa.software_architecture_design.drawsnapdrawingtool.enumeration.Forme;
import it.unisa.software_architecture_design.drawsnapdrawingtool.forme.*;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.control.ColorPicker;


import java.util.List;
import java.util.Locale;
import java.util.Optional;

/**
 * La classe {@code DrawState} rappresenta lo stato di disegno e per mezzo del Pattern State
 * si occupa di gestire la logica degli handler relativi agli eventi di interazione con
 * il canvas nello stato di disegno. Vi è una distinzione tra le diverse figure per mezzo di un
 * enum. Si occupa di fornire anche la finestra di dialogo per la scelta dei colori e la creazione
 * di una preview della figura per una corretta implementazione del drag & drop.
 */
public class DrawState implements DrawingState{
    /*
     * Attributi
     */
    private Forme formaCorrente; //Enum della figura corrente che si intende creare
    private boolean creazionePoligono=false; //Booleano che indica che vi è un poligono in creazione
    private FactoryPoligono factoryPoligono; //Il factory da usare per la creazione del poligono
    private AttributiForma attributiForma; //Gli attributi per la creazione della figura
    private Forma currentDrawingShapePreview = null; //Preview dellac forma da disegnare
    private double startX; //Punto di inizio (coordinata X) per il drag & drop
    private double startY; //Punto di inizio (coordinata Y) per il drag & drop
    private boolean dialogShown = false; //Booleano per indicare che la finestra di dialogo è stata già mostrata

    /*
     * Costruttore, getter e setter (Alcuni inutilizzati)
     */
    public DrawState(Forme formaCorrente) {
        this.formaCorrente = formaCorrente;
        System.out.println(formaCorrente);
    }

    public Forme getFormaCorrente() {
        return formaCorrente;
    }

    public void setFormaCorrente(Forme formaCorrente) {
        this.formaCorrente = formaCorrente;
    }

    public boolean getCreazionePoligono(){
        return creazionePoligono;
    }

    public void setCreazionePoligono(boolean creazionePoligono){
        this.creazionePoligono=creazionePoligono;
    }

    public Forma getCurrentDrawingShapePreview() {
        return currentDrawingShapePreview;
    }

    public void setCurrentDrawingShapePreview(Forma currentDrawingShapePreview) {
        this.currentDrawingShapePreview = currentDrawingShapePreview;
    }

    /*
     * Logica della classe
     */

    /**
     * Gestisce l'evento di pressione del mouse sul canvas in modo da iniziare il
     * processo di creazione della {@code formaCorrente}
     * @param event è l'evento di pressione del mouse
     * @param forme è la lista di tutte le forme presenti sul canvas (forse da togliere)
     * @param coordinataX è la coordinata logica per l'asse x dell'evento mouse
     * @param coordinataY è la coordinata logica per l'asse y dell'evento mouse
     * @return {@code true} se gli attributi sono stati ottenuti correttemante e il processo
     * di "Inizio Creazione" è avvenuto correttamente, altrimenti {@code false}
     */
    @Override
    public boolean handleMousePressed(MouseEvent event, DrawSnapModel forme, double coordinataX, double coordinataY) {
        //Caso Poligono
        if(formaCorrente == Forme.POLIGONO){

            //Se il poligono non è stato ancora creato lo si inizia a creare definendo il factory (da correggere in Builder mi sa)
            if(!creazionePoligono){

                //Visione della finestra di dialogo
                if(!dialogShown) {
                    attributiForma = helpUIHandleMousePressed(formaCorrente);
                    if (attributiForma == null) {
                        return false;
                    }
                    dialogShown = true;
                }

                //Creazione del factory (builder)
                factoryPoligono = new FactoryPoligono();
                creazionePoligono=true;
            }else {

                //Distinzione tra aggiunta di un punto e fine creazione tramite il numero di click
                if (event.getClickCount() == 1) { //1 click: aggiunta punto
                    factoryPoligono.addPunto(coordinataX, coordinataY);
                    return false;
                } else if (event.getClickCount() == 2 && factoryPoligono.getSize() > 2) { //2 click: fine creazione
                    Forma formaCreata = createShapePreview(coordinataX, coordinataY);

                    //Aggiunta alla lista di forme
                    if (formaCreata != null) {
                        forme.add(formaCreata);
                        creazionePoligono = false;
                        dialogShown = false;
                        currentDrawingShapePreview = null;
                        return true;
                    } else return false;
                }
                return false;
            }
            return false;
        }else{//Caso alternativo al Poligono

            //Visione della finestra di dialogo
            if(!dialogShown) {
                attributiForma = helpUIHandleMousePressed(formaCorrente);
                if (attributiForma == null) return false;
                dialogShown = true;
            }

            //Assegnazione delle coordinate di inizio per il drag & drop
            this.startX = coordinataX;
            this.startY = coordinataY;
            this.currentDrawingShapePreview = null;
            return false;
        }
    }

    /**
     * Gestisce l'evento di trascinamento del mouse sul canvas in modo da continuare il
     * processo di creazione della {@code formaCorrente}
     * @param event è l'evento di trascinamento del mouse
     * @param forme è la lista di tutte le forme presenti sul canvas
     * @param coordinataX è la coordinata logica per l'asse x dell'evento mouse
     * @param coordinataY è la coordinata logica per l'asse y dell'evento mouse
     * @return {@code false} nel caso in cui si sta creando un poligono (la cui creazione
     * non avviene con il drag & drop), altrimenti {@code true}
     */
    @Override
    public boolean handleMouseDragged(MouseEvent event, DrawSnapModel forme, double coordinataX, double coordinataY) {
        this.currentDrawingShapePreview = createShapePreview(coordinataX, coordinataY);
        return true;
    }

    /**
     * Gestisce l'evento di rilascio del mouse sul canvas in modo da terminare il
     * processo di creazione della {@code formaCorrente}
     * @param event è l'evento di rilascio del mouse
     * @param coordinataX è la coordinata logica per l'asse x dell'evento mouse
     * @param coordinataY è la coordinata logica per l'asse y dell'evento mouse
     * @return {@code false} nel caso in cui si sta creando un poligono (la cui creazione
     * non avviene con il drag & drop), altrimenti {@code true}
     */
    @Override
    public boolean handleMouseReleased(MouseEvent event, DrawSnapModel forme, double coordinataX, double coordinataY) {
        if(formaCorrente == Forme.POLIGONO) return false;
        Forma formaCreata = createShapePreview(coordinataX, coordinataY);
        if(formaCreata != null)  forme.add(formaCreata);
        this.currentDrawingShapePreview = null;
        this.dialogShown = false;
        return true;
    }

    /**
     * Gestisce la visualizzazione di una finestra di dialogo che consente all'utente di
     * selezionare i colori della figura da disegnare.
     * Il dialogo permette di:
     * selezionare il colore del bordo tramite un {@link ColorPicker},
     * selezionare il colore di riempimento (solo se la figura non è una linea) tramite un {@link ColorPicker},
     * specificare angolo di inclinazione (solo se la figura non è una linea) tramite un {@link Spinner},
     * specificare la stringa di testo (solo se la figura è un testo), tramite un {@link TextField}
     * @param tipoForma è il tipo di figura geometrica selezionata
     * @return un oggetto {@link AttributiForma} contenente i colori selezionati se l'utente conferma,
     * oppure {@code null} se l'utente annulla l'operazione
     */
    protected AttributiForma helpUIHandleMousePressed(Forme tipoForma) {
        //Setting della finestra di dialogo
        Dialog<AttributiForma> dialog = new Dialog<>();
        dialog.setTitle("Conferma Disegno");
        Locale.setDefault(new Locale("it", "IT"));
        Label headerLabel = new Label("Vuoi inserire la figura scelta qui?");
        headerLabel.setStyle("-fx-font-size: 20px;");
        StackPane headerPane = new StackPane(headerLabel);
        headerPane.setPadding(new Insets(20, 0, 10, 0));
        dialog.getDialogPane().setHeader(headerPane);

        // ColorPicker per il bordo della figura
        Label bordoLabel = new Label("Colore del bordo:");
        bordoLabel.setStyle("-fx-font-size: 18px;");
        ColorPicker bordoPicker = new ColorPicker(Color.BLACK);
        bordoPicker.setPrefWidth(150);
        VBox bordoBox = new VBox(5, bordoLabel, bordoPicker);
        bordoBox.setAlignment(Pos.CENTER);

        // ColorPicker per l'interno della figura
        VBox internoBox = null;
        ColorPicker internoPicker = null;
        if (tipoForma != Forme.LINEA) {
            Label internoLabel = new Label("Colore di riempimento:");
            internoLabel.setStyle("-fx-font-size: 18px;");
            internoPicker = new ColorPicker(Color.WHITE);
            internoPicker.setPrefWidth(150);
            internoBox = new VBox(5, internoLabel, internoPicker);
            internoBox.setAlignment(Pos.CENTER);
        }

        //Spinner per l'angolo di inclinazione
        Label angoloLabel = new Label("Angolo inclinazione (°):");
        angoloLabel.setStyle("-fx-font-size: 18px;");
        Spinner<Double> spinnerAngolo = new Spinner<>(-360, 360.0, 0.0, 1.0);
        spinnerAngolo.setEditable(true);
        spinnerAngolo.setTooltip(new Tooltip("Angolo di rotazione in gradi (0-360)"));

        //TextField per la stringa di testo
        TextField textField = null;
        VBox textBox = null;
        if(tipoForma == Forme.TEXT){
            Label testoLabel = new Label("Testo:");
            testoLabel.setStyle("-fx-font-size: 18px;");
            textField = new TextField();
            textField.setPromptText("Inserisci testo qui");
            textField.setPrefWidth(200);
            textBox = new VBox(5, testoLabel, textField);
            textBox.setAlignment(Pos.CENTER);
        }

        //Composizione VBox
        VBox contentBox = new VBox(15);
        contentBox.setPadding(new Insets(20));
        contentBox.getChildren().addAll(bordoBox);
        if (internoBox != null) contentBox.getChildren().add(internoBox);
        if(textBox != null) contentBox.getChildren().add(textBox);
        dialog.getDialogPane().setContent(contentBox);
        dialog.getDialogPane().setMinWidth(400);

        // Pulsanti conferma e annulla
        ButtonType confirmButton = new ButtonType("Conferma", ButtonBar.ButtonData.OK_DONE);
        ButtonType cancelButton = new ButtonType("Annulla", ButtonBar.ButtonData.CANCEL_CLOSE);
        dialog.getDialogPane().getButtonTypes().addAll(confirmButton, cancelButton);

        //Valori finali di testo, colorPicker e angolo
        ColorPicker finalInternoPicker = internoPicker;
        ColorPicker finalBordoPicker = bordoPicker;
        TextField finalTextField = textField;
        Spinner <Double> finalAngoloPicker = spinnerAngolo;

        //Handler per quando si clicca su conferma
        final Button okButton = (Button) dialog.getDialogPane().lookupButton(confirmButton);

        //Se si tratta di un TEXT ottenere la stringa
        if(tipoForma == Forme.TEXT){
            okButton.addEventHandler(ActionEvent.ACTION, event -> {

                //Se la stringa è vuota mostrare un warning
                if(finalTextField.getText().trim().isEmpty()){
                    event.consume();
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("Error");
                    alert.setHeaderText(null);
                    alert.setContentText("Il campo testo ha bisogno di non essere una stringa vuota");
                    alert.showAndWait();
                }
            });
        }

        //ResultConverter per quando si chiude la finestra di dialogo
        dialog.setResultConverter(dialogButton -> {

            //Caso in cui si clicca il bottone di conferma
            if (dialogButton == confirmButton) {

                //Creazione di AttributiForma e assegnazione parametri
                AttributiForma attributi = new AttributiForma();
                attributi.setColore(finalBordoPicker.getValue());
                attributi.setColoreInterno(finalInternoPicker != null
                        ? finalInternoPicker.getValue()
                        : Color.TRANSPARENT);
                if(finalAngoloPicker != null){
                    attributi.setAngoloInclinazione(finalAngoloPicker.getValue());
                }else{
                    attributi.setAngoloInclinazione(0.0);
                }
                if(tipoForma == Forme.TEXT){
                    attributi.setTesto(finalTextField.getText());
                }
                attributi.setLarghezza(100.0);
                attributi.setAltezza(100.0);
                return attributi;
            }else return null; //Caso in cui si clicca annulla
        });

        //Aspetta l'interazione dell'utente con la finestra
        Optional<AttributiForma> result = dialog.showAndWait();
        return result.orElse(null);
    }

    /**
     * Gestisce la creazione di una {@code currentShapePreview} per la giusta
     * implementazione del drag & drop per la creazione della figura
     * @param coordinataX è la coordinata logica per l'asse x dell'evento mouse
     * @param coordinataY è la coordinata logica per l'asse y dell'evento mouse
     * @return la {@code currentShapePreview} della forma da creare
     */
    private Forma createShapePreview(double coordinataX, double coordinataY){

        //Se attributi è null non viene creata alcuna preview
        if (attributiForma == null){
            System.out.println("ERROR");
            return null;
        }

        double finalLarghezza, finalAltezza, finalAngle;
        double finalCentroX, finalCentroY;

        //Calcolo delle dimensioni in seguito al drag
        if(formaCorrente == Forme.LINEA){
            finalLarghezza = Math.sqrt(Math.pow(coordinataX - startX, 2) + Math.pow(coordinataY - startY, 2));
            finalAngle = Math.toDegrees(Math.atan2(coordinataY- startY, coordinataX - startX));
            finalAltezza = 0.0;
            finalCentroX = startX;
            finalCentroY = startY;
        }else if (formaCorrente == Forme.POLIGONO){
            finalLarghezza = factoryPoligono.getLarghezza();
            finalAltezza = factoryPoligono.getAltezza();
            finalCentroX = factoryPoligono.getCentroX();
            finalCentroY = factoryPoligono.getCentroY();
            finalAngle = attributiForma.getAngoloInclinazione();
        }else{
            finalLarghezza = Math.abs(coordinataX - startX);
            finalAltezza = Math.abs(coordinataY-startY);
            finalCentroX = (startX + coordinataX) /2.0;
            finalCentroY = (startY + coordinataY) / 2.0;
            if(finalLarghezza < 1) finalLarghezza = 100;
            if(finalAltezza < 1) finalAltezza = 100;
            finalAngle = attributiForma.getAngoloInclinazione();
        }

        //Creazione della preview corrispondente
        switch(formaCorrente){
            case ELLISSE:
                return new FactoryEllisse().creaForma(finalCentroX, finalCentroY, finalAltezza,
                        finalLarghezza, attributiForma.getAngoloInclinazione(), attributiForma.getColore(),
                        attributiForma.getColoreInterno());
            case RETTANGOLO:
                return new FactoryRettangolo().creaForma(finalCentroX, finalCentroY, finalAltezza,
                        finalLarghezza, attributiForma.getAngoloInclinazione(), attributiForma.getColore(),
                        attributiForma.getColoreInterno());
            case LINEA:
                return new FactoryLinea().creaForma(startX, startY, 0, finalLarghezza,
                        finalAngle, attributiForma.getColore(), null);
            case POLIGONO:
                return factoryPoligono.creaForma(finalCentroX, finalCentroY, finalAltezza,
                        finalLarghezza, attributiForma.getAngoloInclinazione(), attributiForma.getColore(),
                        attributiForma.getColoreInterno());
            case TEXT:
                FactoryTesto factoryTesto = new FactoryTesto();
                if(attributiForma.getTesto() == null || attributiForma.getTesto().equals("")) {
                    return null;
                }
                factoryTesto.setTesto(attributiForma.getTesto());
                return factoryTesto.creaForma(finalCentroX, finalCentroY, finalAltezza,
                        finalLarghezza, attributiForma.getAngoloInclinazione(), attributiForma.getColore(),
                        attributiForma.getColoreInterno());
            default:
                return null;
        }
    }

    /**
     * Gestisce il reset di variabili locali quando si resetta la drawMode
     */
    public void resetDialogShown() {
        this.dialogShown = false;
        this.currentDrawingShapePreview = null;
        this.attributiForma = null;
        this.creazionePoligono = false;
    }

    /**
     * Metodo di utilità per il disegno dinamico del Poligono
     * @return la lista di coordinate X ad un certo momento
     */
    public List<Double> getPuntiX(){
        return factoryPoligono.getPuntiX();
    }

    /**
     * Metodo di utilità per il disegno dinamico del Poligono
     * @return la lista di coordinate Y ad un certo momento
     */
    public List<Double> getPuntiY(){
        return factoryPoligono.getPuntiY();
    }
}
