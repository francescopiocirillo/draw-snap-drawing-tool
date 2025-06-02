package it.unisa.software_architecture_design.drawsnapdrawingtool.commands;

import it.unisa.software_architecture_design.drawsnapdrawingtool.DrawSnapModel;

/**
 * La classe {@code DecomposeCommand} rappresenta un comando che permette di
 * decomporre l'insieme di figure precedentemente composte
 */
public class DecomposeCommand implements Command{
    /*
     * Attributi
     */
    public DrawSnapModel forme;

    /*
     * Costruttore, getter e setter
     */
    public DecomposeCommand(DrawSnapModel forme) {
        this.forme = forme;
    }

    /*
     * Logica della classe
     */
    /**
     * Esegue il comando di decomponi
     */
    @Override
    public void execute() {
        forme.decomponiFormaSelezionata();
    }
}
