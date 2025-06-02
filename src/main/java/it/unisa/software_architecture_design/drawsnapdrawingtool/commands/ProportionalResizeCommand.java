package it.unisa.software_architecture_design.drawsnapdrawingtool.commands;

import it.unisa.software_architecture_design.drawsnapdrawingtool.DrawSnapModel;
import it.unisa.software_architecture_design.drawsnapdrawingtool.forme.Forma;
import javafx.scene.paint.Color;

/**
 * La classe {@code ProportionalResizeCommand} rappresenta un comando che ridimensiona
 * una figura proporzionalmente al {@param proporzioneUpdate}.
 */
public class ProportionalResizeCommand implements Command {
    private final DrawSnapModel forme;
    private double proporzioneUpdate;

    public ProportionalResizeCommand(DrawSnapModel forme, double proporzioneUpdate) {
        this.forme = forme;
        this.proporzioneUpdate = proporzioneUpdate;
    }

    @Override
    public void execute() {
        forme.proportionalResize(proporzioneUpdate);
    }
}
