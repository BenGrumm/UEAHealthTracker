package controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import model.Exercise;
import model.ExerciseDBHelper;
import model.Food;
import model.FoodDBHelper;
import sample.GUI;
import sample.Main;
import testing.ExerciseDBTesting;
import testing.FoodDBTesting;

import java.io.IOException;

public class ViewFoodsListController extends Controller{

    public Button dateRangeButton;
    public DatePicker dateFrom;
    public DatePicker dateTo;
    public Label listFoodsLabel;
    public ListView<Food> foodsList;

    private ObservableList<Food> observableList;

    public void initialize(){
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
