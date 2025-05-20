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

    public Forma get(int index){
        return forme.get(index);
    }

    public Forma getFirst(){
        return forme.getFirst();
    }

    public Iterator<Forma> getIteratorForme() {
        return forme.iterator();
    }

    public boolean isEmpty(){
        return forme.isEmpty();
    }

    public int size(){
        return forme.size();
    }

    public boolean contains(Forma f){
        return forme.contains(f);
    }

    public void addFormaCopiata(Forma f){
        formeCopiate.add(f);
    }

    public void removeFormaCopiata(Forma f){
        formeCopiate.remove(f);
    }

    public int sizeFormeCopiate(){
        return formeCopiate.size();
    }

    public Forma getFormaCopiata(int index){
        return formeCopiate.get(index);
    }

    public Forma getFirstFormaCopiata(){
        return formeCopiate.getFirst();
    }

    public Forma getLastFormaCopiata(){return formeCopiate.getLast();}

    /**
     * La Forma Selezionata viene rimossa dalla lista forme e reinserita dopo la decorazione
     * @param formaSelezionata
     */
    public Forma selezionaForma(Forma formaSelezionata){
        if (!(formaSelezionata instanceof FormaSelezionataDecorator)) {
            forme.remove(formaSelezionata);
            formaSelezionata = new FormaSelezionataDecorator(formaSelezionata);
            forme.add(formaSelezionata);
        }
        return formaSelezionata;
    }

    public void deselezionaEccetto(Forma formaSelezionata) {
        if(formaSelezionata != null){
            forme.remove(formaSelezionata);
        }

        List<Forma> formeDaDeselezionare = new ArrayList<>();
        formeDaDeselezionare.addAll(forme);
        for(Forma f : formeDaDeselezionare){
            if(f instanceof  FormaSelezionataDecorator){
                forme.remove(f);
                forme.add(((FormaSelezionataDecorator) f).getForma());
            }
        }

        if(formaSelezionata != null){
            forme.add(formaSelezionata);
        }
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
