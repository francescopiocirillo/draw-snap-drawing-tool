package it.unisa.software_architecture_design.drawsnapdrawingtool.commands;


import it.unisa.software_architecture_design.drawsnapdrawingtool.DrawSnapModel;
import it.unisa.software_architecture_design.drawsnapdrawingtool.forme.Forma;

/**
 * La classe {@code PasteCommand} rappresenta un comando che incolla le figure precedentemente copiate.
 */
public class PasteCommand implements Command {
    /*
     * Attributi
     */
    private final DrawSnapModel forme;
    private final double x;
    private final double y;

    /*
     * Costruttore, getter e setter
     */
    public PasteCommand(DrawSnapModel forme, double x, double y) {
        this.forme = forme;
        this.x = x;
        this.y = y;
    }

    /*
     * Logica della classe
     */

    /**
     * Esegue il comando di copia della lista di Forme
     */
    @Override
    public void execute() {
        Forma ultimaForma = forme.getLastFormaCopiata();
        if (ultimaForma != null) {

            Forma nuova = ultimaForma.clone();
            if (nuova != null) {
                nuova.setCoordinataX(x);
                nuova.setCoordinataY(y);
                forme.add(nuova);
            }
        }
    }

}
