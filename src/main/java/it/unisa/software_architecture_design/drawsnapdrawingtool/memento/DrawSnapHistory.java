package it.unisa.software_architecture_design.drawsnapdrawingtool.memento;

import it.unisa.software_architecture_design.drawsnapdrawingtool.DrawSnapModel;

import java.util.Stack;

public class DrawSnapHistory {
    private final Stack<DrawSnapMemento> history;

    public DrawSnapHistory() {
        history = new Stack<>();
    }

    public void saveState(DrawSnapMemento memento) {
        history.push(memento);
    }

    public DrawSnapMemento undo() {
        if (history.isEmpty())
            return new DrawSnapMemento();

        // stato attuale
        DrawSnapMemento currentState = history.pop();

        DrawSnapMemento previousState;
        if (history.isEmpty()) {
            // se la history è vuota previousState deve essere per forza un memento nuovo
            previousState = new DrawSnapMemento();
        } else {
            // stato precedente
            previousState = history.peek();
        }


        return previousState;
    }
}
