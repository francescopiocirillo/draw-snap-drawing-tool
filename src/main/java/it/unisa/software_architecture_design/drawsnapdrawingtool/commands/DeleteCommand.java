package it.unisa.software_architecture_design.drawsnapdrawingtool.commands;

import it.unisa.software_architecture_design.drawsnapdrawingtool.DrawSnapModel;
import it.unisa.software_architecture_design.drawsnapdrawingtool.forme.Forma;
import it.unisa.software_architecture_design.drawsnapdrawingtool.forme.FormaSelezionataDecorator;

import java.util.Iterator;
import java.util.List;

/**
 * La classe {@code DeleteCommand} rappresenta un comando che elimina le figure selezionate.
 */
public class DeleteCommand implements Command{
    /*
     * Attributi
     */
    private final DrawSnapModel forme;

    /*
     * Costruttore, getter e setter
     */
    public DeleteCommand(DrawSnapModel forme) {
        this.forme = forme;
    }

    /*
     * Logica della classe
     */

    /**
     * Esegue il comando di elimina della lista di Forme
     */
    @Override
    public void execute() {
        Forma formaDaRimuovere = null;
        Iterator<Forma> it = forme.getIteratorForme();
        while (it.hasNext()) {
            Forma formaCorrente = it.next();
            if(formaCorrente instanceof FormaSelezionataDecorator){
                formaDaRimuovere = formaCorrente;
                break;
            }
        }
        if(formaDaRimuovere != null){
            forme.remove(formaDaRimuovere);
        }
    }
}
