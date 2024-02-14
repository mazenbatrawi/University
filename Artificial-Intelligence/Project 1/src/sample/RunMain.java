package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class RunMain extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(RunMain.class.getResource("main.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 875, 728);
        stage.setTitle("Home");
        stage.setScene(scene);
        stage. setResizable(false);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
