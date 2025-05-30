package it.unisa.software_architecture_design.drawsnapdrawingtool.commands;

import it.unisa.software_architecture_design.drawsnapdrawingtool.DrawSnapModel;

public class ReflectCommand implements Command {
    private final DrawSnapModel forme;

    public ReflectCommand(DrawSnapModel forme) {
        this.forme = forme;
    }

    @Override
    public void execute() {
        forme.reflect();
    }
}
