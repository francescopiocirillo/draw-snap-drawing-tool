package it.unisa.software_architecture_design.drawsnapdrawingtool;

import it.unisa.software_architecture_design.drawsnapdrawingtool.forme.Ellisse;
import it.unisa.software_architecture_design.drawsnapdrawingtool.forme.Forma;
import it.unisa.software_architecture_design.drawsnapdrawingtool.forme.FormaSelezionataDecorator;
import it.unisa.software_architecture_design.drawsnapdrawingtool.memento.DrawSnapMemento;
import javafx.scene.paint.Color;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class DrawSnapModelTest {

    @Test
    void testSaveToMementoEllisseDecorato() {
        DrawSnapModel model = new DrawSnapModel();

        Ellisse ellisse = new Ellisse(100, 100, 50, 0, Color.BLUE, 80, Color.YELLOW);
        model.add(ellisse);

        // Seleziona la forma (aggiunge il decoratore)
        model.selezionaForma(ellisse);

        // Salva lo stato
        DrawSnapMemento memento = model.saveToMemento();

        // Ottiene la lista di forme salvate
        List<Forma> formeSalvate = memento.getSavedState();

        // Deve esserci una sola forma
        assertEquals(1, formeSalvate.size());

        Forma formaSalvata = formeSalvate.get(0);

        // Non deve essere una forma selezionata (il decoratore deve essere rimosso)
        assertFalse(formaSalvata instanceof FormaSelezionataDecorator);

        // Deve essere un'istanza di Ellisse
        assertTrue(formaSalvata instanceof Ellisse);

        Ellisse ellisseSalvata = (Ellisse) formaSalvata;

        // Confronta attributi per verificarne la correttezza
        assertEquals(ellisse.getCoordinataX(), ellisseSalvata.getCoordinataX());
        assertEquals(ellisse.getCoordinataY(), ellisseSalvata.getCoordinataY());
        assertEquals(ellisse.getLarghezza(), ellisseSalvata.getLarghezza());
        assertEquals(ellisse.getAltezza(), ellisseSalvata.getAltezza());
        assertEquals(ellisse.getColore(), ellisseSalvata.getColore());
        assertEquals(ellisse.getColoreInterno(), ellisseSalvata.getColoreInterno());
    }

    @Test
    void testSaveToMementoModelVuoto() {
        DrawSnapModel model = new DrawSnapModel();

        DrawSnapMemento memento = model.saveToMemento();

        assertTrue(memento.getSavedState().isEmpty(), "il memento dovrebbe essere vuoto visto che il model è vuoto");
    }

    @Test
    void testSaveToMementoMultepliciForme() {
        DrawSnapModel model = new DrawSnapModel();

        Ellisse e1 = new Ellisse(10, 20, 30, 0, Color.RED, 40, Color.BLUE);
        Ellisse e2 = new Ellisse(50, 60, 70, 0, Color.GREEN, 80, Color.YELLOW);
        model.add(e1);
        model.add(e2);

        DrawSnapMemento memento = model.saveToMemento();

        List<Forma> saved = memento.getSavedState();
        assertEquals(2, saved.size());
        assertTrue(saved.get(0) instanceof Ellisse);
        assertTrue(saved.get(1) instanceof Ellisse);
    }

    @Test
    void testSaveToMementoRimuoveDecoratorDaMultepliciForme() {
        DrawSnapModel model = new DrawSnapModel();

        Ellisse e1 = new Ellisse(10, 20, 30, 0, Color.RED, 40, Color.BLUE);
        Ellisse e2 = new Ellisse(50, 60, 70, 0, Color.GREEN, 80, Color.YELLOW);
        model.add(e1);
        model.add(e2);

        model.selezionaForma(e1);
        model.selezionaForma(e2);

        DrawSnapMemento memento = model.saveToMemento();

        for (Forma f : memento.getSavedState()) {
            assertFalse(f instanceof FormaSelezionataDecorator, "FormaSelezionataDecorator dovrebbe essere rimosso");
        }
    }

    @Test
    void testSaveToMementoDeepCopy() {
        DrawSnapModel model = new DrawSnapModel();

        Ellisse original = new Ellisse(10, 20, 30, 0, Color.BLACK, 40, Color.WHITE);
        model.add(original);

        DrawSnapMemento memento = model.saveToMemento();
        Ellisse saved = (Ellisse) memento.getSavedState().get(0);

        // modifica dell'originale
        original.setColoreInterno(Color.RED);

        // verifica che il memento non è impattato dalla modifica dell'originale
        assertNotEquals(original.getColoreInterno(), saved.getColoreInterno(),
                "Memento dovrebbe conservare una deep copy, non una reference");
    }

}
