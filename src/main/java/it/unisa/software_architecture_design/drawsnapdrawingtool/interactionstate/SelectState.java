package it.unisa.software_architecture_design.drawsnapdrawingtool.interactionstate;

import it.unisa.software_architecture_design.drawsnapdrawingtool.forme.Forma;
import it.unisa.software_architecture_design.drawsnapdrawingtool.forme.FormaSelezionataDecorator;
import javafx.scene.control.ToolBar;
import javafx.scene.input.MouseEvent;

import java.util.ArrayList;
import java.util.List;

public class SelectState implements DrawingState{

    private double offsetX;
    private double offsetY;
    private ToolBar toolBarFX; // barra in alto delle modifiche

    public SelectState(ToolBar toolBarFX) { this.toolBarFX = toolBarFX; }

    /**
     * @param event l'evento di pressione del mouse
     * @param forme lista delle forme presenti sul foglio di disegno
     */
    @Override
    public void handleMousePressed(MouseEvent event, List<Forma> forme) {
        double coordinataX = event.getX();
        double coordinataY = event.getY();

        Forma formaSelezionata = null;

        for (Forma f : forme) {
            if(f.contiene(coordinataX, coordinataY)){
                formaSelezionata = f;
                offsetX = coordinataX - f.getCoordinataX();
                offsetY = coordinataY - f.getCoordinataY();

                System.out.println("FORMA SELEZIONATA PRE: " + formaSelezionata);
                System.out.println("COORD X: " + formaSelezionata.getCoordinataX());
                System.out.println("COORD Y: " + formaSelezionata.getCoordinataY());

                break;
            }
        }

        List<Forma> formeNonSelezionate = new ArrayList<>(forme);
        if (formaSelezionata != null) {
            toolBarFX.setDisable(false); //abilita la barra in alto delle modifiche

            System.out.println("INGRESSO fs");
            System.out.println("FORMA SELEZIONATA: " + formaSelezionata);
            System.out.println("COORD X: " + formaSelezionata.getCoordinataX());
            System.out.println("COORD Y: " + formaSelezionata.getCoordinataY());

            forme.remove(formaSelezionata);
            formeNonSelezionate.remove(formaSelezionata);
            if (formaSelezionata instanceof FormaSelezionataDecorator) {
                forme.add(formaSelezionata);
            }else {
                forme.add(new FormaSelezionataDecorator(formaSelezionata));
            }
        }else{
            toolBarFX.setDisable(true); //disabilita la barra in alto delle modifiche
        }
        deselezionaHelper(forme, formeNonSelezionate);
    }

    public void deselezionaHelper(List<Forma> forme, List<Forma> formeDaDeselezionare) {
        for(Forma f : formeDaDeselezionare){
            if(f instanceof  FormaSelezionataDecorator){
                forme.remove(f);
                forme.add(((FormaSelezionataDecorator) f).getForma());
            }
        }
    }

    public void disattivaToolBar() {
        toolBarFX.setDisable(true); //disabilita la barra in alto delle modifiche
    }

    /**
     * @param event l'evento di trascinamento del mouse
     */
    @Override
    public void handleMouseDragged(MouseEvent event, List<Forma> forme) {
        System.out.println("INGRESSO FUNZIONE");
        double mouseX = event.getX();
        double mouseY = event.getY();

        for (Forma f : forme) {
            if (f instanceof FormaSelezionataDecorator) {
                Forma formaOriginale = ((FormaSelezionataDecorator) f).getForma();
                System.out.println("FORMA ORIGINALE: " + f);
                System.out.println("COORD X: " + f.getCoordinataX());
                System.out.println("COORD Y: " + f.getCoordinataY());

                double newX = mouseX - offsetX;
                double newY = mouseY - offsetY;

                formaOriginale.setCoordinataX(newX);
                formaOriginale.setCoordinataY(newY);
                break; // Presupponendo una sola forma selezionata
            }
        }
    }

    /**
     * @param event l'evento di rilascio del mouse
     */
    @Override
    public void handleMouseReleased(MouseEvent event) {

    }
}
