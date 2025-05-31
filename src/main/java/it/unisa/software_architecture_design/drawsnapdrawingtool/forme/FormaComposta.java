package it.unisa.software_architecture_design.drawsnapdrawingtool.forme;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.List;

public class FormaComposta extends Forma{

    private List<Forma> forme;

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

    @Override
    public void setColore(Color color) {
        for(Forma f:forme){
            f.setColore(color);
        }
    }

    public void setColoreInterno(Color color) {
        for(Forma formaCorrente:forme){
            if(formaCorrente instanceof Ellisse){
                Ellisse ellisse = (Ellisse) formaCorrente;
                ellisse.setColoreInterno(color);
                System.out.println("cambio colore dell'ellissi in " + color);
            } else if (formaCorrente instanceof Rettangolo) {
                Rettangolo rettangolo = (Rettangolo) formaCorrente;
                rettangolo.setColoreInterno(color);
                System.out.println("cambio colore del rettangolo in " + color);
            } else if (formaCorrente instanceof FormaComposta){
                FormaComposta fc = (FormaComposta) formaCorrente;
                fc.setColoreInterno(color);
                System.out.println("cambio colore della forma composta in " + color);
            }else if (formaCorrente instanceof Poligono) {
                Poligono poligono = (Poligono) formaCorrente;
                poligono.setColoreInterno(color);
                System.out.println("cambio colore del rettangolo in " + color);
            }else if (formaCorrente instanceof Testo) {
                Testo testo = (Testo) formaCorrente;
                testo.setColoreInterno(color);
            }
        }
    }

    @Override
    public Forma clone() {
        List<Forma> newListaForme = new ArrayList<>();
        for(Forma f:forme){
            newListaForme.add(f.clone());
        }
        Forma newForma = new FormaComposta(newListaForme);
        return newForma;
    }

    @Override
    public void proportionalResize(double proporzione){
        for(Forma f:forme){
            f.proportionalResize(proporzione);
        }
    }

    @Override
    public void specchia(){
        for(Forma f:forme){
            f.specchia();
        }
    }

    public void setAngoloInclinazione(double angoloDiInclinazione){
        for(Forma f:forme){
            f.setAngoloInclinazione(angoloDiInclinazione);
        }
    }

    public void setCoordinataXForDrag(double coordinataXMouseDragged){
        for(Forma f:forme){
            f.setCoordinataXForDrag(coordinataXMouseDragged);
        }
    }

    public void setCoordinataYForDrag(double coordinataYMouseDragged){
        for(Forma f:forme){
            f.setCoordinataYForDrag(coordinataYMouseDragged);
        }
    }

    public void setOffsetX(double coordinataXPressed){
        for(Forma f:forme){
            f.setOffsetX(coordinataXPressed);
        }
    }

    public void setOffsetY(double coordinataYPressed){
        for(Forma f:forme){
            f.setOffsetY(coordinataYPressed);
        }
    }
}
