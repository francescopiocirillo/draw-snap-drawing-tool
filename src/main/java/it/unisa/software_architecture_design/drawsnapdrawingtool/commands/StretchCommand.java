package it.unisa.software_architecture_design.drawsnapdrawingtool.commands;

import it.unisa.software_architecture_design.drawsnapdrawingtool.DrawSnapModel;

/**
 * La classe {@code ResizeCommand} rappresenta un comando che ridimensiona
 * una figura cambiandone altezza e larghezza.
 */
public class StretchCommand implements Command {
    private final DrawSnapModel forme;
    private double Altezza;
    private double Larghezza;

    public StretchCommand(DrawSnapModel forme, double Larghezza, double Altezza) {
        this.forme = forme;
        this.Altezza = Altezza;
        this.Larghezza = Larghezza;
    }

    @Override
    public void execute() {
        forme.stretch(Larghezza, Altezza);
    }
}
