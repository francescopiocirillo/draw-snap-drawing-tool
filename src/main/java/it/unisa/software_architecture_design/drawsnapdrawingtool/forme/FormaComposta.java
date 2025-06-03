package it.unisa.software_architecture_design.drawsnapdrawingtool.forme;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.List;

/**
 * La classe {@link FormaComposta} rappresenta un insieme di {@link Forma} raggruppate
 */
public class FormaComposta extends Forma{
    /*
     * Attributi
     */
    private List<Forma> forme;

    /*
     * Costruttori, getter e setter
     */
    public FormaComposta() {
        forme = new ArrayList<Forma>();
    }

    public FormaComposta(List<Forma> forme){
        this.forme = forme;
    }

    public List<Forma> getForme() {
        return forme;
    }

    public void add(Forma forma){
        forme.add(forma);
    }

    @Override
    public void setColore(Color color) {
        for(Forma f:forme){
            f.setColore(color);
        }
    }

    public void setColoreInterno(Color color) {
        for(Forma formaCorrente:forme){
            if(formaCorrente instanceof Forma2D){
                Forma2D f2d = (Forma2D) formaCorrente;
                f2d.setColoreInterno(color);
                System.out.println("cambio colore della forma2d in " + color);
            }else if (formaCorrente instanceof Poligono) {
                Poligono poligono = (Poligono) formaCorrente;
                poligono.setColoreInterno(color);
                System.out.println("cambio colore del poligono in " + color);
            } else if (formaCorrente instanceof FormaComposta){
                FormaComposta fc = (FormaComposta) formaCorrente;
                fc.setColoreInterno(color);
                System.out.println("cambio colore della forma composta in " + color);
            }else if (formaCorrente instanceof Testo) {
                Testo testo = (Testo) formaCorrente;
                testo.setColoreInterno(color);
            }
        }
    }

    @Override
    public void setAngoloInclinazione(double angoloDiInclinazione){
        for(Forma f:forme){
            f.setAngoloInclinazione(angoloDiInclinazione);
        }
    }

    @Override
    public void setCoordinataXForDrag(double coordinataXMouseDragged){
        for(Forma f:forme){
            f.setCoordinataXForDrag(coordinataXMouseDragged);
        }
    }

    @Override
    public void setCoordinataYForDrag(double coordinataYMouseDragged){
        for(Forma f:forme){
            f.setCoordinataYForDrag(coordinataYMouseDragged);
        }
    }

    @Override
    public void setOffsetX(double coordinataXPressed){
        for(Forma f:forme){
            f.setOffsetX(coordinataXPressed);
        }
    }

    @Override
    public void setOffsetY(double coordinataYPressed){
        for(Forma f:forme){
            f.setOffsetY(coordinataYPressed);
        }
    }

    @Override
    public void proportionalResize(double proporzione){
        for(Forma f:forme){
            f.proportionalResize(proporzione);
        }
    }

    /**
     * Decora le forme componenti.
     */
    public void decorate(){
        int i = 0;
        int limit = forme.size();
        Forma current;
        while(i<limit){
            current = forme.get(i);
            forme.set(i, new FormaSelezionataDecorator(current));
            i++;
        }
    }

    /**
     * Rimuove la decorazione dalle forme componenti.
     */
    public void undecorate(){
        int i = 0;
        int limit = forme.size();
        Forma current;
        while(i<limit){
            current = forme.get(i);
            if(current instanceof FormaSelezionataDecorator){
                current = ((FormaSelezionataDecorator) current).undecorate();
            }
            forme.set(i, current);
            i++;
        }
    }

    @Override
    public void disegna(GraphicsContext gc) {
        for(Forma f:forme){
            f.disegna(gc);
        }
    }

    @Override
    public boolean contiene(double puntoDaValutareX, double puntoDaValutareY) {
        for(Forma f:forme){
            if(f.contiene(puntoDaValutareX, puntoDaValutareY)){
                return true;
            }
        }
        return false;
    }

    /**
     * Gestisce la ridistribuzione dei valori della {@link Forma} per specchiarla
     * lungo l'asse verticale che passa per il centro della {@link Forma} stessa
     */
    @Override
    public void specchiaInVerticale() {
        for(Forma f:forme){
            f.specchiaInVerticale();
        }
    }

    /**
     * Gestisce la ridistribuzione dei valori della {@link Forma} per specchiarla
     * lungo l'asse orizzontale che passa per il centro della {@link Forma} stessa
     */
    @Override
    public void specchiaInOrizzontale() {
        for(Forma f:forme){
            f.specchiaInOrizzontale();
        }
    }

    /**
     * Gestisce la clonazione dell'oggetto creandone una nuova istanza
     * Permette di modificare l'elemento clonato senza intaccare quello originale
     * @return la {@link Forma} clonata.
     */
    @Override
    public Forma clone() {
        List<Forma> newListaForme = new ArrayList<>();
        for(Forma f:forme){
            newListaForme.add(f.clone());
        }
        Forma newForma = new FormaComposta(newListaForme);
        return newForma;
    }

}
