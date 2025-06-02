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
     * Gestisce il salvataggio del {@link DrawSnapMemento} fornito come @param nella
     * @param memento è il {@link DrawSnapMemento} da salvare
     */
    public void saveState(DrawSnapMemento memento) {
        history.push(memento);
    }

    /**
     * Esegue l'operazione di undo scartando il top dello {@link Stack} in quanto rappresentante
     * lo stato corrente e restituendo lo stato che è second-to-the-top che diventa il top
     * in quanto nuovo stato corrente.
     * @return il {@link DrawSnapMemento} rappresentante lo stato da ripristinare
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
