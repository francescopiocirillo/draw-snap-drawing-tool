package it.unisa.software_architecture_design.drawsnapdrawingtool.interactionstate;

import it.unisa.software_architecture_design.drawsnapdrawingtool.DrawSnapModel;
import javafx.application.Platform;
import javafx.geometry.Bounds;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.ScrollPane;
import javafx.scene.Cursor;
import javafx.scene.input.MouseEvent;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.stubbing.Answer;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Classe di test per {@link MoveCanvasState}
 */
public class MoveCanvasStateTest {
    // Mock degli oggetti JavaFX che interagiscono con MoveCanvasState
    @Mock
    private Canvas mockCanvas;
    @Mock
    private ScrollPane mockScrollPane;
    @Mock
    private MouseEvent mockMouseEvent;
    @Mock
    private DrawSnapModel mockDrawSnapModel;

    private MoveCanvasState moveCanvasState;

    private double currentHValue = 0.0;
    private double currentVValue = 0.0;

    /**
     * Inizializza il toolkit JavaFX prima di eseguire tutti i test.
     */
    @BeforeAll
    static void initJFX() {
        try {
            Platform.startup(() -> {});
        } catch (IllegalStateException e) {
            // Toolkit già inizializzato, ignora
        }
    }

    /**
     * Configura l'ambiente di test prima di ogni singolo test.
     * Inizializza i mock e definisce il loro comportamento atteso.
     */
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Configura mockScrollPane per restituire mockCanvas quando getContent() viene chiamato.
        when(mockScrollPane.getContent()).thenReturn(mockCanvas);

        moveCanvasState = new MoveCanvasState(mockCanvas, mockScrollPane);

        when(mockScrollPane.getHvalue()).thenAnswer((Answer<Double>) invocation -> currentHValue);
        when(mockScrollPane.getVvalue()).thenAnswer((Answer<Double>) invocation -> currentVValue);

        // Cattura gli argomenti passati a setHvalue() e setVvalue() e aggiorna le variabili interne.
        // Questo permette di testare che i valori di scorrimento siano impostati correttamente.
        doAnswer((Answer<Void>) invocation -> {
            currentHValue = invocation.getArgument(0);
            return null;
        }).when(mockScrollPane).setHvalue(anyDouble());

        doAnswer((Answer<Void>) invocation -> {
            currentVValue = invocation.getArgument(0);
            return null;
        }).when(mockScrollPane).setVvalue(anyDouble());

        // Configura il mock delle dimensioni del contenuto (canvas) e della viewport dello ScrollPane.
        // Questi valori sono usati per i calcoli di spostamento in handleMouseDragged.
        Bounds mockContentBounds = mock(Bounds.class);
        when(mockContentBounds.getWidth()).thenReturn(2000.0);
        when(mockContentBounds.getHeight()).thenReturn(1500.0);
        when(mockCanvas.getBoundsInLocal()).thenReturn(mockContentBounds);

        Bounds mockViewportBounds = mock(Bounds.class);
        when(mockViewportBounds.getWidth()).thenReturn(1000.0);
        when(mockViewportBounds.getHeight()).thenReturn(800.0);
        when(mockScrollPane.getViewportBounds()).thenReturn(mockViewportBounds);
    }

    /**
     * Verifica che il cursore del canvas venga cambiato in {@code Cursor.CLOSED_HAND}
     * quando viene gestito un evento di pressione del mouse.
     */
    @Test
    void testHandleMousePressed() {
        when(mockMouseEvent.getSceneX()).thenReturn(100.0);
        when(mockMouseEvent.getSceneY()).thenReturn(120.0);
        when(mockMouseEvent.isPrimaryButtonDown()).thenReturn(true);

        boolean result = moveCanvasState.handleMousePressed(mockMouseEvent, mockDrawSnapModel);

        verify(mockCanvas).setCursor(Cursor.CLOSED_HAND);
        assertTrue(result);
    }

    /**
     * Verifica che i valori di scorrimento (`Hvalue` e `Vvalue`) dello ScrollPane
     * vengano aggiornati correttamente in base allo spostamento del mouse e alla logica di clamping.
     */
    @Test
    void testHandleMouseDragged() {
        // Simula una posizione iniziale del mouse per il drag
        when(mockMouseEvent.getSceneX()).thenReturn(100.0);
        when(mockMouseEvent.getSceneY()).thenReturn(120.0);
        moveCanvasState.handleMousePressed(mockMouseEvent, mockDrawSnapModel);

        // Simula il trascinamento del mouse a una nuova posizione
        when(mockMouseEvent.getSceneX()).thenReturn(150.0);
        when(mockMouseEvent.getSceneY()).thenReturn(150.0);

        boolean result = moveCanvasState.handleMouseDragged(mockMouseEvent, mockDrawSnapModel);

        double deltaX = 150.0 - 100.0;
        double deltaY = 150.0 - 120.0;
        double contentWidth = 2000.0;
        double viewportWidth = 1000.0;
        double contentHeight = 1500.0;
        double viewportHeight = 800.0;

        double hDelta = deltaX / (contentWidth - viewportWidth);
        double vDelta = deltaY / (contentHeight - viewportHeight);

        double expectedClampedHValue = Math.max(0, Math.min(1, currentHValue - hDelta));
        double expectedClampedVValue = Math.max(0, Math.min(1, currentVValue - vDelta));

        // Verifica che setHvalue e setVvalue siano stati chiamati con i valori attesi
        verify(mockScrollPane).setHvalue(eq(expectedClampedHValue));
        verify(mockScrollPane).setVvalue(eq(expectedClampedVValue));
        assertTrue(result);
    }

    /**
     * Simula un trascinamento partendo da valori di scorrimento preesistenti..
     */
    @Test
    void testHandleMouseDraggedWithExistingScrollValue() {
        // Valori iniziali di scorrimento
        currentHValue = 0.5;
        currentVValue = 0.5;

        // Posizione iniziale del mouse
        when(mockMouseEvent.getSceneX()).thenReturn(100.0);
        when(mockMouseEvent.getSceneY()).thenReturn(120.0);
        moveCanvasState.handleMousePressed(mockMouseEvent, mockDrawSnapModel);

        // Simula il trascinamento del mouse a una nuova posizione
        when(mockMouseEvent.getSceneX()).thenReturn(150.0);
        when(mockMouseEvent.getSceneY()).thenReturn(150.0);

        moveCanvasState.handleMouseDragged(mockMouseEvent, mockDrawSnapModel);

        double deltaX = 150.0 - 100.0;
        double deltaY = 150.0 - 120.0;
        double contentWidth = 2000.0;
        double viewportWidth = 1000.0;
        double contentHeight = 1500.0;
        double viewportHeight = 800.0;

        double hDelta = deltaX / (contentWidth - viewportWidth);
        double vDelta = deltaY / (contentHeight - viewportHeight);

        // Calcola i valori finali attesi tenendo conto del clamp
        double expectedFinalHValue = Math.max(0, Math.min(1, 0.5 - hDelta));
        double expectedFinalVValue = Math.max(0, Math.min(1, 0.5 - vDelta));

        // Verifica che setHvalue e setVvalue siano stati chiamati con i valori calcolati
        verify(mockScrollPane).setHvalue(eq(expectedFinalHValue));
        verify(mockScrollPane).setVvalue(eq(expectedFinalVValue));
    }

    /**
     * Verifica che il cursore del canvas venga reimpostato su {@code Cursor.OPEN_HAND}
     * quando viene gestito un evento di rilascio del mouse.
     */
    @Test
    void testHandleMouseReleased() {
        boolean result = moveCanvasState.handleMouseReleased(mockMouseEvent);

        verify(mockCanvas).setCursor(Cursor.OPEN_HAND);
        assertTrue(result);
    }
}