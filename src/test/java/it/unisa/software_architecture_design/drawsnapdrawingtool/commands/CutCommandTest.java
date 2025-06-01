package it.unisa.software_architecture_design.drawsnapdrawingtool.commands;

import it.unisa.software_architecture_design.drawsnapdrawingtool.DrawSnapModel;
import it.unisa.software_architecture_design.drawsnapdrawingtool.forme.Forma;
import it.unisa.software_architecture_design.drawsnapdrawingtool.forme.FormaSelezionataDecorator;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
class CutCommandTest {

    private DrawSnapModel forme;

    @BeforeEach
    void setUp() {
        forme = new DrawSnapModel();
    }

    @Test
    void testExecute_ListaVuota(){
        forme.clear();

        //Eseguo il comando di taglia
        Command cut = new CutCommand(forme);
        cut.execute();

        //Controllo la modifica della dimensione della lista iniziale di forme
        assertEquals(0, forme.size(), "Dimensione errata");

        //Controllo se la dimensione della lista FormeCopiate è corretta
        assertEquals(0, forme.sizeFormeCopiate(), "Dimensione errata");
    }

    @Test
    void testExecute_NessunaFormaSelezionata(){
        //Creo una prima forma
        Forma forma = new Forma(10, 20, 30, 0, Color.RED) {
            @Override
            public void disegna(GraphicsContext gc) {
                //Mock
            }

            @Override
            public boolean contiene(double puntoDaValutareX, double puntoDaValutareY) {
                return false; //Mock: nessun punto è contenuto
            }

            @Override
            public void specchiaInVerticale(){}

            @Override
            public void specchiaInOrizzontale() {}

        };

        //Aggiungo la forma a lista
        forme.add(forma);

        //Eseguo il comando di taglia
        Command cut = new CutCommand(forme);
        cut.execute();

        //Controllo la modifica della dimensione della lista iniziale di forme
        assertEquals(1, forme.size(), "Dimensione errata");

        //Controllo se la dimensione della lista FormeCopiate è corretta
        assertEquals(0, forme.sizeFormeCopiate(), "Dimensione errata");
    }

    @Test
    void testExecute_UnaFormaSelezionata(){
        //Creo una forma selezionata
        Forma formaSelezionata = new FormaSelezionataDecorator(new Forma(50, 60, 30, 0, Color.BLUE) {
            @Override
            public void disegna(GraphicsContext gc) {
                // Mock
            }

            @Override
            public boolean contiene(double puntoDaValutareX, double puntoDaValutareY) {
                return true; // Mock: Il punto è contenuto
            }
            @Override
            public void specchiaInVerticale(){}

            @Override
            public void specchiaInOrizzontale() {}
        });

        //Aggiungo la forma a lista
        forme.add(formaSelezionata);

        //Eseguo il comando di taglia
        Command cut = new CutCommand(forme);
        cut.execute();

        //Controllo cse la forma selezionata è ancora presente nella lista forme
        assertFalse(forme.contains(formaSelezionata), "Forma selezionata ancora presente nel foglio di disegno");

        //Controllo se la dimensione della lista FormeCopiate è corretta
        assertEquals(1, forme.sizeFormeCopiate(), "Dimensione errata");

        //Controllo che la forma copiata è uguale (a livello di attributi) a quella selezionata
        assertTrue( forme.getFirstFormaCopiata().confrontaAttributi(formaSelezionata),"Forma selezionata non presente tra quelle copiate");

        //Controllo che la forma copiata non abbia lo stesso riferimento di quella selezionata
        assertNotSame(forme.getFirstFormaCopiata(), formaSelezionata, "le due forme hanno lo stesso riferimento");

        //Controllo la modifica della dimensione della lista iniziale di forme
        assertEquals(0, forme.size(), "Dimensione errata");

    }

    @Test
    void testExecute_dueFormeSulFoglio(){
        //Creo una prima forma
        Forma forma1 = new Forma(10, 20, 30, 0, Color.RED) {
            @Override
            public void disegna(GraphicsContext gc) {
                //Mock
            }

            @Override
            public boolean contiene(double puntoDaValutareX, double puntoDaValutareY) {
                return false; //Mock: nessun punto è contenuto
            }

            @Override
            public void specchiaInVerticale(){}

            @Override
            public void specchiaInOrizzontale() {}


        };

        //Ne creo una seconda selezionata
        Forma formaSelezionata = new FormaSelezionataDecorator(new Forma(50, 60, 30, 0, Color.BLUE) {
            @Override
            public void disegna(GraphicsContext gc) {
                // Mock
            }

            @Override
            public boolean contiene(double puntoDaValutareX, double puntoDaValutareY) {
                return true; // Mock: Il punto è contenuto
            }
            @Override
            public void specchiaInVerticale(){}

            @Override
            public void specchiaInOrizzontale() {}

        });

        //Aggiungo le forma a lista
        forme.add(forma1);
        forme.add(formaSelezionata);

        //Eseguo il comando di taglia
        Command cut = new CutCommand(forme);
        cut.execute();

        //Controllo cse la forma selezionata è ancora presente nella lista forme
        assertFalse(forme.contains(formaSelezionata), "Forma selezionata ancora presente nel foglio di disegno");

        //Controllo se la dimensione della lista FormeCopiate è corretta
        assertEquals(1, forme.sizeFormeCopiate(), "Dimensione errata");

        //Controllo che la forma copiata è uguale (a livello di attributi) a quella selezionata
        assertTrue( forme.getFirstFormaCopiata().confrontaAttributi(formaSelezionata),"Forma selezionata non presente tra quelle copiate");

        //Controllo che la forma copiata non abbia lo stesso riferimento di quella selezionata
        assertNotSame(forme.getFirstFormaCopiata(), formaSelezionata, "le due forme hanno lo stesso riferimento");

        //Controllo la modifica della lista iniziale di forme
        assertEquals(1, forme.size(), "Dimensione errata");
        assertTrue(forme.contains(forma1), "Forma normale non presente nel foglio di disegno");
    }

    @Test
    void testExecute_tanteFormeSulFoglio(){
        //Creo venti forme
        for (int i = 0; i < 20; i++) {
            forme.add(new Forma(10 + i, 20 + i, 30, 0, Color.RED) {
                @Override
                public void disegna(GraphicsContext gc) {}
                @Override
                public boolean contiene(double puntoDaValutareX, double puntoDaValutareY) { return false; }

                @Override
                public void specchiaInVerticale(){}

                @Override
                public void specchiaInOrizzontale() {}
            });
        }

        //ne creo una selezionata
        Forma formaSelezionata = new FormaSelezionataDecorator(new Forma(50, 60, 30, 0, Color.BLUE) {
            @Override
            public void disegna(GraphicsContext gc) {}
            @Override
            public boolean contiene(double puntoDaValutareX, double puntoDaValutareY) { return true; }

            @Override
            public void specchiaInVerticale(){}

            @Override
            public void specchiaInOrizzontale() {}
        });

        //Aggiungo la forma selezionata a lista
        forme.add(formaSelezionata);

        //Eseguo il comando di taglia
        Command cut = new CutCommand(forme);
        cut.execute();

        //Controllo cse la forma selezionata è ancora presente nella lista forme
        assertFalse(forme.contains(formaSelezionata), "Forma selezionata ancora presente nel foglio di disegno");

        //Controllo se la dimensione della lista FormeCopiate è corretta
        assertEquals(1, forme.sizeFormeCopiate(), "Dimensione errata");

        //Controllo che la forma copiata è uguale (a livello di attributi) a quella selezionata
        assertTrue( forme.getLastFormaCopiata().confrontaAttributi(formaSelezionata),"Forma selezionata non presente tra quelle copiate");

        //Controllo che la forma copiata non abbia lo stesso riferimento di quella selezionata
        assertNotSame(forme.getLastFormaCopiata(), formaSelezionata, "le due forme hanno lo stesso riferimento");

        //Controllo la modifica della dimensione della lista iniziale di forme
        assertEquals(20, forme.size(), "Dimensione errata");
    }

}