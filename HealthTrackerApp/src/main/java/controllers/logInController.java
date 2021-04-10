package controllers;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;

import java.net.URL;
import java.util.*;
import java.util.regex.Pattern;

public class logInController extends Controller implements Initializable {

    @FXML
    private TextField usernameTextField;

    @FXML
    public void test(){

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        usernameTextField.setStyle("-fx-text-fill:white");
        System.out.println("yajr");
    }
}
