package it.unisa.software_architecture_design.drawsnapdrawingtool.commands;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;

/**
 * La classe {@code ZoomCommand} rappresenta un comando per zoommare il foglio di disegno.
 */
public class ZoomCommand implements Command{
    /*
     * Attributi
     */
    private final Canvas canvas;
    private final Double[] zoomLevels;
    private final int targetIndex;

    /*
     * Costruttore, getter e setter
     */
    public ZoomCommand(Canvas canvas, Double[] zoomLevels, int targetIndex) {
        this.canvas = canvas;
        this.zoomLevels = zoomLevels;
        this.targetIndex = targetIndex;
    }

    /*
     * Logica della classe
     */

    /**
     * Esegue il comando di zoom
     */
    @Override
    public void execute() {
        if(targetIndex >= zoomLevels.length || targetIndex < 0) return;
        double scale = zoomLevels[targetIndex];
        canvas.setScaleX(scale);
        canvas.setScaleY(scale);
    }


}
