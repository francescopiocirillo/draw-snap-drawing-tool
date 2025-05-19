package it.unisa.software_architecture_design.drawsnapdrawingtool.commands;

import it.unisa.software_architecture_design.drawsnapdrawingtool.forme.Forma;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

/**
 * Classe per il testing del metodo {@execute} della classe {@code SaveCommand}
 */
public class SaveCommandTestExecute {

    private Stage mockStage;
    private SaveCommand saveCommand;
    private List<Forma> forme;

    /**
     * Prima di ogni metodo di test vengono inizializzati tre oggetti che saranno utilizzati
     */
    @BeforeEach
    public void setUp() {
        mockStage = mock(Stage.class);
        forme = new ArrayList<>();
        saveCommand = spy(new SaveCommand(forme, mockStage));
    }

    /**
     * Verifica che in una situazione regolare avvenga la chiamata a salvaFormeSuFile
     */
    @Test
    public void testExecute_chiamataASalvaFormeSuFile() {
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
        doReturn(null).when(saveCommand).showFileChooser();

        saveCommand.execute();

        verify(saveCommand, never()).salvaFormeSuFile(any());
    }

}
