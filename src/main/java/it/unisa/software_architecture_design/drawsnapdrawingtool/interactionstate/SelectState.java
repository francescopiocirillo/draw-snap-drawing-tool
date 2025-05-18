package it.unisa.software_architecture_design.drawsnapdrawingtool.interactionstate;

import it.unisa.software_architecture_design.drawsnapdrawingtool.forme.Forma;
import it.unisa.software_architecture_design.drawsnapdrawingtool.forme.FormaSelezionataDecorator;
import javafx.scene.input.MouseEvent;

import java.util.ArrayList;
import java.util.List;

public class SelectState implements DrawingState{
    /**
     * @param event l'evento di pressione del mouse
     * @param forme lista delle forme presenti sul foglio di disegno
     */
    @Override
    public void handleMousePressed(MouseEvent event, List<Forma> forme) {
        double coordinataX = event.getX();
        double coordinataY = event.getY();
        List<Forma> formeSelezionate = new ArrayList<Forma>();
        for (Forma f : forme) {
            if(f.contiene(coordinataX, coordinataY)){
                formeSelezionate.add(f);
            }
        }
        for(Forma f : formeSelezionate){
            forme.remove(f);
            forme.add(new FormaSelezionataDecorator(f));
        }
    }
    /**
     * @param event l'evento di pressione del mouse
     */
    @Override
    public void handleMouseDragged(MouseEvent event) {

    }

    /**
     * @param event l'evento di pressione del mouse
     */
    @Override
    public void handleMouseReleased(MouseEvent event) {

    }
}
