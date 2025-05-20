package it.unisa.software_architecture_design.drawsnapdrawingtool.commands;


import it.unisa.software_architecture_design.drawsnapdrawingtool.forme.Forma;
import it.unisa.software_architecture_design.drawsnapdrawingtool.forme.FormaSelezionataDecorator;

import java.util.ArrayList;
import java.util.List;

/**
 * La classe {@code PasteCommand} rappresenta un comando che incolla le figure selezionate.
 */
public class PasteCommand implements Command {
    /*
     * Attributi
     */
    private final List<Forma> forme;
    private List<Forma> formeCopiate;
    private final double x;
    private final double y;

    /*
     * Costruttore, getter e setter
     */
    public PasteCommand(List<Forma> forme, List<Forma> formeCopiate, double x, double y) {
        this.forme = forme;
        this.formeCopiate = formeCopiate;
        this.x = x;
        this.y = y;
    }


    public List<Forma> getForme() {
        return forme;
    }

    public List<Forma> getFormeCopiate() {
        return formeCopiate;
    }

    /*
     * Logica della classe
     */

    /**
     * Esegue il comando di copia della lista di Forme
     */
    @Override
    public void execute() {

        if (formeCopiate != null && !formeCopiate.isEmpty()) {
            Forma ultimaForma = formeCopiate.getLast();

            Forma nuova = ultimaForma.clone();
            if (nuova != null) {
                nuova.setCoordinataX(x);
                nuova.setCoordinataY(y);
                forme.add(nuova);
            }
        }

    }

}
