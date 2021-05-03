package controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.util.Callback;
import model.Exercise;
import model.ExerciseDBHelper;
import model.Food;
import model.FoodDBHelper;
import sample.GUI;
import sample.Main;
import testing.FoodDBTesting;

import java.io.IOException;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class ViewFoodsController extends Controller{

    public Label graphLabel;
    public LineChart foodsLineChart;
    public DatePicker dateFrom;
    public DatePicker dateTo;
    public Button dateRangeButton;

    public void initialize() {
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

        if(Main.debug){
            // Add user & foods here
            FoodDBTesting.populateDBWithTestData();
        }

        Food[] allFoods = new FoodDBHelper().getAllFoods(FoodDBHelper.Order.ASC);

        if(allFoods.length > 0){
            populateGraphWithRange(allFoods, allFoods[0].getDateConsumed(), allFoods[allFoods.length - 1].getDateConsumed());
        }else{
            // TODO put error message or something
            displayErrorNoData();
        }
    }

    private void populateGraphWithRange(Food[] allFoods, LocalDate xStart, LocalDate xEnd) {
        foodsLineChart.getData().clear();

        XYChart.Series<String, Double> calsConsumed = new XYChart.Series<>();

        calsConsumed.setName("Calories Consumed");

        int daysBetweenDates = (int) ChronoUnit.DAYS.between(xStart, xEnd);
        System.out.println("Days between dates = " + daysBetweenDates + ", num ex = " + allFoods.length);

        if(daysBetweenDates > 0) {
            int arrPosition = 0;
            LocalDate date;
            double caloriesConsumed;
            // For every date in range, set number of mins exercised and calories burned for date
            for (int i = 0; i <= daysBetweenDates; i++) {
                System.out.println(arrPosition);
                if(     allFoods != null &&
                        arrPosition < allFoods.length &&
                        xStart.plusDays(i).equals(allFoods[arrPosition].getDateConsumed())){
                    System.out.println("Inside");
                    date = allFoods[arrPosition].getDateConsumed();
                    caloriesConsumed = allFoods[arrPosition].getCalories();

                    // Only want one data point for every day so check if multiple entries for current day
                    while((arrPosition + 1) < allFoods.length && date.equals(allFoods[arrPosition + 1].getDateConsumed())){
                        System.out.println("In while = " + arrPosition);
                        arrPosition++;
                        caloriesConsumed += allFoods[arrPosition].getCalories();
                    }

                    calsConsumed.getData().add(new XYChart.Data<>(date.toString(), caloriesConsumed));
                    arrPosition++;
                }else{
                    calsConsumed.getData().add(new XYChart.Data<>(xStart.plusDays(i).toString(), 0.0));
                }
            }
        }

        foodsLineChart.setCreateSymbols(false);

        foodsLineChart.getData().add(calsConsumed);

        foodsLineChart.autosize();
        foodsLineChart.applyCss();
    }

    public void changeDateRange(ActionEvent actionEvent) {
        System.out.println("Yes Clicked Here");

        if(dateFrom.getValue() != null && dateTo.getValue() != null && !dateTo.getValue().isBefore(dateFrom.getValue())){
            Food[] foods = new FoodDBHelper().getFoodsWithinRange(dateFrom.getValue(), dateTo.getValue(), FoodDBHelper.Order.ASC);
            System.out.println(foods.length);
            if(foods.length != 0) {
                populateGraphWithRange(foods, dateFrom.getValue(), dateTo.getValue());
            }else {
                displayErrorNoData();
            }
            graphLabel.setText("Meals From " + dateFrom.getValue() + " to " + dateTo.getValue());
            dateRangeButton.setText("View Meal Data");
        }else if(dateFrom.getValue() != null && dateTo.getValue().isBefore(dateFrom.getValue())){
            graphLabel.setText("Error Date From Is Greater Than Date To");
        }
    }

    private void displayErrorNoData(){
        foodsLineChart.getData().clear();
        dateRangeButton.setText("No Data Found");
    }

    public void switchToListActivity(ActionEvent actionEvent) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view_foods_list.fxml"));

        try {
            Parent root = loader.load();
            GUI.changeScene(root);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
