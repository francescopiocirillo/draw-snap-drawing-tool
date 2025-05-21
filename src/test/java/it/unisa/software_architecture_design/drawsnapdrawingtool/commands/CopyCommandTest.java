package it.unisa.software_architecture_design.drawsnapdrawingtool.commands;

import it.unisa.software_architecture_design.drawsnapdrawingtool.DrawSnapModel;
import it.unisa.software_architecture_design.drawsnapdrawingtool.forme.Forma;
import it.unisa.software_architecture_design.drawsnapdrawingtool.forme.FormaSelezionataDecorator;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CopyCommandTest {

    private DrawSnapModel model;

    @BeforeEach
    void setUp() {
        model = new DrawSnapModel();
    }

    // Verifica che se la lista delle forme è vuota, dopo l'esecuzione di CopyCommand la lista rimane vuota
    @Test
    void testExecute_ListaVuota() {
        CopyCommand copy = new CopyCommand(model);
        copy.execute();

        assertEquals(0, model.sizeFormeCopiate(), "La lista copiata deve essere vuota");
    }

    // Verifica che se nessuna forma è selezionata, CopyCommand non copia nulla nella lista delle forme copiate
    @Test
    void testExecute_NessunaFormaSelezionata() {
        Forma forma = new Forma(10, 20, 30, 0, Color.RED) {
            @Override public void disegna(GraphicsContext gc) {}
            @Override public boolean contiene(double x, double y) { return false; }
        };
        model.add(forma);

        CopyCommand copy = new CopyCommand(model);
        copy.execute();

        assertEquals(0, model.sizeFormeCopiate(), "La lista copiata deve rimanere vuota");
    }



    // Verifica che CopyCommand copi solo le forme selezionate lasciando intatte
    // quelle non selezionate
    @Test
    void testExecute_DueFormeUnaSelezionata() {
        Forma forma1 = new Forma(10, 20, 30, 0, Color.RED) {
            @Override public void disegna(GraphicsContext gc) {}
            @Override public boolean contiene(double x, double y) { return false; }
        };
        Forma formaSelezionata = new FormaSelezionataDecorator(new Forma(70, 80, 30, 0, Color.GREEN) {
            @Override public void disegna(GraphicsContext gc) {}
            @Override public boolean contiene(double x, double y) { return true; }
        });

        model.add(forma1);
        model.add(formaSelezionata);

        CopyCommand copy = new CopyCommand(model);
        copy.execute();

        assertEquals(1, model.sizeFormeCopiate(), "Deve copiare solo la forma selezionata");
        assertTrue(model.contains(forma1), "La forma non selezionata deve rimanere");
        assertTrue(model.contains(formaSelezionata), "La forma selezionata deve rimanere");
    }



    // Verifica che CopyCommand gestisca correttamente forme con colore null,
    // copiandole senza modificare questo attributo
    @Test
    void testExecute_ColoreNull() {
        Forma formaNullColor = new FormaSelezionataDecorator(new Forma(10, 20, 30, 0, null) {
            @Override public void disegna(GraphicsContext gc) {}
            @Override public boolean contiene(double x, double y) { return true; }
        });
        model.add(formaNullColor);

        CopyCommand copy = new CopyCommand(model);
        copy.execute();

        assertEquals(1, model.sizeFormeCopiate());
        assertNull(model.getFormaCopiata(0).getColore(), "Il colore deve rimanere null");
    }

    // Verifica che CopyCommand copi correttamente anche forme con attributi anomali
    // come larghezza 0, coordinate a 0 e colore null
    @Test
    void testExecute_FormaConAttributiAnomali() {
        Forma formaAnomala = new FormaSelezionataDecorator(new Forma(0, 0, 0, 0, null) {
            @Override public void disegna(GraphicsContext gc) {}
            @Override public boolean contiene(double x, double y) { return true; }
        });
        model.add(formaAnomala);

        CopyCommand copy = new CopyCommand(model);
        copy.execute();

        Forma copia = model.getFormaCopiata(0);
        assertEquals(0, copia.getCoordinataX());
        assertEquals(0, copia.getCoordinataY());
        assertEquals(0, copia.getLarghezza());
        assertNull(copia.getColore());
    }
}