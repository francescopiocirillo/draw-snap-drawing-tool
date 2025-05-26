package it.unisa.software_architecture_design.drawsnapdrawingtool.commands;

import javafx.scene.canvas.Canvas;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Classe per il testing della classe {@code ZoomCommand}
 */
class ZoomCommandTest {

    private Canvas canvas;
    private Double[] zoomLevels = {0.1, 1.0, 1.5, 2.0};

    /**
     * Inizializza il mock del canvas prima diogni test
     */
    @BeforeEach
    void setUp() {
        canvas = mock(Canvas.class);
    }

    /**
     * Controlla che il comando venga eseguito correttamente quando viene passato un indice valido
     */
    @Test
    void testExecute_ValidIndex() {
        int targetIndex = 2;

        ZoomCommand command = new ZoomCommand(canvas, zoomLevels, targetIndex);
        command.execute();

        verify(canvas).setScaleX(1.5);
    }

    /**
     * Controlla che il comando non venga eseguito se viene passato un indice negativo
     */
    @Test
    void testExecute_NegativeIndex() {
        int targetIndex = -1;

        ZoomCommand command = new ZoomCommand(canvas, zoomLevels, targetIndex);
        command.execute();

        verify(canvas, never()).setScaleX(anyDouble());
    }

    /**
     * Controlla che il comando non venga eseguito se viene passato un indice fuori dal range
     */
    @Test
    void testExecute_IndexOutOfRange() {
        int targetIndex = 6;

        ZoomCommand command = new ZoomCommand(canvas, zoomLevels, targetIndex);
        command.execute();

        verify(canvas, never()).setScaleX(anyDouble());
    }

    /**
     * Controlla che il comando non dia problemi se eseguito più volte sullo stesso livello.
     */
    @Test
    void testExecute_MultipleTimesZoomAtEachLevel() {
        int targetIndex = 2;

        ZoomCommand command = new ZoomCommand(canvas, zoomLevels, targetIndex);
        command.execute();
        command.execute();
        command.execute();

        verify(canvas, times(3)).setScaleX(1.5);
    }

    /**
     * Controlla che ci sia un {@code NullPointerException} se viene passato un null come canvas
     */
    @Test
    void testExecute_NullCanvas() {
        ZoomCommand command = new ZoomCommand(null, zoomLevels, 0);

        assertThrows(NullPointerException.class, command::execute);
    }


}