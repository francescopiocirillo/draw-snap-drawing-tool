package it.unisa.software_architecture_design.drawsnapdrawingtool.forme;


public abstract class FormaDecorator extends Forma{
    /*
     * Attributi
     */
    private Forma formaDecorata;
    /*
     * Costruttore
     */
    public FormaDecorator(Forma forma) {
        this.formaDecorata = forma;
    }

    public Forma getForma() {
        return formaDecorata;
    }

}
//double coordinataX, double coordinataY, double larghezza, double angoloInclinazione, Color colore