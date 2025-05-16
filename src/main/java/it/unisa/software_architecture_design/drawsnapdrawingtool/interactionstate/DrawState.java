package it.unisa.software_architecture_design.drawsnapdrawingtool.interactionstate;

import javafx.scene.canvas.Canvas;
import javafx.scene.input.MouseEvent;

public class DrawState implements DrawingState{
    /*
     * Attributi
     */
    private Canvas canvas;
    private String figuraCorrente;

    /*
     * Costruttore, getter e setter
     */
    public DrawState(Canvas canvas, String figuraCorrente) {
        this.canvas = canvas;
        this.figuraCorrente = figuraCorrente;
    }

    public Canvas getCanvas() {
        return canvas;
    }

    public void setCanvas(Canvas canvas) {
        this.canvas = canvas;
    }

    public String getFiguraCorrente() {
        return figuraCorrente;
    }

    public void setFiguraCorrente(String figuraCorrente) {
        this.figuraCorrente = figuraCorrente;
    }

    /*
     * Logica della classe
     */
    @Override
    public void handleMousePressed(MouseEvent event) {
        //WIP
    }

    @Override
    public void handleMouseDragged(MouseEvent event) {
        //WIP
    }

    @Override
    public void handleMouseReleased(MouseEvent event) {
        //WIP
    }
}
