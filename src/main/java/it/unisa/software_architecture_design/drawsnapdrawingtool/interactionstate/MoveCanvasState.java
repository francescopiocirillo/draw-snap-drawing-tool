package it.unisa.software_architecture_design.drawsnapdrawingtool.interactionstate;

import it.unisa.software_architecture_design.drawsnapdrawingtool.DrawSnapModel;
import javafx.scene.Cursor;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.canvas.Canvas;


/**
 * La classe {@code MoveCanvasState} rappresenta lo stato di movimento sul canva e per mezzo del Pattern State
 * si occupa della logica degli handler relativi agli eventi di interazione con il canvas nello stato di movimento
 * permettendo lo spostamento della visuale della tela.
 */
public class MoveCanvasState implements DrawingState {
    /*
     * Attributi
     */
    private final Canvas canvas;
    private final ScrollPane scrollPane;
    private double lastX;
    private double lastY;
    /*
     * Costruttore
     */
    public MoveCanvasState(Canvas canvas, ScrollPane scrollPane) {
        this.canvas = canvas;
        this.scrollPane = scrollPane;
    }

    /**
     * Gestisce l'evento di pressione del mouse sul canvas per permettere lo spostamento
     * @param event l'evento di pressione del mouse
     * @param forme la lista di forme presenti sul canvas (non utilizzato in questo metodo)
     * @param coordinataX coordinata logica per l'asse x dell'evento mouse
     * @param coordinataY coordinata logica per l'asse y dell'evento mouse
     */
    @Override
    public boolean handleMousePressed(MouseEvent event, DrawSnapModel forme, double coordinataX, double coordinataY) {
        lastX = coordinataX;
        lastY = coordinataY;
        canvas.setCursor(Cursor.CLOSED_HAND);
        return false;
    }

    /**
     * Gestisce l'evento di trascinamento del mouse per implementare lo spostamento del canvas.
     * Quando il mouse viene trascinato, aggiorna i valori di scorrimento della ScrollPane
     * traslando la vista in base allo spostamento del mouse.
     * @param event l'evento di trascinamento del mouse
     * @param forme la lista di forme presenti sul canvas (non utilizzato in questo metodo)
     * @param coordinataX coordinata logica per l'asse x dell'evento mouse
     * @param coordinataY coordinata logica per l'asse y dell'evento mouse
     */
    @Override
    public boolean handleMouseDragged(MouseEvent event, DrawSnapModel forme, double coordinataX, double coordinataY) {
        double deltaX = coordinataX - lastX;
        double deltaY = coordinataY - lastY;

        double contentWidth = scrollPane.getContent().getBoundsInLocal().getWidth();
        double contentHeight = scrollPane.getContent().getBoundsInLocal().getHeight();

        double viewportWidth = scrollPane.getViewportBounds().getWidth();
        double viewportHeight = scrollPane.getViewportBounds().getHeight();

        double hDelta = deltaX / (contentWidth - viewportWidth);
        double vDelta = deltaY / (contentHeight - viewportHeight);

        // Applica lo spostamento ai valori di scorrimento, limitandoli all'intervallo [0, 1]
        scrollPane.setHvalue(clamp(scrollPane.getHvalue() - hDelta, 0, 1));
        scrollPane.setVvalue(clamp(scrollPane.getVvalue() - vDelta, 0, 1));

        lastX = coordinataX;
        lastY = coordinataY;

        return false;
    }
    /**
     * Gestisce l'evento di rilascio del mouse sul canvas e rende il cursore una manina aperta
     * @param event evento di rilascio del mouse
     * @param coordinataX coordinata logica per l'asse x dell'evento mouse
     * @param coordinataY coordinata logica per l'asse y dell'evento mouse
     */
    @Override
    public boolean handleMouseReleased(MouseEvent event, double coordinataX, double coordinataY) {
        canvas.setCursor(Cursor.OPEN_HAND);
        return false;
    }

    /**
     * Restringe un valore all'interno di un intervallo specificato.
     * @param value il valore da limitare
     * @param min il valore minimo consentito
     * @param max il valore massimo consentito
     * @return il valore limitato nell'intervallo [min, max]
     */
    private double clamp(double value, double min, double max) {
        return Math.max(min, Math.min(max, value));
    }
}
