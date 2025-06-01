package it.unisa.software_architecture_design.drawsnapdrawingtool.interactionstate;

import it.unisa.software_architecture_design.drawsnapdrawingtool.DrawSnapModel;
import it.unisa.software_architecture_design.drawsnapdrawingtool.forme.Forma;
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
            forme.deselezionaEccetto((Forma) null); // il cast è necessario per l'overloading del metodo, senza non capirebbe quale metodo chiamare
            ((SelectState) this.currentState).disattivaToolBar();
        }
        this.currentState = currentState;
    }

    /*
     * Logica della classe
     */

    /**
     * DrawingContext delega la gestione dell'evento pressione del mouse allo stato corrente
     * @param event è l'evento di pressione del Mouse
     * @param forme è la lista delle forme presenti sul canvas
     * @param x è la coordinata logica per l'asse x dell'evento mouse
     * @param y è la coordinata logica per l'asse y dell'evento mouse
     * @return {@code true} in caso di risultato positivo, altrimenti {@code false}
     */
    public boolean handleMousePressed(MouseEvent event, DrawSnapModel forme, double x, double y){
        return currentState.handleMousePressed(event, forme,x, y);
    }

    /**
     * DrawingContext delega la gestione dell'evento di trascinamento del mouse allo stato corrente
     * @param event è l'evento di pressione del Mouse
     * @param forme è la lista delle forme presenti sul canvas
     * @param x è la coordinata logica per l'asse x dell'evento mouse
     * @param y è la coordinata logica per l'asse y dell'evento mouse
     * @return {@code true} in caso di risultato positivo, altrimenti {@code false}
     */
    public boolean handleMouseDragged(MouseEvent event, DrawSnapModel forme, double x, double y){
        return currentState.handleMouseDragged(event, forme, x, y);
    }

    /**
     * DrawingContext delega la gestione dell'evento di rilascio del mouse allo stato corrente
     * @param event è l'evento di rilascio del Mouse
     * @param x è la coordinata logica per l'asse x dell'evento mouse
     * @param y è la coordinata logica per l'asse y dell'evento mouse
     * @return {@code true} in caso di risultato positivo, altrimenti {@code false}
     */
    public boolean handleMouseReleased(MouseEvent event, DrawSnapModel forme, double x, double y){
        return currentState.handleMouseReleased(event, forme, x, y);
    }

}
