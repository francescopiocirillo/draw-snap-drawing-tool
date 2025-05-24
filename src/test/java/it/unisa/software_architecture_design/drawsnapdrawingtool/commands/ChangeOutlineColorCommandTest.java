package it.unisa.software_architecture_design.drawsnapdrawingtool.commands;

import it.unisa.software_architecture_design.drawsnapdrawingtool.DrawSnapModel;
import it.unisa.software_architecture_design.drawsnapdrawingtool.forme.*;
import javafx.scene.paint.Color;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;

class ChangeOutlineColorCommandTest {

    private DrawSnapModel drawSnapModel;
    private Ellisse ellisse;
    private Rettangolo rettangolo;
    private Linea linea;
    private ChangeOutlineColorCommand command;

    @Test
    void testExecute_ellisse() {
        DrawSnapModel drawSnapModel = mock(DrawSnapModel.class);
        ellisse = new Ellisse(100, 100, 50, 0, Color.BLACK, 30, Color.WHITE);

        doAnswer(invocation -> {
            Color colore = invocation.getArgument(0);

            ellisse.setColore(colore);
            return null;
        }).when(drawSnapModel).changeOutlineColor(any(Color.class));

        command = new ChangeOutlineColorCommand(drawSnapModel, Color.BLUE);

        assertEquals(Color.BLACK, ellisse.getColore()); //controllo colore prima della modifica

        command.execute(); //modifica

        assertEquals(Color.BLUE, ellisse.getColore()); //controllo colore dopo la modifica
        Mockito.verify(drawSnapModel).changeOutlineColor(Color.BLUE);
    }

    @Test
    void testExecute_rettangolo() {
        drawSnapModel = mock(DrawSnapModel.class);
        rettangolo = new Rettangolo(50, 50, 1000, 30, Color.VIOLET, 30, Color.PINK);

        doAnswer(invocation -> {
            Color colore = invocation.getArgument(0);

            rettangolo.setColore(colore);
            return null;
        }).when(drawSnapModel).changeOutlineColor(any(Color.class));

        command = new ChangeOutlineColorCommand(drawSnapModel, Color.PURPLE);

        assertEquals(Color.VIOLET, rettangolo.getColore()); //controllo colore prima della modifica

        command.execute(); //modifica

        assertEquals(Color.PURPLE, rettangolo.getColore()); //controllo colore dopo la modifica
        Mockito.verify(drawSnapModel).changeOutlineColor(Color.PURPLE);
    }

    @Test
    void testExecute_linea() {
        DrawSnapModel drawSnapModel = mock(DrawSnapModel.class);
        linea = new Linea(200, 150, 77, 60, Color.BLACK);

        doAnswer(invocation -> {
            Color colore = invocation.getArgument(0);

            linea.setColore(colore);
            return null;
        }).when(drawSnapModel).changeOutlineColor(any(Color.class));

        command = new ChangeOutlineColorCommand(drawSnapModel, Color.BLUE);

        assertEquals(Color.BLACK, linea.getColore()); //controllo colore prima della modifica

        command.execute(); //modifica

        assertEquals(Color.BLUE, linea.getColore()); //controllo colore dopo la modifica
        Mockito.verify(drawSnapModel).changeOutlineColor(Color.BLUE);
    }


    @Test
    void testChangeFillColor_ellisse() {
        DrawSnapModel model = new DrawSnapModel();
        Ellisse ellisse = new Ellisse(50, 50, 100, 100, Color.PINK,50,Color.RED);
        model.add(ellisse);
        model.selezionaForma(ellisse);

        // Cambia il colore di contorno della figura selezionata
        Color nuovoColore = Color.BLUE;
        model.changeOutlineColor(nuovoColore);

        // Verifica che il colore di contorno della figura sia cambiato
        Forma formaSelezionata = model.getFormaSelezionata();
        assertEquals(nuovoColore, ((Ellisse) ((FormaSelezionataDecorator) formaSelezionata).getForma()).getColore());
    }

    @Test
    void testChangeFillColor_rettangolo() {
        DrawSnapModel model = new DrawSnapModel();
        Rettangolo rettangolo  = new Rettangolo(50, 50, 100, 100, Color.ORANGE,50,Color.YELLOW);
        model.add(rettangolo);
        model.selezionaForma(rettangolo);

        // Cambia il colore di contorno della figura selezionata
        Color nuovoColore = Color.GREEN;
        model.changeOutlineColor(nuovoColore);

        // Verifica che il colore di contorno della figura sia cambiato
        Forma formaSelezionata = model.getFormaSelezionata();
        assertEquals(nuovoColore, ((Rettangolo) ((FormaSelezionataDecorator) formaSelezionata).getForma()).getColore());
    }

    @Test
    void testChangeFillColor_linea() {
        DrawSnapModel model = new DrawSnapModel();
        Linea linea  = new Linea(50, 50, 100, 100, Color.ORANGE);
        model.add(linea);
        model.selezionaForma(linea);

        // Cambia il colore di riempimento della figura selezionata
        Color nuovoColore = Color.GREEN;
        model.changeOutlineColor(nuovoColore);

        // Verifica che il colore di riempimento della figura sia cambiato
        Forma formaSelezionata = model.getFormaSelezionata();
        assertEquals(nuovoColore, ((Linea ) ((FormaSelezionataDecorator) formaSelezionata).getForma()).getColore());
    }
}