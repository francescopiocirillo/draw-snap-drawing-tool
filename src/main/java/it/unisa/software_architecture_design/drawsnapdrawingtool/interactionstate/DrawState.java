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

import java.util.List;
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

    /***
     * Gestisce l'evento di pressione del mouse sul canvas in modo da creare la forma giusta
     * @param event è l'evento di pressione del mouse
     * @param forme lista di tutte le forme presenti sul canvas
     */
    @Override
    public void handleMousePressed(MouseEvent event, List<Forma> forme) {
        AttributiForma attributiForma = helpUIHandleMousePressed();

        if(attributiForma == null){
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
     * Classe di utilità che aiuta nella creazione della UI necessaria all'input delle caratteristiche
     * della forma creata.
     * @return attributiForma gli attributi per la creazione di una Forma
     */
    protected AttributiForma helpUIHandleMousePressed() {
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
        return new AttributiForma();
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
        //NA
    }
}
