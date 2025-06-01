package it.unisa.software_architecture_design.drawsnapdrawingtool.commands;

import it.unisa.software_architecture_design.drawsnapdrawingtool.DrawSnapModel;
import it.unisa.software_architecture_design.drawsnapdrawingtool.forme.Forma;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PasteCommandTest {

    private DrawSnapModel model;

    @BeforeEach
    void setUp() {
        model = new DrawSnapModel();
    }


    // Incolla correttamente una forma clonata alle coordinate date
    @Test
    void testExecute_ConFormaCopiata() {
        Forma originale = new Forma(10, 20, 30, 0, Color.RED) {
            @Override public void disegna(GraphicsContext gc) {}
            @Override public boolean contiene(double x, double y) { return false; }
            @Override
            public Forma clone() {
                return new Forma(getCoordinataX(), getCoordinataY(), getLarghezza(), 0, getColore()) {
                    @Override public void disegna(GraphicsContext gc) {}
                    @Override public boolean contiene(double x, double y) { return false; }
                    @Override
                    public void specchiaInVerticale(){}

                    @Override
                    public void specchiaInOrizzontale() {}
                };
            }
            @Override
            public void specchiaInVerticale(){}

            @Override
            public void specchiaInOrizzontale() {}
        };
        model.addFormaCopiata(originale);

        PasteCommand paste = new PasteCommand(model, 100, 200);
        paste.execute();

        assertEquals(1, model.size(), "Deve esserci una forma incollata");
        Forma incollata = model.get(0);

        assertNotSame(originale, incollata, "La forma incollata deve essere una copia, non lo stesso riferimento");
        assertEquals(100, incollata.getCoordinataX(), "La coordinata X deve essere quella specificata");
        assertEquals(200, incollata.getCoordinataY(), "La coordinata Y deve essere quella specificata");
        assertEquals(originale.getLarghezza(), incollata.getLarghezza(), "La larghezza deve essere uguale all'originale");
        assertEquals(originale.getColore(), incollata.getColore(), "Il colore deve essere uguale all'originale");
    }

    // Se il clone restituisce null, la forma non deve essere incollata
    @Test
    void testExecute_CloneNull() {
        Forma formaCheNonClona = new Forma(0, 0, 10, 0, Color.BLUE) {
            @Override public void disegna(GraphicsContext gc) {}
            @Override public boolean contiene(double x, double y) { return false; }
            @Override
            public Forma clone() {
                return null; // clone non valido
            }
            @Override
            public void specchiaInVerticale(){}

            @Override
            public void specchiaInOrizzontale() {}
        };
        model.addFormaCopiata(formaCheNonClona);

        PasteCommand paste = new PasteCommand(model, 50, 50);
        paste.execute();

        assertEquals(0, model.size(), "Non deve essere aggiunta nessuna forma se il clone è null");
    }

    // Incolla più volte aggiunge più forme nel modello
    @Test
    void testExecute_MultiplePaste() {
        Forma originale = new Forma(5, 5, 15, 0, Color.GREEN) {
            @Override public void disegna(GraphicsContext gc) {}
            @Override public boolean contiene(double x, double y) { return false; }
            @Override
            public Forma clone() {
                return new Forma(getCoordinataX(), getCoordinataY(), getLarghezza(), 0, getColore()) {
                    @Override public void disegna(GraphicsContext gc) {}
                    @Override public boolean contiene(double x, double y) { return false; }
                    @Override
                    public void specchiaInVerticale(){}

                    @Override
                    public void specchiaInOrizzontale() {}
                };
            }
            @Override
            public void specchiaInVerticale(){}

            @Override
            public void specchiaInOrizzontale() {}
        };
        model.addFormaCopiata(originale);

        PasteCommand paste1 = new PasteCommand(model, 10, 20);
        PasteCommand paste2 = new PasteCommand(model, 30, 40);

        paste1.execute();
        paste2.execute();

        assertEquals(2, model.size(), "Devono esserci due forme incollate");

        Forma prima = model.get(0);
        Forma seconda = model.get(1);

        assertEquals(10, prima.getCoordinataX());
        assertEquals(20, prima.getCoordinataY());

        assertEquals(30, seconda.getCoordinataX());
        assertEquals(40, seconda.getCoordinataY());
    }

    // Verifica che dopo l'esecuzione di PasteCommand la lista delle forme copiate rimanga invariata
    @Test
    void testExecute_FormeCopiateInvariate() {
        Forma formaCopiata = new Forma(15, 25, 35, 0, Color.BLUE) {
            @Override public void disegna(GraphicsContext gc) {}
            @Override public boolean contiene(double x, double y) { return false; }
            @Override
            public Forma clone() {
                return new Forma(getCoordinataX(), getCoordinataY(), getLarghezza(), 0, getColore()) {
                    @Override public void disegna(GraphicsContext gc) {}
                    @Override public boolean contiene(double x, double y) { return false; }
                    @Override
                    public void specchiaInVerticale(){}

                    @Override
                    public void specchiaInOrizzontale() {}
                };
            }
            @Override
            public void specchiaInVerticale(){}

            @Override
            public void specchiaInOrizzontale() {}
        };
        model.addFormaCopiata(formaCopiata);

        int copiePrima = model.sizeFormeCopiate();

        PasteCommand paste = new PasteCommand(model, 50, 60);
        paste.execute();

        int copieDopo = model.sizeFormeCopiate();

        assertEquals(copiePrima, copieDopo, "La lista delle forme copiate non deve essere modificata");
    }

    // Verifica che se nella lista forme ci sono già delle forme, PasteCommand aggiunge senza rimuovere quelle esistenti
    @Test
    void testExecute_ConFormePreesistenti() {
        Forma formaPreesistente = new Forma(5, 5, 20, 0, Color.BLACK) {
            @Override public void disegna(GraphicsContext gc) {}
            @Override public boolean contiene(double x, double y) { return false; }
            @Override
            public void specchiaInVerticale(){}

            @Override
            public void specchiaInOrizzontale() {}
        };
        model.add(formaPreesistente);

        Forma formaCopiata = new Forma(10, 10, 25, 0, Color.ORANGE) {
            @Override public void disegna(GraphicsContext gc) {}
            @Override public boolean contiene(double x, double y) { return false; }
            @Override
            public Forma clone() {
                return new Forma(getCoordinataX(), getCoordinataY(), getLarghezza(), 0, getColore()) {
                    @Override public void disegna(GraphicsContext gc) {}
                    @Override public boolean contiene(double x, double y) { return false; }
                    @Override
                    public void specchiaInVerticale(){}

                    @Override
                    public void specchiaInOrizzontale() {}
                };
            }
            @Override
            public void specchiaInVerticale(){}

            @Override
            public void specchiaInOrizzontale() {}
        };
        model.addFormaCopiata(formaCopiata);

        int sizePrima = model.size();

        PasteCommand paste = new PasteCommand(model, 100, 100);
        paste.execute();

        assertEquals(sizePrima + 1, model.size(), "La forma incollata deve essere aggiunta senza rimuovere le esistenti");
        assertTrue(model.contains(formaPreesistente), "La forma preesistente deve essere ancora presente");
    }

}