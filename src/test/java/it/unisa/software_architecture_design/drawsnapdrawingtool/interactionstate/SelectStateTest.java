package it.unisa.software_architecture_design.drawsnapdrawingtool.interactionstate;

import it.unisa.software_architecture_design.drawsnapdrawingtool.forme.Forma;
import it.unisa.software_architecture_design.drawsnapdrawingtool.forme.FormaSelezionataDecorator;
import javafx.scene.input.MouseEvent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

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
        selectState = new SelectState();
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
        List<Forma> forme = new ArrayList<>();
        forme.add(mockForma);

        // 3. MouseEvent simulato
        MouseEvent event = mock(MouseEvent.class);
        when(event.getX()).thenReturn(50.0);
        when(event.getY()).thenReturn(50.0);

        // 4. Chiamata al metodo
        selectState.handleMousePressed(event, forme);

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
        List<Forma> forme = new ArrayList<>();
        forme.add(mockForma);

        // 3. MouseEvent simulato
        MouseEvent event = mock(MouseEvent.class);
        when(event.getX()).thenReturn(10.0);
        when(event.getY()).thenReturn(10.0);

        // 4. Chiamata al metodo
        selectState.handleMousePressed(event, forme);

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
        List<Forma> forme = new ArrayList<>();
        forme.add(forma1);
        forme.add(forma2);

        // 3. MouseEvent simulato
        MouseEvent event = mock(MouseEvent.class);
        when(event.getX()).thenReturn(30.0);
        when(event.getY()).thenReturn(30.0);

        // 4. Chiamata al metodo
        selectState.handleMousePressed(event, forme);

        // Verifica: solo la seconda forma è decorata
        assertFalse(forme.get(0) instanceof FormaSelezionataDecorator);
        assertInstanceOf(FormaSelezionataDecorator.class, forme.get(1));
    }

    /**
     * Verifica che non siano lanciate eccezioni se la lista di forme è vuota.
     */
    @Test
    public void testHandleMousePressed_ListaVuota() {
        List<Forma> forme = new ArrayList<>();

        MouseEvent event = mock(MouseEvent.class);
        when(event.getX()).thenReturn(20.0);
        when(event.getY()).thenReturn(20.0);

        assertDoesNotThrow(() -> selectState.handleMousePressed(event, forme));
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
        List<Forma> forme = new ArrayList<>();
        forme.add(decorata);

        // 3. MouseEvent simulato
        MouseEvent event = mock(MouseEvent.class);
        when(event.getX()).thenReturn(40.0);
        when(event.getY()).thenReturn(40.0);

        // 4. Chiamata al metodo
        selectState.handleMousePressed(event, forme);

        // Verifica: la forma rimane decorata (non si rompe il comportamento)
        assertInstanceOf(FormaSelezionataDecorator.class, forme.getFirst());
    }

    /**
     * Verifica che una forma già decorata venga rimpiazzata (o gestita correttamente)
     * se viene cliccata di nuovo.
     */
    @Test
    public void testHandleMousePressed_FormaGiaDecorataDiventaNonSelezionata() {
        // 1. Mock di una Forma decorata
        Forma baseForma = mock(Forma.class);
        when(baseForma.contiene(40, 40)).thenReturn(true);
        when(baseForma.getCoordinataX()).thenReturn(40.0);
        when(baseForma.getCoordinataY()).thenReturn(40.0);

        Forma formaDecorata = mock(Forma.class);
        when(formaDecorata.contiene(40, 40)).thenReturn(false);
        Forma decorata = new FormaSelezionataDecorator(formaDecorata);

        // 2. Lista con una forma decorata
        List<Forma> forme = new ArrayList<>();
        forme.add(baseForma);
        forme.add(decorata);

        // 3. MouseEvent simulato
        MouseEvent event = mock(MouseEvent.class);
        when(event.getX()).thenReturn(40.0);
        when(event.getY()).thenReturn(40.0);

        // 4. Chiamata al metodo
        selectState.handleMousePressed(event, forme);

        // Verifica: la forma rimane decorata (non si rompe il comportamento)
        assertInstanceOf(FormaSelezionataDecorator.class, forme.get(0));
        assertFalse(forme.get(1) instanceof FormaSelezionataDecorator);
    }


}
