package it.unisa.software_architecture_design.drawsnapdrawingtool.interactionstate;

import it.unisa.software_architecture_design.drawsnapdrawingtool.DrawSnapModel;
import it.unisa.software_architecture_design.drawsnapdrawingtool.forme.Forma;
import it.unisa.software_architecture_design.drawsnapdrawingtool.forme.FormaSelezionataDecorator;
import it.unisa.software_architecture_design.drawsnapdrawingtool.forme.Linea;
import javafx.scene.control.Button;
import javafx.scene.control.ToolBar;
import javafx.scene.input.MouseEvent;

import java.util.Iterator;

/**
 * La classe {@code SelectState} rappresenta lo stato di selezione e per mezzo del Pattern State
 * si occupa della logica degli handler relativi agli eventi di interazione con il canvas nello stato di selezione
 * permettendo la selezione di una figura e il suo spostamento.
 */
public class SelectState implements DrawingState{
    /*
     * Attributi
     */
    private double offsetX;
    private double offsetY;
    private ToolBar toolBarFX; // barra in alto delle modifiche
    private Button changeFillColorButton;
    /*
     * Costruttore
     */
    public SelectState(ToolBar toolBarFX, Button changeFillColorButton) {
        this.toolBarFX = toolBarFX;
        this.changeFillColorButton = changeFillColorButton;
    }

    /*
     * Logica della classe
     */

    /**
     * Gestisce l'evento di pressione del mouse sul canvas per permettere la selezione di una figura
     * @param event l'evento di pressione del mouse
     * @param forme lista delle forme presenti sul foglio di disegno
     */
    @Override
    public boolean handleMousePressed(MouseEvent event, DrawSnapModel forme) {
        double coordinataX = event.getX();
        double coordinataY = event.getY();

        Forma formaSelezionata = null;

        Iterator<Forma> it = forme.getIteratorForme();
        while (it.hasNext()) {
            Forma f = it.next();
            if(f.contiene(coordinataX, coordinataY)){
                formaSelezionata = f;
                // offset utili per lo spostamento della figura in caso di MouseDragged
                offsetX = coordinataX - f.getCoordinataX();
                offsetY = coordinataY - f.getCoordinataY();
            }
        }

        if (formaSelezionata != null) {
            formaSelezionata = forme.selezionaForma(formaSelezionata);
            if(toolBarFX != null) {
                toolBarFX.setDisable(false); //abilita la barra in alto delle modifiche
            }
        }else{
            if(toolBarFX != null) {
                toolBarFX.setDisable(true); //disabilita la barra in alto delle modifiche
            }
        }

        forme.deselezionaEccetto(formaSelezionata);
        disattivaChangeFillColorButton(formaSelezionata);
        return false;
    }

    /**
     * Metodo di utilità per disattivare la toolBar quando non necessaria
     */
    public void disattivaToolBar() {
        if(toolBarFX != null) {
            toolBarFX.setDisable(true); //disabilita la barra in alto delle modifiche
        }
    }

    public void disattivaChangeFillColorButton(Forma formaSelezionata) {
        if(formaSelezionata != null) {
            Forma formaDecorata = ((FormaSelezionataDecorator)formaSelezionata).getForma();
            if (formaDecorata instanceof Linea) {
                changeFillColorButton.setDisable(true);
            } else {
                changeFillColorButton.setDisable(false);
            }
        }
    }

    /**
     * Gestisce l'evento di trascinamento del mouse permettendo lo spostamento della forma selezionata.
     * @param event l'evento di trascinamento del mouse
     * @param forme la lista di forme presenti sul canvas
     */
    @Override
    public boolean handleMouseDragged(MouseEvent event, DrawSnapModel forme) {
        double mouseX = event.getX();
        double mouseY = event.getY();

        Iterator<Forma> it = forme.getIteratorForme();
        while (it.hasNext()) {
            Forma f = it.next();
            if (f instanceof FormaSelezionataDecorator) {
                double newX = mouseX - offsetX;
                double newY = mouseY - offsetY;

                f.setCoordinataX(newX);
                f.setCoordinataY(newY);
                break; // Presupponendo una sola forma selezionata
            }
        }
        return true;
    }

    /**
     * METODO MOMENTANEAMENTE NON NECESSARI0
     * @param event evento di rilascio del mouse
     */
    @Override
    public boolean handleMouseReleased(MouseEvent event) {
        //NA
        return false;
    }
}
