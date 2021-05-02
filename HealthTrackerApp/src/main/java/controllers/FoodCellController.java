package controllers;

import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import model.ExerciseDBHelper;
import model.Food;


import java.io.IOException;

public class FoodCellController extends ListCell<Food> {

    public HBox hBox;
    public Text dateText;
    public Text calsText;
    public Button deleteCellButton;

    private FXMLLoader fxmLoader;

    @Override
    protected void updateItem(Food food, boolean empty) {
        super.updateItem(food, empty);

        if(empty || food == null){
            setText(null);
            setGraphic(null);
        }else{
            if(fxmLoader == null){
                fxmLoader = new FXMLLoader(getClass().getResource("/food_list_cell.fxml"));
                fxmLoader.setController(this);

                try {
                    fxmLoader.load();
                }catch (IOException e){
                    e.printStackTrace();
                }
            }

            deleteCellButton.setOnAction(event -> {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION,
                        "Are you sure you want to delete this food entry?",
                        ButtonType.YES,
                        ButtonType.NO);

                alert.showAndWait();

                if(alert.getResult() == ButtonType.YES){
                    System.out.println("Delete Yes");
                    // new FoodDBHelper().removeExerciseFromDB(getItem());
                    getListView().getItems().remove(getItem());
                }
            });

            calsText.setText(Double.toString(food.getCalories()));
            dateText.setText(food.getDateConsumed().toString());

            setText(null);
            setGraphic(hBox);
        }
    }
}
