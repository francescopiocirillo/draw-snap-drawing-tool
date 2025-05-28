package it.unisa.software_architecture_design.drawsnapdrawingtool.commands;


import javafx.application.Platform;
import javafx.geometry.BoundingBox;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.ScrollPane;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


/**
 * Classe per il testing della classe {@code ZoomCommand}
 */

class ZoomCommandTest {

    private Canvas canvas;
    private ScrollPane scrollPane;
    private double baseCanvasWidth = 2048;
    private double baseCanvasHeight = 2048;


    /**
     * Parametro per l'inizializzazione del tool kit di javafx
     */
    private static boolean javaFxToolKitInitialized = false;

    /**
     * Prima di fare i test vi è bisogno di controllare che il tool kit di java fx sia caricato
     * Se non presente, non sarà possibile inizializzare il contesto grafico e interagire con le sue API
     * @throws IllegalStateException se il toolkit di JavaFX non si inizializza entro il tempo massimo specificato
     * @throws InterruptedException se l'attesa per l'inizializzazione fatta da {@code countDownLatch.await()}
     * viene interrotta
     */
    @BeforeAll
    public static void initJavaFX() throws Exception{
        if(!javaFxToolKitInitialized) {
            CountDownLatch countDownLatch = new CountDownLatch(1);
            Platform.startup(() -> {
                javaFxToolKitInitialized = true;
                countDownLatch.countDown();
            });
            if((!countDownLatch.await(5, TimeUnit.SECONDS))){
                throw new IllegalStateException("JavaFxToolKit not initialized");
            }
        }
    }
    /**
     * Inizializza il mock del canvas prima diogni test
     */

    @BeforeEach
    void setUp() {
        canvas = mock(Canvas.class);
        scrollPane = mock(ScrollPane.class);

        when(scrollPane.getViewportBounds()).thenReturn(new BoundingBox(0, 0, 800, 600));

        when(canvas.getWidth()).thenReturn(baseCanvasWidth);
        when(canvas.getHeight()).thenReturn(baseCanvasHeight);
    }

    /**
     * Controlla che ci sia un {@code NullPointerException} se viene passato un null come canvas
     */

    @Test
    void testExecute_NullCanvas() {
        ZoomCommand command = new ZoomCommand(null, scrollPane, baseCanvasWidth, baseCanvasHeight, 0);
        assertThrows(NullPointerException.class, command::execute);
    }

    /**
     * Controlla che venga eseguito correttamente il ridimensionamento del canvas con il livello
     * normale 1.0
     */
    @Test
    void testExecute_NormalZoomLevel_1_25x() {
        double zoomLevel = 1.25;

        double expectedWidth = baseCanvasWidth * zoomLevel;
        double expectedHeight = baseCanvasHeight * zoomLevel;
        ZoomCommand command = new ZoomCommand(canvas, scrollPane, baseCanvasWidth, baseCanvasHeight, zoomLevel);
        command.execute();

        verify(canvas, times(1)).setWidth(expectedWidth);
        verify(canvas, times(1)).setHeight(expectedHeight);
    }

    /**
     * Controlla che venga eseguito correttamente il ridimensionamento del canvas con il livello
     * ridotto 0.5
     */
    @Test
    void testExecute_ReducedZoomLevel_1_0x() {
        double zoomLevel = 1.0;

        double expectedWidth = baseCanvasWidth * zoomLevel;
        double expectedHeight = baseCanvasHeight * zoomLevel;
        ZoomCommand command = new ZoomCommand(canvas, scrollPane, baseCanvasWidth, baseCanvasHeight, zoomLevel);
        command.execute();

        verify(canvas, times(1)).setWidth(expectedWidth);
        verify(canvas, times(1)).setHeight(expectedHeight);
    }

    /**
     * Controlla che venga eseguito correttamente il ridimensionamento del canvas con il livello
     * aumentato 2.0
     */
    @Test
    void testExecute_IncreasedZoomLevel_2_0x() {
        double zoomLevel = 2.0;

        double expectedWidth = baseCanvasWidth * zoomLevel;
        double expectedHeight = baseCanvasHeight * zoomLevel;
        ZoomCommand command = new ZoomCommand(canvas, scrollPane, baseCanvasWidth, baseCanvasHeight, zoomLevel);
        command.execute();

        verify(canvas, times(1)).setWidth(expectedWidth);
        verify(canvas, times(1)).setHeight(expectedHeight);
    }

    /**
     * Controlla che venga eseguito correttamente il ridimensionamento del canvas più volte con
     * lo stesso livello
     */
    @Test
    void testExecute_MultipleTimesSameZoomLevel() {
        double zoomLevel = 1.25;

        double expectedWidth = baseCanvasWidth * zoomLevel;
        double expectedHeight = baseCanvasHeight * zoomLevel;
        ZoomCommand command = new ZoomCommand(canvas, scrollPane, baseCanvasWidth, baseCanvasHeight, zoomLevel);
        command.execute();
        command.execute();
        command.execute();

        verify(canvas, times(3)).setWidth(expectedWidth);
        verify(canvas, times(3)).setHeight(expectedHeight);
    }

}
