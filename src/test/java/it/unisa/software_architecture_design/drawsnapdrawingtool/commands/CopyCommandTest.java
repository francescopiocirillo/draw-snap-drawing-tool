package it.unisa.software_architecture_design.drawsnapdrawingtool.commands;

import it.unisa.software_architecture_design.drawsnapdrawingtool.forme.Forma;
import it.unisa.software_architecture_design.drawsnapdrawingtool.forme.FormaSelezionataDecorator;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CopyCommandTest {

    private List<Forma> forme;
    private List<Forma> formeCopiate;

    @BeforeEach
    void setUp() {
        forme = new ArrayList<>();
        formeCopiate = new ArrayList<>();
    }

    @Test
    void testExecute_ListaVuota() {
        // Lista di partenza vuota
        Command copy = new CopyCommand(forme, formeCopiate);
        copy.execute();

        // Nessuna forma da copiare
        assertEquals(0, formeCopiate.size(), "La lista copiata deve essere vuota");
    }

    @Test
    void testExecute_NessunaFormaSelezionata() {
        // Forma normale non selezionata
        Forma forma = new Forma(10, 20, 30, 0, Color.RED) {
            @Override public void disegna(GraphicsContext gc) {}
            @Override public boolean contiene(double x, double y) { return false; }
        };

        forme.add(forma);

        Command copy = new CopyCommand(forme, formeCopiate);
        copy.execute();

        // Nessuna copia deve essere fatta
        assertEquals(0, formeCopiate.size(), "La lista copiata deve rimanere vuota");
    }

    @Test
    void testExecute_FormaSelezionata() {
        // Forma decorata come selezionata
        Forma formaSelezionata = new FormaSelezionataDecorator(new Forma(50, 60, 30, 0, Color.BLUE) {
            @Override public void disegna(GraphicsContext gc) {}
            @Override public boolean contiene(double x, double y) { return true; }
        });

        forme.add(formaSelezionata);

        Command copy = new CopyCommand(forme, formeCopiate);
        copy.execute();

        // Deve essere copiata
        assertEquals(1, formeCopiate.size(), "La forma selezionata deve essere copiata");
        assertTrue(formeCopiate.getFirst().confrontaAttributi(formaSelezionata), "Gli attributi devono essere identici");
        assertNotSame(formeCopiate.getFirst(), formaSelezionata, "La copia deve essere una nuova istanza");
    }

    @Test
    void testExecute_ColoreNull() {
        // Forma selezionata con colore null
        Forma formaNullColor = new FormaSelezionataDecorator(new Forma(10, 20, 30, 0, null) {
            @Override public void disegna(GraphicsContext gc) {}
            @Override public boolean contiene(double x, double y) { return false; }
        });

        forme.add(formaNullColor);

        Command copy = new CopyCommand(forme, formeCopiate);
        copy.execute();

        assertEquals(1, formeCopiate.size(), "La forma con colore null deve essere copiata");
        assertNull(formeCopiate.getFirst().getColore(), "Il colore deve rimanere null nella copia");
    }

    @Test
    void testExecute_FormaConAttributiAnomali() {
        // Forma selezionata con attributi anomali (es. larghezza = 0)
        Forma formaAnomala = new FormaSelezionataDecorator(new Forma(0, 0, 0, 0, null) {
            @Override public void disegna(GraphicsContext gc) {}
            @Override public boolean contiene(double x, double y) { return false; }
        });

        forme.add(formaAnomala);

        Command copy = new CopyCommand(forme, formeCopiate);
        copy.execute();

        assertEquals(1, formeCopiate.size(), "La forma anomala deve essere copiata");
        Forma copia = formeCopiate.getFirst();
        assertEquals(0, copia.getCoordinataX());
        assertEquals(0, copia.getCoordinataY());
        assertEquals(0, copia.getLarghezza());
        assertNull(copia.getColore());
    }
}
