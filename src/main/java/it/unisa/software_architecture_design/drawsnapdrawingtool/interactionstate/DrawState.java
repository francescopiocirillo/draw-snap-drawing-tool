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
     */
    @Override
    public void handleMousePressed(MouseEvent event, List<Forma> forme) {
        AttributiForma attributiForma = helpUIHandleMousePressed(formaCorrente);

        Forma formaCreata = null;
        double coordinataX = event.getX();
        double coordinataY = event.getY();
        switch (formaCorrente) {
            case ELLISSE:
                formaCreata = new FactoryEllisse().creaForma(coordinataX, coordinataY,
                        attributiForma.altezza, attributiForma.larghezza,
                        attributiForma.angoloInclinazione, attributiForma.colore, attributiForma.coloreInterno);
                System.out.println("È un'ellisse");
                break;
            case RETTANGOLO:
                formaCreata = new FactoryRettangolo().creaForma(coordinataX, coordinataY,
                        attributiForma.altezza, attributiForma.larghezza,
                        attributiForma.angoloInclinazione, attributiForma.colore, attributiForma.coloreInterno);
                System.out.println("È un rettangolo");
                break;

            case LINEA:
                formaCreata = new FactoryLinea().creaForma(coordinataX, coordinataY,
                        attributiForma.altezza, attributiForma.larghezza,
                        attributiForma.angoloInclinazione, attributiForma.colore, attributiForma.coloreInterno);
                System.out.println("È una linea");
                break;
        }
        forme.add(formaCreata);
    }

    /**
     * Mostra una finestra di dialogo che consente all'utente di selezionare i colori della figura da disegnare.
     * Il dialogo visualizza un {@link ColorPicker} per il colore del bordo e, se la figura
     * selezionata non è una linea ({@code Forme.LINEA}), anche un {@link ColorPicker} per il colore interno.
     * @param tipoForma il tipo di figura geometrica selezionata
     * @return un oggetto {@link AttributiForma} contenente i colori selezionati se l'utente conferma, oppure {@code null} se l'utente annulla
     */

    private AttributiForma helpUIHandleMousePressed(Forme tipoForma) {
        Dialog<AttributiForma> dialog = new Dialog<>(); //modale di dialogo
        dialog.setTitle("Conferma Disegno");
        Locale.setDefault(new Locale("it", "IT")); //per settare le scritte nel colorpicker in italiano

        Label headerLabel = new Label("Vuoi inserire la figura scelta qui?");
        headerLabel.setStyle("-fx-font-size: 20px;");

        // StackPane per contenere e centrare il contenuto della finestra
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
        if (tipoForma != Forme.LINEA) { //se la figura selezionata è chiusa, si può scegliere il colore interno
            Label internoLabel = new Label("Colore di riempimento:");
            internoLabel.setStyle("-fx-font-size: 18px;");
            internoPicker = new ColorPicker(Color.WHITE);
            internoPicker.setPrefWidth(150);
            internoBox = new VBox(5, internoLabel, internoPicker);
            internoBox.setAlignment(Pos.CENTER);
        }

        VBox contentBox = new VBox(15);
        contentBox.setPadding(new Insets(20));
        contentBox.setAlignment(Pos.CENTER);
        contentBox.getChildren().add(bordoBox);
        if (internoBox != null) {
            contentBox.getChildren().add(internoBox);
        }
        // Layout della modale
        dialog.getDialogPane().setContent(contentBox);
        dialog.getDialogPane().setMinWidth(400);
        dialog.getDialogPane().setMinHeight(250);

        dialog.getDialogPane().setContent(contentBox);

        // Pulsanti di conferma e annulla
        ButtonType confirmButton = new ButtonType("Conferma", ButtonBar.ButtonData.OK_DONE);
        ButtonType cancelButton = new ButtonType("Annulla", ButtonBar.ButtonData.CANCEL_CLOSE);
        dialog.getDialogPane().getButtonTypes().addAll(confirmButton, cancelButton);

        ColorPicker finalInternoPicker = internoPicker; // necessario per la lambda

        dialog.setResultConverter(dialogButton -> {   //se viene premuto conferma crea un nuovo AttributiForma con i colori scelti
            if (dialogButton == confirmButton) {
                AttributiForma attributi = new AttributiForma();
                attributi.colore = bordoPicker.getValue();
                attributi.coloreInterno = finalInternoPicker != null //se è una linea il colore interno è settato a trasparente
                        ? finalInternoPicker.getValue()
                        : Color.TRANSPARENT;
                return attributi;
            }
            return null;
        });

        Optional<AttributiForma> result = dialog.showAndWait(); // aspetta che l'utente interagisca e restituisce un Optional
        return result.orElse(null);
    }




    /*
    * handleMousePressed del controller deve aprire la finestra di dialogo per inserire i dati
    * della figura, dopodiché solo quando l'utente clicca il tasto di conferma deve prendere
    * i parametri inseriti dall'utente e passarli all'handleMousePressed di DrawingContext, che li passa
    * a handleMousePressed di DrawState, che crea la figura e la aggiunge alla lista figure, dopodiché il controller
    * deve ricaricare il canvas (tipo con una funzione redrawAll()) per mostrare anche la figura aggiornata
    * */

    @Override
    public void handleMouseDragged(MouseEvent event, List<Forma> forme) {
        //WIP
    }

    @Override
    public void handleMouseReleased(MouseEvent event) {
        //WIP
    }
}
