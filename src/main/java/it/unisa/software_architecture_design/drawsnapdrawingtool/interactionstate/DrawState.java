package it.unisa.software_architecture_design.drawsnapdrawingtool.interactionstate;

import it.unisa.software_architecture_design.drawsnapdrawingtool.enumeration.Forme;
import it.unisa.software_architecture_design.drawsnapdrawingtool.forme.*;
import javafx.geometry.Insets;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;

import java.util.List;
import java.util.Optional;

public class DrawState implements DrawingState{
    /*
     * Attributi
     */
    private Forme formaCorrente;
    private List<Forma> forme;

    /*
     * Costruttore, getter e setter
     */
    public DrawState(List<Forma> forme, Forme formaCorrente) {
        this.formaCorrente = formaCorrente;
        System.out.println(formaCorrente);
    }

    public List<Forma> getForme() {
        return forme;
    }

    public void setForme(List<Forma> forme) {
        this.forme = forme;
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
        AttributiForma attributiForma = helpUIHandleMousePressed();

        Forma formaCreata = null;
        switch (formaCorrente) {
            case ELLISSE:
                formaCreata = new FactoryEllisse().creaForma(attributiForma.coordinataX, attributiForma.coordinataY,
                        attributiForma.altezza, attributiForma.larghezza,
                        attributiForma.angoloInclinazione, attributiForma.colore, attributiForma.coloreInterno);
                System.out.println("È un'ellisse");
                break;
            case RETTANGOLO:
                formaCreata = new FactoryRettangolo().creaForma(attributiForma.coordinataX, attributiForma.coordinataY,
                        attributiForma.altezza, attributiForma.larghezza,
                        attributiForma.angoloInclinazione, attributiForma.colore, attributiForma.coloreInterno);
                System.out.println("È un rettangolo");
                // Parametri mock per testare il disegno, da rimuovere una volta disponibile la factory
                double x = event.getX();
                double y = event.getY();
                double larghezza = 60;
                double altezza = 100;
                double inclinazione = 0;
                Color coloreBordo = Color.BLACK;
                Color coloreInterno = Color.BLACK;

                formaCreata = new Rettangolo(x, y, larghezza, inclinazione, coloreBordo, altezza, coloreInterno);

                break;

            case LINEA:
                formaCreata = new FactoryLinea().creaForma(attributiForma.coordinataX, attributiForma.coordinataY,
                        attributiForma.altezza, attributiForma.larghezza,
                        attributiForma.angoloInclinazione, attributiForma.colore, attributiForma.coloreInterno);
                System.out.println("È una linea");
                // Parametri mock per testare il disegno, da rimuovere una volta disponibile la factory
                double lineaX = event.getX();
                double lineaY = event.getY();

                formaCreata = new Linea(lineaX, lineaY, 50, 0, Color.BLACK);

                break;
        }
        forme.add(formaCreata);
    }

    private AttributiForma helpUIHandleMousePressed() {
        Dialog dialog = new Dialog<>(); // Modale di dialogo
        dialog.setTitle("Conferma Disegno");
        Label headerLabel = new Label("Vuoi inserire la figura scelta qui?");
        headerLabel.setStyle("-fx-font-size: 16px;");

        // StackPane per contenere e centrare il contenuto della finestra
        StackPane headerPane = new StackPane(headerLabel);
        headerPane.setPadding(new Insets(40, 0, -20, 0)); // Padding per centrare meglio la frase

        dialog.getDialogPane().setHeader(headerPane);

        // Pulsanti di conferma e annulla
        ButtonType confirmButton = new ButtonType("Conferma", ButtonBar.ButtonData.OK_DONE);
        ButtonType cancelButton = new ButtonType("Annulla", ButtonBar.ButtonData.CANCEL_CLOSE);
        dialog.getDialogPane().getButtonTypes().addAll(confirmButton, cancelButton);

        Optional result = dialog.showAndWait(); // aspetta che l'utente interagisca e restituisce un Optional contenente il bottone cliccato
        AttributiForma attributiForma = new AttributiForma();
        return attributiForma;
    }


    /*
    * handleMousePressed del controller deve aprire la finestra di dialogo per inserire i dati
    * della figura, dopodiché solo quando l'utente clicca il tasto di conferma deve prendere
    * i parametri inseriti dall'utente e passarli all'handleMousePressed di DrawingContext, che li passa
    * a handleMousePressed di DrawState, che crea la figura e la aggiunge alla lista figure, dopodiché il controller
    * deve ricaricare il canvas (tipo con una funzione redrawAll()) per mostrare anche la figura aggiornata
    * */

    @Override
    public void handleMouseDragged(MouseEvent event) {
        //WIP
    }

    @Override
    public void handleMouseReleased(MouseEvent event) {
        //WIP
    }
}
