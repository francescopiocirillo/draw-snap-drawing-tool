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
     * Ridistribuisce i valori della figura per specchiarla lungo l'asse verticale che passa per il
     * cetro della figura stessa
     */
    @Override
    public void specchia() {
        getForma().specchia();
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

        double x = getForma().getCoordinataX(); // Coordinata X globale della forma (il suo punto di riferimento)
        double y = getForma().getCoordinataY(); // Coordinata Y globale della forma (il suo punto di riferimento)
        double angoloInclinazione = getForma().getAngoloInclinazione();

        double larghezza = LARGHEZZA_DEFAULT;
        double altezza = ALTEZZA_DEFAULT;

        // Variabili per l'offset del centro della bounding box rispetto al punto di riferimento (x,y)
        double offsetX_bbox = 0;
        double offsetY_bbox = 0;

        if (getForma() instanceof Rettangolo) {
            Rettangolo rettangolo = (Rettangolo) getForma();
            larghezza = rettangolo.getLarghezza();
            altezza = rettangolo.getAltezza();
            // Assumendo che per Rettangolo (x,y) siano già il centro della bounding box, l'offset è 0.
            // Se (x,y) fosse top-left, allora offsetX_bbox = larghezza / 2, offsetY_bbox = altezza / 2.
        } else if (getForma() instanceof Ellisse) {
            Ellisse ellisse = (Ellisse) getForma();
            larghezza = ellisse.getLarghezza();
            altezza = ellisse.getAltezza();
            // Assumendo che per Ellisse (x,y) siano già il centro, l'offset è 0.
        } else if (getForma() instanceof Linea) {
            Linea linea = (Linea) getForma();
            larghezza = linea.getLarghezza();
            // Per la Linea, l'altezza della bounding box potrebbe essere la sua larghezza o un valore fisso minimo.
            // L'offset dipende da come la Linea calcola il suo getCoordinataX/Y (se è il punto iniziale o il centro).
            // Se la Linea è gestita come un segmento con (x,y) = centro, allora offsetX/Y_bbox = 0.
            // Se la Linea è (x1,y1) e (x2,y2) e x,y è x1,y1, allora l'offset sarà (x2-x1)/2, (y2-y1)/2.
            // Per ora manteniamo 0 e verifichiamo il Poligono.
        } else if (getForma() instanceof Poligono) {
            Poligono poligono = (Poligono) getForma();
            larghezza = poligono.getLarghezza(); // Ottiene la larghezza intrinseca del poligono
            altezza = poligono.getAltezza();     // Ottiene l'altezza intrinseca del poligono

            // Questo è il punto chiave per il Poligono:
            // L'offset del centro della bounding box intrinseca rispetto all'origine interna (0,0) del poligono.
            // Poiché il contesto grafico verrà traslato a (x,y) (il punto di riferimento del poligono),
            // dobbiamo aggiungere questo offset per raggiungere il vero centro della bounding box.
            offsetX_bbox = poligono.getIntrinsicCenterX();
            offsetY_bbox = poligono.getIntrinsicCenterY();
        }

        // Calcola le dimensioni del rettangolo di selezione, aggiungendo il margine
        double rectWidth = larghezza + 2 * MARGINE_SELEZIONE;
        double rectHeight = altezza + 2 * MARGINE_SELEZIONE;

        // Applica le trasformazioni al contesto grafico:
        // 1. Trasla l'origine del contesto al punto di riferimento globale della forma (x,y).
        gc.translate(x, y);
        // 2. Ruota il contesto grafico dell'angolo di inclinazione della forma (attorno a x,y).
        gc.rotate(angoloInclinazione);

        // 3. Disegna il rettangolo di selezione.
        // L'origine del rettangolo è calcolata per essere centrata rispetto a:
        // (0,0) del contesto attuale + (offsetX_bbox, offsetY_bbox)
        gc.strokeRect(offsetX_bbox - rectWidth / 2, offsetY_bbox - rectHeight / 2, rectWidth, rectHeight);

        gc.restore();
    }


}
