package it.unisa.software_architecture_design.drawsnapdrawingtool.commands;

import it.unisa.software_architecture_design.drawsnapdrawingtool.DrawSnapModel;

public class ReflectCommand implements Command {
    private final DrawSnapModel forme;
    private final boolean horizontal;

    public ReflectCommand(DrawSnapModel forme, boolean horizontal) {
        this.forme = forme;
        this.horizontal = horizontal;
    }

    @Override
    public void execute() {
        forme.reflect(horizontal);
    }
}
