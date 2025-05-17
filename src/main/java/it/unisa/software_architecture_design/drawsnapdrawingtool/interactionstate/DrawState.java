package it.unisa.software_architecture_design.drawsnapdrawingtool.interactionstate;

import it.unisa.software_architecture_design.drawsnapdrawingtool.enumeration.Forme;
import it.unisa.software_architecture_design.drawsnapdrawingtool.forme.Forma;
import javafx.scene.input.MouseEvent;

import java.util.List;

public class DrawState implements DrawingState{
    /*
     * Attributi
     */
    private Forme figuraCorrente;
    private List<Forma> forme;

    /*
     * Costruttore, getter e setter
     */
    public DrawState(List<Forma> forme, Forme figuraCorrente) {
        this.figuraCorrente = figuraCorrente;
    }

    public List<Forma> getForme() {
        return forme;
    }

    public void setForme(List<Forma> forme) {
        this.forme = forme;
    }

    public Forme getFiguraCorrente() {
        return figuraCorrente;
    }

    public void setFiguraCorrente(Forme figuraCorrente) {
        this.figuraCorrente = figuraCorrente;
    }

    /*
     * Logica della classe
     */

    /**
     * Gestisce l'evento di pressione del mouse sul canvas in modo da creare la forma giusta
     * @param event è l'evento di pressione del mouse
     */
    @Override
    public void handleMousePressed(MouseEvent event, List<Forma> forme/*aggiungere parametri ricevuti dalla finestra di dialogo del task 1.3.2*/) {
        Forma formaCreata = null;
        switch (figuraCorrente) {
            case ELLISSE:
                //formaCreata = new FactoryEllisse().creaForma(/*aggiungere parametri*/);
                System.out.println("È un'ellisse");
                break;
            case RETTANGOLO:
                //formaCreata = new FactoryRettangolo().creaForma(/*aggiungere parametri*/);
                System.out.println("È un rettangolo");
                break;
            case LINEA:
                //formaCreata = new FactoryLinea().creaForma(/*aggiungere parametri*/);
                System.out.println("È una linea");
                break;
        }
        //forme.add(formaCreata);
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
