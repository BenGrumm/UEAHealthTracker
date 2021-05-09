package controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.util.Callback;
import model.Exercise;
import model.ExerciseDBHelper;
import model.Food;
import model.FoodDBHelper;
import sample.GUI;
import sample.Main;
import testing.ExerciseDBTesting;
import testing.FoodDBTesting;

import java.io.IOException;
import java.time.LocalDate;

public class ViewFoodsListController extends Controller{

    public Button dateRangeButton;
    public DatePicker dateFrom;
    public DatePicker dateTo;
    public Label listFoodsLabel;
    public ListView<Food> foodsList;

    private ObservableList<Food> observableList;

    public void initialize(){
        // Set Selectable days to be from current day and before
        Callback<DatePicker, DateCell> cb = d -> new DateCell(){
            @Override
            public void updateItem(LocalDate item, boolean empty) {
                super.updateItem(item, empty);
                setDisable(item.isAfter(LocalDate.now()));
            }
        };

        dateFrom.setDayCellFactory(cb);
        dateTo.setDayCellFactory(cb);

        observableList = FXCollections.observableArrayList();

        if(Main.debug) {
            FoodDBTesting.populateDBWithTestData();
        }

        Food[] foods = new FoodDBHelper().getAllFoods(FoodDBHelper.Order.DESC);

        System.out.println(foods.length);

        observableList.addAll(foods);

        foodsList.setItems(observableList);
        foodsList.setCellFactory(foodListView -> new FoodCellController());
    }

    public void changeDateRange(ActionEvent actionEvent) {
        if(dateFrom.getValue() != null && dateTo.getValue() != null && !dateTo.getValue().isBefore(dateFrom.getValue())){
            Food[] foods = new FoodDBHelper().getFoodsWithinRange(dateFrom.getValue(), dateTo.getValue(), FoodDBHelper.Order.ASC);
            System.out.println(foods.length);
            observableList.clear();
            observableList.addAll(foods);
            foodsList.setItems(observableList);
            listFoodsLabel.setText("Meals From " + dateFrom.getValue() + " to " + dateTo.getValue());
            dateRangeButton.setText("View Foods");
        }else if(dateFrom.getValue() != null && dateTo.getValue().isBefore(dateFrom.getValue())){
            listFoodsLabel.setText("Error Date From Is Greater Than Date To");
        }
    }

    public void switchToGraphActivity(ActionEvent actionEvent) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view_foods.fxml"));

        try {
            Parent root = loader.load();
            GUI.changeScene(root);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
