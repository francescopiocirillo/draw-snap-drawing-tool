package it.unisa.software_architecture_design.drawsnapdrawingtool.commands;

import it.unisa.software_architecture_design.drawsnapdrawingtool.DrawSnapModel;
import it.unisa.software_architecture_design.drawsnapdrawingtool.memento.DrawSnapHistory;

/**
 * La classe {@code UndoCommand} rappresenta un comando che permette di
 * annullare le azioni effettuate
 */
public class UndoCommand implements Command {
    /*
     * Attributi
     */
    private final DrawSnapModel forme;
    private final DrawSnapHistory history;

    /*
     * Costruttore, getter e setter
     */
    public UndoCommand(DrawSnapModel forme, DrawSnapHistory history) {
        this.forme = forme;
        this.history = history;
    }

    /*
     * Logica della classe
     */
    /**
     * Esegue il comando di annulla
     */
    @Override
    public void execute() {
        forme.restoreFromMemento(history.undo());
    }
}
