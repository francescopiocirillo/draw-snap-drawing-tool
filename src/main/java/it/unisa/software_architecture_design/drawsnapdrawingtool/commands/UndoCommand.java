package it.unisa.software_architecture_design.drawsnapdrawingtool.commands;

import it.unisa.software_architecture_design.drawsnapdrawingtool.DrawSnapModel;
import it.unisa.software_architecture_design.drawsnapdrawingtool.memento.DrawSnapHistory;

public class UndoCommand implements Command {
    private final DrawSnapModel forme;
    private final DrawSnapHistory history;

    public UndoCommand(DrawSnapModel forme, DrawSnapHistory history) {
        this.forme = forme;
        this.history = history;
    }

    @Override
    public void execute() {
        forme.restoreFromMemento(history.undo());
    }
}
