package it.unisa.software_architecture_design.drawsnapdrawingtool;

import it.unisa.software_architecture_design.drawsnapdrawingtool.forme.*;
import it.unisa.software_architecture_design.drawsnapdrawingtool.memento.DrawSnapMemento;
import javafx.scene.paint.Color;

import java.io.Serial;
import java.io.Serializable;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.List;

import java.util.Iterator;

/**
 * Classe che rappresenta i dati e la logica di business dell'applicativo
 */
public class DrawSnapModel implements Serializable {
    /*
     * Attributi
     */
    private List<Forma> forme = null;
    private transient List<Forma> formeCopiate = null; //Lista per tenere salvate le variabili copiate
    @Serial
    private static final long serialVersionUID = 1001L;

    /*
     * Costruttore
     */
    public DrawSnapModel() {
        forme = new ArrayList<Forma>();
        formeCopiate = new ArrayList<Forma>();
    }

    /**
     * Metodo per aggiungere una forma al disegno
     * @param f -> la forma da aggiungere
     */
    public void add(Forma f){
        forme.add(f);
    }

    /**
     * Metodo per rimuovere una forma dal disegno
     * @param f -> la forma da rimuovere
     */
    public void remove(Forma f){
        forme.remove(f);
    }

    /**
     * Metodo per rimuovere tutte le forme presenti nel disegno
     */
    public void clear(){
        forme.clear();
    }

    /**
     * Metodo per aggiungere una lista di forme al disegno
     * @param forme -> lista di forme da aggiungere
     */
    public void addAll(List<Forma> forme){
        this.forme.addAll(forme);
    }

    /**
     * Metodo per prendere una forma tramite indice
     * @param index -> indice della forma
     * @return la forma selezionata
     */
    public Forma get(int index){
        return forme.get(index);
    }

    /**
     * Metodo per ottenere la prima forma aggiunta
     * @return la prima forma presente nella lista
     */
    public Forma getFirst(){
        return forme.getFirst();
    }

    /**
     * Metodo per creare l'iterator da usare nelle liste di Forme
     * @return Iterator
     */
    public Iterator<Forma> getIteratorForme() {
        return forme.iterator();
    }

    /**
     * Metodo per controllare se la lista di forme presenti nel disegno è vuota
     * @return {@code true} se la lista è vuota, altrimenti {@code false}
     */
    public boolean isEmpty(){
        return forme.isEmpty();
    }

    /**
     * Metodo per ottenere il numerod i forme presenti nel disegno
     * @return numero di forme
     */
    public int size(){
        return forme.size();
    }

    /**
     * Metodo per controllare che una forma è presente nel foglio did isegno
     * @param f -> la forma di cui controllare la presenza
     * @return {@code true} se la forma è presente, altrimenti {@code false}
     */
    public boolean contains(Forma f){
        return forme.contains(f);
    }

    /**
     * Metodo per aggiungere una forma alla lista di forme copiate
     * @param f -> la forma da aggiungere
     */
    public void addFormaCopiata(Forma f){
        formeCopiate.add(f);
    }

    /**
     * Metodo per rimuovere una forma dalla lista di forme copiate
     * @param f -> forma da rimuovere
     */
    public void removeFormaCopiata(Forma f){
        formeCopiate.remove(f);
    }

    /**
     * Metodo per ottenere il numerod i forme copiate
     * @return numero di forme
     */
    public int sizeFormeCopiate(){
        return formeCopiate.size();
    }

    /**
     * Metodo per controllare se la lista di forme copiate è vuota
     * @return {@code true} se la lista è vuota, altrimenti {@code false}
     */
    public boolean isEmptyFormeCopiate(){
        return formeCopiate.isEmpty();
    }

    /**
     * Metodo per prendere una forma copiata tramite indice
     * @param index -> indice della forma
     * @return la forma selezionata
     */
    public Forma getFormaCopiata(int index){
        return formeCopiate.get(index);
    }

    /**
     * Metodo per ottenere la prima forma copiata
     * @return la prima forma copiata nella lista
     */
    public Forma getFirstFormaCopiata(){
        return formeCopiate.getFirst();
    }

    /**
     * Metodo per ottenere l'ultima forma copiata
     * @return l'ultima forma copiata nella lista
     */
    public Forma getLastFormaCopiata(){return formeCopiate.getLast();}


    /**
     * Metodo per inserire una forma all'inizio della lista
     * @param f la forma da spostare
     */
    public void moveToBack(Forma f) {
        forme.remove(f);
        forme.addFirst(f);
    }

    /**
     * Metodo per inserire una forma alla fine della lista
     * @param f la forma da spostare
     */
    public void moveToFront(Forma f) {
        forme.remove(f);
        forme.add(f);
    }

    /**
     * La Forma Selezionata viene rimossa dalla lista forme e reinserita dopo la decorazione
     * @param formaSelezionata -> la forma da selezionare
     * @return la forma selezionata
     */
    public Forma selezionaForma(Forma formaSelezionata){
        if (!(formaSelezionata instanceof FormaSelezionataDecorator)) {
            int index = forme.indexOf(formaSelezionata);
            if (index != -1) {
                forme.remove(index);
                formaSelezionata = new FormaSelezionataDecorator(formaSelezionata);
                forme.add(index, formaSelezionata);
            }
        }
        return formaSelezionata;
    }

    /**
     * Deseleziona, rimuovendo il Decorator, tutte le Forme memorizzate eccetto quella
     * fornita come parametro. La deselezione avviene sulla lista interna al Model.
     * @param formaSelezionata forma da non deselezionare
     */
    public void deselezionaEccetto(Forma formaSelezionata) {
        deselezionaEccetto(formaSelezionata, this.forme);
    }

    /**
     * Deseleziona, rimuovendo il Decorator, tutte le Forme memorizzate eccetto quella
     * fornita come parametro. La deselezione avviene sulla lista fornita.
     * @param formaSelezionata forma da non deselezionare
     */
    public void deselezionaEccetto(Forma formaSelezionata, List<Forma> forme) {
        int index = 0;
        if(formaSelezionata != null){
            index = forme.indexOf(formaSelezionata);
            forme.remove(formaSelezionata);
        }

        List<Forma> formeDaDeselezionare = new ArrayList<>();
        formeDaDeselezionare.addAll(forme);
        forme.clear();
        for(Forma f : formeDaDeselezionare){
            forme.remove(f);
            if(f instanceof  FormaSelezionataDecorator){
                f = ((FormaSelezionataDecorator) f).undecorate();
                forme.add(f);
            }else{
                forme.add(f);
            }
        }

        if(formaSelezionata != null){
            forme.add(index, formaSelezionata);
        }
    }

    /**
     * Popola la lista interna al Model con il contenuto dell'istanza di Model passata come parametro.
     * @param nuovoModel -> istanza di Model da cui prendere le forme.
     */
    public void rebuildForme(DrawSnapModel nuovoModel){
        forme.clear();
        Iterator<Forma> it = nuovoModel.getIteratorForme();
        while (it.hasNext()) {
            Forma formaCorrente = it.next();
            forme.add(formaCorrente);
        }
    }

    /**
     * Metodo che prendere la forma selezionata
     * @return -> la forma selezionata
     */
    public Forma getFormaSelezionata(){
        Forma formaSelezionata = null;
        for(Forma f:forme){
            if(f instanceof FormaSelezionataDecorator){
                formaSelezionata = f;
            }
        }
        return formaSelezionata;
    }

    /**
     * Metodo che controlla se c'è una forma selezionata nella lista
     * @return {@code true} se è presente una forma selezionata, altrimenti {@cide false}
     */
    public boolean thereIsFormaSelezionata(){
        boolean result = false;
        for(Forma f:forme){
            if(f instanceof FormaSelezionataDecorator){
                result = true;
            }
        }
        return result;
    }

    /**
     * Fornisce una copia della lista di forme
     * @return una copia della lista di forme
     */
    public List<Forma> getCopy() {
        List<Forma> copia = new ArrayList<>();
        for (Forma f : forme) {
            copia.add(f.clone());
        }
        return copia;
    }

    /**
     * Salva lo stato attuale dell'applicazione in un memento
     * @return il memento contenente lo stato attuale dell'applicazione
     */
    public DrawSnapMemento saveToMemento() {
        List<Forma> copia = getCopy();
        deselezionaEccetto(null, copia);
        return new DrawSnapMemento(copia); // deep copy dello stato corrente
    }

    /**
     * Ripristina lo stato dell'applicazione a quello del memento fornito
     * @param memento -> il memento da ripristinare
     */
    public void restoreFromMemento(DrawSnapMemento memento) {
        this.forme.clear();
        this.forme.addAll(memento.getSavedState());
    }

    /**
     * Cambia il colore del riempimento della figura selezionata con @colore. La figura non può essere una
     * linea perchè il bottone sarà disattivato se la figura selezionata non ha un colore interno
     * @param colore colore deciso dall'utente per aggiornare la figura selezionata
     */
    public void changeFillColor(Color colore) {
        System.out.println("model: " + colore);
        for(Forma f:forme){
            if(f instanceof FormaSelezionataDecorator){
                System.out.println("forma selezionata nel command" );
                // se la forma è composta la decorazione delle forme interne blocca il propagarsi verso il basso
                // della nuova colorazione, togliere e rimettere la decorazione risolve il problema
                // per il colore di bordo visto che è una qualità che hanno tutte le figure è bastato aggiungere il
                // metodo setColor a FormaSelezionataDecorator
                FormaSelezionataDecorator formaCorrente = (FormaSelezionataDecorator)f;
                ((FormaSelezionataDecorator) f).undecorate();
                if(formaCorrente.getForma() instanceof Ellisse){
                    Ellisse ellisse = (Ellisse) formaCorrente.getForma();
                    ellisse.setColoreInterno(colore);
                    System.out.println("cambio colore dell'ellissi in " + colore);
                } else if (formaCorrente.getForma() instanceof Rettangolo) {
                    Rettangolo rettangolo = (Rettangolo) formaCorrente.getForma();
                    rettangolo.setColoreInterno(colore);
                    System.out.println("cambio colore del rettangolo in " + colore);
                } else if (formaCorrente.getForma() instanceof FormaComposta){
                    FormaComposta fc = (FormaComposta) formaCorrente.getForma();
                    fc.setColoreInterno(colore);
                    System.out.println("cambio colore della forma composta in " + colore);
                } else if (formaCorrente.getForma() instanceof Poligono) {
                    Poligono poligono = (Poligono) formaCorrente.getForma();
                    poligono.setColoreInterno(colore);
                    System.out.println("cambio colore del rettangolo in " + colore);
                }else if (formaCorrente.getForma() instanceof Testo) {
                    Testo testo = (Testo) formaCorrente.getForma();
                    testo.setColoreInterno(colore);
                }
                ((FormaSelezionataDecorator) f).decorate();

            }
        }
    }

    /**
     * Cambia il colore del contorno della figura selezionata con @colore.
     * @param colore colore deciso dall'utente per aggiornare la figura selezionata
     */
    public void changeOutlineColor(Color colore) {
        System.out.println("model: " + colore);

        for(Forma f:forme){
            if(f instanceof FormaSelezionataDecorator){

                System.out.println("colore aggiornato");
                f.setColore(colore);
            }
        }
    }

    /**
     * Ridimensiona la forma attualmente selezionata.
     * @param larghezza La larghezza desiderata per la forma. Per i poligoni,
     * questo valore viene utilizzato come riferimento per il calcolo
     * del fattore di scala sull'asse X della bounding box.
     * @param altezza L'altezza desiderata per la forma. Per i poligoni, questo valore
     * viene utilizzato come riferimento per il calcolo del fattore di scala
     * sull'asse Y della bounding box.
     */
    public void resize(double larghezza, double altezza) {
        // Itera attraverso le forme per trovare quella selezionata
        System.out.println("model");
        Forma formaDaRidimensionare = null;
        for (Forma f : forme) {
            if (f instanceof FormaSelezionataDecorator) {
                formaDaRidimensionare = ((FormaSelezionataDecorator) f).getForma();
                break;
            }
        }

        if (formaDaRidimensionare == null) {
            System.err.println("Nessuna forma selezionata trovata per il ridimensionamento.");
            return;
        }

        if (formaDaRidimensionare instanceof Poligono) {
            Poligono poligono = (Poligono) formaDaRidimensionare;
            double larghezzaCorrente = poligono.getLarghezza(); // Larghezza attuale della bounding box
            double altezzaCorrente = poligono.getAltezza();     // Altezza attuale della bounding box

            // Calcola i fattori di scala (nuova dimensione / dimensione corrente)
            double fattoreScalaX = (larghezzaCorrente > 0) ? larghezza / larghezzaCorrente : 1.0;
            double fattoreScalaY = (altezzaCorrente > 0) ? altezza / altezzaCorrente : 1.0;
            poligono.scala(fattoreScalaX, fattoreScalaY);

            System.out.println("  Poligono ridimensionato. Nuove dimensioni bounding box: W=" + poligono.getLarghezza() + ", H=" + poligono.getAltezza());

        } else if (formaDaRidimensionare instanceof Rettangolo) {
            Rettangolo rettangolo = (Rettangolo) formaDaRidimensionare;
            rettangolo.setLarghezza(larghezza);
            rettangolo.setAltezza(altezza);
            System.out.println("  Tipo: Rettangolo. Impostate nuove dimensioni: W=" + larghezza + ", H=" + larghezza);

        } else if (formaDaRidimensionare instanceof Testo){
            Testo testo = (Testo) formaDaRidimensionare;
            testo.setLarghezza(larghezza);
            testo.setAltezza(altezza);
            System.out.println("  Tipo: Testo. Impostate nuove dimensioni: W=" + larghezza + ", H=" + larghezza);
        }else if (formaDaRidimensionare instanceof Ellisse) {
            Ellisse ellisse = (Ellisse) formaDaRidimensionare;
            ellisse.setLarghezza(larghezza);
            ellisse.setAltezza(altezza);
            System.out.println("  Tipo: Ellisse. Impostate nuove dimensioni: W=" + larghezza + ", H=" + larghezza);

        } else {
            Linea linea = (Linea) formaDaRidimensionare;
            linea.setLarghezza(larghezza);
            System.out.println("  Tipo: Linea. Impostata nuova dimensione: W=" + larghezza + ", H=" + larghezza);
        }
    }

    public int countFormeSelezionate() {
        int count = 0;
        Iterator<Forma> it = forme.iterator();
        while (it.hasNext()) {
            Forma f = it.next();
            if (f instanceof FormaSelezionataDecorator) {
                count++;
            }
        }
        return count;
    }

    public void creaFormaComposta(){
        FormaComposta fc = new FormaComposta();
        Iterator<Forma> it = forme.iterator();
        while (it.hasNext()) {
            Forma f = it.next();
            if (f instanceof FormaSelezionataDecorator) {
                f = ((FormaSelezionataDecorator) f).undecorate();
                fc.add(f);
                it.remove();
            }
        }
        forme.add(fc);
    }

    public void rotation(double angoloSelezionato){
        System.out.println("model: " + angoloSelezionato);

        for(Forma f:forme){
            if(f instanceof FormaSelezionataDecorator){

                System.out.println("angolo aggiornato");
                ((FormaSelezionataDecorator) f).getForma().setAngoloInclinazione(angoloSelezionato);
            }
        }
    }

    public void reflect(){
        Forma formaSelezionata = null;
        for(Forma f:forme){
            if(f instanceof FormaSelezionataDecorator){
                formaSelezionata = f;
                break;
            }
        }
        if (formaSelezionata != null) {
            Forma forma = ((FormaSelezionataDecorator) formaSelezionata).getForma();

            forma.specchia();
        }
    }
}
