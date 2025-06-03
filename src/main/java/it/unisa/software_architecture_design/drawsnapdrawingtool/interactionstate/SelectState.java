package it.unisa.software_architecture_design.drawsnapdrawingtool.interactionstate;

import it.unisa.software_architecture_design.drawsnapdrawingtool.DrawSnapModel;
import it.unisa.software_architecture_design.drawsnapdrawingtool.forme.Forma;
import it.unisa.software_architecture_design.drawsnapdrawingtool.forme.FormaComposta;
import it.unisa.software_architecture_design.drawsnapdrawingtool.forme.FormaSelezionataDecorator;
import it.unisa.software_architecture_design.drawsnapdrawingtool.forme.Linea;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Button;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ToolBar;
import javafx.scene.input.MouseEvent;

import java.util.Iterator;

/**
 * La classe {@link SelectState} rappresenta lo stato di selezione e per mezzo del Pattern State
 * si occupa della logica degli {@link javafx.event.EventHandler} relativi ai
 * {@link MouseEvent} di interazione con il {@link javafx.scene.canvas.Canvas} nello stato di selezione
 * permettendo la selezione di una {@link Forma} e il suo spostamento.
 */
public class SelectState implements DrawingState{
    /*
     * Attributi
     */
    private final ToolBar toolBarFX; // barra in alto delle modifiche
    private final Button changeFillColorButton;
    private final MenuItem composeButton; // pulsante per comporre le forme

    /*
     * Costruttore, getter e setter
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
     * Gestisce il {@link MouseEvent} di pressione sul {@link javafx.scene.canvas.Canvas} per
     * permettere la selezione di una {@link Forma}
     * @param event è il {@link MouseEvent} di pressione che ha causato la chiamata al metodo
     * @param forme è il {@link DrawSnapModel}  presente nel {@link Canvas}
     * @param coordinataX è la coordinata logica per l'asse x del {@link MouseEvent}
     * @param coordinataY è la coordinata logica per l'asse y del {@link MouseEvent}
     * @return {@code false} poiché il {@link MouseEvent} di pressione, inclusa la logica di
     * selezione e gestione delle {@link Forma},è completamente gestito da questo metodo e
     * non necessita di ulteriori elaborazioni da parte di altre componenti.
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
     * Gestisce il {@link MouseEvent} di trascinamento sul {@link Canvas} per
     * permettere lo spostamento della {@link FormaSelezionataDecorator}
     * @param event è il {@link MouseEvent} di trascinamento che ha causato la chiamata al metodo
     * @param forme è il {@link DrawSnapModel}  presente nel {@link Canvas}
     * @param coordinataX è la coordinata logica per l'asse x del {@link MouseEvent}
     * @param coordinataY è la coordinata logica per l'asse y del {@link MouseEvent}
     * @return {@code true} se una {@link FormaSelezionataDecorator} è stata spostata con successo,
     * {@code false} se non ci sono {@link FormaSelezionataDecorator} o se ci sono più
     * {@link FormaSelezionataDecorator} (il metodo non gestisce lo spostamento in questi casi).
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
     * @param event è il {@link MouseEvent} di rilascio che ha causato la chiamata al metodo
     * @param forme è il {@link DrawSnapModel}  presente nel {@link Canvas}
     * @param coordinataX è la coordinata logica per l'asse x del {@link MouseEvent}
     * @param coordinataY è la coordinata logica per l'asse y del {@link MouseEvent}
     * @return {@code false} poiché il metodo non implementa alcuna funzionalità al momento ed
     * è marcato come non necessario.
     */
    @Override
    public boolean handleMouseReleased(MouseEvent event, DrawSnapModel forme, double coordinataX, double coordinataY) {
        //NA
        return false;
    }

    /*
     * Metodi Ausiliari
     */

    /**
     * Gestisce la disabilitazione della {@code toolBarFX} quando non necessaria
     */
    public void disattivaToolBar() {
        if(toolBarFX != null) {
            toolBarFX.setDisable(true); //disabilita la barra in alto delle modifiche
        }
    }

    /**
     * Gestisce l'abilitazione della {@code toolBarFX} quando necessaria
     */
    public void attivaToolBar() {
        if(toolBarFX != null) {
            toolBarFX.setDisable(false); //abilita la barra in alto delle modifiche
        }
    }

    /**
     * Gestisce la disabilitazione del {@code composeButton} quando non necessario
     */
    public void disattivaComposeButton() {
        if(composeButton != null) {
            composeButton.setDisable(true); //disabilita la barra in alto delle modifiche
        }
    }

    /**
     * Gestisce l'abilitazione del {@code composeButton} quando necessario
     */
    public void attivaComposeButton() {
        if(composeButton != null) {
            composeButton.setDisable(false); //abilita la barra in alto delle modifiche
        }
    }

    /**
     * Gestisce l'abilitazione o la disabilitazione del {@code changeFillColorButton}
     * in base alla {@link FormaSelezionataDecorator}.
     */
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

    /**
     * Gestisce l'abilitazione o la disabilitazione del {@code changeFillColorButton}
     * tramite booleano
     * @param disable {@code true} per disabilitare il {@link Button}, {@code false} per abilitarlo.
     */
    private void changeFillColorButtonDisable(boolean disable) {
        if(changeFillColorButton != null) {
            changeFillColorButton.setDisable(disable);
        }
    }
}
