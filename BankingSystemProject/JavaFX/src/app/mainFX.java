package app;

import header.HeaderController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.stage.Stage;

import java.net.URL;

public class mainFX extends Application {

    public final static String APP_FXML = "/app/appFxml.fxml";

    public static void main(String[] args){
        Thread.currentThread().setName("main");
        launch(args);
    }
    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader();
        URL url = getClass().getResource(APP_FXML);
        fxmlLoader.setLocation(url);
        Parent root = fxmlLoader.load(url.openStream());
        Scene scene = new Scene(root, 1102, 902);
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
