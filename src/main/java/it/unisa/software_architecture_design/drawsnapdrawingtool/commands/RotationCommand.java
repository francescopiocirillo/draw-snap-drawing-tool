package it.unisa.software_architecture_design.drawsnapdrawingtool.commands;

import it.unisa.software_architecture_design.drawsnapdrawingtool.DrawSnapModel;
import javafx.scene.paint.Color;

public class RotationCommand implements Command {
    private final DrawSnapModel forme;
    private final double angleSelezionato;

    public RotationCommand(DrawSnapModel forme, double angleSelezionato) {
        this.forme = forme;
        this.angleSelezionato =  angleSelezionato;
    }

    @Override
    public void execute() {
        System.out.println("command" + angleSelezionato);

        forme.rotation(angleSelezionato);
    }

}
