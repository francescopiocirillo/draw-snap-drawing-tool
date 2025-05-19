package it.unisa.software_architecture_design.drawsnapdrawingtool.commands;

import it.unisa.software_architecture_design.drawsnapdrawingtool.forme.Forma;

import java.util.List;

/**
 * La classe {@code DeleteCommand} rappresenta un comando che elimina le figure selezionate.
 */
public class DeleteCommand implements Command{
    /*
     * Attributi
     */
    private final List<Forma> forme;
    private final Forma formaDaEliminare;

    /*
     * Costruttore, getter e setter
     */
    public DeleteCommand(List<Forma> forme, Forma formaDaEliminare) {
        this.forme = forme;
        this.formaDaEliminare = formaDaEliminare;
    }

    public Forma getFormaDaEliminare() {
        return formaDaEliminare;
    }

    public List<Forma> getForme() {
        return forme;
    }

    /*
     * Logica della classe
     */

    /**
     * Esegue il comando di elimina della lista di Forme
     */
    @Override
    public void execute() {
        forme.remove(formaDaEliminare);
    }
}
