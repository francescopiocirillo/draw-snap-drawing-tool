package it.unisa.software_architecture_design.drawsnapdrawingtool.interactionstate;

import it.unisa.software_architecture_design.drawsnapdrawingtool.enumeration.Forme;
import it.unisa.software_architecture_design.drawsnapdrawingtool.forme.AttributiForma;
import it.unisa.software_architecture_design.drawsnapdrawingtool.forme.Forma;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Classe di test per {@link DrawState}
 */
class DrawStateTest {

    /**
     * Classe che estende DrawState facendo l'override di {@code helpUIHandleMousePressed} in modo
     * da non dover interagire con l'interfaccia grafica.
     */
    class TestableDrawState extends DrawState {
        private final AttributiForma attributiFinti;

        public TestableDrawState(Forme formaCorrente, AttributiForma attributiFinti) {
            super(formaCorrente);
            this.attributiFinti = attributiFinti;
        }

        @Override
        protected AttributiForma helpUIHandleMousePressed() {
            return attributiFinti;
        }
    }

    /**
     * Verifica la corretta creazione dell'Ellisse quando avviene la pressione del mouse.
     */
    @Test
    void testHandleMousePressed_CreaEllisse() {
        // Setup
        AttributiForma attributi = new AttributiForma();
        attributi.setAltezza(100);
        attributi.setLarghezza(200);
        attributi.setAngoloInclinazione(0);

        Color coloreNero = Color.color(0, 0, 0); // nero
        Color coloreBianco = Color.color(1, 1, 1); // bianco

        attributi.setColore(coloreNero);
        attributi.setColoreInterno(coloreBianco);

        DrawState state = new TestableDrawState(Forme.ELLISSE, attributi);

        MouseEvent mouseEvent = mock(MouseEvent.class);
        when(mouseEvent.getX()).thenReturn(50.0);
        when(mouseEvent.getY()).thenReturn(75.0);

        List<Forma> forme = new ArrayList<>();

        // Chiamata al metodo
        state.handleMousePressed(mouseEvent, forme);

        // Assert
        assertEquals(1, forme.size());
        Forma forma = forme.get(0);
        assertNotNull(forma);
        assertEquals("Ellisse", forma.getClass().getSimpleName());
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

        DrawState state = new TestableDrawState(Forme.RETTANGOLO, attributi);

        MouseEvent mouseEvent = mock(MouseEvent.class);
        when(mouseEvent.getX()).thenReturn(10.0);
        when(mouseEvent.getY()).thenReturn(20.0);

        List<Forma> forme = new ArrayList<>();

        state.handleMousePressed(mouseEvent, forme);

        assertEquals(1, forme.size());
        assertEquals("Rettangolo", forme.get(0).getClass().getSimpleName());
    }

    /**
     * Verifica la corretta creazione della Linea quando avviene la pressione del mouse.
     */
    @Test
    void testHandleMousePressed_CreaLinea() {
        AttributiForma attributi = new AttributiForma();
        attributi.setAltezza(0); // La linea potrebbe non avere altezza/larghezza significative
        attributi.setLarghezza(0);
        attributi.setColore(Color.BLACK);

        DrawState state = new TestableDrawState(Forme.LINEA, attributi);

        MouseEvent mouseEvent = mock(MouseEvent.class);
        when(mouseEvent.getX()).thenReturn(0.0);
        when(mouseEvent.getY()).thenReturn(0.0);

        List<Forma> forme = new ArrayList<>();

        state.handleMousePressed(mouseEvent, forme);

        assertEquals(1, forme.size());
        assertEquals("Linea", forme.get(0).getClass().getSimpleName());
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

        DrawState state = new TestableDrawState(Forme.ELLISSE, attributi);

        MouseEvent mouseEvent = mock(MouseEvent.class);
        when(mouseEvent.getX()).thenReturn(-10.0);
        when(mouseEvent.getY()).thenReturn(-20.0);

        List<Forma> forme = new ArrayList<>();

        state.handleMousePressed(mouseEvent, forme);

        assertEquals(1, forme.size());
        Forma forma = forme.get(0);
        assertNotNull(forma);
    }

    /**
     * Verifica che anche se l'oggetto contenente gli attributi della Forma è null
     * il comportamento resta controllato, senza eccezioni ma invece con parametri di default.
     */
    @Test
    void testHandleMousePressed_AttributiNull_DefaultFallback() {
        DrawState state = new TestableDrawState(Forme.RETTANGOLO, null);

        MouseEvent mouseEvent = mock(MouseEvent.class);
        when(mouseEvent.getX()).thenReturn(10.0);
        when(mouseEvent.getY()).thenReturn(20.0);

        List<Forma> forme = new ArrayList<>();

        assertDoesNotThrow(() -> state.handleMousePressed(mouseEvent, forme));

        assertEquals(1, forme.size());
        Forma forma = forme.get(0);
        assertNotNull(forma);
        assertEquals("Rettangolo", forma.getClass().getSimpleName());
    }

}
