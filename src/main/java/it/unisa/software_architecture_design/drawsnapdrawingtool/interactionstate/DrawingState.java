package it.unisa.software_architecture_design.drawsnapdrawingtool.interactionstate;

import it.unisa.software_architecture_design.drawsnapdrawingtool.DrawSnapModel;
import javafx.scene.input.MouseEvent;

/**
 * L'interfaccia {@code DrawingState} sarà implementata da tutti gli stati dell'applicazione, che dovranno
 * infatti gestire, ognuno in maniera differenziata, almeno i tre eventi per i quali sono definiti i metodi
 * in questa interfaccia
 */
public interface DrawingState {
    boolean handleMousePressed(MouseEvent event, DrawSnapModel forme);

    boolean handleMouseDragged(MouseEvent event, DrawSnapModel forme);

    boolean handleMouseReleased(MouseEvent event);
}
