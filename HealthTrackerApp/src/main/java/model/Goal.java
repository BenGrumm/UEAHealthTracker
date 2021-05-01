package model;
import java.time.LocalDate;
import javafx.fxml.FXML;
public class Goal {
    enum goal{
        WEIGHT,
        DIET,
        STEPS
    }
    private int goalID;
    private String name;
    private goal goalType;
    private LocalDate dateStart, dateEnd;

    public Goal(){
        name = "";
        goalType = goal.WEIGHT;
        dateStart = LocalDate.now();
        dateEnd = LocalDate.now();
    }

    public Goal(String name, goal goalType, LocalDate dateEnd){
        this.name = name;
        this.goalType = goalType;
        this.dateStart = LocalDate.now();
        this.dateEnd = dateEnd;
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
}
