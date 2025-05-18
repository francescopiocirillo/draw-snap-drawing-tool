package it.unisa.software_architecture_design.drawsnapdrawingtool.interactionstate;

import it.unisa.software_architecture_design.drawsnapdrawingtool.forme.Forma;
import it.unisa.software_architecture_design.drawsnapdrawingtool.forme.FormaSelezionataDecorator;
import javafx.scene.input.MouseEvent;

import java.util.ArrayList;
import java.util.List;

/**
 * La classe {@code SelectState} rappresenta lo stato di selezione e per mezzo del Pattern State
 * si occupa della logica degli handler relativi agli eventi di interazione con il canvas nello stato di selezione
 * permettendo la selezione di una figura e il suo spostamento.
 */
public class SelectState implements DrawingState{

    private double offsetX;
    private double offsetY;

    /**
     * Gestisce l'evento di pressione del mouse sul canvas per permettere la selezione di una figura
     * @param event l'evento di pressione del mouse
     * @param forme lista delle forme presenti sul foglio di disegno
     */
    @Override
    public void handleMousePressed(MouseEvent event, List<Forma> forme) {
        double coordinataX = event.getX();
        double coordinataY = event.getY();

        Forma formaSelezionata = null;

        for (Forma f : forme) {
            if(f.contiene(coordinataX, coordinataY)){
                formaSelezionata = f;
                // offset utili per lo spostamento della figura in caso di MouseDragged
                offsetX = coordinataX - f.getCoordinataX();
                offsetY = coordinataY - f.getCoordinataY();
                break;
            }
        }

        List<Forma> formeNonSelezionate = new ArrayList<>(forme);
        if (formaSelezionata != null) {
            forme.remove(formaSelezionata);
            formeNonSelezionate.remove(formaSelezionata);
            // una forma già decorata con la selezione non ha bisogno di essere ridecorata
            if (formaSelezionata instanceof FormaSelezionataDecorator) {
                forme.add(formaSelezionata);
            }else {
                forme.add(new FormaSelezionataDecorator(formaSelezionata));
            }
        }

        deselezionaHelper(forme, formeNonSelezionate);
    }

    /**
     * Metodo di utilità che permette la deselezione di tutte le Forme della lista {@code forme} che sono
     * presenti in {@code formeDaDeselezionare} e che sono selezionate.
     * @param forme la lista di tutte le forme
     * @param formeDaDeselezionare la lista delle forme da deselezionare
     */
    public void deselezionaHelper(List<Forma> forme, List<Forma> formeDaDeselezionare) {
        for(Forma f : formeDaDeselezionare){
            if(f instanceof  FormaSelezionataDecorator){
                forme.remove(f);
                forme.add(((FormaSelezionataDecorator) f).getForma());
            }
        }
    }

    /**
     * Gestisce l'evento di trascinamento del mouse permettendo lo spostamento della forma selezionata.
     * @param event l'evento di trascinamento del mouse
     * @param forme la lista di forme presenti sul canvas
     */
    @Override
    public void handleMouseDragged(MouseEvent event, List<Forma> forme) {
        double mouseX = event.getX();
        double mouseY = event.getY();

        for (Forma f : forme) {
            if (f instanceof FormaSelezionataDecorator) {
                Forma formaOriginale = ((FormaSelezionataDecorator) f).getForma();

                double newX = mouseX - offsetX;
                double newY = mouseY - offsetY;

                formaOriginale.setCoordinataX(newX);
                formaOriginale.setCoordinataY(newY);
                break; // Presupponendo una sola forma selezionata
            }
        }
    }

    /**
     * METODO MOMENTANEAMENTE NON NECESSARI0
     * @param event evento di rilascio del mouse
     */
    @Override
    public void handleMouseReleased(MouseEvent event) {
        //NA
    }
}
