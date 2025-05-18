package it.unisa.software_architecture_design.drawsnapdrawingtool.commands;

import it.unisa.software_architecture_design.drawsnapdrawingtool.forme.Forma;
import it.unisa.software_architecture_design.drawsnapdrawingtool.forme.FormaSelezionataDecorator;

import java.util.List;

/**
 * La classe {@code DeleteCommand} rappresenta un comando che elimina le figure selezionate.
 */
public class DeleteCommand implements Command{
    /*
     * Attributi
     */
    private final List<Forma> forme;

    /*
     * Costruttore, getter e setter
     */
    public DeleteCommand(List<Forma> forme) {
        this.forme = forme;
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
        Forma formaDaRimuovere = null;
        for(Forma formaCorrente : forme){
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
