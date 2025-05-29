
package it.unisa.software_architecture_design.drawsnapdrawingtool.interactionstate;

import it.unisa.software_architecture_design.drawsnapdrawingtool.DrawSnapModel;
import it.unisa.software_architecture_design.drawsnapdrawingtool.forme.Forma;
import it.unisa.software_architecture_design.drawsnapdrawingtool.forme.FormaSelezionataDecorator;
import javafx.scene.control.ToolBar;
import javafx.scene.input.MouseEvent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


/**
 * Classe di test per {@link SelectState}
 */

class SelectStateTest {

    private SelectState selectState;


    /**
     * Inizializza l'istanza di {@link SelectState} prima di ciascun test.
     */

    @BeforeEach
    public void setUp() {
        selectState = new SelectState( null, null , null);
    }


    /**
     * Il test controlla che al click di una forma questa venga correttamente decorata
     * con {@link FormaSelezionataDecorator}.
     * Il test utilizza un mock di {@link Forma}.
     */

    @Test
    public void testHandleMousePressed_FormaSelezionataCorrettamente() {
        // 1. Mock di una Forma semplice
        Forma mockForma = mock(Forma.class);
        when(mockForma.contiene(50, 50)).thenReturn(true);
        when(mockForma.getCoordinataX()).thenReturn(50.0);
        when(mockForma.getCoordinataY()).thenReturn(50.0);

        // 2. Lista con una forma
        DrawSnapModel forme = new DrawSnapModel();
        forme.add(mockForma);

        // 3. MouseEvent simulato
        MouseEvent event = mock(MouseEvent.class);
        when(event.getX()).thenReturn(50.0);
        when(event.getY()).thenReturn(50.0);

        // 4. Chiamata al metodo
        selectState.handleMousePressed(event, forme, 50.0, 50.0);

        // 5. Verifica: la forma è ora decorata
        assertEquals(1, forme.size());
        assertInstanceOf(FormaSelezionataDecorator.class, forme.getFirst());
    }


/**
     * Il test controlla che al click su un'area che non contiene una forma questa non venga decorata
     * con {@link FormaSelezionataDecorator}.
     * Il test utilizza un mock di {@link Forma}.
     */

    @Test
    public void testHandleMouseReleased_FormaNonSelezionata() {
        // 1. Mock di una Forma semplice
        Forma mockForma = mock(Forma.class);
        when(mockForma.contiene(10, 10)).thenReturn(false);
        when(mockForma.getCoordinataX()).thenReturn(10.0);
        when(mockForma.getCoordinataY()).thenReturn(10.0);

        // 2. Lista con una forma
        DrawSnapModel forme = new DrawSnapModel();
        forme.add(mockForma);

        // 3. MouseEvent simulato
        MouseEvent event = mock(MouseEvent.class);
        when(event.getX()).thenReturn(10.0);
        when(event.getY()).thenReturn(10.0);

        // 4. Chiamata al metodo
        selectState.handleMousePressed(event, forme, 10.0, 10.0);

        // 5. Verifica: la forma non è decorata
        assertEquals(1, forme.size());
        assertFalse(forme.getFirst() instanceof FormaSelezionataDecorator);
    }


/**
     * Verifica che in presenza di più Forme solo quella effettivamente cliccata viene decorata
     * con {@link FormaSelezionataDecorator}.
     * Il test utilizza un mock di {@link Forma}.
     */

    @Test
    public void testHandleMousePressed_UnaSolaFormaContieneIlPunto() {
        // 1. Mock di due Forme semplici
        Forma forma1 = mock(Forma.class);
        Forma forma2 = mock(Forma.class);

        when(forma1.contiene(30, 30)).thenReturn(false);
        when(forma2.contiene(30, 30)).thenReturn(true);
        when(forma2.getCoordinataX()).thenReturn(30.0);
        when(forma2.getCoordinataY()).thenReturn(30.0);

        // 2. Lista con due forme
        DrawSnapModel forme = new DrawSnapModel();
        forme.add(forma1);
        forme.add(forma2);

        // 3. MouseEvent simulato
        MouseEvent event = mock(MouseEvent.class);
        when(event.getX()).thenReturn(30.0);
        when(event.getY()).thenReturn(30.0);

        // 4. Chiamata al metodo
        selectState.handleMousePressed(event, forme, 30.0, 30.0);

        // Verifica: solo la seconda forma è decorata
        assertFalse(forme.get(0) instanceof FormaSelezionataDecorator);
        assertInstanceOf(FormaSelezionataDecorator.class, forme.get(1));
    }


/**
     * Verifica che non siano lanciate eccezioni se la lista di forme è vuota.
     */

    @Test
    public void testHandleMousePressed_ListaVuota() {
        DrawSnapModel forme = new DrawSnapModel();

        MouseEvent event = mock(MouseEvent.class);
        when(event.getX()).thenReturn(20.0);
        when(event.getY()).thenReturn(20.0);

        assertDoesNotThrow(() -> selectState.handleMousePressed(event, forme, 20.0, 20.0));
        assertTrue(forme.isEmpty());
    }


/**
     * Verifica che una forma già decorata resti decorata, senza comportamenti anomali.
     */

    @Test
    public void testHandleMousePressed_FormaGiaDecorata() {
        // 1. Mock di una Forma decorata
        Forma baseForma = mock(Forma.class);
        when(baseForma.contiene(40, 40)).thenReturn(true);
        when(baseForma.getCoordinataX()).thenReturn(40.0);
        when(baseForma.getCoordinataY()).thenReturn(40.0);

        Forma decorata = new FormaSelezionataDecorator(baseForma);

        // 2. Lista con una forma decorata
        DrawSnapModel forme = new DrawSnapModel();
        forme.add(decorata);

        // 3. MouseEvent simulato
        MouseEvent event = mock(MouseEvent.class);
        when(event.getX()).thenReturn(40.0);
        when(event.getY()).thenReturn(40.0);

        // 4. Chiamata al metodo
        selectState.handleMousePressed(event, forme, 40.0, 40.0);

        // Verifica: la forma rimane decorata (non si rompe il comportamento)
        assertInstanceOf(FormaSelezionataDecorator.class, forme.getFirst());
    }


/*
     * Test relativi a handleMouseDragged
     */



/**
     * Helper per impostare offsetX e offsetY via reflection
     * @param selectState istanza dello stato di selezione
     * @param offsetX offset sull'asse X che si desidera impostare
     * @param offsetY offset sull'asse Y che si desidera impostare
     * @throws Exception a causa della reflection fatta con {@code setOffset}
     */

    private void setOffset(SelectState selectState, double offsetX, double offsetY) throws Exception {
        Field offsetXField = SelectState.class.getDeclaredField("offsetX");
        offsetXField.setAccessible(true);
        offsetXField.set(selectState, offsetX);

        Field offsetYField = SelectState.class.getDeclaredField("offsetY");
        offsetYField.setAccessible(true);
        offsetYField.set(selectState, offsetY);
    }


/**
     * Verifica il normale spostamento di una figura selezionata
     * @throws Exception a causa della reflection fatta con {@code setOffset}
     */

    @Test
    void testHandleMouseDragged_FormaSelezionataSiMuove() throws Exception {
        // Mock della forma base
        Forma forma = mock(Forma.class);

        // Decoratore con la forma
        FormaSelezionataDecorator decorata = new FormaSelezionataDecorator(forma);

        DrawSnapModel forme = new DrawSnapModel();
        forme.add(decorata);

        // Mock del MouseEvent
        MouseEvent event = mock(MouseEvent.class);
        when(event.getX()).thenReturn(100.0);
        when(event.getY()).thenReturn(150.0);

        // Imposta offsetX = 20, offsetY = 30
        setOffset(selectState, 20.0, 30.0);

        // Chiamata al metodo
        selectState.handleMouseDragged(event, forme, 100.0, 150.0);

        // Verifica che la forma interna abbia ricevuto nuove coordinate
        verify(forma).setCoordinataX(80.0); // 100 - 20
        verify(forma).setCoordinataY(120.0); // 150 - 30
    }


/**
     * Verifica che se una forma non è selezionata allora non viene spostata.
     * @throws Exception a causa della reflection fatta con {@code setOffset}
     */

    @Test
    void testHandleMouseDragged_NessunaFormaSelezionata() throws Exception {
        // Lista senza decoratori
        Forma nonSelezionata = mock(Forma.class);
        DrawSnapModel forme = new DrawSnapModel();
        forme.add(nonSelezionata);

        MouseEvent event = mock(MouseEvent.class);
        when(event.getX()).thenReturn(200.0);
        when(event.getY()).thenReturn(300.0);

        setOffset(selectState, 10.0, 10.0);

        selectState.handleMouseDragged(event, forme, 10.0, 10.0);

        // Verifica che la forma normale NON venga modificata
        verify(nonSelezionata, never()).setCoordinataX(anyDouble());
        verify(nonSelezionata, never()).setCoordinataY(anyDouble());
    }


/**
     * Verifica che anche se ci sono più forme solo quella selezionata viene spostata.
     * @throws Exception a causa della reflection fatta con {@code setOffset}
     */

    @Test
    void testHandleMouseDragged_PiuFormeSoloUnaSelezionata() throws Exception {
        Forma normale = mock(Forma.class);
        Forma selezionata = mock(Forma.class);
        FormaSelezionataDecorator decorata = new FormaSelezionataDecorator(selezionata);

        DrawSnapModel forme = new DrawSnapModel();
        forme.add(normale);
        forme.add(decorata);

        MouseEvent event = mock(MouseEvent.class);
        when(event.getX()).thenReturn(300.0);
        when(event.getY()).thenReturn(400.0);

        setOffset(selectState, 100.0, 50.0);

        selectState.handleMouseDragged(event, forme, 300.0, 400.0);

        // Deve spostare solo la forma decorata
        verify(selezionata).setCoordinataX(200.0);
        verify(selezionata).setCoordinataY(350.0);

        // La forma normale non deve essere toccata
        verify(normale, never()).setCoordinataX(anyDouble());
        verify(normale, never()).setCoordinataY(anyDouble());
    }


/**
     * Verifica che non ci sono comportamenti anomali se la lista è vuota.
     * @throws Exception a causa della reflection fatta con {@code setOffset}
     */

    @Test
    void testHandleMouseDragged_ListaVuota() throws Exception {
        DrawSnapModel forme = new DrawSnapModel();

        MouseEvent event = mock(MouseEvent.class);
        when(event.getX()).thenReturn(123.0);
        when(event.getY()).thenReturn(456.0);

        setOffset(selectState, 10.0, 20.0);

        // Nessuna eccezione, nessuna modifica
        assertDoesNotThrow(() -> selectState.handleMouseDragged(event, forme, 123.0, 456.0));
    }
}

