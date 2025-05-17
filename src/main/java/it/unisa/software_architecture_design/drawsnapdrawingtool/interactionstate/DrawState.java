package it.unisa.software_architecture_design.drawsnapdrawingtool.interactionstate;

import it.unisa.software_architecture_design.drawsnapdrawingtool.enumeration.Forme;
import it.unisa.software_architecture_design.drawsnapdrawingtool.forme.FactoryEllisse;
import it.unisa.software_architecture_design.drawsnapdrawingtool.forme.FactoryLinea;
import it.unisa.software_architecture_design.drawsnapdrawingtool.forme.FactoryRettangolo;
import it.unisa.software_architecture_design.drawsnapdrawingtool.forme.Forma;
import it.unisa.software_architecture_design.drawsnapdrawingtool.forme.Linea;
import it.unisa.software_architecture_design.drawsnapdrawingtool.forme.Rettangolo;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;

import java.util.List;

public class DrawState implements DrawingState{
    /*
     * Attributi
     */
    private Forme formaCorrente;
    private List<Forma> forme;

    /*
     * Costruttore, getter e setter
     */
    public DrawState(List<Forma> forme, Forme formaCorrente) {
        this.formaCorrente = formaCorrente;
        System.out.println(formaCorrente);
    }

    public List<Forma> getForme() {
        return forme;
    }

    public void setForme(List<Forma> forme) {
        this.forme = forme;
    }

    public Forme getFormaCorrente() {
        return formaCorrente;
    }

    public void setFormaCorrente(Forme formaCorrente) {
        this.formaCorrente = formaCorrente;
    }

    /*
     * Logica della classe
     */

    /**
     * Gestisce l'evento di pressione del mouse sul canvas in modo da creare la forma giusta
     * @param event è l'evento di pressione del mouse
     */
    @Override
    public void handleMousePressed(MouseEvent event, List<Forma> forme, double coordinataX, double coordinataY,
                                   double altezza, double larghezza, double angoloInclinazione, Color colore,
                                   Color coloreInterno) {
        Forma formaCreata = null;
        switch (formaCorrente) {
            case ELLISSE:
                formaCreata = new FactoryEllisse().creaForma(coordinataX, coordinataY, altezza, larghezza,
                                                            angoloInclinazione, colore, coloreInterno);
                System.out.println("È un'ellisse");
                break;
            case RETTANGOLO:
                formaCreata = new FactoryRettangolo().creaForma(coordinataX, coordinataY, altezza, larghezza,
                                                            angoloInclinazione, colore, coloreInterno);
                System.out.println("È un rettangolo");
                // Parametri mock per testare il disegno, da rimuovere una volta disponibile la factory
                double x = event.getX();
                double y = event.getY();
                double larghezza = 60;
                double altezza = 100;
                double inclinazione = 0;
                Color coloreBordo = Color.BLACK;
                Color coloreInterno = Color.BLACK;

                formaCreata = new Rettangolo(x, y, larghezza, inclinazione, coloreBordo, altezza, coloreInterno);

                break;

            case LINEA:
                formaCreata = new FactoryLinea().creaForma(coordinataX, coordinataY, altezza, larghezza,
                                                         angoloInclinazione, colore, coloreInterno);
                System.out.println("È una linea");
                // Parametri mock per testare il disegno, da rimuovere una volta disponibile la factory
                double lineaX = event.getX();
                double lineaY = event.getY();

                formaCreata = new Linea(lineaX, lineaY, 50, 0, Color.BLACK);

                break;
        }
        forme.add(formaCreata);
    }


    /*
    * handleMousePressed del controller deve aprire la finestra di dialogo per inserire i dati
    * della figura, dopodiché solo quando l'utente clicca il tasto di conferma deve prendere
    * i parametri inseriti dall'utente e passarli all'handleMousePressed di DrawingContext, che li passa
    * a handleMousePressed di DrawState, che crea la figura e la aggiunge alla lista figure, dopodiché il controller
    * deve ricaricare il canvas (tipo con una funzione redrawAll()) per mostrare anche la figura aggiornata
    * */

    @Override
    public void handleMouseDragged(MouseEvent event) {
        //WIP
    }

    @Override
    public void handleMouseReleased(MouseEvent event) {
        //WIP
    }
}
