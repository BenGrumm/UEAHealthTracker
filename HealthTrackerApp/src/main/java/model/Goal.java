package model;
import java.time.LocalDate;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.layout.*;
import javafx.scene.control.*;
import javafx.scene.paint.Color;

public class Goal {
    public enum goal{
        WEIGHT,
        DIET,
        STEPS
    }
    private int goalID, copiedID;
    private float progress, target;
    private String name;
    private goal goalType;
    private LocalDate dateStart, dateEnd;


    public Goal(){
        name = "";
        goalType = goal.WEIGHT;
        dateStart = LocalDate.now();
        dateEnd = LocalDate.now();
    }

    public Goal(String name, goal goalType, float progress, float target){
        this.name = name;
        this.goalType = goalType;
        this.progress = progress;
        this.target = target;
    }

    public Goal(String name, goal goalType, LocalDate dateEnd, float progress, float target){
        this.name = name;
        this.goalType = goalType;
        this.dateStart = LocalDate.now();
        this.dateEnd = dateEnd;
        this.progress = progress;
        this.target = target;
    }

    public Goal(int goalID, String name, goal goalType,float progress, float target, LocalDate dateStart, LocalDate dateEnd, int copiedID) {
        this.goalID = goalID;
        this.name = name;
        this.goalType = goalType;
        this.progress = progress;
        this.target = target;
        this.dateStart = dateStart;
        this.dateEnd = dateEnd;
        this.copiedID = copiedID;
    }

    public Goal(int goalID, String name, goal goalType,float progress, float target, int copiedID) {
        this.goalID = goalID;
        this.name = name;
        this.goalType = goalType;
        this.progress = progress;
        this.target = target;
        this.copiedID = copiedID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public goal getGoalType() {
        return goalType;
    }

    public void setGoalType(goal goalType) {
        this.goalType = goalType;
    }

    public LocalDate getDateStart() {
        return dateStart;
    }

    public void setDateStart(LocalDate dateStart) {
        this.dateStart = dateStart;
    }

    public LocalDate getDateEnd() {
        return dateEnd;
    }

    public void setDateEnd(LocalDate dateEnd) {
        this.dateEnd = dateEnd;
    }

    public int getGoalID() {
        return goalID;
    }

    public void setGoalID(int goalID) {
        this.goalID = goalID;
    }

    public float getProgress() {
        return progress;
    }

    public void setProgress(float progress) {
        this.progress = progress;
    }

    public float getTarget() {
        return target;
    }

    public void setTarget(float target) {
        this.target = target;
    }

    public int getCopiedID() {
        return copiedID;
    }

    public void setCopiedID(int copiedID) {
        this.copiedID = copiedID;
    }

    public void renderGoal(Pane pane){
        GoalDBHelper goalDBHelper = new GoalDBHelper();
        VBox container = new VBox();
        Label goalName = new Label(name);
        container.setStyle("-fx-border-color: black;\n" +
                "-fx-border-insets: 5;\n" +
                "-fx-border-width: 2;\n" +
                "-fx-border-radius: 4;\n" +
                "-fx-border-style: solid;\n" +
                "-fx-padding: 3;\n" +
                "-fx-alignment: center;\n");
        goalName.setStyle("-fx-border-color: black;\n" +
                "-fx-border-insets: 5;\n" +
                "-fx-border-width: 1;\n" +
                "-fx-border-style: solid;\n" +
                "-fx-padding: 3;\n");
        container.getChildren().add(goalName);
        container.getChildren().add(new Label(goalType.toString()));
        //container.getChildren().add(new Label("Status: " + Float.toString(progress)));
        container.getChildren().add(new Label(String.format("Status: %.2f", progress)));
        //container.getChildren().add(new Label("Target: " + Float.toString(target)));
        container.getChildren().add(new Label(String.format("Target: %.2f", target)));



        if(goalType != goal.DIET){

            //System.out.println(name + " : " + dateEnd);
            container.getChildren().add(new Label(String.format("Start date: %s", dateStart.toString())));
            container.getChildren().add(new Label(String.format("End date: %s", dateEnd.toString())));
            Button deleteButton = new Button();
            deleteButton.setText("Delete");
            deleteButton.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent actionEvent) {
                    System.out.println("test");
                    goalDBHelper.removeGoal(goalID);
                    container.getChildren().clear();
                    container.setStyle(null);
                }
            });

            container.getChildren().add(deleteButton);
        }

        pane.getChildren().add(container);
    }
}
