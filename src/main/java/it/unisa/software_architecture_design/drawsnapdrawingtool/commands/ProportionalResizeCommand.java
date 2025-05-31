package it.unisa.software_architecture_design.drawsnapdrawingtool.commands;

import it.unisa.software_architecture_design.drawsnapdrawingtool.DrawSnapModel;
import javafx.scene.paint.Color;

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
