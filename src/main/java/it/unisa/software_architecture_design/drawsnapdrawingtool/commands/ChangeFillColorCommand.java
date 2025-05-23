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

public class ChangeFillColorCommand implements Command {
    private final DrawSnapModel forme;
    private final Color coloreSelezionato;

    public ChangeFillColorCommand(DrawSnapModel forme, Color coloreSelezionato) {
        this.forme = forme;
        this.coloreSelezionato = coloreSelezionato;
    }

    @Override
    public void execute() {
        System.out.println("command" + coloreSelezionato);

        forme.changeFillColor(coloreSelezionato);
    }
}
