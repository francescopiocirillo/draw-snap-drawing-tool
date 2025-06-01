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

class BackToFrontCommandTest {

    private DrawSnapModel model;

    @BeforeEach
    void setUp() {
        model = new DrawSnapModel();
    }

    // Se la lista è vuota, l'esecuzione non deve causare errori
    @Test
    void testExecute_ListaVuota() {
        BackToFrontCommand command = new BackToFrontCommand(model);
        assertDoesNotThrow(command::execute, "Non deve lanciare eccezioni con lista vuota");
        assertEquals(0, model.size(), "La lista deve rimanere vuota");
    }

    //Se non ci sono forme selezionate, nessuna modifica deve essere fatta
    @Test
    void testExecute_NessunaFormaSelezionata() {
        Forma f1 = new Forma(10, 10, 20, 0, Color.RED) {
            @Override public void disegna(GraphicsContext gc) {}
            @Override public boolean contiene(double x, double y) { return false; }
            @Override
            public void specchiaInVerticale(){}

            @Override
            public void specchiaInOrizzontale() {}
        };
        Forma f2 = new Forma(10, 10, 20, 0, Color.RED) {
            @Override public void disegna(GraphicsContext gc) {}
            @Override public boolean contiene(double x, double y) { return false; }
            @Override
            public void specchiaInVerticale(){}

            @Override
            public void specchiaInOrizzontale() {}
        };
        model.add(f1);
        model.add(f2);

        BackToFrontCommand command = new BackToFrontCommand(model);
        command.execute();

        assertEquals(f1, model.get(0), "La forma f1 deve rimanere nella stessa posizione");
        assertEquals(f2, model.get(1), "La forma f2 deve rimanere nella stessa posizione");

    }

    // Se c'è una forma selezionata, deve essere spostata in primo piano
    @Test
    void testExecute_FormaSelezionataVienePortataDavanti() {
        Forma nonSelezionata = new Forma(10, 10, 20, 0, Color.BLUE) {
            @Override public void disegna(GraphicsContext gc) {}
            @Override public boolean contiene(double x, double y) { return false; }
            @Override
            public void specchiaInVerticale(){}

            @Override
            public void specchiaInOrizzontale() {}
        };
        Forma formaSelezionata = new FormaSelezionataDecorator(new Forma(20, 20, 30, 0, Color.GREEN) {
            @Override public void disegna(GraphicsContext gc) {}
            @Override public boolean contiene(double x, double y) { return true; }
            @Override
            public void specchiaInVerticale(){}

            @Override
            public void specchiaInOrizzontale() {}
        });

        model.add(nonSelezionata);
        model.add(formaSelezionata);

        BackToFrontCommand command = new BackToFrontCommand(model);
        command.execute();

        // Dopo l'esecuzione, la forma selezionata deve essere l'ultima
        assertEquals(formaSelezionata, model.get(model.size() - 1), "La forma selezionata deve essere portata alla fine della lista");
    }

    //test con la forma selezionata già in primo piano, non deve cambiare niente
    @Test
    void testExecute_FormaGiaInCima() {
        Forma formaSelezionata = new FormaSelezionataDecorator(new Forma(10, 10, 20, 0, Color.RED) {
            @Override public void disegna(GraphicsContext gc) {}
            @Override public boolean contiene(double x, double y) { return true; }
            @Override
            public void specchiaInVerticale(){}

            @Override
            public void specchiaInOrizzontale() {}
        });

        model.add(new Forma(5, 5, 15, 0, Color.GRAY) {
            @Override public void disegna(GraphicsContext gc) {}
            @Override public boolean contiene(double x, double y) { return false; }
            @Override
            public void specchiaInVerticale(){}

            @Override
            public void specchiaInOrizzontale() {}
        });
        model.add(formaSelezionata);

        BackToFrontCommand command = new BackToFrontCommand(model);
        command.execute();

        assertEquals(formaSelezionata, model.get(model.size() - 1), "La forma selezionata era già in cima alla lista e deve restare lì");
    }

    // test con una sola forma nel canva
    @Test
    void testExecute_SoloUnaFormaSelezionata() {
        Forma selezionata = new FormaSelezionataDecorator(new Forma(10, 10, 20, 0, Color.RED) {
            @Override public void disegna(GraphicsContext gc) {}
            @Override public boolean contiene(double x, double y) { return true; }
            @Override
            public void specchiaInVerticale(){}

            @Override
            public void specchiaInOrizzontale() {}
        });

        model.add(selezionata);

        BackToFrontCommand command = new BackToFrontCommand(model);

        assertDoesNotThrow(command::execute, "Il comando non deve lanciare eccezioni con una sola forma");
        assertEquals(selezionata, model.get(0), "La forma selezionata deve restare nella stessa posizione se è l'unica");
    }



}
