package it.unisa.software_architecture_design.drawsnapdrawingtool.commands;

import it.unisa.software_architecture_design.drawsnapdrawingtool.forme.AttributiForma;
import it.unisa.software_architecture_design.drawsnapdrawingtool.forme.Rettangolo;
import javafx.stage.Stage;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import it.unisa.software_architecture_design.drawsnapdrawingtool.forme.Forma;
import org.junit.jupiter.api.io.TempDir;
import org.mockito.Mockito;

import java.io.File;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

/**
 * Classe per il testing del metodo {@code salvaFormeSuFile} della classe {@code SaveCommand}
 */
class SaveCommandTestSalvaFormeSuFile {

    @TempDir
    Path tempDir;

    /**
     * Verifica che vengano correttamente scritte informazioni su un file
     */
    @Test
    void salvaFormeSuFile_DovrebbeSalvareCorrettamenteLeForme() {
        // Preparazione
        List<Forma> forme = new ArrayList<>();
        AttributiForma attr = new AttributiForma();
        Forma forma = new Rettangolo(attr.getCoordinataX(), attr.getCoordinataY(), attr.getLarghezza(),
                attr.getAngoloInclinazione(), attr.getColore(), attr.getAltezza(), attr.getColoreInterno());
        forme.add(forma);

        Stage stageMock = Mockito.mock(Stage.class);
        SaveCommand saveCommand = new SaveCommand(forme, stageMock);

        File testFile = tempDir.resolve("test.dsnap").toFile();

        // Esecuzione
        saveCommand.salvaFormeSuFile(testFile);

        // Verifica
        assertTrue(testFile.exists());
        assertTrue(testFile.length() > 0);
    }

    /**
     * Verifica che se il file è null allora viene lanciata una {@code NullPointerException}
     */
    @Test
    void salvaFormeSuFile_ConFileNulloDovrebbeLanciareNullPointerException() {
        // Preparazione
        List<Forma> forme = new ArrayList<>();
        Stage stageMock = Mockito.mock(Stage.class);
        SaveCommand saveCommand = new SaveCommand(forme, stageMock);

        // Esecuzione e verifica
        assertThrows(NullPointerException.class, () -> saveCommand.salvaFormeSuFile(null));
    }

    /**
     * Verifica che se non ci sono Forme nella lista allora il file creato è vuoto
     */
    @Test
    void salvaFormeSuFile_ConListaFormeVuotaDovrebbeCreareFileVuoto() {
        // Preparazione
        List<Forma> forme = new ArrayList<>();
        Stage stageMock = Mockito.mock(Stage.class);
        SaveCommand saveCommand = new SaveCommand(forme, stageMock);

        File testFile = tempDir.resolve("empty.dsnap").toFile();

        // Esecuzione
        saveCommand.salvaFormeSuFile(testFile);

        // Verifica
        assertTrue(testFile.exists());
        assertTrue(testFile.length() > 0); // Anche una lista vuota serializzata occupa spazio
    }
}