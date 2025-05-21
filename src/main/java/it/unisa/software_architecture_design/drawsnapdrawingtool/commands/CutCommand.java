package it.unisa.software_architecture_design.drawsnapdrawingtool.commands;

import it.unisa.software_architecture_design.drawsnapdrawingtool.DrawSnapModel;
import it.unisa.software_architecture_design.drawsnapdrawingtool.forme.Forma;
import it.unisa.software_architecture_design.drawsnapdrawingtool.forme.FormaSelezionataDecorator;

import java.util.Iterator;
import java.util.List;

/**
 * La classe {@code CutCommand} rappresenta un comando che prima copia e poi elimina le figure selezionate.
 */
public class CutCommand implements Command{
    /*
     * Attributi
     */
    private final DrawSnapModel forme;

    /*
     * Costruttore, getter e setter
     */
    public CutCommand(DrawSnapModel forme) {
        this.forme = forme;
    }

    /*
     * Logica della classe
     */

    /**
     * Esegue il comando di taglia della lista di Forme
     */
    @Override
    public void execute() {
        Forma formaDaTagliare = null;
        Iterator<Forma> it = forme.getIteratorForme();
        while (it.hasNext()) {
            Forma formaCorrente = it.next();
            if(formaCorrente instanceof FormaSelezionataDecorator){
                formaDaTagliare = formaCorrente;
                break;
            }
        }
        if(formaDaTagliare != null){
            forme.addFormaCopiata(formaDaTagliare.clone());
            forme.remove(formaDaTagliare);
        }
    }


}
