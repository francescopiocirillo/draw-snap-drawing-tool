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

    public double getCoordinataY() {
        return formaDecorata.getCoordinataY();
    }

    public double getCoordinataX() {
        return formaDecorata.getCoordinataX();
    }

    public void setCoordinataY(double coordinataY) {
        formaDecorata.setCoordinataY(coordinataY);
    }

    public void setCoordinataX(double coordinataX) {
        formaDecorata.setCoordinataX(coordinataX);
    }


    @Override
    public boolean confrontaAttributi(Forma forma) {
        FormaDecorator decorator = (FormaDecorator) forma;
        return this.formaDecorata.confrontaAttributi(decorator.getForma());
    }

    @Override
    public Forma clone(){
        return (Forma) formaDecorata.clone();
    }
}