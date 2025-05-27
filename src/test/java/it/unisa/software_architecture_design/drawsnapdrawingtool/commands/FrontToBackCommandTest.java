package it.unisa.software_architecture_design.drawsnapdrawingtool.commands;

import it.unisa.software_architecture_design.drawsnapdrawingtool.DrawSnapModel;
import it.unisa.software_architecture_design.drawsnapdrawingtool.forme.Forma;
import it.unisa.software_architecture_design.drawsnapdrawingtool.forme.FormaSelezionataDecorator;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class FrontToBackCommandTest {

    private DrawSnapModel model;

    @BeforeEach
    void setUp() {
        model = new DrawSnapModel();
    }

    // Se la lista è vuota, l'esecuzione non deve causare errori
    @Test
    void testExecute_ListaVuota() {
        FrontToBackCommand command = new FrontToBackCommand(model);
        assertDoesNotThrow(command::execute, "Non deve lanciare eccezioni con lista vuota");
        assertEquals(0, model.size(), "La lista deve rimanere vuota");
    }

    //Se non ci sono forme selezionate, nessuna modifica deve essere fatta
    @Test
    void testExecute_NessunaFormaSelezionata() {
        Forma f1 = new Forma(10, 10, 20, 0, Color.RED) {
            @Override public void disegna(GraphicsContext gc) {}
            @Override public boolean contiene(double x, double y) { return false; }
        };
        Forma f2 = new Forma(10, 10, 20, 0, Color.RED) {
            @Override public void disegna(GraphicsContext gc) {}
            @Override public boolean contiene(double x, double y) { return false; }
        };
        model.add(f1);
        model.add(f2);

        FrontToBackCommand command = new FrontToBackCommand(model);
        command.execute();

        // Nessuna forma selezionata: ordine invariato
        assertEquals(f1, model.get(0), "La forma f1 deve rimanere nella stessa posizione");
        assertEquals(f2, model.get(1), "La forma f2 deve rimanere nella stessa posizione");
    }

    // Se c'è una forma selezionata, deve essere spostata in secondo piano
    @Test
    void testExecute_FormaSelezionataVienePortataDietro() {
        Forma nonSelezionata = new Forma(10, 10, 20, 0, Color.BLUE) {
            @Override public void disegna(GraphicsContext gc) {}
            @Override public boolean contiene(double x, double y) { return false; }
        };
        Forma formaSelezionata = new FormaSelezionataDecorator(new Forma(20, 20, 30, 0, Color.GREEN) {
            @Override public void disegna(GraphicsContext gc) {}
            @Override public boolean contiene(double x, double y) { return true; }
        });

        model.add(nonSelezionata);
        model.add(formaSelezionata);

        FrontToBackCommand command = new FrontToBackCommand(model);
        command.execute();

        // La forma selezionata deve essere la prima della lista
        assertEquals(formaSelezionata, model.get(0), "La forma selezionata deve essere portata in secondo piano");
    }

    //test con la forma selezionata già in secondo piano, non deve cambiare niente
    @Test
    void testExecute_FormaGiaInFondo() {
        Forma formaSelezionata = new FormaSelezionataDecorator(new Forma(10, 10, 20, 0, Color.RED) {
            @Override public void disegna(GraphicsContext gc) {}
            @Override public boolean contiene(double x, double y) { return true; }
        });

        model.add(formaSelezionata);
        model.add(new Forma(5, 5, 15, 0, Color.GRAY) {
            @Override public void disegna(GraphicsContext gc) {}
            @Override public boolean contiene(double x, double y) { return false; }
        });

        FrontToBackCommand command = new FrontToBackCommand(model);
        command.execute();

        // La forma selezionata era già in secondo piano, deve restare in prima posizione della lista
        assertEquals(formaSelezionata, model.get(0), "La forma selezionata era già in secondo piano");
    }

    // test con una sola forma nel canva
    @Test
    void testExecute_SoloUnaFormaSelezionata() {
        Forma selezionata = new FormaSelezionataDecorator(new Forma(10, 10, 20, 0, Color.RED) {
            @Override public void disegna(GraphicsContext gc) {}
            @Override public boolean contiene(double x, double y) { return true; }
        });

        model.add(selezionata);

        FrontToBackCommand command = new FrontToBackCommand(model);

        assertDoesNotThrow(command::execute, "Il comando non deve lanciare eccezioni con una sola forma");
        assertEquals(selezionata, model.get(0), "La forma selezionata deve restare nella stessa posizione se è l'unica");
    }

}
