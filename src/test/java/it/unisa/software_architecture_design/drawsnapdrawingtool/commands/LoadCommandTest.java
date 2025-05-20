package it.unisa.software_architecture_design.drawsnapdrawingtool.commands;

import it.unisa.software_architecture_design.drawsnapdrawingtool.forme.AttributiForma;
import it.unisa.software_architecture_design.drawsnapdrawingtool.forme.Forma;
import it.unisa.software_architecture_design.drawsnapdrawingtool.forme.Rettangolo;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.mockito.MockedConstruction;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

import static org.mockito.Mockito.*;

/**
 * Classe per il testing della classe {@code LoadCommand}
 */
class LoadCommandTest {

    @TempDir
    Path tempDir;

    private List<Forma> forme;
    private Stage mockStage;
    private LoadCommand loadCommand;

    /**
     * Metodo di settaggio dei test che si occupa di istanziare gli attributi che i diversi test useranno
     */
    @BeforeEach
    void setUp() {
        forme = new ArrayList<>();
        mockStage = mock(Stage.class);
        loadCommand = new LoadCommand(forme, mockStage);
    }

    /**
     * Verifica che vengano correttamente caricate informazioni da un file
     */
    @Test
    void caricaFormeDaFile_DovrebbeCaricareCorrettamenteLeForme() {
        List<Forma> formeOriginali = new ArrayList<>(); //forme presenti nel file salvato

        AttributiForma attr = new AttributiForma();
        Forma forma = new Rettangolo(attr.getCoordinataX(), attr.getCoordinataY(), attr.getLarghezza(),
                attr.getAngoloInclinazione(), attr.getColore(), attr.getAltezza(), attr.getColoreInterno());

        formeOriginali.add(forma);

        // si crea un altra lista per non intaccare quelle originali
        List<Forma> formeSalvate = new ArrayList<>(formeOriginali);

        //Simulo il salvataggio su file
        SaveCommand save = new SaveCommand(formeSalvate, mockStage);
        File fileSalvato = tempDir.resolve("testForLoad.dsnap").toFile(); //File da caricare
        save.salvaFormeSuFile(fileSalvato);

        //Controlli sulla corretta creazione del file
        assertTrue(fileSalvato.exists(), "Il file non esiste");
        assertTrue(fileSalvato.length() > 0, "il file salvato è vuoto");

        //Mock del fileChooser
        try (MockedConstruction<FileChooser> mockedFileChooser = mockConstruction(FileChooser.class,
                (mockFileChooser, context) -> {
                    ObservableList<FileChooser.ExtensionFilter> filters = FXCollections.observableArrayList();
                    when(mockFileChooser.getExtensionFilters()).thenReturn(filters);
                    when(mockFileChooser.showOpenDialog(mockStage)).thenReturn(fileSalvato);
                })) {

            //Eseguo il comando di load
            loadCommand.execute();

            //Verifico che il filechooser è stato aperto
            FileChooser chooser = mockedFileChooser.constructed().get(0);
            verify(chooser).showOpenDialog(mockStage);

            //Controllo che la lista di forme presente nell'applicativo sia stato modificato
            assertFalse(forme.isEmpty(), "la lista di forme non dovrebbe essere vuota");
            assertEquals(formeOriginali.size(), forme.size(), "Non sono state caricate tutte le forme");
        }
    }

    /**
     * Verifico che le forme non vengano caricate dopo aver annullato l'operazione su un
     * foglio di disegno non vuoto
     */
    @Test
    void caricaFordeDaFile_QuandoNessunFileSelezionatoNonModificaForme_ConFoglioVuotoInizialmente(){
        //Svuoto le forme presenti nell'applicativo
        forme.clear();

        //Definisco le forme presenti nel file salvato
        List<Forma> formeOriginali = new ArrayList<>(); //forme presenti nel file salvato

        AttributiForma attr = new AttributiForma();
        Forma forma = new Rettangolo(attr.getCoordinataX(), attr.getCoordinataY(), attr.getLarghezza(),
                attr.getAngoloInclinazione(), attr.getColore(), attr.getAltezza(), attr.getColoreInterno());

        formeOriginali.add(forma);

        // si crea un altra lista per non intaccare quelle originali
        List<Forma> formeSalvate = new ArrayList<>(formeOriginali);

        //Simulo il salvataggio su file
        SaveCommand save = new SaveCommand(formeSalvate, mockStage);
        File fileSalvato = tempDir.resolve("testForLoad.dsnap").toFile(); //File da caricare
        save.salvaFormeSuFile(fileSalvato);

        //Controlli sulla corretta creazione del file
        assertTrue(fileSalvato.exists(), "Il file non esiste");
        assertTrue(fileSalvato.length() > 0, "il file salvato è vuoto");

        //Mock del fileChooser
        try (MockedConstruction<FileChooser> mockedFileChooser = mockConstruction(FileChooser.class,
                (mockFileChooser, context) -> {
                    ObservableList<FileChooser.ExtensionFilter> filters = FXCollections.observableArrayList();
                    when(mockFileChooser.getExtensionFilters()).thenReturn(filters);
                    when(mockFileChooser.showOpenDialog(mockStage)).thenReturn(null);
                })) {

            //Eseguo il comando di load
            loadCommand.execute();

            //Verifico che il filechooser è stato aperto
            FileChooser createdFileChooser = mockedFileChooser.constructed().get(0);
            verify(createdFileChooser).showOpenDialog(mockStage);

            //Controllo che la lista di forme presente nell'applicativo sia stato modificato
            assertEquals(0, forme.size(), "La lista delle forme non dovrebbe cambiare.");
            assertTrue(forme.isEmpty(), "La lista delle forme dovrebbe essere vuota.");
        }
    }

    /**
     * Verifico che le forme non vengano caricate dopo aver annullato l'operazione su un
     * foglio già precedentemente vuoto
     */
    @Test
    void caricaFordeDaFile_QuandoNessunFileSelezionatoNonModificaForme_ConFoglioNonVuotoInizialmente(){
        //Carico una forma nel foglio di disegno dell'applicativo
        AttributiForma attr1 = new AttributiForma();
        Forma forma1 = new Rettangolo(attr1.getCoordinataX(), attr1.getCoordinataY(), attr1.getLarghezza(),
                attr1.getAngoloInclinazione(), attr1.getColore(), attr1.getAltezza(), attr1.getColoreInterno());

        forme.add(forma1);

        int formeDim = forme.size();

        //Definisco le forme presenti nel file salvato
        List<Forma> formeOriginali = new ArrayList<>(); //forme presenti nel file salvato

        AttributiForma attr2 = new AttributiForma();
        Forma forma2 = new Rettangolo(attr2.getCoordinataX(), attr2.getCoordinataY(), attr2.getLarghezza(),
                attr2.getAngoloInclinazione(), attr2.getColore(), attr2.getAltezza(), attr2.getColoreInterno());

        formeOriginali.add(forma2);

        // si crea un altra lista per non intaccare quelle originali
        List<Forma> formeSalvate = new ArrayList<>(formeOriginali);

        //Simulo il salvataggio su file
        SaveCommand save = new SaveCommand(formeSalvate, mockStage);
        File fileSalvato = tempDir.resolve("testForLoad.dsnap").toFile(); //File da caricare
        save.salvaFormeSuFile(fileSalvato);

        //Controlli sulla corretta creazione del file
        assertTrue(fileSalvato.exists(), "Il file non esiste");
        assertTrue(fileSalvato.length() > 0, "il file salvato è vuoto");

        try (MockedConstruction<FileChooser> mockedFileChooser = mockConstruction(FileChooser.class,
                (mockFileChooser, context) -> {
                    ObservableList<FileChooser.ExtensionFilter> filters = FXCollections.observableArrayList();
                    when(mockFileChooser.getExtensionFilters()).thenReturn(filters);
                    when(mockFileChooser.showOpenDialog(mockStage)).thenReturn(null);
                })) {

            loadCommand.execute();

            FileChooser createdFileChooser = mockedFileChooser.constructed().get(0);
            verify(createdFileChooser).showOpenDialog(mockStage);

            assertEquals(formeDim, forme.size(), "La lista delle forme non dovrebbe cambiare.");

        }
    }

    /**
     * Verifica che se il file è null allora viene lanciata una {@code NullPointerException}
     */
    @Test
    void caricaFormeDaFile_ConFileNulloDovrebbeLanciareNullPointerException(){
        //Controllo che venga lanciata l'eccezione
        assertThrows(NullPointerException.class, () -> loadCommand.caricaFormeDaFile(null));
    }



}