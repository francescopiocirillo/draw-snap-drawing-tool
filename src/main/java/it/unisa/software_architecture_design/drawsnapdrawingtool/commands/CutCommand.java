package it.unisa.software_architecture_design.drawsnapdrawingtool.commands;

import it.unisa.software_architecture_design.drawsnapdrawingtool.forme.Forma;
import it.unisa.software_architecture_design.drawsnapdrawingtool.forme.FormaSelezionataDecorator;

import java.util.List;

/**
 * La classe {@code CutCommand} rappresenta un comando che prima copia e poi elimina le figure selezionate.
 */
public class CutCommand implements Command{
    /*
     * Attributi
     */
    private final List<Forma> forme;
    private List<Forma> formeCopiate;

    /*
     * Costruttore, getter e setter
     */
    public CutCommand(List<Forma> forme, List<Forma> formeCopiate) {
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
     * Esegue il comando di taglia della lista di Forme
     */
    @Override
    public void execute() {
        Forma formaDaTagliare = null;
        for(Forma formaCorrente : forme){
            if(formaCorrente instanceof FormaSelezionataDecorator){
                formaDaTagliare = formaCorrente;
                break;
            }
        }
        if(formaDaTagliare != null){
            formeCopiate.add(formaDaTagliare.clone());
            forme.remove(formaDaTagliare);
        }
    }


}
