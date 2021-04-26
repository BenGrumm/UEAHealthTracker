package sample;

import controllers.groupController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class GUI extends Application {
    static Stage mainStage;
    /**
     * Method to change the current scene
     * @param root Loader.load()
     */
    public static void changeScene(Parent root){
        Stage primaryStage = mainStage;
        primaryStage.setTitle("UEA Health Tracker");
        primaryStage.setScene(new Scene(root, 600, 400));
        primaryStage.getIcons().add(new Image("/icon.png"));
        primaryStage.show();
    }


    @Override
    public void start(Stage primaryStage) throws Exception{
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/main_menu.fxml"));
        Parent root = loader.load();
        primaryStage.setTitle("UEA Health Tracker");
        primaryStage.setScene(new Scene(root, 600, 400));
        primaryStage.getIcons().add(new Image("/icon.png"));
        primaryStage.show();
        mainStage = primaryStage;
    }
}