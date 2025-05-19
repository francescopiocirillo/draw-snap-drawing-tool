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
    private static Forma formaCopiata; //Variabile per tenere salvata la variabile copiata

    /*
     * Costruttore, getter e setter
     */
    public CutCommand(List<Forma> forme) {
        this.forme = forme;
    }

    public List<Forma> getForme() {
        return forme;
    }

    public Forma getFormaCopiata() {return formaCopiata;}

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
            formaCopiata = formaDaTagliare.clone();
            forme.remove(formaDaTagliare);
        }
    }


}
