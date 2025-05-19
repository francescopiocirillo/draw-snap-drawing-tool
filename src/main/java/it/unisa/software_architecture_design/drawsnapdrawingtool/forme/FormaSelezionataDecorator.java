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

    /*
     * Costruttore
     */
    public FormaSelezionataDecorator(Forma forma) {
        super(forma); //riceve la forma da decorare
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
        disegnaIndicatoreDiSelezione(gc);
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
        double larghezza = getForma().getLarghezza();
        double altezza = ALTEZZA_DEFAULT;
        if (getForma() instanceof Rettangolo) {
            Rettangolo rettangolo = (Rettangolo) getForma();
            altezza = rettangolo.getAltezza();
        } else if (getForma() instanceof Ellisse) {
            Ellisse ellisse = (Ellisse) getForma();
            altezza = ellisse.getAltezza();
        }

        // Aggiungi margine
        double rectWidth = larghezza + 2 * MARGINE_SELEZIONE;
        double rectHeight = altezza + 2 * MARGINE_SELEZIONE;

        // Traslazione al centro della forma e rotazione di 45 gradi
        gc.translate(x, y);
        gc.rotate(getForma().getAngoloInclinazione()); // Rotazione in senso orario di 45 gradi

        // Disegna il rettangolo centrato sull'origine (che ora è il centro della forma)
        gc.strokeRect(-rectWidth / 2, -rectHeight / 2, rectWidth, rectHeight);

        gc.restore(); // Ripristina il contesto grafico originales
    }
}
