package it.unisa.software_architecture_design.drawsnapdrawingtool.commands;

import it.unisa.software_architecture_design.drawsnapdrawingtool.DrawSnapModel;
import it.unisa.software_architecture_design.drawsnapdrawingtool.forme.Forma;
import it.unisa.software_architecture_design.drawsnapdrawingtool.forme.FormaSelezionataDecorator;

import java.util.Iterator;

/**
 * La classe {@code CopyCommand} rappresenta un comando che copia le figure selezionate.
 */
public class CopyCommand implements Command{
    /*
     * Attributi
     */
    private final DrawSnapModel forme;

    /*
     * Costruttore, getter e setter
     */
    public CopyCommand(DrawSnapModel forme) {this.forme = forme;}

    /*
     * Logica della classe
     */

    /**
     * Esegue il comando di copia della lista di Forme
     */
    @Override
    public void execute() {
        Forma formaDaCopiare = null;
        Iterator<Forma> it = forme.getIteratorForme();
        while (it.hasNext()) {
            Forma formaCorrente = it.next();
            if(formaCorrente instanceof FormaSelezionataDecorator){
                formaDaCopiare = formaCorrente;
                break;
            }
        }
        if(formaDaCopiare != null){
            forme.addFormaCopiata(formaDaCopiare.clone());
        }
    }
}