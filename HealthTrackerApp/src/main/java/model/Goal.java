package model;
import java.time.LocalDate;
import javafx.fxml.FXML;
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
}
