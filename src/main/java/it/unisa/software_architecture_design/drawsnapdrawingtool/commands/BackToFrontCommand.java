package it.unisa.software_architecture_design.drawsnapdrawingtool.commands;

import it.unisa.software_architecture_design.drawsnapdrawingtool.DrawSnapModel;
import it.unisa.software_architecture_design.drawsnapdrawingtool.forme.Forma;
import it.unisa.software_architecture_design.drawsnapdrawingtool.forme.FormaSelezionataDecorator;

import java.util.Iterator;

/**
 * La classe {@code BackToFrontCommand} rappresenta un comando che mette in primo piano nel disegno la
 * figura selezionata.
 */
public class BackToFrontCommand implements Command {
    /*
     * Attributi
     */
    private DrawSnapModel forme;

    /*
     * Costruttore, getter e setter
     */
    public BackToFrontCommand(DrawSnapModel forme) {this.forme = forme;}

    /*
     * Logica della classe
     */

    /**
     * Esegue il comando di posizionamento in primo piano della forma selezionata
     */
    @Override
    public void execute() {
        Forma formaDaPosizionare = null;
        Iterator<Forma> it = forme.getIteratorForme();
        while (it.hasNext()) {
            Forma formaCorrente = it.next();
            if(formaCorrente instanceof FormaSelezionataDecorator){
                formaDaPosizionare = formaCorrente;
                break;
            }
        }
        if(formaDaPosizionare != null){
            forme.moveToFront(formaDaPosizionare);
        }
    }
}
