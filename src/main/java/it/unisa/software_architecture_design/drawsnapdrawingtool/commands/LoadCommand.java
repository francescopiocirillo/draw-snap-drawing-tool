package it.unisa.software_architecture_design.drawsnapdrawingtool.commands;

import it.unisa.software_architecture_design.drawsnapdrawingtool.forme.Forma;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.List;

/**
 * La classe {@code LoadCommand} rappresenta un comando che carica una lista di {@link Forma}
 * da un file con estensione ".dsnap".
 */
public class LoadCommand implements Command {

    /*
     * Attributi
     */
    private final List<Forma> forme;
    private final Stage stage;

    /*
     * Costruttore, getter e setter
     */
    public LoadCommand(List<Forma> forme, Stage stage) {
        this.forme = forme;
        this.stage = stage;
    }

    public Stage getStage() {
        return stage;
    }

    public List<Forma> getForme() {
        return forme;
    }

    /*
     * Logica della classe
     */

    /**
     * Esegue il comando di caricamento della lista di Forme su File
     */
    @Override
    public void execute() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Carica Disegno");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Disegno (*.dsnap)", "*.dsnap"));
        File file = fileChooser.showOpenDialog(stage);

        if (file != null) {
            caricaFormeDaFile(file);
        }
    }

    /**
     * Metodo di utilità che esegue l'effettivo caricamento della lista forme nel file specificato
     * @param file File dal quale caricare la lista
     */
    void caricaFormeDaFile(File file) {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            List<Forma> formeCaricate = (List<Forma>) ois.readObject();
            forme.clear();
            forme.addAll(formeCaricate);
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
