package it.unisa.software_architecture_design.drawsnapdrawingtool.forme;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class FormaSelezionataDecorator extends FormaDecorator{
    /*
     * Attributi
     */
    private static final double MARGINE_SELEZIONE = 5.0;
    private static final double SPESSORE_BORDO = 2.0;
    private static final double ALTEZZA_DEFAULT = 10.0;
    private static final double LARGHEZZA_DEFAULT = 10.0;

    /*
     * Costruttore
     */
    public FormaSelezionataDecorator(Forma forma) {
        super(forma); //riceve la forma da decorare
        decorate();
    }

    /*
     * Logica della classe
     */

    /**
     * @param gc il contesto grafico del canvas sul quale si disegna
     */
    @Override
    public void disegna(GraphicsContext gc) {
        getForma().disegna(gc);
        if(!(getForma() instanceof FormaComposta)){
            disegnaIndicatoreDiSelezione(gc);
        }
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

    public void decorate(){
        if(getForma() instanceof FormaComposta) {
            ((FormaComposta) getForma()).decorate();
        }
    }

    public Forma undecorate(){
        if(getForma() instanceof FormaComposta){
            ((FormaComposta) getForma()).undecorate();
        }
        return getForma();
    }

    /**
     * Ridistribuisce i valori della figura per specchiarla lungo l'asse verticale che passa per il
     * cetro della figura stessa
     */
    @Override
    public void specchia() {
        getForma().specchia();
    }

    @Override
    public Forma clone(){
        return getForma().clone();
    }

    @Override
    public void proportionalResize(double proporzione){
        getForma().proportionalResize(proporzione);
    }

    public void setAngoloInclinazione(double angoloDiInclinazione){
        getForma().setAngoloInclinazione(angoloDiInclinazione);
    }

    public void setOffsetX(double offsetX){
        getForma().setOffsetX(offsetX);
    }

    public void setOffsetY(double offsetY){
        getForma().setOffsetY(offsetY);
    }

    public void setCoordinataXForDrag(double coordinataXMouseDragged){
        getForma().setCoordinataXForDrag(coordinataXMouseDragged);
    }

    public void setCoordinataYForDrag(double coordinataYMouseDragged){
        getForma().setCoordinataYForDrag(coordinataYMouseDragged);
    }

    /**
     * Disegna intorno alla figura un bordo evidenziato per indicare che la
     * figura è stata cliccata
     *
     * @param gc il contesto grafico del canvas sul quale si disegna
     */
    private void disegnaIndicatoreDiSelezione(GraphicsContext gc) {
        gc.save();

        // Colore e stile dell'evidenziazione
        gc.setStroke(Color.BLACK);
        gc.setLineWidth(SPESSORE_BORDO);
        gc.setLineDashes(10.0, 5.0);

        double x = getForma().getCoordinataX();
        double y = getForma().getCoordinataY();
        double angoloInclinazione = getForma().getAngoloInclinazione();

        double larghezza = LARGHEZZA_DEFAULT;
        double altezza = ALTEZZA_DEFAULT;


        double offsetX_bbox = 0;
        double offsetY_bbox = 0;

        if (getForma() instanceof Rettangolo) {
            Rettangolo rettangolo = (Rettangolo) getForma();
            larghezza = rettangolo.getLarghezza();
            altezza = rettangolo.getAltezza();
        } else if (getForma() instanceof Ellisse) {
            Ellisse ellisse = (Ellisse) getForma();
            larghezza = ellisse.getLarghezza();
            altezza = ellisse.getAltezza();
        } else if (getForma() instanceof Linea) {
            Linea linea = (Linea) getForma();
            larghezza = linea.getLarghezza();
        } else if (getForma() instanceof Poligono) {
            Poligono poligono = (Poligono) getForma();
            larghezza = poligono.getLarghezza();
            altezza = poligono.getAltezza();

            offsetX_bbox = poligono.getIntrinsicCenterX();
            offsetY_bbox = poligono.getIntrinsicCenterY();
        }else if (getForma() instanceof Testo){
            Testo testo = (Testo) getForma();
            larghezza = testo.getRenderedWidth();
            altezza = testo.getRenderedHeight();
        }

        double rectWidth = larghezza + 2 * MARGINE_SELEZIONE;
        double rectHeight = altezza + 2 * MARGINE_SELEZIONE;


        gc.translate(x, y);

        gc.rotate(angoloInclinazione);

        gc.strokeRect(offsetX_bbox - rectWidth / 2, offsetY_bbox - rectHeight / 2, rectWidth, rectHeight);

        gc.restore();
    }


}
