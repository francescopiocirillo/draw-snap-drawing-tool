package it.unisa.software_architecture_design.drawsnapdrawingtool.commands;

import it.unisa.software_architecture_design.drawsnapdrawingtool.DrawSnapModel;
import it.unisa.software_architecture_design.drawsnapdrawingtool.enumeration.Forme;
import it.unisa.software_architecture_design.drawsnapdrawingtool.memento.DrawSnapHistory;
import javafx.geometry.Pos;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

/**
 * La classe {@code ChangeFillColorCommand} rappresenta un comando che permette di cambiare
 * il colore del riempimento delle figure selezionate.
 */
public class ChangeFillColorCommand implements Command {
    /*
     * Attributi
     */
    private final DrawSnapModel forme;
    private final Color coloreSelezionato;

    /*
     * Costruttore, getter e setter
     */
    public ChangeFillColorCommand(DrawSnapModel forme, Color coloreSelezionato) {
        this.forme = forme;
        this.coloreSelezionato = coloreSelezionato;
    }

    /*
     * Logica della classe
     */
    /**
     * Esegue il comando di cambiaColoreInterno
     */
    @Override
    public void execute() {
        System.out.println("command" + coloreSelezionato);

        forme.changeFillColor(coloreSelezionato);
    }
}
