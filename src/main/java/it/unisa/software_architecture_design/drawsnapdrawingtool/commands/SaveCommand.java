package it.unisa.software_architecture_design.drawsnapdrawingtool.commands;

import it.unisa.software_architecture_design.drawsnapdrawingtool.DrawSnapModel;
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
    private final DrawSnapModel forme;
    private final Stage stage;

    /*
     * Costruttore, getter e setter
     */
    public SaveCommand(DrawSnapModel forme, Stage stage) {
        this.forme = forme;
        this.stage = stage;
    }

    public Stage getStage() {
        return stage;
    }

    /*
     * Logica della classe
     */

    /**
     * Esegue il comando di salvataggio della lista di Forme su File
     */
    @Override
    public void execute() {
        File file = showFileChooser();

        if (file != null) {
            salvaFormeSuFile(file);
        }
    }
    protected File showFileChooser() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Salva Disegno");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Disegno (*.dsnap)", "*.dsnap"));
        return fileChooser.showSaveDialog(stage);
    }

    /**
     * Metodo di utilità che esegue l'effettivo salvataggio della lista forme nel file specificato
     * @param file File nel quale salvare la lista
     */
    void salvaFormeSuFile(File file) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file))) {
            forme.deselezionaEccetto(null);
            oos.writeObject(forme);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
