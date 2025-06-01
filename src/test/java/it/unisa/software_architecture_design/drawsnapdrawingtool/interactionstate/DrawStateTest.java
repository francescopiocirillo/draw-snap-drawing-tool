package it.unisa.software_architecture_design.drawsnapdrawingtool.interactionstate;

import it.unisa.software_architecture_design.drawsnapdrawingtool.DrawSnapModel;
import it.unisa.software_architecture_design.drawsnapdrawingtool.enumeration.Forme;
import it.unisa.software_architecture_design.drawsnapdrawingtool.forme.*;

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
    /**
     * Verifica che un poligono non venga creato se si effettua un doppio click
     * dopo aver definito meno di tre punti.
     */
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


    /**
     * Verifica la corretta creazione del Poligono attraverso più click.
     */
    @Test
    void testHandleMousePressed_CreaPoligono() {
        AttributiForma attributiPoligono = new AttributiForma();
        attributiPoligono.setColore(Color.GREEN);
        attributiPoligono.setColoreInterno(Color.LIGHTGREEN);
        attributiPoligono.setAngoloInclinazione(15.0);
        attributiPoligono.setAltezza(0);
        attributiPoligono.setLarghezza(0);

        DrawState state = spy(new DrawState(Forme.POLIGONO));
        doReturn(attributiPoligono).when(state).helpUIHandleMousePressed(Forme.POLIGONO);

        DrawSnapModel forme = new DrawSnapModel();

        double x_dialog_coord = 5.0, y_dialog_coord = 5.0;   // Coordinate per il primo click (dialogo)
        double x1 = 30.0, y1 = 20.0; // Primo punto del poligono
        double x2 = 50.0, y2 = 50.0; // Secondo punto del poligono
        double x3 = 10.0, y3 = 50.0; // Terzo punto del poligono
        double x_double_click = x3, y_double_click = y3; // Coordinate per il doppio click (non usate per aggiungere punto)

        MouseEvent mouseEvent1_dialog = mock(MouseEvent.class);
        when(mouseEvent1_dialog.getButton()).thenReturn(MouseButton.PRIMARY);
        when(mouseEvent1_dialog.getClickCount()).thenReturn(1);

        MouseEvent mouseEvent2_addP1 = mock(MouseEvent.class);
        when(mouseEvent2_addP1.getButton()).thenReturn(MouseButton.PRIMARY);
        when(mouseEvent2_addP1.getClickCount()).thenReturn(1);

        MouseEvent mouseEvent3_addP2 = mock(MouseEvent.class);
        when(mouseEvent3_addP2.getButton()).thenReturn(MouseButton.PRIMARY);
        when(mouseEvent3_addP2.getClickCount()).thenReturn(1);

        MouseEvent mouseEvent4_addP3 = mock(MouseEvent.class);
        when(mouseEvent4_addP3.getButton()).thenReturn(MouseButton.PRIMARY);
        when(mouseEvent4_addP3.getClickCount()).thenReturn(1);

        MouseEvent mouseEvent5_finalize = mock(MouseEvent.class);
        when(mouseEvent5_finalize.getButton()).thenReturn(MouseButton.PRIMARY);
        when(mouseEvent5_finalize.getClickCount()).thenReturn(2); // Doppio click


        // Primo click: apre il dialogo mockato e inizializza la factory
        boolean result1 = state.handleMousePressed(mouseEvent1_dialog, forme, x_dialog_coord, y_dialog_coord);
        assertFalse(result1, "handleMousePressed dovrebbe restituire false dopo il dialogo iniziale per il poligono.");
        assertEquals(0, forme.size(), "Nessuna forma dovrebbe essere creata dopo il dialogo.");
        verify(state).helpUIHandleMousePressed(Forme.POLIGONO);
        // Dopo il primo click creazionePoligono diventa false, quindi getCreazionePoligono() è true.
        assertTrue(state.getCreazionePoligono(), "getCreazionePoligono() dovrebbe restituire true (modalità aggiunta punti attiva).");

        // Secondo click: aggiunge il primo punto (x1, y1)
        boolean result2 = state.handleMousePressed(mouseEvent2_addP1, forme, x1, y1);
        assertFalse(result2, "handleMousePressed dovrebbe restituire false durante l'aggiunta di punti.");
        assertEquals(0, forme.size());
        assertEquals(1, state.getPuntiX().size());
        assertEquals(x1, state.getPuntiX().get(0));
        assertEquals(y1, state.getPuntiY().get(0));
        assertTrue(state.getCreazionePoligono(), "Modalità aggiunta punti dovrebbe rimanere attiva.");


        // Terzo click: aggiunge il secondo punto (x2, y2)
        boolean result3 = state.handleMousePressed(mouseEvent3_addP2, forme, x2, y2);
        assertFalse(result3, "handleMousePressed dovrebbe restituire false durante l'aggiunta di punti.");
        assertEquals(0, forme.size());
        assertEquals(2, state.getPuntiX().size());
        assertEquals(x2, state.getPuntiX().get(1));
        assertEquals(y2, state.getPuntiY().get(1));
        assertTrue(state.getCreazionePoligono(), "Modalità aggiunta punti dovrebbe rimanere attiva.");

        // Quarto click: aggiunge il terzo punto (x3, y3)
        boolean result4 = state.handleMousePressed(mouseEvent4_addP3, forme, x3, y3);
        assertFalse(result4, "handleMousePressed dovrebbe restituire false durante l'aggiunta di punti.");
        assertEquals(0, forme.size());
        assertEquals(3, state.getPuntiX().size());
        assertEquals(x3, state.getPuntiX().get(2));
        assertEquals(y3, state.getPuntiY().get(2));
        assertTrue(state.getCreazionePoligono(), "Modalità aggiunta punti dovrebbe rimanere attiva.");

        // Quinto click (doppio click): finalizza il poligono
        boolean result5 = state.handleMousePressed(mouseEvent5_finalize, forme, x_double_click, y_double_click);
        assertTrue(result5, "handleMousePressed dovrebbe restituire true alla finalizzazione del poligono.");
        assertEquals(1, forme.size(), "Dovrebbe essere creato una forma Poligono.");

        Forma formaCreata = forme.get(0);
        assertTrue(formaCreata instanceof Poligono, "La forma creata dovrebbe essere un Poligono.");
        Poligono poligonoCreato = (Poligono) formaCreata;

        // Calcola il centro atteso dai punti aggiunti: (x1,y1), (x2,y2), (x3,y3)
        double expectedCenterX = (x1 + x2 + x3) / 3.0;
        double expectedCenterY = (y1 + y2 + y3) / 3.0;

        assertEquals(expectedCenterX, poligonoCreato.getCoordinataX(), 0.001, "Coordinata X del poligono errata.");
        assertEquals(expectedCenterY, poligonoCreato.getCoordinataY(), 0.001, "Coordinata Y del poligono errata.");
        assertEquals(attributiPoligono.getColore(), poligonoCreato.getColore(), "Colore del bordo errato.");
        assertEquals(attributiPoligono.getColoreInterno(), poligonoCreato.getColoreInterno(), "Colore interno errato.");
        assertEquals(attributiPoligono.getAngoloInclinazione(), poligonoCreato.getAngoloInclinazione(), 0.001, "Angolo di inclinazione errato.");

        // Dopo la creazione, creazionePoligono torna true, quindi getCreazionePoligono() è false.
        assertFalse(state.getCreazionePoligono(), "getCreazionePoligono() dovrebbe restituire false dopo la creazione del poligono.");
    }

    /**
     * Verifica che la creazione del poligono venga annullata se l'utente preme "Annulla" nel dialogo.
     */
    @Test
    void testHandleMousePressed_CreaPoligono_AnnullatoDaDialogo() {
        DrawState state = spy(new DrawState(Forme.POLIGONO));
        doReturn(null).when(state).helpUIHandleMousePressed(Forme.POLIGONO);

        MouseEvent mouseEvent = mock(MouseEvent.class);
        when(mouseEvent.getButton()).thenReturn(MouseButton.PRIMARY);
        when(mouseEvent.getClickCount()).thenReturn(1); // Necessario per entrare nel blocco if(creazionePoligono)

        DrawSnapModel forme = new DrawSnapModel();

        boolean result = state.handleMousePressed(mouseEvent, forme, 10.0, 10.0);

        assertFalse(result, "handleMousePressed dovrebbe restituire false se il dialogo è annullato.");
        assertEquals(0, forme.size(), "Nessuna forma dovrebbe essere creata.");
        verify(state).helpUIHandleMousePressed(Forme.POLIGONO);
        // Se il dialogo è annullato, creazionePoligono rimane true.
        assertFalse(state.getCreazionePoligono(), "getCreazionePoligono() dovrebbe restituire false se la creazione del poligono non è iniziata.");
    }

    /**
     * Verifica che il poligono non venga creato se si fa un doppio click con meno di tre punti definiti.
     */
    @Test
    void testHandleMousePressed_CreaPoligono_DoppioClickConMenoDiTrePunti() {
        AttributiForma attributiPoligono = new AttributiForma();
        attributiPoligono.setColore(Color.ORANGE);
        attributiPoligono.setColoreInterno(Color.YELLOW);
        attributiPoligono.setAngoloInclinazione(0);
        attributiPoligono.setAltezza(0);
        attributiPoligono.setLarghezza(0);


        DrawState state = spy(new DrawState(Forme.POLIGONO));
        doReturn(attributiPoligono).when(state).helpUIHandleMousePressed(Forme.POLIGONO);

        DrawSnapModel forme = new DrawSnapModel();

        double x_dialog = 0, y_dialog = 0;
        double x1 = 10, y1 = 10; // Punto 1
        double x2 = 20, y2 = 10; // Punto 2
        double x_double_click = 30, y_double_click = 30; // Coordinate per il doppio click

        MouseEvent mouseEventDialog = mock(MouseEvent.class);
        when(mouseEventDialog.getButton()).thenReturn(MouseButton.PRIMARY);
        when(mouseEventDialog.getClickCount()).thenReturn(1);

        MouseEvent mouseEventP1 = mock(MouseEvent.class);
        when(mouseEventP1.getButton()).thenReturn(MouseButton.PRIMARY);
        when(mouseEventP1.getClickCount()).thenReturn(1);

        MouseEvent mouseEventP2 = mock(MouseEvent.class);
        when(mouseEventP2.getButton()).thenReturn(MouseButton.PRIMARY);
        when(mouseEventP2.getClickCount()).thenReturn(1);

        MouseEvent mouseEventDoubleClick = mock(MouseEvent.class);
        when(mouseEventDoubleClick.getButton()).thenReturn(MouseButton.PRIMARY);
        when(mouseEventDoubleClick.getClickCount()).thenReturn(2);

        // Primo click (dialogo)
        state.handleMousePressed(mouseEventDialog, forme, x_dialog, y_dialog);
        assertTrue(state.getCreazionePoligono(), "Modalità aggiunta punti dovrebbe essere attiva dopo il dialogo."); // creazionePoligono = false

        // Secondo click
        state.handleMousePressed(mouseEventP1, forme, x1, y1);
        assertEquals(1, state.getPuntiX().size());
        assertTrue(state.getCreazionePoligono(), "Modalità aggiunta punti dovrebbe rimanere attiva.");


        // Terzo click
        state.handleMousePressed(mouseEventP2, forme, x2, y2);
        assertEquals(2, state.getPuntiX().size()); // Ora ci sono 2 punti
        assertTrue(state.getCreazionePoligono(), "Modalità aggiunta punti dovrebbe rimanere attiva.");

        // Doppio click
        boolean result = state.handleMousePressed(mouseEventDoubleClick, forme, x_double_click, y_double_click);

        assertFalse(result, "handleMousePressed dovrebbe restituire false se si fa doppio click con meno di 3 punti.");
        assertEquals(0, forme.size(), "Nessun poligono dovrebbe essere creato.");
        // Lo stato di creazionePoligono (interno) rimane false, quindi getCreazionePoligono() è true.
        assertTrue(state.getCreazionePoligono(), "Lo stato di aggiunta punti dovrebbe rimanere attivo.");
    }

    /**
     * Verifica la corretta creazione di un Testo quando avviene la pressione del mouse.
     */
    @Test
    void testHandleMousePressed_CreaTesto() {
        AttributiForma attributi = new AttributiForma();
        attributi.setAltezza(50);
        attributi.setLarghezza(150);
        attributi.setAngoloInclinazione(0);
        attributi.setColore(Color.RED);
        attributi.setColoreInterno(Color.BLACK);
        attributi.setTesto("Hello World");

        DrawState state = spy(new DrawState(Forme.TEXT));
        doReturn(attributi).when(state).helpUIHandleMousePressed(Forme.TEXT);

        MouseEvent mouseEvent = mock(MouseEvent.class);
        when(mouseEvent.getX()).thenReturn(100.0);
        when(mouseEvent.getY()).thenReturn(100.0);
        when(mouseEvent.getClickCount()).thenReturn(1);
        when(mouseEvent.getButton()).thenReturn(MouseButton.PRIMARY);

        DrawSnapModel forme = new DrawSnapModel();

        state.handleMousePressed(mouseEvent, forme, 100.0, 100.0);

        assertEquals(1, forme.size());
        Testo forma = (Testo) forme.get(0);
        assertNotNull(forma);
        assertEquals("Testo", forma.getClass().getSimpleName());
        assertEquals(100.0, forma.getCoordinataX(), 0.001);
        assertEquals(100.0, forma.getCoordinataY(), 0.001);
        assertEquals(150.0, forma.getLarghezza(), 0.001);
        assertEquals(50.0, forma.getAltezza(), 0.001);
        assertEquals(Color.RED, forma.getColore());
        assertEquals(Color.BLACK, forma.getColoreInterno());
        assertEquals("Hello World", forma.getTesto());
    }

    /**
     * Verifica che il Testo non venga creato se la stringa di testo è vuota.
     */
    @Test
    void testHandleMousePressed_CreaTesto_TestoVuoto() {
        AttributiForma attributi = new AttributiForma();
        attributi.setAltezza(50);
        attributi.setLarghezza(150);
        attributi.setAngoloInclinazione(0);
        attributi.setColore(Color.RED);
        attributi.setColoreInterno(Color.BLACK);
        attributi.setTesto(""); // Testo vuoto

        DrawState state = spy(new DrawState(Forme.TEXT));
        doReturn(attributi).when(state).helpUIHandleMousePressed(Forme.TEXT);

        MouseEvent mouseEvent = mock(MouseEvent.class);
        when(mouseEvent.getX()).thenReturn(100.0);
        when(mouseEvent.getY()).thenReturn(100.0);
        when(mouseEvent.getClickCount()).thenReturn(1);
        when(mouseEvent.getButton()).thenReturn(MouseButton.PRIMARY);

        DrawSnapModel forme = new DrawSnapModel();

        state.handleMousePressed(mouseEvent, forme, 100.0, 100.0);

        assertEquals(0, forme.size(), "Nessun testo dovrebbe essere creato con stringa vuota.");
    }

    /**
     * Verifica che il Testo non venga creato se l'utente annulla il dialogo.
     */
    @Test
    void testHandleMousePressed_CreaTesto_AnnullatoDaDialogo() {
        DrawState state = spy(new DrawState(Forme.TEXT));
        doReturn(null).when(state).helpUIHandleMousePressed(Forme.TEXT); // Simula l'annullamento del dialogo

        MouseEvent mouseEvent = mock(MouseEvent.class);
        when(mouseEvent.getX()).thenReturn(100.0);
        when(mouseEvent.getY()).thenReturn(100.0);
        when(mouseEvent.getClickCount()).thenReturn(1);
        when(mouseEvent.getButton()).thenReturn(MouseButton.PRIMARY);

        DrawSnapModel forme = new DrawSnapModel();

        state.handleMousePressed(mouseEvent, forme, 100.0, 100.0);

        assertEquals(0, forme.size(), "Nessun testo dovrebbe essere creato se il dialogo è annullato.");
    }


}