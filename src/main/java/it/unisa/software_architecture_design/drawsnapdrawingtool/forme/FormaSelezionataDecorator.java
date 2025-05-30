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


    /**
     * Disegna intorno alla figura un bordo evidenziato per indicare che la
     * figura è stata cliccata
     *
     * @param gc il contesto grafico del canvas sul quale si disegna
     */
    private void disegnaIndicatoreDiSelezione(GraphicsContext gc) {
        gc.save();
        // Colore e stile dell'evidenziazione
        gc.setStroke(Color.BLACK); // Colore dell'indicatore
        gc.setLineWidth(SPESSORE_BORDO);    // Spessore del bordo
        gc.setLineDashes(10.0, 5.0); // Imposta la linea tratteggiata: 10 pixel linea, 5 pixel spazio

        // Disegna un rettangolo più grande attorno alla forma
        double x = getForma().getCoordinataX();
        double y = getForma().getCoordinataY();
        double larghezza = LARGHEZZA_DEFAULT;
        double altezza = ALTEZZA_DEFAULT;

        boolean isPoligono = false;

        if (getForma() instanceof Rettangolo) {
            Rettangolo rettangolo = (Rettangolo) getForma();
            larghezza = rettangolo.getLarghezza();
            altezza = rettangolo.getAltezza();
        } else if (getForma() instanceof Ellisse) {
            Ellisse ellisse = (Ellisse) getForma();
            altezza = ellisse.getAltezza();
            larghezza = ellisse.getLarghezza();
        } else if (getForma() instanceof Linea) {
            Linea linea = (Linea) getForma();
            larghezza = linea.getLarghezza();
        } else if (getForma() instanceof Poligono) {
            Poligono poligono = (Poligono) getForma();
            altezza = poligono.getAltezza();
            larghezza = poligono.getLarghezza();
            isPoligono = true;
        }

        // Aggiungi margine
        double rectWidth = larghezza + 2 * MARGINE_SELEZIONE;
        double rectHeight = altezza + 2 * MARGINE_SELEZIONE;

        if (!isPoligono) {
            // Per le forme che non sono poligoni, applichiamo traslazione e rotazione
            gc.translate(x, y);
            gc.rotate(getForma().getAngoloInclinazione()); // Rotazione in senso orario
            // Disegna il rettangolo centrato sull'origine (che ora è il centro della forma)
            gc.strokeRect(-rectWidth / 2, -rectHeight / 2, rectWidth, rectHeight);
        } else {
            // Per i Poligoni, disegna direttamente il bounding box allineato agli assi.
            gc.strokeRect(x - rectWidth / 2, y - rectHeight / 2, rectWidth, rectHeight);
        }
        gc.restore(); // Ripristina il contesto grafico originale
    }

}
