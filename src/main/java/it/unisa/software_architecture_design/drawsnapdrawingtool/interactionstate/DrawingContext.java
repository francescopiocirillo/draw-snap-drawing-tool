package it.unisa.software_architecture_design.drawsnapdrawingtool.interactionstate;

import javafx.scene.canvas.Canvas;
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
    private Canvas canvas;
    private DrawingState currentState;

    /*
     * Costruttore, getter e setter
     */
    public DrawingContext(Canvas canvas, DrawingState currentState) {
        this.canvas = canvas;
        this.currentState = currentState;
        initializeEventHandlers();
    }

    public Canvas getCanvas() {
        return canvas;
    }

    public void setCanvas(Canvas canvas) {
        this.canvas = canvas;
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
     * Inizializza gli event handler per gli eventi di interesse relativi al {@link Canvas}
     */
    private void initializeEventHandlers() {
        canvas.setOnMousePressed(this::handleMousePressed);
        canvas.setOnMouseDragged(this::handleMouseDragged);
        canvas.setOnMouseReleased(this::handleMouseReleased);
    }

    /**
     * DrawingContext delega la gestione dell'evento pressione del mouse allo stato corrente
     * @param event l'evento di pressione del Mouse
     */
    private void handleMousePressed(MouseEvent event){
        currentState.handleMousePressed(event);
    }

    /**
     * DrawingContext delega la gestione dell'evento di trascinamento del mouse allo stato corrente
     * @param event l'evento di trascinamento del Mouse
     */
    private void handleMouseDragged(MouseEvent event){
        currentState.handleMouseDragged(event);
    }

    /**
     * DrawingContext delega la gestione dell'evento di rilascio del mouse allo stato corrente
     * @param event l'evento di rilascio del Mouse
     */
    private void handleMouseReleased(MouseEvent event){
        currentState.handleMouseReleased(event);
    }

}
