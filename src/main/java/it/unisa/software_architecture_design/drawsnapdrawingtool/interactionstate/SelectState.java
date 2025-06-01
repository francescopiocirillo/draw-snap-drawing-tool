package it.unisa.software_architecture_design.drawsnapdrawingtool.interactionstate;

import it.unisa.software_architecture_design.drawsnapdrawingtool.DrawSnapModel;
import it.unisa.software_architecture_design.drawsnapdrawingtool.forme.Forma;
import it.unisa.software_architecture_design.drawsnapdrawingtool.forme.FormaComposta;
import it.unisa.software_architecture_design.drawsnapdrawingtool.forme.FormaSelezionataDecorator;
import it.unisa.software_architecture_design.drawsnapdrawingtool.forme.Linea;
import javafx.scene.control.Button;
import javafx.scene.control.MenuItem;
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
    private final ToolBar toolBarFX; // barra in alto delle modifiche
    private final Button changeFillColorButton;
    private final MenuItem composeButton; // pulsante per comporre le forme

    /*
     * Costruttore
     */
    public SelectState(ToolBar toolBarFX, Button changeFillColorButton, MenuItem composeButton) {
        this.toolBarFX = toolBarFX;
        this.changeFillColorButton = changeFillColorButton;
        this.composeButton = composeButton;
    }

    /*
     * Logica della classe
     */

    /**
     * Gestisce l'evento di pressione del mouse sul canvas per permettere la selezione di una figura
     * @param event è l'evento di pressione del mouse
     * @param forme è la lista delle forme presenti sul foglio di disegno
     * @param coordinataX è la coordinata logica per l'asse x dell'evento mouse
     * @param coordinataY è la coordinata logica per l'asse y dell'evento mouse
     * @return
     */
    @Override
    public boolean handleMousePressed(MouseEvent event, DrawSnapModel forme, double coordinataX , double coordinataY) {
        Forma formaSelezionata = null;

        Iterator<Forma> it = forme.getIteratorForme();
        while (it.hasNext()) {
            Forma f = it.next();
            if(f.contiene(coordinataX, coordinataY)){
                formaSelezionata = f;
                // offset utili per lo spostamento della figura in caso di MouseDragged
                f.setOffsetX(coordinataX);
                f.setOffsetY(coordinataY);
                System.out.println("è contenuta");
            }
        }
        if(!event.isControlDown()){
            forme.deselezionaEccetto(formaSelezionata);
        }

        int count = forme.countFormeSelezionate();

        if(count == 0){
            if (formaSelezionata != null) {
                formaSelezionata = forme.selezionaForma(formaSelezionata);
                attivaToolBar();
            }else{
                disattivaToolBar();
            }
            // Se una forma è stata selezionata, la decoriamo per indicare che è selezionata
            forme.deselezionaEccetto(formaSelezionata);
            disattivaChangeFillColorButton(formaSelezionata);
        }else if(count == 1){
            if (formaSelezionata != null) {
                Forma formaSelezionataOld = forme.getFormaSelezionata();
                formaSelezionata = forme.selezionaForma(formaSelezionata);
                if(formaSelezionata != formaSelezionataOld){
                    disattivaToolBar();
                    attivaComposeButton();
                    System.out.println("più di una Forma selezionata, attivo il compose button");
                }
            }else{
                forme.deselezionaEccetto(null);
                disattivaToolBar();
                disattivaComposeButton();
            }
        }else{
            if (formaSelezionata != null) {
                formaSelezionata = forme.selezionaForma(formaSelezionata);
            }else{
                forme.deselezionaEccetto(null);
                disattivaComposeButton();
            }
        }

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

    /**
     * Metodo di utilità per attivare la toolBar quando non necessaria
     */
    public void attivaToolBar() {
        if(toolBarFX != null) {
            toolBarFX.setDisable(false); //abilita la barra in alto delle modifiche
        }
    }

    /**
     * Metodo di utilità per disattivare la toolBar quando non necessaria
     */
    public void disattivaComposeButton() {
        if(composeButton != null) {
            composeButton.setDisable(true); //disabilita la barra in alto delle modifiche
        }
    }

    /**
     * Metodo di utilità per attivare la toolBar quando non necessaria
     */
    public void attivaComposeButton() {
        if(composeButton != null) {
            composeButton.setDisable(false); //abilita la barra in alto delle modifiche
        }
    }

    public void disattivaChangeFillColorButton(Forma formaSelezionata) {
        if(formaSelezionata != null) {
            Forma formaDecorata = ((FormaSelezionataDecorator)formaSelezionata).getForma();
            if (formaDecorata instanceof Linea) {
                changeFillColorButtonDisable(true);
            } else {
                changeFillColorButtonDisable(false);
            }
        }
    }

    private void changeFillColorButtonDisable(boolean disable) {
        if(changeFillColorButton != null) {
            changeFillColorButton.setDisable(disable);
        }
    }

    /**
     * Gestisce l'evento di trascinamento del mouse permettendo lo spostamento della forma selezionata.
     * @param event è l'evento di trascinamento del mouse
     * @param forme è la lista di forme presenti sul canvas
     * @param coordinataX è la coordinata logica per l'asse x dell'evento mouse
     * @param coordinataY è la coordinata logica per l'asse y dell'evento mouse
     * @return
     */
    @Override
    public boolean handleMouseDragged(MouseEvent event, DrawSnapModel forme, double coordinataX, double coordinataY) {
        if(forme.countFormeSelezionate() > 1){
            return false; // Non gestiamo lo spostamento se ci sono più forme selezionate
        }

        boolean result = false;
        Iterator<Forma> it = forme.getIteratorForme();
        while (it.hasNext()) {
            Forma f = it.next();
            if (f instanceof FormaSelezionataDecorator) {
                f.setCoordinataXForDrag(coordinataX);
                f.setCoordinataYForDrag(coordinataY);
                result = true;
                break; // Presupponendo una sola forma selezionata
            }
        }
        return result;
    }

    /**
     * METODO MOMENTANEAMENTE NON NECESSARI0
     * @param event è l'evento di rilascio del mouse
     * @param coordinataX è la coordinata logica per l'asse x dell'evento mouse
     * @param coordinataY è la coordinata logica per l'asse y dell'evento mouse
     * @return
     */
    @Override
    public boolean handleMouseReleased(MouseEvent event, DrawSnapModel forme, double coordinataX, double coordinataY) {
        //NA
        return false;
    }
}
