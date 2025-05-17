package it.unisa.software_architecture_design.drawsnapdrawingtool.commands;

import it.unisa.software_architecture_design.drawsnapdrawingtool.forme.Forma;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.List;

/**
 * La classe {@code SaveCommand} rappresenta un comando che salva una lista di {@link Forma}
 * in un file con estensione ".dsnap".
 */
public class SaveCommand implements Command {

    /*
     * Attributi
     */
    private final List<Forma> forme;
    private final Stage stage;

    /*
     * Costruttore, getter e setter
     */
    public SaveCommand(List<Forma> forme, Stage stage) {
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
     * Esegue il comando di salvataggio della lista di Forme su File
     */
    @Override
    public void execute() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Salva Disegno");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Disegno (*.dsnap)", "*.dsnap"));
        File file = fileChooser.showSaveDialog(stage);

        if (file != null) {
            salvaFormeSuFile(file);
        }
    }

    /**
     * Metodo di utilità che esegue l'effettivo salvataggio della lista forme nel file specificato
     * @param file File nel quale salvare la lista
     */
    private void salvaFormeSuFile(File file) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file))) {
            oos.writeObject(forme);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
