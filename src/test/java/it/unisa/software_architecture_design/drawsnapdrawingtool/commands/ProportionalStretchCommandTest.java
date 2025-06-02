package it.unisa.software_architecture_design.drawsnapdrawingtool.commands;

import it.unisa.software_architecture_design.drawsnapdrawingtool.DrawSnapModel;
import it.unisa.software_architecture_design.drawsnapdrawingtool.forme.Ellisse;
import it.unisa.software_architecture_design.drawsnapdrawingtool.forme.Linea;
import it.unisa.software_architecture_design.drawsnapdrawingtool.forme.Rettangolo;
import javafx.scene.paint.Color;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyDouble;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;

class ProportionalStretchCommandTest {
    private DrawSnapModel drawSnapModel;
    private Ellisse ellisse;
    private Rettangolo rettangolo;
    private Linea linea;
    private ProportionalResizeCommand command;

    @Test
    void testExecute_ellisse() {
        drawSnapModel = mock(DrawSnapModel.class);
        ellisse = new Ellisse(100, 100, 50, 0, Color.BLACK, 30, Color.WHITE);


        doAnswer(invocation -> {
            double proporzione = invocation.getArgument(0);
            ellisse.proportionalResize(proporzione);
            return null;
        }).when(drawSnapModel).proportionalResize(anyDouble());

        double initialLarghezza = ellisse.getLarghezza();
        double initialAltezza = ellisse.getAltezza();
        double proporzioneUpdate = 150.0; // Scala a 150%

        command = new ProportionalResizeCommand(drawSnapModel, proporzioneUpdate);

        assertEquals(50, initialLarghezza, 0.001); //controllo larghezza prima della modifica
        assertEquals(30, initialAltezza, 0.001); //controllo altezza prima della modifica

        command.execute();

        assertEquals(initialLarghezza * (proporzioneUpdate / 100.0), ellisse.getLarghezza(), 0.001); //controllo larghezza dopo la modifica
        assertEquals(initialAltezza * (proporzioneUpdate / 100.0), ellisse.getAltezza(), 0.001); //controllo altezza dopo la modifica
    }

    @Test
    void testExecute_ellisseScaleDown() {
        drawSnapModel = mock(DrawSnapModel.class);
        ellisse = new Ellisse(10, 120, 200, 20, Color.BLUE, 150, Color.LIGHTBLUE); // Larghezza 200, Altezza 150

        doAnswer(invocation -> {
            double proporzione = invocation.getArgument(0);
            ellisse.proportionalResize(proporzione);
            return null;
        }).when(drawSnapModel).proportionalResize(anyDouble());

        double initialLarghezza = ellisse.getLarghezza();
        double initialAltezza = ellisse.getAltezza();
        double proporzioneUpdate = 50.0; // Scala a 50%

        command = new ProportionalResizeCommand(drawSnapModel, proporzioneUpdate);

        assertEquals(200, initialLarghezza, 0.001);
        assertEquals(150, initialAltezza, 0.001);

        command.execute();

        assertEquals(initialLarghezza * (proporzioneUpdate / 100.0), ellisse.getLarghezza(), 0.001);
        assertEquals(initialAltezza * (proporzioneUpdate / 100.0), ellisse.getAltezza(), 0.001);
    }

    @Test
    void testExecute_rettangolo() {
        drawSnapModel = mock(DrawSnapModel.class);
        rettangolo = new Rettangolo(93, 75, 120, 56, Color.AZURE, 45, Color.LIGHTGREEN); // Larghezza 120, Altezza 45

        doAnswer(invocation -> {
            double proporzione = invocation.getArgument(0);
            rettangolo.proportionalResize(proporzione);
            return null;
        }).when(drawSnapModel).proportionalResize(anyDouble());

        double initialLarghezza = rettangolo.getLarghezza();
        double initialAltezza = rettangolo.getAltezza();
        double proporzioneUpdate = 75.0; // Scala a 75%

        command = new ProportionalResizeCommand(drawSnapModel, proporzioneUpdate);

        assertEquals(120, initialLarghezza, 0.001);
        assertEquals(45, initialAltezza, 0.001);

        command.execute();

        assertEquals(initialLarghezza * (proporzioneUpdate / 100.0), rettangolo.getLarghezza(), 0.001);
        assertEquals(initialAltezza * (proporzioneUpdate / 100.0), rettangolo.getAltezza(), 0.001);
    }

    @Test
    void testExecute_rettangoloScaleUp() {
        drawSnapModel = mock(DrawSnapModel.class);
        rettangolo = new Rettangolo(102, 357, 58, 180, Color.LIGHTGRAY, 41, Color.LIGHTGOLDENRODYELLOW); // Larghezza 58, Altezza 41

        doAnswer(invocation -> {
            double proporzione = invocation.getArgument(0);
            rettangolo.proportionalResize(proporzione);
            return null;
        }).when(drawSnapModel).proportionalResize(anyDouble());

        double initialLarghezza = rettangolo.getLarghezza();
        double initialAltezza = rettangolo.getAltezza();
        double proporzioneUpdate = 200.0; // Scala a 200%

        command = new ProportionalResizeCommand(drawSnapModel, proporzioneUpdate);

        assertEquals(58, initialLarghezza, 0.001);
        assertEquals(41, initialAltezza, 0.001);

        command.execute();

        assertEquals(initialLarghezza * (proporzioneUpdate / 100.0), rettangolo.getLarghezza(), 0.001);
        assertEquals(initialAltezza * (proporzioneUpdate / 100.0), rettangolo.getAltezza(), 0.001);
    }

    @Test
    void testExecute_linea() {
        drawSnapModel = mock(DrawSnapModel.class);
        linea = new Linea(369, 123, 100, 96, Color.LIGHTGRAY); // Larghezza 100 (rappresenta la lunghezza)

        doAnswer(invocation -> {
            double proporzione = invocation.getArgument(0);
            linea.proportionalResize(proporzione);
            return null;
        }).when(drawSnapModel).proportionalResize(anyDouble());

        double initialLarghezza = linea.getLarghezza();
        double proporzioneUpdate = 70.0; // Scala a 70%

        command = new ProportionalResizeCommand(drawSnapModel, proporzioneUpdate);

        assertEquals(100, initialLarghezza, 0.001);

        command.execute();

        assertEquals(initialLarghezza * (proporzioneUpdate / 100.0), linea.getLarghezza(), 0.001);
    }

    @Test
    void testExecute_lineaScaleUp() {
        drawSnapModel = mock(DrawSnapModel.class);
        linea = new Linea(10, 10, 50, 50, Color.BLACK); // Larghezza 50

        doAnswer(invocation -> {
            double proporzione = invocation.getArgument(0);
            linea.proportionalResize(proporzione);
            return null;
        }).when(drawSnapModel).proportionalResize(anyDouble());

        double initialLarghezza = linea.getLarghezza();
        double proporzioneUpdate = 130.0; // Scala a 130%

        command = new ProportionalResizeCommand(drawSnapModel, proporzioneUpdate);

        assertEquals(50, initialLarghezza, 0.001);

        command.execute();

        assertEquals(initialLarghezza * (proporzioneUpdate / 100.0), linea.getLarghezza(), 0.001);
    }

}