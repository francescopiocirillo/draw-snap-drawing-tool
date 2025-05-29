package it.unisa.software_architecture_design.drawsnapdrawingtool.commands;

import it.unisa.software_architecture_design.drawsnapdrawingtool.DrawSnapModel;
import it.unisa.software_architecture_design.drawsnapdrawingtool.forme.Forma;

public class ComposeCommand implements Command{

    public DrawSnapModel forme;

    public ComposeCommand(DrawSnapModel forme) {
        this.forme = forme;
    }

    @Override
    public void execute() {
        forme.creaFormaComposta();
    }
}
