package it.unisa.software_architecture_design.drawsnapdrawingtool.commands;

import it.unisa.software_architecture_design.drawsnapdrawingtool.DrawSnapModel;
import it.unisa.software_architecture_design.drawsnapdrawingtool.forme.Forma;
import javafx.scene.paint.Color;

/**
 * La classe {@code ProportionalResizeCommand} rappresenta un comando che ridimensiona
 * una figura proporzionalmente al {@param proporzioneUpdate}.
 */
public class ProportionalResizeCommand implements Command {
    /*
     * Attributi
     */
    private final DrawSnapModel forme;
    private double proporzioneUpdate;

    /*
     * Costruttore, getter e setter
     */
    public ProportionalResizeCommand(DrawSnapModel forme, double proporzioneUpdate) {
        this.forme = forme;
        this.proporzioneUpdate = proporzioneUpdate;
    }

    /*
     * Logica della classe
     */
    /**
     * Esegue il comando di ridimensionamento proporzionale
     */
    @Override
    public void execute() {
        forme.proportionalResize(proporzioneUpdate);
    }
}
