package it.unisa.software_architecture_design.drawsnapdrawingtool;

import it.unisa.software_architecture_design.drawsnapdrawingtool.forme.Forma;
import it.unisa.software_architecture_design.drawsnapdrawingtool.forme.FormaSelezionataDecorator;

import javax.swing.*;
import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import java.util.Iterator;

public class DrawSnapModel implements Serializable {

    private List<Forma> forme = null;
    private transient List<Forma> formeCopiate = null; //Lista per tenere salvate le variabili copiate
    @Serial
    private static final long serialVersionUID = 1001L;

    public DrawSnapModel() {
        forme = new ArrayList<Forma>();
        formeCopiate = new ArrayList<Forma>();
    }

    public void add(Forma f){
        forme.add(f);
    }

    public void remove(Forma f){
        forme.remove(f);
    }

    public void clear(){
        forme.clear();
    }

    public void addAll(List<Forma> forme){
        this.forme.addAll(forme);
    }

    public Iterator<Forma> getIteratorForme() {
        return forme.iterator();
    }

    public void addFormaCopiata(Forma f){
        formeCopiate.add(f);
    }

    public void removeFormaCopiata(Forma f){
        formeCopiate.remove(f);
    }

    public void selezionaForma(Forma formaSelezionata){

    }

    public void rebuildForme(DrawSnapModel nuovoModel){
        forme.clear();
        Iterator<Forma> it = nuovoModel.getIteratorForme();
        while (it.hasNext()) {
            Forma formaCorrente = it.next();
            forme.add(formaCorrente);
        }
    }

    public List<Forma> getCopy() { return new ArrayList<>(forme); }

}
