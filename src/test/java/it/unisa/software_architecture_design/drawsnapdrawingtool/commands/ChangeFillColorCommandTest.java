package it.unisa.software_architecture_design.drawsnapdrawingtool.commands;

import it.unisa.software_architecture_design.drawsnapdrawingtool.DrawSnapModel;
import it.unisa.software_architecture_design.drawsnapdrawingtool.forme.*;
import javafx.scene.paint.Color;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;

class ChangeFillColorCommandTest {

    private DrawSnapModel drawSnapModel;
    private Ellisse ellisse;
    private Rettangolo rettangolo;
    private ChangeFillColorCommand command;

    @Test
    void testExecute_ellisse() {
        drawSnapModel = mock(DrawSnapModel.class);
        ellisse = new Ellisse(100, 100, 50, 0, Color.BLACK, 30, Color.WHITE);

        // Quando viene chiamato changeFillColor sul modello, esegui l'effettiva modifica
        doAnswer(invocation -> {
            Color colore = invocation.getArgument(0);

            ellisse.setColoreInterno(colore);
            return null;
        }).when(drawSnapModel).changeFillColor(any(Color.class));

        command = new ChangeFillColorCommand(drawSnapModel, Color.BLUE);

        assertEquals(Color.WHITE, ellisse.getColoreInterno()); //controllo colore prima della modifica

        command.execute(); //modifica

        assertEquals(Color.BLUE, ellisse.getColoreInterno()); //controllo colore dopo la modifica
        Mockito.verify(drawSnapModel).changeFillColor(Color.BLUE);
    }

    @Test
    void testExecute_rettangolo() {
        drawSnapModel = mock(DrawSnapModel.class);
        rettangolo = new Rettangolo(50, 50, 1000, 30, Color.VIOLET, 30, Color.PINK);

        doAnswer(invocation -> {
            Color colore = invocation.getArgument(0);

            rettangolo.setColoreInterno(colore);
            return null;
        }).when(drawSnapModel).changeFillColor(any(Color.class));

        command = new ChangeFillColorCommand(drawSnapModel, Color.PURPLE);

        assertEquals(Color.PINK, rettangolo.getColoreInterno()); //controllo colore prima della modifica

        command.execute(); //modifica

        assertEquals(Color.PURPLE, rettangolo.getColoreInterno()); //controllo colore dopo la modifica
        Mockito.verify(drawSnapModel).changeFillColor(Color.PURPLE);
    }

    @Test
    void testChangeFillColor_ellisse() {
        DrawSnapModel model = new DrawSnapModel();
        Ellisse ellisse = new Ellisse(50, 50, 100, 100, Color.PINK,50,Color.RED); // Crea un'ellisse con dimensioni iniziali
        model.add(ellisse); // Aggiungi la forma al modello
        model.selezionaForma(ellisse); // Seleziona la forma

        // Cambia il colore di riempimento della figura selezionata
        Color nuovoColore = Color.BLUE;
        model.changeFillColor(nuovoColore);

        // Verifica che il colore di riempimento della figura sia cambiato
        Forma formaSelezionata = model.getFormaSelezionata();
        assertEquals(nuovoColore, ((Ellisse) ((FormaSelezionataDecorator) formaSelezionata).getForma()).getColoreInterno());
    }

    @Test
    void testChangeFillColor_rettangolo() {
        DrawSnapModel model = new DrawSnapModel();
        Rettangolo rettangolo  = new Rettangolo(50, 50, 100, 100, Color.ORANGE,50,Color.YELLOW); // Crea un'ellisse con dimensioni iniziali
        model.add(rettangolo); // Aggiungi la forma al modello
        model.selezionaForma(rettangolo); // Seleziona la forma

        // Cambia il colore di riempimento della figura selezionata
        Color nuovoColore = Color.GREEN;
        model.changeFillColor(nuovoColore);

        // Verifica che il colore di riempimento della figura sia cambiato
        Forma formaSelezionata = model.getFormaSelezionata();
        assertEquals(nuovoColore, ((Rettangolo) ((FormaSelezionataDecorator) formaSelezionata).getForma()).getColoreInterno());
    }

    /*Non si eseguono test sulla linea perchè la linea non ha l'attributo colore interno quindi il tasto di modifica
    * relativo è disattivato quando la figura selezionata è una linea*/
}