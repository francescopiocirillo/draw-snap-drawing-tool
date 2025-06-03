package it.unisa.software_architecture_design.drawsnapdrawingtool.commands;

import it.unisa.software_architecture_design.drawsnapdrawingtool.DrawSnapModel;
import javafx.scene.paint.Color;

/**
 * La classe {@code RotationCommand} rappresenta un comando che permette di
 * comporre modificare l'angolo di inclinazione della figura selezionata
 */
public class RotationCommand implements Command {
    /*
     * Attributi
     */
    private final DrawSnapModel forme;
    private final double angleSelezionato;

    /*
     * Costruttore, getter e setter
     */
    public RotationCommand(DrawSnapModel forme, double angleSelezionato) {
        this.forme = forme;
        this.angleSelezionato =  angleSelezionato;
    }

    /*
     * Logica della classe
     */
    /**
     * Esegue il comando di rotazione della figura selezionata
     */
    @Override
    public void execute() {
        forme.rotation(angleSelezionato);
    }

}
