package it.unisa.software_architecture_design.drawsnapdrawingtool.forme;

import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.List;

public class FactoryPoligono implements FactoryForma{

    private List<Double> puntiY;
    private List<Double> puntiX;

    public List<Double> getPuntiY() {
        return puntiY;
    }

    public void setPuntiY(List<Double> puntiY) {
        this.puntiY = puntiY;
    }

    public List<Double> getPuntiX() {
        return puntiX;
    }

    public void setPuntiX(List<Double> puntiX) {
        this.puntiX = puntiX;
    }

    public FactoryPoligono() {
        puntiY = new ArrayList<Double>();
        puntiX = new ArrayList<Double>();
    }

    /**
     * Crea una forma con i parametri specificati.
     *
     * @param coordinataX        La coordinata X della posizione della forma.
     * @param coordinataY        La coordinata Y della posizione della forma.
     * @param larghezza          La larghezza della forma.
     * @param angoloInclinazione L'angolo di inclinazione della forma (in gradi).
     * @param colore             Il colore della forma.
     * @param coloreInterno      Il colore di riempimento interno della forma.
     * @return Un'istanza di una classe che implementa l'interfaccia `Forma`.
     */
    @Override
    public Forma creaForma(double coordinataX, double coordinataY, double altezza, double larghezza,
                           double angoloInclinazione, Color colore, Color coloreInterno) {
        System.out.println("dimensione liste" + puntiX.size() + puntiY.size());
        return new Poligono(coordinataX, coordinataY, altezza, larghezza, angoloInclinazione, colore, puntiX, puntiY, coloreInterno);
    }

    /**
     * Metodo per aggiungere un punto alla lista da usare per la creazione del poligono
     * @param coordinataX   La coordinata X della posizione del punto
     * @param coordinataY   La coordinata Y della posizione del punto
     */
    public void addPunto(double coordinataX, double coordinataY) {
        puntiX.add(coordinataX);
        puntiY.add(coordinataY);
    }

    /**
     * Metodo per ottenere la coordinataX del centro da passare in creaForma come
     * coordinate per la creazione
     * @return  La coordinata X del centro del poligono da creare
     */
    public double getCentroX(){
        if(puntiX.size() == 0){
            return 0.0;
        }
        double sumX = 0.0;
        for(double coordinataX : puntiX){
            sumX += coordinataX;
        }
        return sumX/puntiX.size();
    }

    /**
     * Metodo per ottenere la coordinataY del centro da passare in creaForma come
     * coordinate per la creazione
     * @return  La coordinata Y del centro del poligono da creare
     */
    public double getCentroY(){
        if(puntiY.size() == 0){
            return 0.0;
        }
        double sumY = 0.0;
        for(double coordinataY : puntiY){
            sumY += coordinataY;
        }
        return sumY/puntiY.size();
    }

    /**
     * Metodo per ottenere la  larghezza del poligono che si vuole creare in base ai punti inseriti
     * @return  La larghezza
     */
    public double getLarghezza(){
        if(puntiX.size() < 2){
            return 0.0;
        }

        double minX = Double.MAX_VALUE;
        double maxX = Double.MIN_VALUE;

        for(double coordinataX : puntiX){
            if(coordinataX < minX){
                minX = coordinataX;
            }
            if(coordinataX > maxX){
                maxX = coordinataX;
            }
        }
        return maxX - minX;
    }

    /**
     * Metodo per ottenere l'altezza del poligono che si vuole creare in base ai punti inseriti
     * @return  L'altezza
     */
    public double getAltezza(){
        if(puntiY.size() < 2){
            return 0.0;
        }
        double minY = Double.MAX_VALUE;
        double maxY = Double.MIN_VALUE;

        for(double coordinataY : puntiY){
            if(coordinataY < minY){
                minY = coordinataY;
            }
            if(coordinataY > maxY){
                maxY = coordinataY;
            }
        }
        return maxY - minY;
    }

    public double getSize(){
        return puntiX.size();
    }


}
