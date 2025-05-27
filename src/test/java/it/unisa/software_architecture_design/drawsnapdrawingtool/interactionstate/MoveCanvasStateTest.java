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
 * Classe di test per {@link MoveCanvasState}.
 */
public class MoveCanvasStateTest {

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

        }
    }

    /**
     * Configura l'ambiente di test prima di ogni singolo test.
     */
    @BeforeEach
    void setUp() {
        // Inizializza tutti i mock annotati con @Mock in questa classe
        MockitoAnnotations.openMocks(this);

        // Crea un'istanza reale di Canvas e poi la spia con Mockito.
        mockCanvas = spy(new Canvas());

        // Configura mockScrollPane per restituire mockCanvas quando getContent() viene chiamato.
        when(mockScrollPane.getContent()).thenReturn(mockCanvas);

        moveCanvasState = new MoveCanvasState(mockCanvas, mockScrollPane);

        // Simula il comportamento dei getter getHvalue() e getVvalue() dello ScrollPane.
        when(mockScrollPane.getHvalue()).thenAnswer((Answer<Double>) invocation -> currentHValue);
        when(mockScrollPane.getVvalue()).thenAnswer((Answer<Double>) invocation -> currentVValue);

        // Cattura gli argomenti passati a setHvalue() e setVvalue() e aggiorna le variabili interne.
        doAnswer((Answer<Void>) invocation -> {
            currentHValue = invocation.getArgument(0);
            return null;
        }).when(mockScrollPane).setHvalue(anyDouble());

        doAnswer((Answer<Void>) invocation -> {
            currentVValue = invocation.getArgument(0);
            return null;
        }).when(mockScrollPane).setVvalue(anyDouble());

        // Configura il mock delle dimensioni del contenuto canvas e della viewport dello ScrollPane.
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
     */
    @Test
    void testHandleMousePressed() {
        // Simula le coordinate della scena al momento della pressione
        when(mockMouseEvent.getSceneX()).thenReturn(100.0);
        when(mockMouseEvent.getSceneY()).thenReturn(120.0);

        double testCoordX = 100.0;
        double testCoordY = 120.0;
        boolean result = moveCanvasState.handleMousePressed(mockMouseEvent, mockDrawSnapModel, testCoordX, testCoordY);

        // Verifica che il metodo setCursor sia stato chiamato su mockCanvas con l'argomento corretto
        verify(mockCanvas).setCursor(Cursor.CLOSED_HAND);
        assertFalse(result, "handleMousePressed dovrebbe restituire false.");
    }

    /**
     * Verifica che i valori di scorrimento dello ScrollPane
     * vengano aggiornati correttamente in base allo spostamento del mouse e alla logica di clamping,
     */
    @Test
    void testHandleMouseDragged() {
        // Simula la posizione iniziale del mouse per l'evento di dragged
        when(mockMouseEvent.getSceneX()).thenReturn(100.0);
        when(mockMouseEvent.getSceneY()).thenReturn(120.0);

        double initialCoordX = 100.0;
        double initialCoordY = 120.0;
        moveCanvasState.handleMousePressed(mockMouseEvent, mockDrawSnapModel, initialCoordX, initialCoordY);

        // Simula il trascinamento del mouse a una nuova posizione
        double draggedCoordX = 150.0;
        double draggedCoordY = 150.0;

        boolean result = moveCanvasState.handleMouseDragged(mockMouseEvent, mockDrawSnapModel, draggedCoordX, draggedCoordY);

        double deltaX = draggedCoordX - initialCoordX;
        double deltaY = draggedCoordY - initialCoordY;
        double contentWidth = mockCanvas.getBoundsInLocal().getWidth();
        double contentHeight = mockCanvas.getBoundsInLocal().getHeight();
        double viewportWidth = mockScrollPane.getViewportBounds().getWidth();
        double viewportHeight = mockScrollPane.getViewportBounds().getHeight();

        double hDelta = deltaX / (contentWidth - viewportWidth);
        double vDelta = deltaY / (contentHeight - viewportHeight);

        // Calcola i valori finali attesi tenendo conto della funzione clamp()
        double expectedClampedHValue = Math.max(0, Math.min(1, currentHValue - hDelta));
        double expectedClampedVValue = Math.max(0, Math.min(1, currentVValue - vDelta));

        // Verifica che setHvalue e setVvalue siano stati chiamati con i valori giusti
        verify(mockScrollPane).setHvalue(eq(expectedClampedHValue));
        verify(mockScrollPane).setVvalue(eq(expectedClampedVValue));

        assertFalse(result, "handleMouseDragged dovrebbe restituire false.");
    }

    /**
     * Simula un trascinamento partendo da valori di scorrimento preesistenti (non zero)
     * e con i nuovi parametri di coordinata.
     */
    @Test
    void testHandleMouseDraggedWithExistingScrollValue() {
        // Imposta valori iniziali di scorrimento simulati
        currentHValue = 0.5;
        currentVValue = 0.5;

        // Simula la posizione iniziale del mouse
        when(mockMouseEvent.getSceneX()).thenReturn(100.0);
        when(mockMouseEvent.getSceneY()).thenReturn(120.0);

        double initialCoordX = 100.0;
        double initialCoordY = 120.0;
        moveCanvasState.handleMousePressed(mockMouseEvent, mockDrawSnapModel, initialCoordX, initialCoordY);

        // Simula il trascinamento del mouse a una nuova posizione
        double draggedCoordX = 150.0;
        double draggedCoordY = 150.0;

        // Chiama il metodo da testare con i nuovi parametri coordinataX e coordinataY
        boolean result = moveCanvasState.handleMouseDragged(mockMouseEvent, mockDrawSnapModel, draggedCoordX, draggedCoordY);

        // Calcoli per determinare i valori attesi per hDelta e vDelta
        double deltaX = draggedCoordX - initialCoordX;
        double deltaY = draggedCoordY - initialCoordY;
        double contentWidth = mockCanvas.getBoundsInLocal().getWidth();
        double contentHeight = mockCanvas.getBoundsInLocal().getHeight();
        double viewportWidth = mockScrollPane.getViewportBounds().getWidth();
        double viewportHeight = mockScrollPane.getViewportBounds().getHeight();

        double hDelta = deltaX / (contentWidth - viewportWidth);
        double vDelta = deltaY / (contentHeight - viewportHeight);

        // Calcola i valori finali attesi tenendo conto della funzione clamp()
        double expectedFinalHValue = Math.max(0, Math.min(1, 0.5 - hDelta));
        double expectedFinalVValue = Math.max(0, Math.min(1, 0.5 - vDelta));

        // Verifica che setHvalue e setVvalue siano stati chiamati con i valori calcolati
        verify(mockScrollPane).setHvalue(eq(expectedFinalHValue));
        verify(mockScrollPane).setVvalue(eq(expectedFinalVValue));
        assertFalse(result, "handleMouseDragged dovrebbe restituire false.");
    }

    /**
     * Verifica che il cursore del canvas venga reimpostato su {@code Cursor.OPEN_HAND}
     */
    @Test
    void testHandleMouseReleased() {
        double testCoordX = 0.0;
        double testCoordY = 0.0;
        boolean result = moveCanvasState.handleMouseReleased(mockMouseEvent, testCoordX, testCoordY);

        // Verifica che il metodo setCursor sia stato chiamato su mockCanvas con l'argomento corretto
        verify(mockCanvas).setCursor(Cursor.OPEN_HAND);
        assertFalse(result, "handleMouseReleased dovrebbe restituire false.");
    }
}
