package it.unisa.software_architecture_design.drawsnapdrawingtool.interactionstate;

import it.unisa.software_architecture_design.drawsnapdrawingtool.DrawSnapModel;
import it.unisa.software_architecture_design.drawsnapdrawingtool.enumeration.Forme;
import it.unisa.software_architecture_design.drawsnapdrawingtool.forme.AttributiForma;
import it.unisa.software_architecture_design.drawsnapdrawingtool.forme.FactoryPoligono;
import it.unisa.software_architecture_design.drawsnapdrawingtool.forme.Forma;
import it.unisa.software_architecture_design.drawsnapdrawingtool.forme.Ellisse;
import it.unisa.software_architecture_design.drawsnapdrawingtool.forme.Rettangolo;
import it.unisa.software_architecture_design.drawsnapdrawingtool.forme.Linea;
import it.unisa.software_architecture_design.drawsnapdrawingtool.forme.Poligono;

import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach; // Importa BeforeEach

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Classe di test per {@link DrawState}
 */
class DrawStateTest {

    private AttributiForma defaultAttributi;

    @BeforeEach
    void setUp() {
        // Inizializza un set di attributi predefiniti per i mock
        defaultAttributi = new AttributiForma();
        defaultAttributi.setAltezza(1);
        defaultAttributi.setLarghezza(1);
        defaultAttributi.setAngoloInclinazione(0);
        defaultAttributi.setColore(Color.BLACK);
        defaultAttributi.setColoreInterno(Color.WHITE);
    }

    /**
     * Verifica la corretta creazione dell'Ellisse quando avviene la pressione del mouse.
     */
    @Test
    void testHandleMousePressed_CreaEllisse() {
        AttributiForma attributi = new AttributiForma();
        attributi.setAltezza(100);
        attributi.setLarghezza(200);
        attributi.setAngoloInclinazione(0);
        attributi.setColore(Color.color(0, 0, 0));
        attributi.setColoreInterno(Color.color(1, 1, 1));

        // Creiamo uno spy della DrawState reale
        DrawState state = spy(new DrawState(Forme.ELLISSE));
        // Stubbiamo il metodo helpUIHandleMousePressed per restituire gli attributi desiderati
        doReturn(attributi).when(state).helpUIHandleMousePressed(Forme.ELLISSE);

        MouseEvent mouseEvent = mock(MouseEvent.class);
        when(mouseEvent.getX()).thenReturn(50.0);
        when(mouseEvent.getY()).thenReturn(75.0);
        when(mouseEvent.getClickCount()).thenReturn(1);
        when(mouseEvent.getButton()).thenReturn(MouseButton.PRIMARY);

        DrawSnapModel forme = new DrawSnapModel();

        state.handleMousePressed(mouseEvent, forme, 50.0, 75.0);

        assertEquals(1, forme.size());
        Ellisse forma = (Ellisse)forme.get(0);
        assertNotNull(forma);
        assertEquals("Ellisse", forma.getClass().getSimpleName());
        assertEquals(50.0, forma.getCoordinataX(), 0.001);
        assertEquals(75.0, forma.getCoordinataY(), 0.001);
        assertEquals(200.0, forma.getLarghezza(), 0.001);
        assertEquals(100.0, forma.getAltezza(), 0.001);
        assertEquals(Color.color(0, 0, 0), forma.getColore());
        assertEquals(Color.color(1, 1, 1), forma.getColoreInterno());
    }

    /**
     * Verifica la corretta creazione del Rettangolo quando avviene la pressione del mouse.
     */
    @Test
    void testHandleMousePressed_CreaRettangolo() {
        AttributiForma attributi = new AttributiForma();
        attributi.setAltezza(50);
        attributi.setLarghezza(80);
        attributi.setColore(Color.BLUE);
        attributi.setColoreInterno(Color.RED);

        DrawState state = spy(new DrawState(Forme.RETTANGOLO));
        doReturn(attributi).when(state).helpUIHandleMousePressed(Forme.RETTANGOLO);

        MouseEvent mouseEvent = mock(MouseEvent.class);
        when(mouseEvent.getX()).thenReturn(10.0);
        when(mouseEvent.getY()).thenReturn(20.0);
        when(mouseEvent.getClickCount()).thenReturn(1);
        when(mouseEvent.getButton()).thenReturn(MouseButton.PRIMARY);

        DrawSnapModel forme = new DrawSnapModel();

        state.handleMousePressed(mouseEvent, forme, 10.0, 20.0);

        assertEquals(1, forme.size());
        Rettangolo forma = (Rettangolo)forme.get(0);
        assertEquals("Rettangolo", forma.getClass().getSimpleName());
        assertEquals(10.0, forma.getCoordinataX(), 0.001);
        assertEquals(20.0, forma.getCoordinataY(), 0.001);
        assertEquals(80.0, forma.getLarghezza(), 0.001);
        assertEquals(50.0, forma.getAltezza(), 0.001);
        assertEquals(Color.BLUE, forma.getColore());
        assertEquals(Color.RED, forma.getColoreInterno());
    }

    /**
     * Verifica la corretta creazione della Linea quando avviene la pressione del mouse.
     */
    @Test
    void testHandleMousePressed_CreaLinea() {
        AttributiForma attributi = new AttributiForma();
        attributi.setAltezza(0);
        attributi.setLarghezza(0);
        attributi.setColore(Color.BLACK);
        attributi.setAngoloInclinazione(0); // Le linee spesso hanno un angolo
        attributi.setColoreInterno(Color.BLACK); // Non usato per linea, ma per coerenza

        DrawState state = spy(new DrawState(Forme.LINEA));
        doReturn(attributi).when(state).helpUIHandleMousePressed(Forme.LINEA);

        MouseEvent mouseEvent = mock(MouseEvent.class);
        when(mouseEvent.getX()).thenReturn(0.0);
        when(mouseEvent.getY()).thenReturn(0.0);
        when(mouseEvent.getClickCount()).thenReturn(1);
        when(mouseEvent.getButton()).thenReturn(MouseButton.PRIMARY);

        DrawSnapModel forme = new DrawSnapModel();

        state.handleMousePressed(mouseEvent, forme, 0.0, 0.0);

        assertEquals(1, forme.size());
        Forma forma = forme.get(0);
        assertEquals("Linea", forma.getClass().getSimpleName());
        assertEquals(0.0, forma.getCoordinataX(), 0.001);
        assertEquals(0.0, forma.getCoordinataY(), 0.001);
        // Per una linea, larghezza e altezza potrebbero rappresentare lunghezza e spessore
        // o semplicemente essere 0 in base all'implementazione.
        // Se la linea è disegnata da (x1, y1) a (x2, y2), getLarghezza() potrebbe essere la lunghezza.
    }

    /**
     * Verifica che una casistica anomala come la creazione della figura con coordinate negative
     * non provochi comportamenti inattesi.
     */
    @Test
    void testHandleMousePressed_CoordinateNegative() {
        AttributiForma attributi = new AttributiForma();
        attributi.setAltezza(30);
        attributi.setLarghezza(40);
        attributi.setColore(Color.GREEN);
        attributi.setAngoloInclinazione(0);
        attributi.setColoreInterno(Color.LIGHTGREEN);

        DrawState state = spy(new DrawState(Forme.ELLISSE));
        doReturn(attributi).when(state).helpUIHandleMousePressed(Forme.ELLISSE);

        MouseEvent mouseEvent = mock(MouseEvent.class);
        when(mouseEvent.getX()).thenReturn(-10.0);
        when(mouseEvent.getY()).thenReturn(-20.0);
        when(mouseEvent.getClickCount()).thenReturn(1);
        when(mouseEvent.getButton()).thenReturn(MouseButton.PRIMARY);

        DrawSnapModel forme = new DrawSnapModel();

        state.handleMousePressed(mouseEvent, forme, -10.0, -20.0);

        assertEquals(1, forme.size());
        Forma forma = forme.get(0);
        assertNotNull(forma);
        assertEquals(-10.0, forma.getCoordinataX(), 0.001);
        assertEquals(-20.0, forma.getCoordinataY(), 0.001);
    }

    /**
     * Verifica che anche se l'oggetto contenente gli attributi della Forma è null
     * il comportamento resta controllato, senza eccezioni ma invece con parametri di default.
     */
    @Test
    void testHandleMousePressed_AttributiNull_DefaultFallback() {
        DrawState state = spy(new DrawState(Forme.RETTANGOLO));
        // Stubbiamo helpUIHandleMousePressed per restituire null, simulando il caso in cui non ci sono attributi
        doReturn(null).when(state).helpUIHandleMousePressed(Forme.RETTANGOLO);

        MouseEvent mouseEvent = mock(MouseEvent.class);
        when(mouseEvent.getX()).thenReturn(10.0);
        when(mouseEvent.getY()).thenReturn(20.0);
        when(mouseEvent.getClickCount()).thenReturn(1);
        when(mouseEvent.getButton()).thenReturn(MouseButton.PRIMARY);

        DrawSnapModel forme = new DrawSnapModel();

        assertDoesNotThrow(() -> state.handleMousePressed(mouseEvent, forme, 10.0, 20.0));

        assertTrue(forme.isEmpty());
    }


    @Test
    void testHandleMouseEvents_CreaPoligonoTramiteFactory() {
        // Gli attributi devono essere passati a DrawState tramite il mock
        AttributiForma attributiPoligono = new AttributiForma();
        attributiPoligono.setColore(Color.BLACK);
        attributiPoligono.setColoreInterno(Color.WHITE);
        attributiPoligono.setAngoloInclinazione(0);
        attributiPoligono.setLarghezza(0); // Questi valori saranno sovrascritti dalla FactoryPoligono
        attributiPoligono.setAltezza(0);

        // Creiamo una FactoryPoligono mockata
        FactoryPoligono mockFactoryPoligono = mock(FactoryPoligono.class);

        // Creiamo uno spy della DrawState reale
        DrawState state = spy(new DrawState(Forme.POLIGONO));

        // Stubba helpUIHandleMousePressed per fornire gli attributi
        doReturn(attributiPoligono).when(state).helpUIHandleMousePressed(Forme.POLIGONO);

        DrawSnapModel forme = new DrawSnapModel();

        // Simula il primo click (inizializza FactoryPoligono e aggiunge il primo punto)
        MouseEvent click1 = mock(MouseEvent.class);
        when(click1.getX()).thenReturn(10.0);
        when(click1.getY()).thenReturn(20.0);
        when(click1.getClickCount()).thenReturn(1);
        when(click1.getButton()).thenReturn(MouseButton.PRIMARY);
        state.handleMousePressed(click1, forme, 10.0, 20.0);

        assertEquals(0, forme.size());

        // Simula il secondo click (aggiunge il secondo punto)
        MouseEvent click2 = mock(MouseEvent.class);
        when(click2.getX()).thenReturn(50.0);
        when(click2.getY()).thenReturn(20.0);
        when(click2.getClickCount()).thenReturn(1);
        when(click2.getButton()).thenReturn(MouseButton.PRIMARY);
        state.handleMousePressed(click2, forme, 50.0, 20.0);

        assertEquals(0, forme.size());

        // Simula il terzo click (aggiunge il terzo punto)
        MouseEvent click3 = mock(MouseEvent.class);
        when(click3.getX()).thenReturn(50.0);
        when(click3.getY()).thenReturn(60.0);
        when(click3.getClickCount()).thenReturn(1);
        when(click3.getButton()).thenReturn(MouseButton.PRIMARY);
        state.handleMousePressed(click3, forme, 50.0, 60.0);

        assertEquals(0, forme.size());

        // Simula il doppio click (finalizza la creazione del poligono)
        MouseEvent doubleClick = mock(MouseEvent.class);
        when(doubleClick.getX()).thenReturn(50.0);
        when(doubleClick.getY()).thenReturn(60.0);
        when(doubleClick.getClickCount()).thenReturn(2);
        when(doubleClick.getButton()).thenReturn(MouseButton.PRIMARY);
        state.handleMousePressed(doubleClick, forme, 50.0, 60.0);

        assertEquals(1, forme.size());
        assertEquals("Poligono", forme.get(0).getClass().getSimpleName());

        Poligono createdPoligono = (Poligono) forme.get(0);

        assertEquals(Color.BLACK, createdPoligono.getColore());
        assertEquals(Color.WHITE, createdPoligono.getColoreInterno());
        assertEquals(0.0, createdPoligono.getAngoloInclinazione(), 0.001);

        // Asserzioni basate sui calcoli di FactoryPoligono per i punti (10,20), (50,20), (50,60)
        assertEquals(36.666, createdPoligono.getCoordinataX(), 0.001);
        assertEquals(33.333, createdPoligono.getCoordinataY(), 0.001);
        assertEquals(40.0, createdPoligono.getLarghezza(), 0.001); // maxX - minX
        assertEquals(40.0, createdPoligono.getAltezza(), 0.001);   // maxY - minY
    }

    @Test
    void testHandleMouseEvents_NonCreaPoligonoConMenoDiTrePunti() {
        AttributiForma attributiPoligono = new AttributiForma();
        attributiPoligono.setColore(Color.BLACK);
        attributiPoligono.setColoreInterno(Color.WHITE);
        attributiPoligono.setAngoloInclinazione(0);

        DrawState state = spy(new DrawState(Forme.POLIGONO));
        doReturn(attributiPoligono).when(state).helpUIHandleMousePressed(Forme.POLIGONO);

        DrawSnapModel forme = new DrawSnapModel();

        // Simula il primo click
        MouseEvent click1 = mock(MouseEvent.class);
        when(click1.getX()).thenReturn(10.0);
        when(click1.getY()).thenReturn(20.0);
        when(click1.getClickCount()).thenReturn(1);
        when(click1.getButton()).thenReturn(MouseButton.PRIMARY);
        state.handleMousePressed(click1, forme, 10.0, 20.0);

        // Simula il secondo click
        MouseEvent click2 = mock(MouseEvent.class);
        when(click2.getX()).thenReturn(50.0);
        when(click2.getY()).thenReturn(20.0);
        when(click2.getClickCount()).thenReturn(1);
        when(click2.getButton()).thenReturn(MouseButton.PRIMARY);
        state.handleMousePressed(click2, forme, 50.0, 20.0);

        // Simula un doppio click (con solo 2 punti aggiunti)
        MouseEvent doubleClick = mock(MouseEvent.class);
        when(doubleClick.getX()).thenReturn(50.0);
        when(doubleClick.getY()).thenReturn(20.0);
        when(doubleClick.getClickCount()).thenReturn(2);
        when(doubleClick.getButton()).thenReturn(MouseButton.PRIMARY);
        state.handleMousePressed(doubleClick, forme, 50.0, 20.0);

        assertEquals(0, forme.size());
    }

    @Test
    void testFactoryResetAfterCreation() {
        AttributiForma attributiPoligono = new AttributiForma();
        attributiPoligono.setColore(Color.BLACK);
        attributiPoligono.setColoreInterno(Color.WHITE);
        attributiPoligono.setAngoloInclinazione(0);

        DrawState state = spy(new DrawState(Forme.POLIGONO));
        doReturn(attributiPoligono).when(state).helpUIHandleMousePressed(Forme.POLIGONO);

        DrawSnapModel forme = new DrawSnapModel();

        // Simula la creazione del primo poligono (3 singoli click + 1 doppio click)
        MouseEvent click1 = mock(MouseEvent.class); when(click1.getX()).thenReturn(10.0); when(click1.getY()).thenReturn(20.0); when(click1.getClickCount()).thenReturn(1); when(click1.getButton()).thenReturn(MouseButton.PRIMARY);
        state.handleMousePressed(click1, forme, 10.0, 20.0);
        MouseEvent click2 = mock(MouseEvent.class); when(click2.getX()).thenReturn(50.0); when(click2.getY()).thenReturn(20.0); when(click2.getClickCount()).thenReturn(1); when(click2.getButton()).thenReturn(MouseButton.PRIMARY);
        state.handleMousePressed(click2, forme, 50.0, 20.0);
        MouseEvent click3 = mock(MouseEvent.class); when(click3.getX()).thenReturn(50.0); when(click3.getY()).thenReturn(60.0); when(click3.getClickCount()).thenReturn(1); when(click3.getButton()).thenReturn(MouseButton.PRIMARY);
        state.handleMousePressed(click3, forme, 50.0, 60.0);
        MouseEvent doubleClick = mock(MouseEvent.class); when(doubleClick.getX()).thenReturn(50.0); when(doubleClick.getY()).thenReturn(60.0); when(doubleClick.getClickCount()).thenReturn(2); when(doubleClick.getButton()).thenReturn(MouseButton.PRIMARY);
        state.handleMousePressed(doubleClick, forme, 50.0, 60.0);

        assertEquals(1, forme.size(), "Poligono non creato"); // Il primo poligono è stato creato

        // Simula l'inizio di una NUOVA creazione di poligono
        // Questo dovrebbe reimpostare la factory interna di DrawState
        MouseEvent newCreationClick = mock(MouseEvent.class);
        when(newCreationClick.getX()).thenReturn(100.0);
        when(newCreationClick.getY()).thenReturn(100.0);
        when(newCreationClick.getClickCount()).thenReturn(1);
        when(newCreationClick.getButton()).thenReturn(MouseButton.PRIMARY);
        state.handleMousePressed(newCreationClick, forme, 100.0, 100.0);

        // Aggiungiamo un secondo punto alla NUOVA factory
        MouseEvent newCreationClick2 = mock(MouseEvent.class);
        when(newCreationClick2.getX()).thenReturn(120.0);
        when(newCreationClick2.getY()).thenReturn(120.0);
        when(newCreationClick2.getClickCount()).thenReturn(1);
        when(newCreationClick2.getButton()).thenReturn(MouseButton.PRIMARY);
        state.handleMousePressed(newCreationClick2, forme, 120.0, 120.0);

        // Tentiamo di chiudere con soli 2 punti nella nuova factory (non dovrebbe creare nulla)
        MouseEvent newCreationDoubleClick = mock(MouseEvent.class);
        when(newCreationDoubleClick.getX()).thenReturn(120.0);
        when(newCreationDoubleClick.getY()).thenReturn(120.0);
        when(newCreationDoubleClick.getClickCount()).thenReturn(2);
        when(newCreationDoubleClick.getButton()).thenReturn(MouseButton.PRIMARY);
        state.handleMousePressed(newCreationDoubleClick, forme, 120.0, 120.0);

        // Ci dovrebbe essere ancora solo 1 forma nel modello (quella creata in precedenza)
        assertEquals(1, forme.size(), "test failed");
    }
}