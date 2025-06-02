package it.unisa.software_architecture_design.drawsnapdrawingtool.commands;

import it.unisa.software_architecture_design.drawsnapdrawingtool.DrawSnapModel;
import javafx.scene.paint.Color;

/**
 * La classe {@code ResizeCommand} rappresenta un comando che ridimensiona
 * una figura cambiandone altezza e larghezza.
 */
public class ResizeCommand implements Command {
    private final DrawSnapModel forme;
    private double Altezza;
    private double Larghezza;

    public ResizeCommand(DrawSnapModel forme, double Larghezza, double Altezza) {
        this.forme = forme;
        this.Altezza = Altezza;
        this.Larghezza = Larghezza;
    }

    @Override
    public void execute() {
        forme.resize(Larghezza, Altezza);
    }
}
