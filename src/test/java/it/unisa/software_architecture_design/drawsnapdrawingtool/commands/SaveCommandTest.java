package it.unisa.software_architecture_design.drawsnapdrawingtool.commands;

import it.unisa.software_architecture_design.drawsnapdrawingtool.DrawSnapModel;
import it.unisa.software_architecture_design.drawsnapdrawingtool.forme.AttributiForma;
import it.unisa.software_architecture_design.drawsnapdrawingtool.forme.Rettangolo;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

import it.unisa.software_architecture_design.drawsnapdrawingtool.forme.Forma;
import org.junit.jupiter.api.io.TempDir;
import org.mockito.Mockito;

import java.io.File;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

/**
 * Classe per il testing della classe {@code SaveCommand}
 */
class SaveCommandTest {

    @TempDir
    Path tempDir;
    private Stage mockStage;
    private SaveCommand saveCommand;
    private DrawSnapModel forme;

    /**
     * Verifica che vengano correttamente scritte informazioni su un file
     */
    @Test
    void salvaFormeSuFile_DovrebbeSalvareCorrettamenteLeForme() {
        // Preparazione
        DrawSnapModel forme = new DrawSnapModel();
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
        DrawSnapModel forme = new DrawSnapModel();
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
        DrawSnapModel forme = new DrawSnapModel();
        Stage stageMock = Mockito.mock(Stage.class);
        SaveCommand saveCommand = new SaveCommand(forme, stageMock);

        File testFile = tempDir.resolve("empty.dsnap").toFile();

        // Esecuzione
        saveCommand.salvaFormeSuFile(testFile);

        // Verifica
        assertTrue(testFile.exists());
        assertTrue(testFile.length() > 0); // Anche una lista vuota serializzata occupa spazio
    }

    /**
     * Verifica che in una situazione regolare avvenga la chiamata a salvaFormeSuFile
     */
    @Test
    public void testExecute_chiamataASalvaFormeSuFile() {
        mockStage = mock(Stage.class);
        DrawSnapModel forme = new DrawSnapModel();
        saveCommand = spy(new SaveCommand(forme, mockStage));

        // Mock del file di salvataggio
        File mockFile = mock(File.class);

        // Mock statico per intercettare la chiamata a showSaveDialog
        FileChooser mockChooser = mock(FileChooser.class);
        when(mockChooser.showSaveDialog(mockStage)).thenReturn(mockFile);

        // Override interno del fileChooser nel metodo execute()
        doReturn(mockFile).when(saveCommand).showFileChooser();

        // Spy per intercettare il metodo salvaFormeSuFile
        doNothing().when(saveCommand).salvaFormeSuFile(mockFile); // con uno Spy si può lasciare il comportamento standard di una classe tranne per certi metodi che vengono intercettati

        // Esecuzione
        saveCommand.execute();

        // Verifica che salvaFormeSuFile sia stato chiamato
        verify(saveCommand).salvaFormeSuFile(mockFile);
    }

    /**
     * Verifica che se non viene fornito un file allora non avviene la chiamata a salvaFormeSuFile
     */
    @Test
    void testExecute_quandoNessunFileSelezionato_nonChiamaSalvaForme() {
        mockStage = mock(Stage.class);
        DrawSnapModel forme = new DrawSnapModel();
        saveCommand = spy(new SaveCommand(forme, mockStage));

        doReturn(null).when(saveCommand).showFileChooser();

        saveCommand.execute();

        verify(saveCommand, never()).salvaFormeSuFile(any());
    }
}