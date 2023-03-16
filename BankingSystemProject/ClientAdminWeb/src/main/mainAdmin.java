package main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.net.URL;

import static utils.Constants.APP_FXML;

public class mainAdmin extends Application {

    public static void main(String[] args) {
        Thread.currentThread().setName("main");
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader();
        URL url = getClass().getResource(APP_FXML);
        fxmlLoader.setLocation(url);
        assert url != null;
        Parent root = fxmlLoader.load(url.openStream());
        Scene scene = new Scene(root, 1102, 902);
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
