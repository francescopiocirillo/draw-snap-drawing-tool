package it.unisa.software_architecture_design.drawsnapdrawingtool.interactionstate;

import it.unisa.software_architecture_design.drawsnapdrawingtool.DrawSnapModel;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.MouseEvent;

/**
 * La classe {@code MoveCanvasState} rappresenta lo stato di movimento sul canva e per mezzo del Pattern State
 * si occupa della logica degli handler relativi agli eventi di interazione con il canvas nello stato di movimento
 * permettendo lo spostamento della visuale della tela.
 */
public class MoveCanvasState implements DrawingState {
    /*
     * Attributi
     */
    private final ScrollPane scrollPane;
    private double lastX;
    private double lastY;
    /*
     * Costruttore
     */
    public MoveCanvasState(ScrollPane scrollPane) {
        this.scrollPane = scrollPane;
    }

    /**
     * Gestisce l'evento di pressione del mouse sul canvas per permettere lo spostamento
     * @param event l'evento di pressione del mouse
     * @param forme la lista di forme presenti sul canvas (non utilizzato in questo metodo)
     */
    @Override
    public boolean handleMousePressed(MouseEvent event, DrawSnapModel forme) {
        lastX = event.getSceneX();
        lastY = event.getSceneY();
        return true;
    }

    /**
     * Gestisce l'evento di trascinamento del mouse per implementare lo spostamento del canvas.
     * Quando il mouse viene trascinato, aggiorna i valori di scorrimento della ScrollPane
     * traslando la vista in base allo spostamento del mouse.
     * @param event l'evento di trascinamento del mouse
     * @param forme la lista di forme presenti sul canvas (non utilizzato in questo metodo)
     */
    @Override
    public boolean handleMouseDragged(MouseEvent event, DrawSnapModel forme) {
        double currentX = event.getSceneX();
        double currentY = event.getSceneY();

        double deltaX = currentX - lastX;
        double deltaY = currentY - lastY;

        // Calcolo dei nuovi valori di scorrimento
        double newHValue = scrollPane.getHvalue() - deltaX / scrollPane.getContent().getBoundsInLocal().getWidth();
        double newVValue = scrollPane.getVvalue() - deltaY / scrollPane.getContent().getBoundsInLocal().getHeight();

        // Limita i valori tra 0 e 1
        scrollPane.setHvalue(Math.max(0, Math.min(1, newHValue)));
        scrollPane.setVvalue(Math.max(0, Math.min(1, newVValue)));

        lastX = currentX;
        lastY = currentY;

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
