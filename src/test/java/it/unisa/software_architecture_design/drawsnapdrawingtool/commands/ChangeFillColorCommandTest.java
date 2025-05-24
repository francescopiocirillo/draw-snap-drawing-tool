package it.unisa.software_architecture_design.drawsnapdrawingtool.commands;

import it.unisa.software_architecture_design.drawsnapdrawingtool.DrawSnapModel;
import it.unisa.software_architecture_design.drawsnapdrawingtool.forme.Ellisse;
import it.unisa.software_architecture_design.drawsnapdrawingtool.forme.FormaSelezionataDecorator;
import it.unisa.software_architecture_design.drawsnapdrawingtool.forme.Linea;
import it.unisa.software_architecture_design.drawsnapdrawingtool.forme.Rettangolo;
import javafx.scene.paint.Color;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;

class ChangeFillColorCommandTest {

    private DrawSnapModel drawSnapModel;
    private ChangeFillColorCommand changeFillColorCommand;
    private Ellisse ellisse;
    private Rettangolo rettangolo;
    private Linea linea;
    private ChangeFillColorCommand command;

    @Test
    void testExecute_ellisse() {
        drawSnapModel = mock(DrawSnapModel.class);
        ellisse = new Ellisse(100, 100, 50, 0, Color.BLACK, 30, Color.WHITE);
        FormaSelezionataDecorator formaSelezionata = new FormaSelezionataDecorator(ellisse);

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
        FormaSelezionataDecorator formaSelezionata = new FormaSelezionataDecorator(rettangolo);

        doAnswer(invocation -> {
            Color colore = invocation.getArgument(0);

            rettangolo.setColoreInterno(colore);
            return null;
        }).when(drawSnapModel).changeFillColor(any(Color.class));

        command = new ChangeFillColorCommand(drawSnapModel, Color.BLUE);

        assertEquals(Color.WHITE, ellisse.getColoreInterno()); //controllo colore prima della modifica

        command.execute(); //modifica

        assertEquals(Color.BLUE, ellisse.getColoreInterno()); //controllo colore dopo la modifica
        Mockito.verify(drawSnapModel).changeFillColor(Color.BLUE);
    }

    /*Non si eseguono test sulla linea perchè la linea non ha l'attributo colore interno quindi il tasto di modifica
    * relativo è disattivato quando la figura selezionata è una linea*/
}