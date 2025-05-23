package it.unisa.software_architecture_design.drawsnapdrawingtool.memento;

import java.util.Stack;

public class DrawSnapHistory {
    private final Stack<DrawSnapMemento> history;

    public DrawSnapHistory() { history = new Stack<>(); }

    public void saveState(DrawSnapMemento memento) {
        history.push(memento);
    }

    public DrawSnapMemento undo(){
        if(history.isEmpty())
            return new DrawSnapMemento();
        return history.pop();
    }
}
