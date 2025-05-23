package it.unisa.software_architecture_design.drawsnapdrawingtool.memento;

import it.unisa.software_architecture_design.drawsnapdrawingtool.DrawSnapModel;
import it.unisa.software_architecture_design.drawsnapdrawingtool.memento.DrawSnapHistory;
import it.unisa.software_architecture_design.drawsnapdrawingtool.memento.DrawSnapMemento;
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
    public void testUndoOnEmptyHistory() {
        DrawSnapMemento result = history.undo();
        assertNotNull(result, "Undo on empty history should return a new memento.");
    }

    @Test
    public void testUndoWithSingleState() {
        DrawSnapMemento state1 = new DrawSnapMemento();
        history.saveState(state1);

        DrawSnapMemento result = history.undo();

        // Deve restituire un nuovo memento, non quello appena rimosso
        assertNotNull(result);
        assertNotSame(state1, result, "Undo should return a new memento if only one state was saved.");
    }

    @Test
    public void testUndoWithMultipleStates() {
        DrawSnapMemento state1 = new DrawSnapMemento();
        DrawSnapMemento state2 = new DrawSnapMemento();

        history.saveState(state1);
        history.saveState(state2);

        DrawSnapMemento result = history.undo();

        assertSame(state1, result, "Undo should return the previous state (state1).");
    }

    @Test
    public void testMultipleUndoCallsExceedingSavedStates() {
        history.saveState(new DrawSnapMemento());
        history.undo(); // Removes the only state
        DrawSnapMemento result = history.undo(); // Should return a new memento
        assertNotNull(result);
    }

    @Test
    public void testUndoDoesNotRemovePreviousState() {
        DrawSnapMemento state1 = new DrawSnapMemento();
        history.saveState(state1);
        history.saveState(new DrawSnapMemento());

        history.undo(); // Should discard top
        DrawSnapMemento result = history.undo(); // Now state1 should be top
        assertNotSame(state1, result); // Because state1 was removed in the second undo
    }

    @Test
    public void testUndoAfterThreeSaves() {
        DrawSnapMemento state1 = new DrawSnapMemento();
        DrawSnapMemento state2 = new DrawSnapMemento();
        DrawSnapMemento state3 = new DrawSnapMemento();

        history.saveState(state1);
        history.saveState(state2);
        history.saveState(state3);

        DrawSnapMemento result = history.undo(); // Expect state2
        assertSame(state2, result);
    }

    @Test
    public void testUndoReturnsCorrectOrder() {
        DrawSnapMemento state1 = new DrawSnapMemento();
        DrawSnapMemento state2 = new DrawSnapMemento();
        history.saveState(state1);
        history.saveState(state2);

        DrawSnapMemento firstUndo = history.undo();  // Should be state1
        DrawSnapMemento secondUndo = history.undo(); // Should be new memento

        assertSame(state1, firstUndo);
        assertNotSame(state1, secondUndo);
    }

    @Test
    public void testUndoAfterInterleavedSaveUndo() {
        DrawSnapMemento state1 = new DrawSnapMemento();
        DrawSnapMemento state2 = new DrawSnapMemento();
        history.saveState(state1);
        history.undo(); // undo -> new memento
        history.saveState(state2);

        DrawSnapMemento result = history.undo(); // Should return new memento
        assertNotSame(state1, result);
        assertNotSame(state2, result);
    }

    @Test
    public void testUndoDoesNotThrowException() {
        assertDoesNotThrow(() -> {
            history.undo();
            history.undo();
        });
    }

    @Test
    public void testSaveStateIncreasesStackSize() {
        history.saveState(new DrawSnapMemento());
        DrawSnapMemento state2 = new DrawSnapMemento();
        history.saveState(state2);
        DrawSnapMemento result = history.undo(); // should return state1

        assertNotNull(result);
        assertNotSame(state2, result);
    }

    @Test
    public void testUndoDoesNotMutateHistoryIncorrectly() {
        DrawSnapMemento state1 = new DrawSnapMemento();
        DrawSnapMemento state2 = new DrawSnapMemento();
        history.saveState(state1);
        history.saveState(state2);

        DrawSnapMemento firstUndo = history.undo();
        DrawSnapMemento secondUndo = history.undo();

        // History should now be empty; third undo should return a new memento
        DrawSnapMemento thirdUndo = history.undo();

        assertNotNull(thirdUndo);
        assertNotSame(state1, thirdUndo);
        assertNotSame(state2, thirdUndo);
    }

    @Test
    public void testUndoAfterClearAndSave() {
        history.saveState(new DrawSnapMemento());
        history.undo(); // clear all
        DrawSnapMemento stateNew = new DrawSnapMemento();
        history.saveState(stateNew);

        DrawSnapMemento result = history.undo();
        assertNotSame(stateNew, result);
    }

    @Test
    public void testConsecutiveUndoConsistency() {
        DrawSnapMemento state1 = new DrawSnapMemento();
        DrawSnapMemento state2 = new DrawSnapMemento();
        DrawSnapMemento state3 = new DrawSnapMemento();

        history.saveState(state1);
        history.saveState(state2);
        history.saveState(state3);

        assertSame(state2, history.undo());
        assertSame(state1, history.undo());
        assertNotSame(state1, history.undo()); // Now it should return new memento
    }

}
