package it.unisa.software_architecture_design.drawsnapdrawingtool.commands;

import it.unisa.software_architecture_design.drawsnapdrawingtool.DrawSnapModel;

/**
 * La classe {@code ReflectCommand} rappresenta un comando che permette di
 * specchiare la figura rispetto all'asse verticale oppure orizzontale
 */
public class ReflectCommand implements Command {
    /*
     * Attributi
     */
    private final DrawSnapModel forme;
    private final boolean horizontal;

    /*
     * Costruttore, getter e setter
     */
    public ReflectCommand(DrawSnapModel forme, boolean horizontal) {
        this.forme = forme;
        this.horizontal = horizontal;
    }

    /*
     * Logica della classe
     */
    /**
     * Esegue il comando di specchiar la figura selezionata
     */
    @Override
    public void execute() {
        forme.reflect(horizontal);
    }
}
