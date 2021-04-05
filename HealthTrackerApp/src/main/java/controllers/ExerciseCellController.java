package controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ListCell;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import model.Exercise;

import java.io.IOException;

public class ExerciseCellController extends ListCell<Exercise> {

    public HBox hBox;
    public Text typeText;
    public Text minsText;
    public Text calsText;
    public Text dateText;

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

            typeText.setText(exercise.getExerciseT().getExerciseDescription());
            minsText.setText(Double.toString(exercise.getMinutesExercised()));
            calsText.setText(Double.toString(exercise.getCaloriesBurned()));
            dateText.setText(exercise.getDate().toString());

            setText(null);
            setGraphic(hBox);
        }
    }

}
