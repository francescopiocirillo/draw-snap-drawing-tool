package it.unisa.software_architecture_design.drawsnapdrawingtool.interactionstate;

import it.unisa.software_architecture_design.drawsnapdrawingtool.DrawSnapModel;
import javafx.scene.input.MouseEvent;

/**
 * La classe {@code DrawingContext} rappresenta il contesto di disegno e per mezzo del Pattern State
 * si occupa di memorizzare qual è lo stato corrente in modo da poter
 * delegare la gestione degli eventi di interazione con il canvas allo stato corrente
 */
public class DrawingContext {
    /*
     * Attributi
     */
    private DrawingState currentState;

    /*
     * Costruttore, getter e setter
     */
    public DrawingContext(DrawingState currentState) {
        this.currentState = currentState;
    }

    public DrawingState getCurrentState() {
        return currentState;
    }

    public void setCurrentState(DrawingState currentState, DrawSnapModel forme) {
        // quando si esce dallo stato di selezione è necessario deselezionare tutte le figure
        if(this.currentState instanceof SelectState){
            forme.deselezionaEccetto(null);
            ((SelectState) this.currentState).disattivaToolBar();
        }
        this.currentState = currentState;
    }

    /*
     * Logica della classe
     */

    /**
     * DrawingContext delega la gestione dell'evento pressione del mouse allo stato corrente
     * @param event l'evento di pressione del Mouse
     * @param forme la lista delle forme presenti sul canvas
     */
    public void handleMousePressed(MouseEvent event, DrawSnapModel forme){
        currentState.handleMousePressed(event, forme);
    }

    /**
     * DrawingContext delega la gestione dell'evento di trascinamento del mouse allo stato corrente
     * @param event l'evento di pressione del Mouse
     * @param forme la lista delle forme presenti sul canvas
     */
    public void handleMouseDragged(MouseEvent event, DrawSnapModel forme){
        currentState.handleMouseDragged(event, forme);
    }

    /**
     * DrawingContext delega la gestione dell'evento di rilascio del mouse allo stato corrente
     * @param event l'evento di rilascio del Mouse
     */
    public void handleMouseReleased(MouseEvent event){
        currentState.handleMouseReleased(event);
    }

}
