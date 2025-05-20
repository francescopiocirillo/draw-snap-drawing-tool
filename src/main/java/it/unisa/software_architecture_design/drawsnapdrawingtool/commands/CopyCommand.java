package it.unisa.software_architecture_design.drawsnapdrawingtool.commands;

import it.unisa.software_architecture_design.drawsnapdrawingtool.forme.Forma;
import it.unisa.software_architecture_design.drawsnapdrawingtool.forme.FormaSelezionataDecorator;

import java.util.List;

/**
 * La classe {@code CopyCommand} rappresenta un comando che copia le figure selezionate.
 */
public class CopyCommand implements Command{
    /*
     * Attributi
     */
    private final List<Forma> forme;
    private List<Forma> formeCopiate;

    /*
     * Costruttore, getter e setter
     */
    public CopyCommand(List<Forma> forme, List<Forma> formeCopiate) {
        this.forme = forme;
        this.formeCopiate = formeCopiate;
    }

    public List<Forma> getForme() {
        return forme;
    }

    public List<Forma> getFormeCopiate() {return formeCopiate;}

    /*
     * Logica della classe
     */

    /**
     * Esegue il comando di copia della lista di Forme
     */
    @Override
    public void execute() {
        Forma formaDaCopiare = null;
        for(Forma formaCorrente : forme){
            if(formaCorrente instanceof FormaSelezionataDecorator){
                formaDaCopiare = formaCorrente;
                break;
            }
        }
        if(formaDaCopiare != null){
            formeCopiate.add(formaDaCopiare.clone());
        }
    }
}
