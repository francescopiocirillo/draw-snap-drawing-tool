package it.unisa.software_architecture_design.drawsnapdrawingtool.memento;

import it.unisa.software_architecture_design.drawsnapdrawingtool.forme.Forma;

import java.util.ArrayList;
import java.util.List;

public class DrawSnapMemento {
    /*
     * Attributi
     */
    private final List<Forma> forme;

    /*
     * Costruttore e getter
     */
    public DrawSnapMemento() {
        forme = new ArrayList<>();
    }

    public DrawSnapMemento(List<Forma> forme) {
        this.forme = forme;
    }

    public List<Forma> getSavedState() {
        return forme;
    }

}
