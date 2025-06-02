package it.unisa.software_architecture_design.drawsnapdrawingtool.interactionstate;

import it.unisa.software_architecture_design.drawsnapdrawingtool.DrawSnapModel;
import javafx.scene.input.MouseEvent;

/**
 * L'interfaccia {@link DrawingState} sarà implementata da tutti gli stati dell'applicazione, che dovranno
 * infatti gestire, ognuno in maniera differenziata, almeno i tre {@link MouseEvent} per i quali sono
 * definiti i metodi in questa interfaccia.
 */
public interface DrawingState {
    boolean handleMousePressed(MouseEvent event, DrawSnapModel forme, double x, double y);

    boolean handleMouseDragged(MouseEvent event, DrawSnapModel forme, double x, double y);

    boolean handleMouseReleased(MouseEvent event, DrawSnapModel forme, double x, double y);
}
