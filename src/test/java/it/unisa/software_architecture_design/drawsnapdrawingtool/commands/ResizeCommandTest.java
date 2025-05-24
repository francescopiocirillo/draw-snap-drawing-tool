package it.unisa.software_architecture_design.drawsnapdrawingtool.commands;

import it.unisa.software_architecture_design.drawsnapdrawingtool.DrawSnapModel;
import it.unisa.software_architecture_design.drawsnapdrawingtool.forme.Ellisse;
import it.unisa.software_architecture_design.drawsnapdrawingtool.forme.Linea;
import it.unisa.software_architecture_design.drawsnapdrawingtool.forme.Rettangolo;
import javafx.scene.paint.Color;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;

class ResizeCommandTest {
    private DrawSnapModel drawSnapModel;
    private Ellisse ellisse;
    private Rettangolo rettangolo;
    private Linea linea;
    private ResizeCommand command;

    @Test
    void testExecute_ellisse() {
        DrawSnapModel drawSnapModel = mock(DrawSnapModel.class);
        ellisse = new Ellisse(100, 100, 50, 0, Color.BLACK, 30, Color.WHITE);

        doAnswer(invocation -> {
            double altezza = invocation.getArgument(1);
            double larghezza = invocation.getArgument(0);

            ellisse.setAltezza(altezza);
            ellisse.setLarghezza(larghezza);
            return null;
        }).when(drawSnapModel).resize(any(double.class), any(double.class));

        command = new ResizeCommand(drawSnapModel, 20, 150);

        assertEquals(50, ellisse.getLarghezza()); //controllo larghezza prima della modifica
        assertEquals(30, ellisse.getAltezza()); //controllo altezza prima della modifica

        command.execute(); //modifica

        assertEquals(20, ellisse.getLarghezza()); //controllo larghezza dopo della modifica
        assertEquals(150, ellisse.getAltezza()); //controllo altezza dopo della modifica
    }

    @Test
    void testExecute_ellisseSoloAltezza() {
        DrawSnapModel drawSnapModel = mock(DrawSnapModel.class);
        ellisse = new Ellisse(10, 120, 200, 20, Color.BLUE, 15, Color.LIGHTBLUE);

        doAnswer(invocation -> {
            double altezza = invocation.getArgument(1);
            double larghezza = invocation.getArgument(0);

            ellisse.setAltezza(altezza);
            ellisse.setLarghezza(larghezza);
            return null;
        }).when(drawSnapModel).resize(any(double.class), any(double.class));

        command = new ResizeCommand(drawSnapModel, 200, 150);

        assertEquals(200, ellisse.getLarghezza()); //controllo larghezza prima della modifica
        assertEquals(15, ellisse.getAltezza()); //controllo altezza prima della modifica

        command.execute(); //modifica

        assertEquals(200, ellisse.getLarghezza()); //controllo larghezza dopo della modifica
        assertEquals(150, ellisse.getAltezza()); //controllo altezza dopo della modifica
    }

    @Test
    void testExecute_ellisseSoloLarghezza() {
        DrawSnapModel drawSnapModel = mock(DrawSnapModel.class);
        ellisse = new Ellisse(88, 56, 74, 25, Color.FIREBRICK, 96, Color.SALMON);

        doAnswer(invocation -> {
            double altezza = invocation.getArgument(1);
            double larghezza = invocation.getArgument(0);

            ellisse.setAltezza(altezza);
            ellisse.setLarghezza(larghezza);
            return null;
        }).when(drawSnapModel).resize(any(double.class), any(double.class));

        command = new ResizeCommand(drawSnapModel, 32, 96);

        assertEquals(74, ellisse.getLarghezza()); //controllo larghezza prima della modifica
        assertEquals(96, ellisse.getAltezza()); //controllo altezza prima della modifica

        command.execute(); //modifica

        assertEquals(32, ellisse.getLarghezza()); //controllo larghezza dopo della modifica
        assertEquals(96, ellisse.getAltezza()); //controllo altezza dopo della modifica
    }

    @Test
    void testExecute_rettangolo() {
        DrawSnapModel drawSnapModel = mock(DrawSnapModel.class);
        rettangolo = new Rettangolo(93, 75, 12, 56, Color.AZURE, 45, Color.LIGHTGREEN);

        doAnswer(invocation -> {
            double altezza = invocation.getArgument(1);
            double larghezza = invocation.getArgument(0);

            rettangolo.setAltezza(altezza);
            rettangolo.setLarghezza(larghezza);
            return null;
        }).when(drawSnapModel).resize(any(double.class), any(double.class));

        command = new ResizeCommand(drawSnapModel, 21, 54);

        assertEquals(12, rettangolo.getLarghezza()); //controllo larghezza prima della modifica
        assertEquals(45, rettangolo.getAltezza()); //controllo altezza prima della modifica

        command.execute(); //modifica

        assertEquals(21, rettangolo.getLarghezza()); //controllo larghezza dopo della modifica
        assertEquals(54, rettangolo.getAltezza()); //controllo altezza dopo della modifica
    }

    @Test
    void testExecute_rettangoloSoloAltezza() {
        DrawSnapModel drawSnapModel = mock(DrawSnapModel.class);
        rettangolo = new Rettangolo(102, 357, 58, 180, Color.LIGHTGRAY, 41, Color.LIGHTGOLDENRODYELLOW);

        doAnswer(invocation -> {
            double altezza = invocation.getArgument(1);
            double larghezza = invocation.getArgument(0);

            rettangolo.setAltezza(altezza);
            rettangolo.setLarghezza(larghezza);
            return null;
        }).when(drawSnapModel).resize(any(double.class), any(double.class));

        command = new ResizeCommand(drawSnapModel, 58, 20);

        assertEquals(58, rettangolo.getLarghezza()); //controllo larghezza prima della modifica
        assertEquals(41, rettangolo.getAltezza()); //controllo altezza prima della modifica

        command.execute(); //modifica

        assertEquals(58, rettangolo.getLarghezza()); //controllo larghezza dopo della modifica
        assertEquals(20, rettangolo.getAltezza()); //controllo altezza dopo della modifica
    }

    @Test
    void testExecute_rettangoloSoloLarghezza() {
        DrawSnapModel drawSnapModel = mock(DrawSnapModel.class);
        rettangolo = new Rettangolo(12, 37, 18, 18, Color.LIGHTGRAY, 18, Color.LIGHTGOLDENRODYELLOW);

        doAnswer(invocation -> {
            double altezza = invocation.getArgument(1);
            double larghezza = invocation.getArgument(0);

            rettangolo.setAltezza(altezza);
            rettangolo.setLarghezza(larghezza);
            return null;
        }).when(drawSnapModel).resize(any(double.class), any(double.class));

        command = new ResizeCommand(drawSnapModel, 58, 18);

        assertEquals(18, rettangolo.getLarghezza()); //controllo larghezza prima della modifica
        assertEquals(18, rettangolo.getAltezza()); //controllo altezza prima della modifica

        command.execute(); //modifica

        assertEquals(58, rettangolo.getLarghezza()); //controllo larghezza dopo della modifica
        assertEquals(18, rettangolo.getAltezza()); //controllo altezza dopo della modifica
    }

    @Test
    void testExecute_linea() {
        DrawSnapModel drawSnapModel = mock(DrawSnapModel.class);
        linea = new Linea(369, 123, 66, 96, Color.LIGHTGRAY);

        doAnswer(invocation -> {
            double larghezza = invocation.getArgument(0);

            linea.setLarghezza(larghezza);
            return null;
        }).when(drawSnapModel).resize(any(double.class), any(double.class));

        command = new ResizeCommand(drawSnapModel, 44, 0);

        assertEquals(66, linea.getLarghezza()); //controllo larghezza prima della modifica

        command.execute(); //modifica

        assertEquals(44, linea.getLarghezza()); //controllo larghezza dopo della modifica
    }
}