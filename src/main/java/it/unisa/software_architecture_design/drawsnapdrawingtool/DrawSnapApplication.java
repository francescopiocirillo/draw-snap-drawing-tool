package it.unisa.software_architecture_design.drawsnapdrawingtool;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;

public class DrawSnapApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(DrawSnapApplication.class.getResource("/it/unisa/software_architecture_design/drawsnapdrawingtool/DrawSnapView.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1400, 600);

        DrawSnapModel model = new DrawSnapModel();

        DrawSnapController controller = fxmlLoader.getController();
        controller.setStage(stage);
        controller.setModel(model);

        // Imposta l'icona
        Image icon = new Image(getClass().getResourceAsStream("/it/unisa/software_architecture_design/drawsnapdrawingtool/images/iconaApp.png"));
        stage.getIcons().add(icon);

        stage.setTitle("DrawSnap - Drawing Tool");
        stage.setResizable(true); // finestra ridimensionabile
        stage.setMinWidth(800); // Imposta altezza
        stage.setMinHeight(600); // larghezza minime

        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}