package it.unisa.software_architecture_design.drawsnapdrawingtool.commands;

import it.unisa.software_architecture_design.drawsnapdrawingtool.DrawSnapModel;

public class DecomposeCommand implements Command{

    public DrawSnapModel forme;

    public DecomposeCommand(DrawSnapModel forme) {
        this.forme = forme;
    }

    @Override
    public void execute() {
        forme.decomponiFormaSelezionata();
    }
}
