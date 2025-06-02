package it.unisa.software_architecture_design.drawsnapdrawingtool.commands;

import it.unisa.software_architecture_design.drawsnapdrawingtool.DrawSnapModel;
import javafx.scene.paint.Color;
/**
 * La classe {@code ChangeOutlineColorCommand} rappresenta un comando che permette di cambiare
 * il colore del bordo delle figure selezionate.
 */
public class ChangeOutlineColorCommand implements Command {
    /*
     * Attributi
     */
    private final DrawSnapModel forme;
    private final Color coloreSelezionato;

    /*
     * Costruttore, getter e setter
     */
    public ChangeOutlineColorCommand(DrawSnapModel forme, Color coloreSelezionato) {
        this.forme = forme;
        this.coloreSelezionato = coloreSelezionato;
    }

    /*
     * Logica della classe
     */
    /**
     * Esegue il comando di cambiaColoreContorno della lista di Forme
     */
    @Override
    public void execute() {
        System.out.println("command" + coloreSelezionato);

        forme.changeOutlineColor(coloreSelezionato);
    }
}
