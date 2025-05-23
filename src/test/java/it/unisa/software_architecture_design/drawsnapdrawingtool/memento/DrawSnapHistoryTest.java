package it.unisa.software_architecture_design.drawsnapdrawingtool.memento;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class DrawSnapHistoryTest {

    private DrawSnapHistory history;

    @BeforeEach
    public void setup() {
        history = new DrawSnapHistory();
    }

    @Test
    public void testUndoConHistoryVuota() {
        DrawSnapMemento result = history.undo();
        assertNotNull(result, "Undo con una empty history dovrebbe fornire un memento di nuova creazione");
    }

    @Test
    public void testUndoConUnSoloStato() {
        DrawSnapMemento state1 = new DrawSnapMemento();
        history.saveState(state1);

        DrawSnapMemento result = history.undo();

        // Deve restituire un nuovo memento, non quello appena rimosso
        assertNotNull(result);
        assertNotSame(state1, result, "Con un unico stato salvato dovrà restituire un memento di nuova creazione");
    }

    @Test
    public void testUndoConMultipliStati() {
        DrawSnapMemento state1 = new DrawSnapMemento();
        DrawSnapMemento state2 = new DrawSnapMemento();

        history.saveState(state1);
        history.saveState(state2);

        DrawSnapMemento result = history.undo();

        assertSame(state1, result, "Undo dovrebbe restituire lo stato precedente che è State1");
    }

    @Test
    public void testMultepliciChiamateAdUndoCheEccedonoIlNumeroDiStatiSalvati() {
        history.saveState(new DrawSnapMemento());
        history.undo(); // Rimuove l'unico stato presente
        DrawSnapMemento result = history.undo(); // Dovrebbe restituire un nuovo memento
        assertNotNull(result);
    }

    @Test
    public void testUndoNonRimuoveLoStatoPrecedente() {
        DrawSnapMemento state1 = new DrawSnapMemento();
        history.saveState(state1);
        history.saveState(new DrawSnapMemento());

        history.undo(); // Dovrebbe scartare la cima dello stack
        DrawSnapMemento result = history.undo(); // Ora State1 dovrebbe essere la cima dello stack
        assertNotSame(state1, result); // perché state1 è stato rimosso dalla seconda undo
    }

    @Test
    public void testUndoDopoTreSalvataggi() {
        DrawSnapMemento state1 = new DrawSnapMemento();
        DrawSnapMemento state2 = new DrawSnapMemento();
        DrawSnapMemento state3 = new DrawSnapMemento();

        history.saveState(state1);
        history.saveState(state2);
        history.saveState(state3);

        DrawSnapMemento result = history.undo(); // lo stato atteso è state2
        assertSame(state2, result);
    }

    @Test
    public void testUndoRestituisceInOrdineCorretto() {
        DrawSnapMemento state1 = new DrawSnapMemento();
        DrawSnapMemento state2 = new DrawSnapMemento();
        history.saveState(state1);
        history.saveState(state2);

        DrawSnapMemento firstUndo = history.undo();  // lo stato atteso è state1
        DrawSnapMemento secondUndo = history.undo(); // dovrebbe essere un new memento()

        assertSame(state1, firstUndo);
        assertNotSame(state1, secondUndo);
    }

    @Test
    public void testUndoAlternanzaSalvataggiUndo() {
        DrawSnapMemento state1 = new DrawSnapMemento();
        DrawSnapMemento state2 = new DrawSnapMemento();
        history.saveState(state1);
        history.undo(); // undo -> new memento
        history.saveState(state2);

        DrawSnapMemento result = history.undo(); // dovrebbe restituire new memento
        assertNotSame(state1, result);
        assertNotSame(state2, result);
    }

    @Test
    public void testUndoNonLanciaEccezioni() {
        assertDoesNotThrow(() -> {
            history.undo();
            history.undo();
        });
    }

    @Test
    public void testUndoRispettaOrdineEventi() {
        DrawSnapMemento state1 = new DrawSnapMemento();
        DrawSnapMemento state2 = new DrawSnapMemento();
        history.saveState(state1);
        history.saveState(state2);

        DrawSnapMemento firstUndo = history.undo();
        DrawSnapMemento secondUndo = history.undo();

        // history dovrebbe ora essere vuoto, il terzo undo dovrebbe restituire un new memento
        DrawSnapMemento thirdUndo = history.undo();

        assertNotNull(thirdUndo);
        assertNotSame(state1, thirdUndo);
        assertNotSame(state2, thirdUndo);
    }

    @Test
    public void testUndoDopoClearSave() {
        history.saveState(new DrawSnapMemento());
        history.undo();
        DrawSnapMemento stateNew = new DrawSnapMemento();
        history.saveState(stateNew);

        DrawSnapMemento result = history.undo();
        assertNotSame(stateNew, result);
    }

    @Test
    public void testUndoCoerenzaDiUndoConsecutive() {
        DrawSnapMemento state1 = new DrawSnapMemento();
        DrawSnapMemento state2 = new DrawSnapMemento();
        DrawSnapMemento state3 = new DrawSnapMemento();

        history.saveState(state1);
        history.saveState(state2);
        history.saveState(state3);

        assertSame(state2, history.undo());
        assertSame(state1, history.undo());
        assertNotSame(state1, history.undo()); // ora dovrebbe restituire un new memento
    }

}
