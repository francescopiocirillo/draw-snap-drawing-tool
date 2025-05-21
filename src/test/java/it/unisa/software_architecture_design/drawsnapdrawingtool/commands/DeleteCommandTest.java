package it.unisa.software_architecture_design.drawsnapdrawingtool.commands;

import it.unisa.software_architecture_design.drawsnapdrawingtool.DrawSnapModel;
import it.unisa.software_architecture_design.drawsnapdrawingtool.forme.Forma;
import it.unisa.software_architecture_design.drawsnapdrawingtool.forme.FormaSelezionataDecorator;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class DeleteCommandTest {

    @Test
    void testExecute_dueFormeSulFoglio() {
        // creo una lista di forme con un'implementazione anonima di Forma
        Forma forma1 = new Forma(10, 20, 30, 0, Color.RED) {

            @Override
            public void disegna(GraphicsContext gc) {
                // Mock
            }

            @Override
            public boolean contiene(double puntoDaValutareX, double puntoDaValutareY) {
                return false; // Mock: Nessun punto è contenuto
            }
        };

        Forma formaSelezionata = new FormaSelezionataDecorator(new Forma(50, 60, 30, 0, Color.BLUE) {
            @Override
            public void disegna(GraphicsContext gc) {
                // Mock
            }

            @Override
            public boolean contiene(double puntoDaValutareX, double puntoDaValutareY) {
                return true; // Mock: Il punto è contenuto
            }
        });

        DrawSnapModel forme = new DrawSnapModel();
        forme.addAll(List.of(forma1, formaSelezionata));
        DeleteCommand deleteCommand = new DeleteCommand(forme);

        deleteCommand.execute();

        assertEquals(1, forme.size());// verifico il numero di forme presenti nella listo dopo l'eliminazione
        assertFalse(forme.contains(formaSelezionata)); //verifico se il punto è ancora contenuto nella formaSelezionata,
        //non deve essere contenuto perchè la forma non è più presente nel foglio di disegno
    }

    @Test
    void testExecute_unaFormaSelezionata() {
        Forma formaSelezionata = new FormaSelezionataDecorator(new Forma(10, 20, 30, 0, Color.RED) {
            @Override
            public void disegna(GraphicsContext gc) {}
            @Override
            public boolean contiene(double puntoDaValutareX, double puntoDaValutareY) { return true; }
        });

        DrawSnapModel forme = new DrawSnapModel();
        forme.addAll(List.of(formaSelezionata));
        DeleteCommand deleteCommand = new DeleteCommand(forme);

        deleteCommand.execute();

        assertEquals(0, forme.size());
    }

    @Test
    void testExecute_formeSovrapposte() {
        Forma forma1 = new Forma(50, 50, 30, 0, Color.RED) {
            @Override
            public void disegna(GraphicsContext gc) {}
            @Override
            public boolean contiene(double puntoDaValutareX, double puntoDaValutareY) {
                return puntoDaValutareX >= 50 && puntoDaValutareX <= 80 &&
                        puntoDaValutareY >= 50 && puntoDaValutareY <= 80;
            }
        };

        Forma formaSelezionata = new FormaSelezionataDecorator(new Forma(50, 50, 30, 0, Color.BLUE) {
            @Override
            public void disegna(GraphicsContext gc) {}
            @Override
            public boolean contiene(double puntoDaValutareX, double puntoDaValutareY) {
                return puntoDaValutareX >= 50 && puntoDaValutareX <= 80 &&
                        puntoDaValutareY >= 50 && puntoDaValutareY <= 80;
            }
        });

        Forma forma3 = new Forma(50, 50, 30, 0, Color.GREEN) {
            @Override
            public void disegna(GraphicsContext gc) {}
            @Override
            public boolean contiene(double puntoDaValutareX, double puntoDaValutareY) {
                return puntoDaValutareX >= 50 && puntoDaValutareX <= 80 &&
                        puntoDaValutareY >= 50 && puntoDaValutareY <= 80;
            }
        };

        DrawSnapModel forme = new DrawSnapModel();
        forme.addAll(List.of(forma1, formaSelezionata, forma3));
        DeleteCommand deleteCommand = new DeleteCommand(forme);

        deleteCommand.execute();

        assertEquals(2, forme.size());
        assertTrue(forme.contains(forma1));
        assertTrue(forme.contains(forma3));
        assertFalse(forme.contains(formaSelezionata));
    }

    @Test
    void testExecute_tanteFormeSulFoglio() {
        // Arrange
        DrawSnapModel forme = new DrawSnapModel();
        for (int i = 0; i < 20; i++) {
            forme.add(new Forma(10 + i, 20 + i, 30, 0, Color.RED) {
                @Override
                public void disegna(GraphicsContext gc) {}
                @Override
                public boolean contiene(double puntoDaValutareX, double puntoDaValutareY) { return false; }
            });
        }

        Forma formaSelezionata = new FormaSelezionataDecorator(new Forma(50, 60, 30, 0, Color.BLUE) {
            @Override
            public void disegna(GraphicsContext gc) {}
            @Override
            public boolean contiene(double puntoDaValutareX, double puntoDaValutareY) { return true; }
        });

        forme.add(formaSelezionata);
        DeleteCommand deleteCommand = new DeleteCommand(forme);

        deleteCommand.execute();

        assertEquals(20, forme.size());
        assertFalse(forme.contains(formaSelezionata));
    }
}