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
            if(current instanceof FormaComposta){
                ((FormaComposta) current).decorate();
            }
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
}
