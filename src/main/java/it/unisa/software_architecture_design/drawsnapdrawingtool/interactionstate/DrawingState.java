package it.unisa.software_architecture_design.drawsnapdrawingtool.interactionstate;

import it.unisa.software_architecture_design.drawsnapdrawingtool.forme.Forma;
import javafx.scene.input.MouseEvent;

import java.util.List;

/**
 * L'interfaccia {@code DrawingState} sarà implementata da tutti gli stati dell'applicazione, che dovranno
 * infatti gestire, ognuno in maniera differenziata, almeno i tre eventi per i quali sono definiti i metodi
 * in questa interfaccia
 */
public interface DrawingState {
    void handleMousePressed(MouseEvent event, List<Forma> forme);

    void handleMouseDragged(MouseEvent event);

    void handleMouseReleased(MouseEvent event);
}
