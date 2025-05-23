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
    void testSaveToMemento() {
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
}
