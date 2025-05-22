package it.unisa.software_architecture_design.drawsnapdrawingtool.commands;

import it.unisa.software_architecture_design.drawsnapdrawingtool.DrawSnapModel;
import it.unisa.software_architecture_design.drawsnapdrawingtool.forme.Forma;
import it.unisa.software_architecture_design.drawsnapdrawingtool.forme.FormaSelezionataDecorator;

import java.util.Iterator;

/**
 * La classe {@code FrontToBackCommand} rappresenta un comando che mette in secondo piano nel disegno la
 * figura selezionata.
 */
public class FrontToBackCommand implements Command {
    /*
     * Attributi
     */
    private DrawSnapModel forme;

    /*
     * Costruttore, getter e setter
     */
    public FrontToBackCommand(DrawSnapModel forme) {this.forme = forme;}

    /*
     * Logica della classe
     */

    /**
     * Esegue il comando di posizionamento in secondo piano della forma selezionata
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
            forme.moveToBack(formaDaPosizionare);
        }
    }
}
