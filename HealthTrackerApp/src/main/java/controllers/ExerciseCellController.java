package controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ListCell;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import model.Exercise;
import model.ExerciseDBHelper;

import java.io.IOException;

public class ExerciseCellController extends ListCell<Exercise> {

    public HBox hBox;
    public Text typeText;
    public Text minsText;
    public Text calsText;
    public Text dateText;
    public Button deleteCellButton;

    private FXMLLoader fxmLoader;

    @Override
    protected void updateItem(Exercise exercise, boolean empty) {
        super.updateItem(exercise, empty);

        if(empty || exercise == null){
            setText(null);
            setGraphic(null);
        }else{
            if(fxmLoader == null){
                fxmLoader = new FXMLLoader(getClass().getResource("/exercise_list_cell.fxml"));
                fxmLoader.setController(this);

                try {
                    fxmLoader.load();
                }catch (IOException e){
                    e.printStackTrace();
                }
            }
            deleteCellButton.setOnAction(event -> {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION,
                        "Are you sure you want to delete this exercise?",
                        ButtonType.YES,
                        ButtonType.NO);
                alert.showAndWait();

                if(alert.getResult() == ButtonType.YES){
                    System.out.println("Delete Yes");
                    new ExerciseDBHelper().removeExerciseFromDB(getItem());
                    getListView().getItems().remove(getItem());
                }
            });

            typeText.setText(exercise.getExerciseT().getExerciseDescription());
            minsText.setText(Double.toString(exercise.getMinutesExercised()));
            calsText.setText(String.format("%.2f", exercise.getCaloriesBurned()));
            dateText.setText(exercise.getDate().toString());

            setText(null);
            setGraphic(hBox);
        }
    }

}
