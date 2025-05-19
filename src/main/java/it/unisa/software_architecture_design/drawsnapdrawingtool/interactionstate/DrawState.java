package it.unisa.software_architecture_design.drawsnapdrawingtool.interactionstate;

import it.unisa.software_architecture_design.drawsnapdrawingtool.enumeration.Forme;
import it.unisa.software_architecture_design.drawsnapdrawingtool.forme.*;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
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
 * si occupa della logica degli handler relativi agli eventi di interazione con il canvas nello stato di disegno
 * differenziando tra le diverse figure per mezzo di un parametro.
 */
public class DrawState implements DrawingState{
    /*
     * Attributi
     */
    private Forme formaCorrente;

    /*
     * Costruttore, getter e setter
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

    /*
     * Logica della classe
     */

    /**
     * Gestisce l'evento di pressione del mouse sul canvas in modo da creare la forma giusta
     * @param event è l'evento di pressione del mouse
     * @param forme lista di tutte le forme presenti sul canvas
     */
    @Override
    public void handleMousePressed(MouseEvent event, List<Forma> forme) {
        AttributiForma attributiForma = helpUIHandleMousePressed(formaCorrente);

        if (attributiForma == null) { // se l'utente ha premuto "Annulla" non fare nulla
            System.out.println("Creazione forma annullata dall'utente.");
            attributiForma = new AttributiForma();
        }

        Forma formaCreata = null;
        double coordinataX = event.getX();
        double coordinataY = event.getY();
        switch (formaCorrente) {
            case ELLISSE:
                formaCreata = new FactoryEllisse().creaForma(coordinataX, coordinataY,
                        attributiForma.getAltezza(), attributiForma.getLarghezza(),
                        attributiForma.getAngoloInclinazione(), attributiForma.getColore(), attributiForma.getColoreInterno());
                System.out.println("È un'ellisse");
                break;
            case RETTANGOLO:
                formaCreata = new FactoryRettangolo().creaForma(coordinataX, coordinataY,
                        attributiForma.getAltezza(), attributiForma.getLarghezza(),
                        attributiForma.getAngoloInclinazione(), attributiForma.getColore(), attributiForma.getColoreInterno());
                System.out.println("È un rettangolo");
                break;

            case LINEA:
                formaCreata = new FactoryLinea().creaForma(coordinataX, coordinataY,
                        attributiForma.getAltezza(), attributiForma.getLarghezza(),
                        attributiForma.getAngoloInclinazione(), attributiForma.getColore(), attributiForma.getColoreInterno());
                System.out.println("È una linea");
                break;
        }
        forme.add(formaCreata);
    }

    /**
     * Mostra una finestra di dialogo che consente all'utente di selezionare i colori della figura da disegnare.
     * Il dialogo permette di:
     * selezionare il colore del bordo tramite un {@link ColorPicker},
     * selezionare il colore di riempimento (solo se la figura non è una linea),
     * specificare dimensioni e angolo di inclinazione tramite {@link Spinner},
     * @param tipoForma il tipo di figura geometrica selezionata
     * @return un oggetto {@link AttributiForma} contenente i colori selezionati se l'utente conferma, oppure {@code null} se l'utente annulla
     */

    protected AttributiForma helpUIHandleMousePressed(Forme tipoForma) {
        Dialog<AttributiForma> dialog = new Dialog<>(); //modale di dialogo
        dialog.setTitle("Conferma Disegno");
        Locale.setDefault(new Locale("it", "IT")); //per settare le scritte nel colorpicker in italiano

        Label headerLabel = new Label("Vuoi inserire la figura scelta qui?");
        headerLabel.setStyle("-fx-font-size: 20px;");
        // StackPane per contenere contenuto della finestra
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

        //Spinner per le dimensioni
        VBox dimensioniBox = new VBox(10);
        dimensioniBox.setAlignment(Pos.CENTER);

        Spinner<Double> spinnerAltezza = new Spinner<>(10.0, 500.0, 100.0, 1.0);
        Spinner<Double> spinnerLarghezza = new Spinner<>(10.0, 500.0, 100.0, 1.0);
        Spinner<Double> spinnerLunghezza = new Spinner<>(10.0, 500.0, 100.0, 1.0);
        Spinner<Double> spinnerAngolo = new Spinner<>(0.0, 360.0, 0.0, 1.0);

        spinnerAltezza.setEditable(true);
        spinnerLarghezza.setEditable(true);
        spinnerLunghezza.setEditable(true);
        spinnerAngolo.setEditable(true);
        // Suggerimenti quando scorri sullo spinner
        spinnerLarghezza.setTooltip(new Tooltip("Imposta la larghezza della figura in pixel"));
        spinnerAltezza.setTooltip(new Tooltip("Imposta l'altezza della figura in pixel"));
        spinnerLunghezza.setTooltip(new Tooltip("Imposta la lunghezza della linea in pixel"));
        spinnerAngolo.setTooltip(new Tooltip("Angolo di rotazione in gradi (0-360)"));

        if (tipoForma == Forme.LINEA) {
            dimensioniBox.getChildren().addAll(
                    new Label("Lunghezza:"), spinnerLunghezza,
                    new Label("Angolo inclinazione (°):"), spinnerAngolo
            );
        } else {
            dimensioniBox.getChildren().addAll(
                    new Label("Altezza:"), spinnerAltezza,
                    new Label("Larghezza:"), spinnerLarghezza,
                    new Label("Angolo inclinazione (°):"), spinnerAngolo
            );
        }

        //Composizione VBox
        VBox contentBox = new VBox(15);
        contentBox.setPadding(new Insets(20));
        contentBox.getChildren().addAll(bordoBox);
        if (internoBox != null) contentBox.getChildren().add(internoBox);
        contentBox.getChildren().add(dimensioniBox);

        dialog.getDialogPane().setContent(contentBox);
        dialog.getDialogPane().setMinWidth(400);
        dialog.getDialogPane().setMinHeight(350);

        // Pulsanti conferma e annulla
        ButtonType confirmButton = new ButtonType("Conferma", ButtonBar.ButtonData.OK_DONE);
        ButtonType cancelButton = new ButtonType("Annulla", ButtonBar.ButtonData.CANCEL_CLOSE);
        dialog.getDialogPane().getButtonTypes().addAll(confirmButton, cancelButton);

        ColorPicker finalInternoPicker = internoPicker; //necessario per la lambda

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == confirmButton) { //se viene premuto conferma crea un nuovo AttributiForma con i parametri scelti
                AttributiForma attributi = new AttributiForma();
                attributi.setColore(bordoPicker.getValue());
                attributi.setColoreInterno(finalInternoPicker != null
                        ? finalInternoPicker.getValue()
                        : Color.TRANSPARENT); //se è una linea il colore interno è settato a trasparente

                if (tipoForma == Forme.LINEA) {
                    attributi.setLarghezza(spinnerLunghezza.getValue());
                    attributi.setAltezza(0);
                    attributi.setAngoloInclinazione(spinnerAngolo.getValue());
                } else {
                    attributi.setAltezza(spinnerAltezza.getValue());
                    attributi.setLarghezza(spinnerLarghezza.getValue());
                    attributi.setAngoloInclinazione(spinnerAngolo.getValue());
                }
                return attributi;
            }
            return null;
        });

        Optional<AttributiForma> result = dialog.showAndWait();  // aspetta che l'utente interagisca e restituisce un Optional
        return result.orElse(null);
    }


    /**
     * METODO MOMENTANEAMENTE NON NECESSARIO
     * @param event evento di trascinamento del mouse
     * @param forme lista di tutte le forme presenti sul canvas
     */
    @Override
    public void handleMouseDragged(MouseEvent event, List<Forma> forme) {
        //NA
    }

    /**
     * METODO MOMENTANEAMENTE NON NECESSARI0
     * @param event evento di rilascio del mouse
     */
    @Override
    public void handleMouseReleased(MouseEvent event) {
        //WIP
    }
}
