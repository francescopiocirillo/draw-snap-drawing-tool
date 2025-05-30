package it.unisa.software_architecture_design.drawsnapdrawingtool.commands;

import it.unisa.software_architecture_design.drawsnapdrawingtool.DrawSnapModel;
import it.unisa.software_architecture_design.drawsnapdrawingtool.forme.*;
import javafx.scene.paint.Color;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RotationCommandTest {

    DrawSnapModel forme;

    @BeforeEach
    void setUp() {
        forme = new DrawSnapModel();
    }

    @Test
    void testExecute_lineaOrizzontale() {
        // Inizializzazione
        Linea linea = new Linea(500, 500, 60, 0, Color.PINK); // L'angolo iniziale è 0
        FormaSelezionataDecorator fsd = new FormaSelezionataDecorator(linea);
        forme.add(fsd);

        // Applicazione del comando
        double nuovoAngolo = 90.0;
        RotationCommand command = new RotationCommand(forme, nuovoAngolo);
        command.execute();

        // Asserzione
        assertEquals(nuovoAngolo, linea.getAngoloInclinazione());
    }

    @Test
    void testExecute_lineaObliquo() {
        // Inizializzazione
        Linea linea = new Linea(350, 500, 65, 50, Color.YELLOW); // L'angolo iniziale è 0
        FormaSelezionataDecorator fsd = new FormaSelezionataDecorator(linea);
        forme.add(fsd);

        // Applicazione del comando
        double nuovoAngolo = 60.0;
        RotationCommand command = new RotationCommand(forme, nuovoAngolo);
        command.execute();

        // Asserzione
        assertEquals(nuovoAngolo, linea.getAngoloInclinazione());
    }

    @Test
    void testExecute_ellisseOrizzontale() {
        // Inizializzazione
        Ellisse ellisse = new Ellisse(50, 50, 20, 0, Color.LIGHTGRAY, 30, Color.RED); // L'angolo iniziale è 0
        FormaSelezionataDecorator fsd = new FormaSelezionataDecorator(ellisse);
        forme.add(fsd);

        // Applicazione del comando
        double nuovoAngolo = 90.0;
        RotationCommand command = new RotationCommand(forme, nuovoAngolo);
        command.execute();

        // Asserzione
        assertEquals(nuovoAngolo, ellisse.getAngoloInclinazione());
    }

    @Test
    void testExecute_ellisseObliquo() {
        // Inizializzazione
        Ellisse ellisse = new Ellisse(75, 25, 13, 74, Color.SALMON, 44, Color.AZURE); // L'angolo iniziale è 0
        FormaSelezionataDecorator fsd = new FormaSelezionataDecorator(ellisse);
        forme.add(fsd);

        // Applicazione del comando
        double nuovoAngolo = 90.0;
        RotationCommand command = new RotationCommand(forme, nuovoAngolo);
        command.execute();

        // Asserzione
        assertEquals(nuovoAngolo, ellisse.getAngoloInclinazione());
    }

    @Test
    void testExecute_rettangoloOrizzontale() {
        // Inizializzazione
        Rettangolo rettangolo = new Rettangolo(10, 10, 50, 0, Color.GREEN, 22, Color.BLUE); // L'angolo iniziale è 0
        FormaSelezionataDecorator fsd = new FormaSelezionataDecorator(rettangolo);
        forme.add(fsd);

        // Applicazione del comando
        double nuovoAngolo = 180.0;
        RotationCommand command = new RotationCommand(forme, nuovoAngolo);
        command.execute();

        // Asserzione
        assertEquals(nuovoAngolo, rettangolo.getAngoloInclinazione());
    }

    @Test
    void testExecute_rettangoloObliquo() {
        // Inizializzazione
        Rettangolo rettangolo = new Rettangolo(65, 98, 88, 150, Color.FUCHSIA, 22, Color.DARKRED); // L'angolo iniziale è 0
        FormaSelezionataDecorator fsd = new FormaSelezionataDecorator(rettangolo);
        forme.add(fsd);

        // Applicazione del comando
        double nuovoAngolo = 180.0;
        RotationCommand command = new RotationCommand(forme, nuovoAngolo);
        command.execute();

        // Asserzione
        assertEquals(nuovoAngolo, rettangolo.getAngoloInclinazione());
    }


}
