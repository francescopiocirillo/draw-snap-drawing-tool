package it.unisa.software_architecture_design.drawsnapdrawingtool.commands;

import it.unisa.software_architecture_design.drawsnapdrawingtool.DrawSnapModel;
import javafx.scene.paint.Color;

public class ChangeOutlineColorCommand implements Command {
    private final DrawSnapModel forme;
    private final Color coloreSelezionato;

    public ChangeOutlineColorCommand(DrawSnapModel forme, Color coloreSelezionato) {
        this.forme = forme;
        this.coloreSelezionato = coloreSelezionato;
    }

    @Override
    public void execute() {
        System.out.println("command" + coloreSelezionato);

        forme.changeOutlineColor(coloreSelezionato);
    }
}
