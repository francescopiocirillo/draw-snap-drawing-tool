package it.unisa.software_architecture_design.drawsnapdrawingtool.interactionstate;

import it.unisa.software_architecture_design.drawsnapdrawingtool.forme.Forma;
import javafx.scene.canvas.Canvas;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;

import java.util.List;

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

    public void setCurrentState(DrawingState currentState) {
        this.currentState = currentState;
    }

    /*
     * Logica della classe
     */

    /**
     * DrawingContext delega la gestione dell'evento pressione del mouse allo stato corrente
     * @param event l'evento di pressione del Mouse
     */
    public void handleMousePressed(MouseEvent event, List<Forma> forme, double coordinataX, double coordinataY,
                                   double altezza, double larghezza, double angoloInclinazione, Color colore,
                                   Color coloreInterno){
        currentState.handleMousePressed(event, forme, coordinataX, coordinataY, altezza, larghezza, angoloInclinazione,
                                    colore, coloreInterno);
    }

    /**
     * DrawingContext delega la gestione dell'evento di trascinamento del mouse allo stato corrente
     * @param event l'evento di trascinamento del Mouse
     */
    public void handleMouseDragged(MouseEvent event){
        currentState.handleMouseDragged(event);
    }

    /**
     * DrawingContext delega la gestione dell'evento di rilascio del mouse allo stato corrente
     * @param event l'evento di rilascio del Mouse
     */
    public void handleMouseReleased(MouseEvent event){
        currentState.handleMouseReleased(event);
    }

}
