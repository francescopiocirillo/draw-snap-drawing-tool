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
    private List<Forma> forme = null; //Lista di forme presenti nel canvas
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

    /*
     * Logica sul Model
     */

    /**
     * Gestisce la popolazione della {@link List} interna al {@link DrawSnapModel}
     * con il contenuto dell'istanza {@link DrawSnapModel} passata come @param
     * @param nuovoModel è l'istanza di {@link DrawSnapModel} da cui prendere le {@link Forma}.
     */
    public void rebuildForme(DrawSnapModel nuovoModel){
        forme.clear();
        Iterator<Forma> it = nuovoModel.getIteratorForme();
        while (it.hasNext()) {
            Forma formaCorrente = it.next();
            forme.add(formaCorrente);
        }
    }

    /*
     * Logica sulla lista di forme presenti nel foglio
     */

    /**
     * Gestisce l'aggiunta di una {@code Forma} al
     * {@link javafx.scene.canvas.GraphicsContext} del {@link javafx.scene.canvas.Canvas}
     * @param f è la {@code Forma} da aggiungere
     */
    public void add(Forma f){
        forme.add(f);
    }

    /**
     * Gestisce la rimozione di una {@link Forma} dal
     * {@link javafx.scene.canvas.GraphicsContext} del {@link javafx.scene.canvas.Canvas}
     * @param f è la {@link Forma} da rimuovere
     */
    public void remove(Forma f){
        forme.remove(f);
    }

    /**
     * Gestisce la rimozione di tutte le {@link Forma} presenti nel
     * {@link javafx.scene.canvas.GraphicsContext} del {@link javafx.scene.canvas.Canvas}
     */
    public void clear(){
        forme.clear();
    }

    /**
     * Gestisce l'aggiunta di una {@link List} si {@link Forma} nel
     * {@link javafx.scene.canvas.GraphicsContext} del {@link javafx.scene.canvas.Canvas}
     * @param forme è la {@link List} di {@link Forma} da aggiungere
     */
    public void addAll(List<Forma> forme){
        this.forme.addAll(forme);
    }

    /**
     * Gestisce il prelevamento di una {@link Forma} presente nella
     * {@link List} di {@link Forma} tramite indice
     * @param index è l'indice della {@link Forma}
     * @return la {@link Forma} corrispondente all'indice
     */
    public Forma get(int index){
        return forme.get(index);
    }

    /**
     * Gestisce il prelevamento della prima {@link Forma} aggiunta nella
     * {@link List} di {@link Forma}
     * @return la prima {@link Forma} presente nella {@link List}
     */
    public Forma getFirst(){
        return forme.getFirst();
    }

    /**
     * Gestisce la creazione di un {@link Iterator} per la
     * {@link List} di {@link Forma}
     * @return {@link Iterator}
     */
    public Iterator<Forma> getIteratorForme() {
        return forme.iterator();
    }

    /**
     * Verifiva se la {@link List} di {@link Forma} è vuota
     * @return {@code true} se la {@link List} è vuota, altrimenti {@code false}
     */
    public boolean isEmpty(){
        return forme.isEmpty();
    }

    /**
     * Fornisce la lunghezza della {@link List} di {@link Forma}
     * @return numero di {@link Forma} presenti nella {@link List}
     */
    public int size(){
        return forme.size();
    }

    /**
     * Verifica che la {@link Forma} sia presente nel {@link javafx.scene.canvas.GraphicsContext}
     * del {@link javafx.scene.canvas.Canvas}
     * @param f è la {@link Forma} di cui controllare la presenza
     * @return {@code true} se la {@link Forma} è presente, altrimenti {@code false}
     */
    public boolean contains(Forma f){
        return forme.contains(f);
    }

    /**
     * Fornisce una copia della {@link List} di {@link Forma}
     * @return una copia della {@link List} di {@link Forma}
     */
    public List<Forma> getCopy() {
        List<Forma> copia = new ArrayList<>();
        for (Forma f : forme) {
            copia.add(f.clone());
        }
        return copia;
    }

    /*
     * Logica sulla lista di forme copiate
     */

    /**
     * Gestisce l'aggiunta di una {@link Forma} alla {@link List}
     * di {@link Forma} copiate
     * @param f è la {@link Forma} da aggiungere
     */
    public void addFormaCopiata(Forma f){
        formeCopiate.add(f);
    }

    /**
     * Gestisce la rimozione di una {@link Forma} dalla {@link List}
     * di {@link Forma} copiate
     * @param f è la {@link Forma} da rimuovere
     */
    public void removeFormaCopiata(Forma f){
        formeCopiate.remove(f);
    }

    /**
     * Gestisce la rimozione di tutte le {@link Forma} dalla {@link List}
     * di {@link Forma} copiate
     */
    public void clearFormeCopiate(){
        formeCopiate.clear();
    }

    /**
     * Gestisce il prelevamento di una {@link Forma} presente nella
     * {@link List} di {@link Forma} copiate tramite indice
     * @param index è l'indice della {@link Forma}
     * @return la {@link Forma} corrispondente all'indice
     */
    public Forma getFormaCopiata(int index){
        return formeCopiate.get(index);
    }

    /**
     * Gestisce il prelevamento della prima {@link Forma} aggiunta nella
     * {@link List} di {@link Forma} copiate
     * @return la prima {@link Forma} copiata nella {@link List}
     */
    public Forma getFirstFormaCopiata(){
        return formeCopiate.getFirst();
    }

    /**
     * Gestisce il prelevamento dell'ultima {@link Forma} aggiunta nella
     * {@link List} di {@link Forma}
     * @return l'ultima {@link Forma} copiata nella {@link List}
     */
    public Forma getLastFormaCopiata(){return formeCopiate.getLast();}

    /**
     * Verifiva se la {@link List} di {@link Forma} copiate è vuota
     * @return {@code true} se la {@link List} è vuota, altrimenti {@code false}
     */
    public boolean isEmptyFormeCopiate(){
        return formeCopiate.isEmpty();
    }

    /**
     * Fornisce la lunghezza della {@link List} di {@link Forma} copiate
     * @return numero di {@link Forma} copiate
     */
    public int sizeFormeCopiate(){
        return formeCopiate.size();
    }

    /*
     * Logica sulle Forme Selezionate
     */

    /**
     * Gestisce la rimozione della {@link Forma} e la sua riaggiunta dopo
     * essere stata decodarata in una {@link FormaSelezionataDecorator}
     * @param formaSelezionata è la {@link Forma} da selezionare
     * @return la {@link FormaSelezionataDecorator}
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
     * Gestisce la deseleziona, rimuovendo il {@link FormaSelezionataDecorator},
     * di tutte le {@link Forma} memorizzate eccetto quella fornita come @param.
     * La deselezione avviene sulla {@link List} interna al {@link DrawSnapModel}
     * @param formaSelezionata è la {@link FormaSelezionataDecorator} da non deselezionare
     */
    public void deselezionaEccetto(Forma formaSelezionata) {
        deselezionaEccetto(formaSelezionata, this.forme);
    }

    /**
     * Gestisce la deseleziona, rimuovendo il {@code FormaSelezionataDecorator},
     * di tutte le {@code Forma} memorizzate eccetto quella fornita come @param.
     * La deselezione avviene sulla {@link List} fornita.
     * @param formaSelezionata è la {@link FormaSelezionataDecorator} da non deselezionare
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
     * Gestisce il prelevamente della {@link FormaSelezionataDecorator} presente
     * nella {@link List} di {@link Forma}
     * @return è la {@link FormaSelezionataDecorator}
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
     * Verifica che ci sia una {@link FormaSelezionataDecorator}
     * nella {@link List} di {@link Forma}
     * @return {@code true} se è presente una {@link FormaSelezionataDecorator},
     * altrimenti {@code false}
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
     * Fornisce il numero di {@link FormaSelezionataDecorator} presente nella
     * {@link List} di {@link Forma}
     * @return il numero di {@link FormaSelezionataDecorator}
     */
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

    /*
     * Logica sul Memento
     */

    /**
     * Gestisce il salvataggio dello stato attuale dell'applicazione in un
     * {@link DrawSnapMemento}
     * @return il {@link DrawSnapMemento} contenente lo stato attuale dell'applicazione
     */
    public DrawSnapMemento saveToMemento() {
        List<Forma> copia = getCopy();
        deselezionaEccetto(null, copia);
        return new DrawSnapMemento(copia); // deep copy dello stato corrente
    }

    /**
     * Gestisce il ripristino dello stato dell'applicazione a quello del
     * {@link DrawSnapMemento} fornito
     * @param memento è il {@link DrawSnapMemento} da ripristinare
     */
    public void restoreFromMemento(DrawSnapMemento memento) {
        this.forme.clear();
        this.forme.addAll(memento.getSavedState());
    }

    /*
     * Logica di Receiver dei Command
     */

    /**
     * Gestisce lo spostamento di una {@link Forma} all'inizio della
     * {@link List} di {@link Forma}
     * @param f è la {@link Forma} da spostare
     */
    public void moveToBack(Forma f) {
        forme.remove(f);
        forme.addFirst(f);
    }

    /**
     * Gestisce lo spostamento di una {@link Forma} alla fine della
     * {@link List} di {@link Forma}
     * @param f la {@link Forma} da spostare
     */
    public void moveToFront(Forma f) {
        forme.remove(f);
        forme.add(f);
    }

    /**
     * Gestisce il cambio del {@link Color} interno della {@link FormaSelezionataDecorator}
     * presente nella {@link List} di {@link Forma}.
     * Il {@link Color} interno non può essere cambiato se si tratta di una {@link Linea}
     * @param colore è il {@link Color} deciso dall'utente per aggiornare la {@link FormaSelezionataDecorator}
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
     * Gestisce il cambio del {@link Color} del bordo della {@link FormaSelezionataDecorator}
     * presente nella {@link List} di {@link Forma}
     * @param colore è il {@link Color} deciso dall'utente per aggiornare la {@link FormaSelezionataDecorator}
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
     * Gestisce lo stiramento della {@link FormaSelezionataDecorator} presente
     * nella {@link List} di {@link Forma}.
     * @param larghezza La larghezza desiderata per la {@link Forma}. Per i {@link Poligono},
     * questo valore viene utilizzato come riferimento per il calcolo
     * del fattore di scala sull'asse X della bounding box.
     * @param altezza L'altezza desiderata per la {@link Forma}. Per i {@link Poligono}, questo valore
     * viene utilizzato come riferimento per il calcolo del fattore di scala
     * sull'asse Y della bounding box.
     */
    public void stretch(double larghezza, double altezza) {
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

    /**
     * Gestisce il cambio di dimensione proporzionale della {@link FormaSelezionataDecorator}
     * presente nella {@link List} di {@link Forma}
     * @param proporzione è la nuova dimensione proporzionale della {@link FormaSelezionataDecorator}
     */
    public void proportionalResize(double proporzione) {
        // Itera attraverso le forme per trovare quella selezionata
        System.out.println("model" );
        Forma formaDaRidimensionare = null;
        for (Forma f : forme) {
            if (f instanceof FormaSelezionataDecorator) {
                formaDaRidimensionare = ((FormaSelezionataDecorator) f).getForma();
                break; // Una volta trovata la forma selezionata, esci dal ciclo
            }
        }

        if (formaDaRidimensionare == null) {
            System.err.println("Nessuna forma selezionata trovata per il ridimensionamento.");
            return;
        }

        formaDaRidimensionare.proportionalResize(proporzione);
    }

    /**
     * Gestisce il cambio di angolo della {@link FormaSelezionataDecorator}
     * presente nella {@link List} di {@link Forma}
     * @param angoloSelezionato è il nuovo angolo della {@link FormaSelezionataDecorator}
     */
    public void rotation(double angoloSelezionato){
        System.out.println("model: " + angoloSelezionato);

        if(angoloSelezionato < 0){
            angoloSelezionato = angoloSelezionato + 360;
        }
        for(Forma f:forme){
            if(f instanceof FormaSelezionataDecorator){

                System.out.println("angolo aggiornato");
                ((FormaSelezionataDecorator) f).getForma().setAngoloInclinazione(angoloSelezionato);
            }
        }
    }

    /**
     * Gestisce lo specchiamento della {@link FormaSelezionataDecorator}
     * presente nella {@link List} di {@link Forma}
     * @param horizontal è il {@link Boolean} che distingue i casi di specchiamento
     *                   orizzontale {@code true} e verticale {@code false}
     */
    public void reflect(boolean horizontal){
        Forma formaSelezionata = null;
        for(Forma f:forme){
            if(f instanceof FormaSelezionataDecorator){
                formaSelezionata = f;
                break;
            }
        }
        if (formaSelezionata != null) {
            Forma forma = ((FormaSelezionataDecorator) formaSelezionata).getForma();
            if(horizontal){
                forma.specchiaInOrizzontale();
            }
            else{
                forma.specchiaInVerticale();
            }
        }
    }

    /*
     * Logica delle forme composte
     */

    /**
     *
     */
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

    /**
     *
     */
    public void decomponiFormaSelezionata(){
        Forma f = getFormaSelezionata();
        forme.remove(f);
        f = ((FormaSelezionataDecorator)f).undecorate();
        if (f instanceof FormaComposta) {
            List<Forma> formeComponenti = ((FormaComposta) f).getForme();
            forme.addAll(formeComponenti);
        }
    }
}
