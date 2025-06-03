package it.unisa.software_architecture_design.drawsnapdrawingtool.commands;

import it.unisa.software_architecture_design.drawsnapdrawingtool.DrawSnapModel;
import it.unisa.software_architecture_design.drawsnapdrawingtool.forme.Forma;
import it.unisa.software_architecture_design.drawsnapdrawingtool.forme.FormaSelezionataDecorator;
import it.unisa.software_architecture_design.drawsnapdrawingtool.memento.DrawSnapHistory;
import javafx.scene.canvas.GraphicsContext;

import java.util.Iterator;
import java.util.List;

/**
 * La classe {@code ClearCommand} rappresenta un comando che svuota il {@link GraphicsContext}
 * corrente e svuota il {@code DrawSnapModel}
 */
public class ClearCommand implements Command{
    /*
     * Attributi
     */
    private final GraphicsContext gc;
    private final DrawSnapModel forme;
    private final DrawSnapHistory history;

    /*
     * Costruttore, getter e setter
     */
    public ClearCommand(GraphicsContext gc,DrawSnapModel forme, DrawSnapHistory history) {
        this.gc = gc;
        this.forme = forme;
        this.history = history;
    }

    /*
     * Logica della classe
     */

    /**
     * Esegue il comando di elimina della lista di Forme
     */
    @Override
    public void execute() {
        forme.clear();
        forme.clearFormeCopiate();
        history.clear();
        gc.clearRect(0, 0, 4096, 4096);
    }
}
