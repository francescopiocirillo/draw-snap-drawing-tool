package it.unisa.software_architecture_design.drawsnapdrawingtool.forme;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

/**
 * La classe {@link Ellisse} rappresenta la {@link Forma} Ellisse e presenta
 * tutte le caratteristiche ereditate da {@link Forma2D}.
 */
public class Ellisse extends Forma2D {
    /*
     * Costruttore
     */
    public Ellisse(double coordinataX, double coordinataY, double larghezza, double angoloInclinazione, Color colore, double altezza, Color coloreInterno) {
        super(coordinataX, coordinataY, larghezza, angoloInclinazione, colore, altezza, coloreInterno);
    }

    /*
      Logica della classe
     */
    
    /**
     * Gestisce il disegno  di una {@link Ellisse} sul {@link GraphicsContext} specificato.
     * @param gc il {@link GraphicsContext} su cui disegnare l'{@link Ellisse}.
     *           Deve essere già inizializzato e associato a un {@link javafx.scene.canvas.Canvas} valido.
     */
    @Override
    public void disegna(GraphicsContext gc) {
        // Salva lo stato iniziale del foglio di disegno
        gc.save();

        // Traslazione al centro dell'ellisse
        gc.translate(getCoordinataX(), getCoordinataY());

        // rotate inclina l'ellisse secondo l'angoloInclinazione indicato
        gc.rotate(getAngoloInclinazione());

        // fill disegna l'area interna dell'ellisse
        gc.setFill(getColoreInterno());
        gc.fillOval(-getLarghezza()/ 2, -getAltezza() / 2, getLarghezza(), getAltezza());

        // stroke disegna il contorno
        gc.setStroke(getColore());
        gc.setLineWidth(2);
        gc.strokeOval(-getLarghezza() / 2, -getAltezza() / 2, getLarghezza(), getAltezza());

        // Ripristina lo stato iniziale del foglio di disegno
        gc.restore();
    }

    /**
     * Verifica se l'{@link Ellisse} contiene un punto specifico nello spazio.
     * L'equazione per verificare se un punto si trova all'interno dell'ellisse è
     * \frac{(puntoDaValutareX-coordinataX)^2}{mezzaLarghezza^2}+\frac{(puntoDaValutareY-coordinataY)^2}{mezzaAltezza^2}<=1
     *
     * @param puntoDaValutareX la coordinata X del punto da valutare
     * @param puntoDaValutareY la coordinata Y del punto da valutare
     * @return {@code true} se il punto specificato (puntoDaValutareX, puntoDaValutareY) si
     * trova all'interno dell'{@link Ellisse}, altrimenti {@code false}.
     */
    @Override
    public boolean contiene(double puntoDaValutareX, double puntoDaValutareY) {
        double mezzaLarghezza = getLarghezza() / 2;
        double mezzaAltezza = getAltezza() / 2;

        // Traslazione rispetto al centro dell'ellisse
        double dx = puntoDaValutareX - getCoordinataX();
        double dy = puntoDaValutareY - getCoordinataY();

        // Rotazione inversa per annullare l'inclinazione
        double radians = Math.toRadians(-getAngoloInclinazione());
        double rotatedX = dx * Math.cos(radians) - dy * Math.sin(radians);
        double rotatedY = dx * Math.sin(radians) + dy * Math.cos(radians);

        // Verifica se il punto trasformato soddisfa l'equazione dell'ellisse
        return (rotatedX * rotatedX) / (mezzaLarghezza * mezzaLarghezza) +
                (rotatedY * rotatedY) / (mezzaAltezza * mezzaAltezza) <= 1;
    }

    /**
     * Gestisce la ridistribuzione dei valori della {@link Forma} per specchiarla
     * lungo l'asse verticale che passa per il centro della {@link Forma} stessa
     */
    @Override
    public void specchiaInVerticale(){
        // Inverti l'angolo rispetto all'asse verticale
        double nuovoAngolo = 180 - getAngoloInclinazione();

        // Imposta il nuovo angolo
        setAngoloInclinazione(nuovoAngolo);
    }

    /**
     * Gestisce la ridistribuzione dei valori della {@link Forma} per specchiarla
     * lungo l'asse orizzontale che passa per il centro della {@link Forma} stessa
     */
    @Override
    public void specchiaInOrizzontale(){
        // Inverti l'angolo rispetto all'asse orizzontale
        double nuovoAngolo = 360 - getAngoloInclinazione();

        // Imposta il nuovo angolo
        setAngoloInclinazione(nuovoAngolo);
    }

    /**
     * Verifica se l' {@link Ellisse} corrente è uguale ad un altra {@link Forma}
     * @param forma è la {@link Forma} con cui fare il confronto
     * @return {@code true} se gli attributi sono uguali, altrimenti {@code false}
     */
    @Override
    public boolean confrontaAttributi(Forma forma){
        if(!(forma instanceof Ellisse ellisse))return false;
        return super.confrontaAttributi(ellisse) &&
                this.getAltezza() == ellisse.getAltezza() &&
                this.getColoreInterno() == ellisse.getColoreInterno();
    }
}
