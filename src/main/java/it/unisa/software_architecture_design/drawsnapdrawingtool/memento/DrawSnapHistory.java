package it.unisa.software_architecture_design.drawsnapdrawingtool.memento;

import it.unisa.software_architecture_design.drawsnapdrawingtool.DrawSnapModel;

import java.util.Stack;

public class DrawSnapHistory {
    private final Stack<DrawSnapMemento> history;
    private boolean primaUndoDopoUnaModifica;

    public DrawSnapHistory() {
        history = new Stack<>();
        primaUndoDopoUnaModifica = true;
    }

    public void saveState(DrawSnapMemento memento) {
        history.push(memento);
        primaUndoDopoUnaModifica = true;
    }

    public DrawSnapMemento undo(){
        if(history.size() == 0)
            return new DrawSnapMemento();

        // stato attuale se si è appena fatta una modifica, stato precedente se si è appena fatta una Undo
        DrawSnapMemento currentState = history.pop();

        DrawSnapMemento previousState;
        if(history.size() == 0){
            previousState = new DrawSnapMemento(); // se la history è vuota previousState deve essere per forza un memento nuovo
        }else{
            // stato precedente se si è appena fatta una modifica, stato precedente al precedente se si è appena fatta una Undo
            previousState = history.pop();
        }


        if(primaUndoDopoUnaModifica){
            primaUndoDopoUnaModifica = false;
            return previousState;
        }else{
            history.push(previousState);
            return currentState;
        }
    }
}
