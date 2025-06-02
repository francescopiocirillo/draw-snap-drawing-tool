package it.unisa.software_architecture_design.drawsnapdrawingtool.memento;

import it.unisa.software_architecture_design.drawsnapdrawingtool.DrawSnapModel;

import java.util.Stack;

public class DrawSnapHistory {
    /*
     * Attributi
     */
    private final Stack<DrawSnapMemento> history;

    /*
     * Costruttore
     */
    public DrawSnapHistory() {
        history = new Stack<>();
    }

    /**
     * Salva il memento fornito come parametro nella
     * @param memento -> il memento da salvare
     */
    public void saveState(DrawSnapMemento memento) {
        history.push(memento);
    }

    /**
     * Esegue l'operazione di undo scartando il top dello stack in quanto rappresentante lo stato corrente
     * e restituendo lo stato che è second-to-the-top che diventa il top in quanto nuovo stato corrente.
     * @return il memento rappresentante lo stato da ripristinare
     */
    public DrawSnapMemento undo() {
        if (history.isEmpty())
            return new DrawSnapMemento();

        // stato attuale
        DrawSnapMemento currentState = history.pop();

        DrawSnapMemento previousState;
        if (history.isEmpty()) {
            // se la history è vuota previousState deve essere un memento nuovo
            previousState = new DrawSnapMemento();
        } else {
            // stato precedente
            previousState = history.peek();
        }

        return previousState;
    }

    public boolean isEmpty(){
        return history.isEmpty();
    }

    public void clear(){
        history.clear();
    }
}
