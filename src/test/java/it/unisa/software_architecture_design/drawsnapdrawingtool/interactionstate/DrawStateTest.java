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

    private AttributiFormaDTO defaultAttributi;

    @BeforeEach
    void setUp() {
        // Attributi predefiniti per i mock
        defaultAttributi = new AttributiFormaDTO();
        defaultAttributi.setAltezza(1);
        defaultAttributi.setLarghezza(1);
        defaultAttributi.setAngoloInclinazione(0);
        defaultAttributi.setColore(Color.BLACK);
        defaultAttributi.setColoreInterno(Color.WHITE);
    }

    /**
     * Simula il comportamento del metodo `helpUIHandleMousePressed` dello spy `DrawState` fornito.
     * @param state Lo spy dell'oggetto `DrawState` il cui metodo `helpUIHandleMousePressed` viene simulato.
     * @param attributiToReturn L'oggetto `AttributiFormaDTO` che il metodo simulato dovrebbe restituire,
     * simulando l'input dell'utente da una finestra di dialogo.
     */
    private void stubUiInteractions(DrawState state, AttributiFormaDTO attributiToReturn) {
        doReturn(attributiToReturn).when(state).helpUIHandleMousePressed(any(Forme.class));
    }

    /**
     * Verifica la corretta creazione dell'Ellisse quando avviene la pressione
     * e successivamente il rilascio del mouse.
     */
    @Test
    void testShapeCreation_CreaEllisse() {
        AttributiFormaDTO attributi = new AttributiFormaDTO();
        attributi.setAltezza(100);
        attributi.setLarghezza(200);
        attributi.setAngoloInclinazione(0);
        attributi.setColore(Color.color(0, 0, 0));
        attributi.setColoreInterno(Color.color(1, 1, 1));

        //Spy della DrawState reale
        DrawState state = spy(new DrawState(Forme.ELLISSE));
        // Stub delle interazioni UI
        stubUiInteractions(state, attributi);

        // Mock degli eventi Mouse
        MouseEvent mousePressedEvent = mock(MouseEvent.class);
        MouseEvent mouseReleasedEvent = mock(MouseEvent.class);

        DrawSnapModel forme = new DrawSnapModel();

        double startX = 50.0;
        double startY = 75.0;

        // Le coordinate di rilascio determinano le dimensioni finali
        double endX = startX + attributi.getLarghezza();
        double endY = startY + attributi.getAltezza();

        // Simulazione pressing del mouse
        boolean pressResult = state.handleMousePressed(mousePressedEvent, forme, startX, startY);

        assertFalse(pressResult, "handleMousePressed dovrebbe restituire false per Ellisse");
        assertEquals(0, forme.size(), "Nessuna forma dovrebbe essere aggiunta solo con mousePressed");
        verify(state).helpUIHandleMousePressed(Forme.ELLISSE);

        // Simulazione rilascio del mouse
        boolean releaseResult = state.handleMouseReleased(mouseReleasedEvent, forme, endX, endY);

        assertTrue(releaseResult, "handleMouseReleased dovrebbe restituire true per Ellisse");
        assertEquals(1, forme.size(), "Ellisse dovrebbe essere stata aggiunta dopo mouseReleased");

        Ellisse forma = (Ellisse) forme.get(0);
        assertNotNull(forma);
        assertEquals("Ellisse", forma.getClass().getSimpleName());

        double expectedCenterX = (startX + endX) / 2.0;
        double expectedCenterY = (startY + endY) / 2.0;

        assertEquals(expectedCenterX, forma.getCoordinataX(), 0.001);
        assertEquals(expectedCenterY, forma.getCoordinataY(), 0.001);
        assertEquals(attributi.getLarghezza(), forma.getLarghezza(), 0.001);
        assertEquals(attributi.getAltezza(), forma.getAltezza(), 0.001);
        assertEquals(attributi.getColore(), forma.getColore());
        assertEquals(attributi.getColoreInterno(), forma.getColoreInterno());
        assertEquals(attributi.getAngoloInclinazione(), forma.getAngoloInclinazione(), 0.001);
    }

    /**
     * Verifica la corretta creazione del Rettangolo quando avviene la pressione
     * e successivamente il rilascio del mouse.
     */
    @Test
    void testShapeCreation_CreaRettangolo() {
        AttributiFormaDTO attributi = new AttributiFormaDTO();
        attributi.setAltezza(50);
        attributi.setLarghezza(80);
        attributi.setColore(Color.BLUE);
        attributi.setColoreInterno(Color.RED);
        attributi.setAngoloInclinazione(0);

        DrawState state = spy(new DrawState(Forme.RETTANGOLO));
        // Stubbiamo delle interazioni UI
        stubUiInteractions(state, attributi);

        MouseEvent mousePressedEvent = mock(MouseEvent.class);
        MouseEvent mouseReleasedEvent = mock(MouseEvent.class);

        DrawSnapModel forme = new DrawSnapModel();

        double startX = 10.0;
        double startY = 20.0;

        double endX = startX + attributi.getLarghezza();
        double endY = startY + attributi.getAltezza();

        // Simulazione pressione del mouse
        boolean pressResult = state.handleMousePressed(mousePressedEvent, forme, startX, startY);

        assertFalse(pressResult, "handleMousePressed dovrebbe restituire false per Rettangolo");
        assertEquals(0, forme.size(), "Nessuna forma dovrebbe essere aggiunta solo con mousePressed");
        verify(state).helpUIHandleMousePressed(Forme.RETTANGOLO);

        // Simulazione rilascio del mouse
        boolean releaseResult = state.handleMouseReleased(mouseReleasedEvent, forme, endX, endY);

        assertTrue(releaseResult, "handleMouseReleased dovrebbe restituire true per Rettangolo");
        assertEquals(1, forme.size(), "Rettangolo dovrebbe essere stato aggiunto dopo mouseReleased");

        Rettangolo forma = (Rettangolo) forme.get(0);
        assertNotNull(forma);
        assertEquals("Rettangolo", forma.getClass().getSimpleName());

        double expectedCenterX = (startX + endX) / 2.0;
        double expectedCenterY = (startY + endY) / 2.0;

        assertEquals(expectedCenterX, forma.getCoordinataX(), 0.001);
        assertEquals(expectedCenterY, forma.getCoordinataY(), 0.001);
        assertEquals(attributi.getLarghezza(), forma.getLarghezza(), 0.001);
        assertEquals(attributi.getAltezza(), forma.getAltezza(), 0.001);
        assertEquals(attributi.getColore(), forma.getColore());
        assertEquals(attributi.getColoreInterno(), forma.getColoreInterno());
        assertEquals(attributi.getAngoloInclinazione(), forma.getAngoloInclinazione(), 0.001);
    }

    /**
     * Verifica la corretta creazione della Linea quando avviene la pressione
     * e successivamente il rilascio del mouse.
     */
    @Test
    void testShapeCreation_CreaLinea() {
        AttributiFormaDTO attributi = new AttributiFormaDTO();
        attributi.setColore(Color.GREEN);
        attributi.setAngoloInclinazione(45.0);

        DrawState state = spy(new DrawState(Forme.LINEA));
        stubUiInteractions(state, attributi);

        // Mock degli eventi mouse
        MouseEvent mousePressedEvent = mock(MouseEvent.class);
        MouseEvent mouseReleasedEvent = mock(MouseEvent.class);
        double startX = 10.0;
        double startY = 10.0;
        double endX = 110.0;
        double endY = 110.0;

        DrawSnapModel forme = new DrawSnapModel();

        // Simulazione pressione del mouse
        boolean pressResult = state.handleMousePressed(mousePressedEvent, forme, startX, startY);

        assertFalse(pressResult, "handleMousePressed dovrebbe restituire false per Linea");
        assertEquals(0, forme.size(), "Nessuna forma dovrebbe essere aggiunta solo con mousePressed per Linea");
        verify(state).helpUIHandleMousePressed(Forme.LINEA);


        // Simulazione rilascio del mouse
        boolean releaseResult = state.handleMouseReleased(mouseReleasedEvent, forme, endX, endY);

        assertTrue(releaseResult, "handleMouseReleased dovrebbe restituire true per Linea");
        assertEquals(1, forme.size(), "La Linea dovrebbe essere stata aggiunta dopo mouseReleased");

        Linea lineaCreata = (Linea) forme.get(0);
        assertNotNull(lineaCreata);
        assertEquals("Linea", lineaCreata.getClass().getSimpleName());

        assertEquals(attributi.getColore(), lineaCreata.getColore());
        // Calcolo della lunghezza e dell'angolo della linea (logica di createShapePreview per LINEA)
        double expectedLength = Math.sqrt(
                Math.pow(endX - startX, 2) +
                        Math.pow(endY - startY, 2)
        );
        double expectedAngle = Math.toDegrees(Math.atan2(endY - startY, endX - startX));

        // Le coordinate centrali per la linea
        double expectedCenterX = (startX + endX) / 2.0;
        double expectedCenterY = (startY + endY) / 2.0;

        assertEquals(expectedCenterX, lineaCreata.getCoordinataX(), 0.001);
        assertEquals(expectedCenterY, lineaCreata.getCoordinataY(), 0.001);
        assertEquals(expectedLength, lineaCreata.getLarghezza(), 0.001);
        assertEquals(expectedAngle, lineaCreata.getAngoloInclinazione(), 0.001);

        assertNull(state.getCurrentDrawingShapePreview(), "La preview dovrebbe essere null dopo il rilascio");
    }

    /**
     * Verifica che una casistica anomala come la creazione della figura con coordinate negative
     * non provochi comportamenti inattesi.
     */
    @Test
    void testShapeCreation_CoordinateNegative() {
        AttributiFormaDTO attributi = new AttributiFormaDTO();
        attributi.setAltezza(30);
        attributi.setLarghezza(40);
        attributi.setColore(Color.GREEN);
        attributi.setAngoloInclinazione(0);
        attributi.setColoreInterno(Color.LIGHTGREEN);

        DrawState state = spy(new DrawState(Forme.ELLISSE));
        doReturn(attributi).when(state).helpUIHandleMousePressed(Forme.ELLISSE);

        MouseEvent mousePressedEvent = mock(MouseEvent.class);
        MouseEvent mouseReleasedEvent = mock(MouseEvent.class);

        DrawSnapModel forme = new DrawSnapModel();

        // Coordinate di inizio negative
        double startX = -10.0;
        double startY = -20.0;

        // Coordinate di fine
        // Ad esempio, una ellisse di larghezza 40 e altezza 30 che inizia a (-10, -20)
        double endX = startX + attributi.getLarghezza();
        double endY = startY + attributi.getAltezza();


        boolean pressResult = state.handleMousePressed(mousePressedEvent, forme, startX, startY);

        assertFalse(pressResult, "handleMousePressed dovrebbe restituire false per Ellisse");
        assertEquals(0, forme.size(), "Nessuna forma dovrebbe essere aggiunta solo con mousePressed");
        verify(state).helpUIHandleMousePressed(Forme.ELLISSE);

        boolean releaseResult = state.handleMouseReleased(mouseReleasedEvent, forme, endX, endY);

        assertTrue(releaseResult, "handleMouseReleased dovrebbe restituire true per Ellisse");
        assertEquals(1, forme.size(), "L'Ellisse dovrebbe essere stata aggiunta dopo mouseReleased");

        Ellisse forma = (Ellisse) forme.get(0);
        assertNotNull(forma);
        assertEquals("Ellisse", forma.getClass().getSimpleName());

        // Verifica delle coordinate e le dimensioni della forma creata
        double expectedCenterX = (startX + endX) / 2.0;
        double expectedCenterY = (startY + endY) / 2.0;

        assertEquals(expectedCenterX, forma.getCoordinataX(), 0.001, "La coordinata X del centro dovrebbe essere corretta");
        assertEquals(expectedCenterY, forma.getCoordinataY(), 0.001, "La coordinata Y del centro dovrebbe essere corretta");

        // Larghezza e altezza sono calcolate come Math.abs(coordinata_finale - coordinata_iniziale)
        assertEquals(attributi.getLarghezza(), forma.getLarghezza(), 0.001, "La larghezza dovrebbe essere corretta");
        assertEquals(attributi.getAltezza(), forma.getAltezza(), 0.001, "L'altezza dovrebbe essere corretta");

        assertEquals(attributi.getColore(), forma.getColore());
        assertEquals(attributi.getColoreInterno(), forma.getColoreInterno());
        assertEquals(attributi.getAngoloInclinazione(), forma.getAngoloInclinazione(), 0.001);

        assertNull(state.getCurrentDrawingShapePreview(), "La preview dovrebbe essere null dopo il rilascio");
    }

    /**
     * Verifica che anche se l'oggetto contenente gli attributi della Forma è null
     * il comportamento resta controllato, senza eccezioni.
     */
    @Test
    void testHandleMousePressed_AttributiNull_DefaultFallback() {
        DrawState state = spy(new DrawState(Forme.RETTANGOLO));
        // Stub di helpUIHandleMousePressed per restituire null, simulando il caso in cui non ci sono attributi
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
     * Verifica la corretta creazione del Poligono attraverso più click.
     */
    @Test
    void testHandleMousePressed_CreaPoligono() {
        AttributiFormaDTO attributiPoligono = new AttributiFormaDTO();
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
        double x_double_click = x3, y_double_click = y3; // Coordinate per il doppio click

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


        // Primo click: apre il dialogo mockato
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
     * Verifica la corretta creazione di un Testo quando avviene la pressione
     * e successivamente il rilascio del mouse.
     */
    @Test
    void testShapeCreation_CreaTesto() {
        AttributiFormaDTO attributi = new AttributiFormaDTO();
        attributi.setAltezza(50);
        attributi.setLarghezza(150);
        attributi.setAngoloInclinazione(0);
        attributi.setColore(Color.RED);
        attributi.setColoreInterno(Color.BLACK);
        attributi.setTesto("Hello World");

        DrawState state = spy(new DrawState(Forme.TESTO));
        stubUiInteractions(state, attributi);

        // Mock degli eventi mouse
        MouseEvent mousePressedEvent = mock(MouseEvent.class);
        MouseEvent mouseReleasedEvent = mock(MouseEvent.class);

        DrawSnapModel forme = new DrawSnapModel();

        // Coordinate di inizio per il drag
        double startX = 100.0;
        double startY = 100.0;

        // Coordinate di fine per il drag
        double endX = startX + attributi.getLarghezza();
        double endY = startY + attributi.getAltezza();

        boolean pressResult = state.handleMousePressed(mousePressedEvent, forme, startX, startY);

        assertFalse(pressResult, "handleMousePressed dovrebbe restituire false per Testo");
        assertEquals(0, forme.size(), "Nessuna forma dovrebbe essere aggiunta solo con mousePressed per Testo");
        verify(state).helpUIHandleMousePressed(Forme.TESTO);

        boolean releaseResult = state.handleMouseReleased(mouseReleasedEvent, forme, endX, endY);

        assertTrue(releaseResult, "handleMouseReleased dovrebbe restituire true per Testo");
        assertEquals(1, forme.size(), "Il Testo dovrebbe essere stato aggiunto dopo mouseReleased");

        Testo forma = (Testo) forme.get(0);
        assertNotNull(forma);
        assertEquals("Testo", forma.getClass().getSimpleName());

        double expectedCenterX = (startX + endX) / 2.0;
        double expectedCenterY = (startY + endY) / 2.0;

        assertEquals(expectedCenterX, forma.getCoordinataX(), 0.001, "La coordinata X del centro dovrebbe essere corretta");
        assertEquals(expectedCenterY, forma.getCoordinataY(), 0.001, "La coordinata Y del centro dovrebbe essere corretta");

        // Larghezza e altezza sono calcolate come Math.abs(coordinata_finale - coordinata_iniziale)
        assertEquals(attributi.getLarghezza(), forma.getLarghezza(), 0.001, "La larghezza dovrebbe essere corretta");
        assertEquals(attributi.getAltezza(), forma.getAltezza(), 0.001, "L'altezza dovrebbe essere corretta");

        assertEquals(attributi.getColore(), forma.getColore());
        assertEquals(attributi.getColoreInterno(), forma.getColoreInterno());
        assertEquals(attributi.getAngoloInclinazione(), forma.getAngoloInclinazione(), 0.001);
        assertEquals(attributi.getTesto(), forma.getTesto(), "Il testo dovrebbe corrispondere");

        assertNull(state.getCurrentDrawingShapePreview(), "La preview dovrebbe essere null dopo il rilascio");
    }
    /**
     * Verifica che il Testo non venga creato se la stringa di testo è vuota.
     */
    @Test
    void testHandleMousePressed_CreaTesto_TestoVuoto() {
        AttributiFormaDTO attributi = new AttributiFormaDTO();
        attributi.setAltezza(50);
        attributi.setLarghezza(150);
        attributi.setAngoloInclinazione(0);
        attributi.setColore(Color.RED);
        attributi.setColoreInterno(Color.BLACK);
        attributi.setTesto("");

        DrawState state = spy(new DrawState(Forme.TESTO));
        doReturn(attributi).when(state).helpUIHandleMousePressed(Forme.TESTO);

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
        DrawState state = spy(new DrawState(Forme.TESTO));
        doReturn(null).when(state).helpUIHandleMousePressed(Forme.TESTO); // Simula l'annullamento del dialogo

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