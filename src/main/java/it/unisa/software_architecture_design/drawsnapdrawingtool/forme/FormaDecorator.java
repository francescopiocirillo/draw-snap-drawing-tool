package it.unisa.software_architecture_design.drawsnapdrawingtool.forme;


import javafx.scene.paint.Color;

public abstract class FormaDecorator extends Forma{
    /*
     * Attributi
     */
    private Forma formaDecorata;

    /*
     * Costruttore, getter, setter e Override
     */
    public FormaDecorator(Forma forma) {
        this.formaDecorata = forma;
    }

    public Forma getForma() {
        return formaDecorata;
    }

    public void setForma(Forma forma) { this.formaDecorata = forma; }

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

    public void setColore(Color color) { formaDecorata.setColore(color); }

    @Override
    public boolean confrontaAttributi(Forma forma) {
        FormaDecorator decorator = (FormaDecorator) forma;
        return this.formaDecorata.confrontaAttributi(decorator.getForma());
    }

    @Override
    public Forma clone(){
        return (Forma) formaDecorata.clone();
    }

    /**
     * Ridistribuisce i valori della figura per specchiarla lungo l'asse verticale che passa per il
     * cetro della figura stessa
     */
    @Override
    public void specchiaInVerticale() {
        getForma().specchiaInVerticale();
    }

    /**
     * Ridistribuisce i valori della figura per specchiarla lungo l'asse orizzontale che passa per il
     * cetro della figura stessa
     */
    @Override
    public void specchiaInOrizzontale() {
        getForma().specchiaInOrizzontale();
    }

    @Override
    public void proportionalResize(double proporzione){
        getForma().proportionalResize(proporzione);
    }

    @Override
    public void setAngoloInclinazione(double angoloDiInclinazione){
        getForma().setAngoloInclinazione(angoloDiInclinazione);
    }

    @Override
    public void setOffsetX(double coordinataXPressed){
        getForma().setOffsetX(coordinataXPressed);
    }

    @Override
    public void setOffsetY(double coordinataYPressed){
        getForma().setOffsetY(coordinataYPressed);
    }

    @Override
    public void setCoordinataXForDrag(double coordinataXMouseDragged){
        getForma().setCoordinataXForDrag(coordinataXMouseDragged);
    }

    @Override
    public void setCoordinataYForDrag(double coordinataYMouseDragged){
        getForma().setCoordinataYForDrag(coordinataYMouseDragged);
    }

    /**
     * Determina se la forma contiene un punto specifico nello spazio.
     *
     * @param puntoDaValutareX La coordinata X del punto da verificare.
     * @param puntoDaValutareY La coordinata Y del punto da verificare.
     * @return {@code true} se il punto specificato (puntoDaValutareX, puntoDaValutareY) si trova all'interno della forma,
     * altrimenti {@code false}.
     */
    @Override
    public boolean contiene(double puntoDaValutareX, double puntoDaValutareY) {
        return getForma().contiene(puntoDaValutareX, puntoDaValutareY);
    }

    /**
     * Applica la decorazione alle componenti della forma composta
     */
    public void decorate(){
        if(getForma() instanceof FormaComposta) {
            ((FormaComposta) getForma()).decorate();
        }
    }

    /**
     * Rimuove la decorazione, eventualmente anche alle componenti della forma composta, e restituisce la forma
     * non decorata.
     */
    public Forma undecorate(){
        if(getForma() instanceof FormaComposta){
            ((FormaComposta) getForma()).undecorate();
        }
        return getForma();
    }
}