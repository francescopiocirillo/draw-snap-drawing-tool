package it.unisa.software_architecture_design.drawsnapdrawingtool.commands;

import it.unisa.software_architecture_design.drawsnapdrawingtool.DrawSnapModel;
import it.unisa.software_architecture_design.drawsnapdrawingtool.forme.Forma;

/**
 * La classe {@code ComposeCommand} rappresenta un comando che permette di
 * comporre insieme le figure selezionate in modo da poterci operare come se
 * fossero una figura unica.
 */
public class ComposeCommand implements Command{
    /*
     * Attributi
     */
    public DrawSnapModel forme;
    /*
     * Costruttore, getter e setter
     */
    public ComposeCommand(DrawSnapModel forme) {
        this.forme = forme;
    }

    /*
     * Logica della classe
     */
    /**
     * Esegue il comando di componi
     */
    @Override
    public void execute() {
        forme.creaFormaComposta();
    }
}
