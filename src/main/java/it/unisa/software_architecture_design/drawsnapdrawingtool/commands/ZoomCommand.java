package it.unisa.software_architecture_design.drawsnapdrawingtool.commands;

import javafx.scene.canvas.Canvas;
import javafx.scene.control.ScrollPane;

/**
 * La classe {@code ZoomCommand} rappresenta un comando per zoommare il foglio di disegno.
 */
public class ZoomCommand implements Command {
    /*
     * Attributi
     */
    private Canvas canvas;
    private double baseCanvasWidth;
    private double baseCanvasHeight;
    private ScrollPane scrollPane;
    private double zoomLevel;

    /*
     * Costruttore
     */
    // Il costruttore ora accetta direttamente le dimensioni finali da impostare
    public ZoomCommand(Canvas canvas, ScrollPane scrollPane, double baseCanvasWidth, double baseCanvasHeight, double zoomLevel) {
        this.canvas = canvas;
        this.baseCanvasWidth = baseCanvasWidth;
        this.baseCanvasHeight = baseCanvasHeight;
        this.scrollPane = scrollPane;
        this.zoomLevel = zoomLevel;
    }

    /*
     * Logica della classe
     */

    /**
     * Esegue il comando di zoom.
     *
     */
    @Override
    public void execute() {
        double desiredCanvasWidth = baseCanvasWidth * zoomLevel;
        double desiredCanvasHeight = baseCanvasHeight * zoomLevel;

        double viewportWidth = scrollPane.getViewportBounds().getWidth();
        double viewportHeight = scrollPane.getViewportBounds().getHeight();

        double finalCanvasWidth = Math.max(desiredCanvasWidth, viewportWidth);
        double finalCanvasHeight = Math.max(desiredCanvasHeight, viewportHeight);
        canvas.setWidth(finalCanvasWidth);
        canvas.setHeight(finalCanvasHeight);
    }

}