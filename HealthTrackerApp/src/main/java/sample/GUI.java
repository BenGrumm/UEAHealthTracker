package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class GUI extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("/main_menu.fxml"));
        primaryStage.setTitle("UEA Health Tracker");
        primaryStage.setScene(new Scene(root, 600, 400));
        primaryStage.getIcons().add(new Image("/icon.png"));
        primaryStage.show();
    }
}