package it.unisa.software_architecture_design.drawsnapdrawingtool;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class DrawSnapApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(DrawSnapApplication.class.getResource("DrawSnapView.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1200, 600);

        DrawSnapModel model = new DrawSnapModel();

        DrawSnapController controller = fxmlLoader.getController();
        controller.setStage(stage);
        controller.setModel(model);

        stage.setResizable(false);
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