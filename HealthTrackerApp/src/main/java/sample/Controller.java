package sample;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Menu;
import javafx.stage.Stage;

public class Controller {

    @FXML
    private Menu logIn;

    @FXML
    private Button wadbutton;

    @FXML
    void changeScene(ActionEvent event) {

        /*Stage stage = (Stage) wadbutton.getScene().getWindow();
        try{
            Parent root = FXMLLoader.load(getClass().getResource("/testing2.fxml"));
            stage.setScene(new Scene(root, 400,400));
        }catch (Exception e){

        }*/
    }

}


