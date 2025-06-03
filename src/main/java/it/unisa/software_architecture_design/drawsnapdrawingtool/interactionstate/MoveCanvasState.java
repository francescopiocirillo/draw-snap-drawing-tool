package it.unisa.software_architecture_design.drawsnapdrawingtool.interactionstate;

import it.unisa.software_architecture_design.drawsnapdrawingtool.DrawSnapModel;
import javafx.scene.Cursor;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.canvas.Canvas;


/**
 * La classe {@code MoveCanvasState} rappresenta lo stato di movimento sul {@link Canvas} e per mezzo
 * del Pattern State si occupa della logica degli {@link javafx.event.EventHandler} relativi ai
 * {@link MouseEvent} di interazione con il {@link Canvas} nello stato di movimento permettendo
 * lo spostamento della visuale della tela.
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
     * Costruttore, getter e setter
     */
    public MoveCanvasState(Canvas canvas, ScrollPane scrollPane) {
        this.canvas = canvas;
        this.scrollPane = scrollPane;
    }

    /*
     * Logica della classe
     */

    /**
     * Gestisce il {@link MouseEvent} di pressione sul {@link Canvas} per permettere lo spostamento
     * @param event è il {@link MouseEvent} di pressione che ha causato la chiamata al metodo
     * @param forme è il {@link DrawSnapModel}  presente nel {@link Canvas}
     * @param coordinataX è la coordinata logica per l'asse x del {@link MouseEvent}
     * @param coordinataY è la coordinata logica per l'asse y del {@link MouseEvent}
     * @return {@code false} poiché il {@link MouseEvent} di pressione  è completamente gestito da
     * questo metodo e non richiede ulteriori elaborazioni da parte di altre componenti.
     */
    @Override
    public boolean handleMousePressed(MouseEvent event, DrawSnapModel forme, double coordinataX, double coordinataY) {
        lastX = event.getSceneX();
        lastY = event.getSceneY();
        canvas.setCursor(Cursor.CLOSED_HAND);
        return false;
    }

    /**
     * Gestisce il {@link MouseEvent} di trascinamento sul {@link Canvas} per continuare lo spostamento
     * Quando il mouse viene trascinato, aggiorna i valori di scorrimento della ScrollPane
     * traslando la vista in base allo spostamento del mouse.
     * @param event è il {@link MouseEvent} di trascinamento che ha causato la chiamata al metodo
     * @param forme è il {@link DrawSnapModel}  presente nel {@link javafx.scene.canvas.Canvas}
     * @param coordinataX è la coordinata logica per l'asse x del {@link MouseEvent}
     * @param coordinataY è la coordinata logica per l'asse y del {@link MouseEvent}
     * @return {@code fasle} poiché il {@link MouseEvent} di trascinamento è completamente gestito da
     * questo metodo e non richiede ulteriori elaborazioni da parte di altre componenti.
     */
    @Override
    public boolean handleMouseDragged(MouseEvent event, DrawSnapModel forme, double coordinataX, double coordinataY) {
        double currentSceneX = event.getSceneX();
        double currentSceneY = event.getSceneY();

        double deltaX = currentSceneX - lastX;
        double deltaY = currentSceneY - lastY;

        double contentWidth = scrollPane.getContent().getBoundsInLocal().getWidth();
        double contentHeight = scrollPane.getContent().getBoundsInLocal().getHeight();

        double viewportWidth = scrollPane.getViewportBounds().getWidth();
        double viewportHeight = scrollPane.getViewportBounds().getHeight();

        double hDelta = deltaX / (contentWidth - viewportWidth);
        double vDelta = deltaY / (contentHeight - viewportHeight);

        // Applica lo spostamento ai valori di scorrimento, limitandoli all'intervallo [0, 1]
        scrollPane.setHvalue(clamp(scrollPane.getHvalue() - hDelta, 0, 1));
        scrollPane.setVvalue(clamp(scrollPane.getVvalue() - vDelta, 0, 1));

        lastX = currentSceneX;
        lastY = currentSceneY;

        return false;
    }

    /**
     * Gestisce il {@link MouseEvent} di rilascio sul {@link Canvas} per terminare lo spostamento
     * @param event è il {@link MouseEvent} di rilascio che ha causato la chiamata al metodo
     * @param forme è il {@link DrawSnapModel}  presente nel {@link javafx.scene.canvas.Canvas}
     * @param coordinataX è la coordinata logica per l'asse x del {@link MouseEvent}
     * @param coordinataY è la coordinata logica per l'asse y del {@link MouseEvent}
     * @return {@code false} poiché il {@link MouseEvent} di rilascio è completamente gestito da
     * questo metodo e non richiede ulteriori elaborazioni da parte di altre componenti.
     */
    @Override
    public boolean handleMouseReleased(MouseEvent event, DrawSnapModel forme, double coordinataX, double coordinataY) {
        canvas.setCursor(Cursor.OPEN_HAND);
        return false;
    }

    /*
     * Metodi Ausiliari
     */

    /**
     * Restringe un valore all'interno di un intervallo specificato.
     * @param value è il valore da limitare
     * @param min è il valore minimo consentito
     * @param max è il valore massimo consentito
     * @return il valore limitato nell'intervallo [min, max]
     */
    private double clamp(double value, double min, double max) {
        return Math.max(min, Math.min(max, value));
    }
}
