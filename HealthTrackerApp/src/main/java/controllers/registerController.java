package controllers;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class registerController extends Controller implements Initializable {

    @FXML
    private Spinner<Integer> weightStoneSpinner;
    @FXML
    private Spinner<Integer> weightPoundsSpinner;
    @FXML
    private Spinner<Double> heightSpinner;
    @FXML
    private ChoiceBox<String> genderBox;

    SpinnerValueFactory<Integer> weightStoneSVF = new SpinnerValueFactory.IntegerSpinnerValueFactory(0,Integer.MAX_VALUE,0,1);
    SpinnerValueFactory<Integer> weightPoundsSVF = new SpinnerValueFactory.IntegerSpinnerValueFactory(0,14,0,1);
    SpinnerValueFactory<Double> heightSpinnerSVF = new SpinnerValueFactory.DoubleSpinnerValueFactory(0,300,0,0.1);

    //Global
    @FXML
    public void changeScene(ActionEvent event) {
        System.out.println("Testing");
    }

    //Global
    @FXML
    public void spinnerClick(Event event) {
        System.out.println("Clicked");
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        weightStoneSpinner.setValueFactory(weightStoneSVF);
        weightPoundsSpinner.setValueFactory(weightPoundsSVF);
        heightSpinner.setValueFactory(heightSpinnerSVF);
        ObservableList<String> genders = FXCollections.observableArrayList("Male", "Female");
        genderBox.setItems(genders);
    }
}
