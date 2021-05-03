package model;

import java.time.LocalDate;

public class Exercise {

    private int id;
    private double minutesExercised, caloriesBurned;
    private ExerciseType exerciseT;
    private LocalDate date;

    public Exercise(int id, double minsExercised, double caloriesBurned, ExerciseType exerciseType, LocalDate date){
        this.id = id;
        this.minutesExercised = minsExercised;
        this.caloriesBurned = caloriesBurned;
        this.exerciseT = exerciseType;
        this.date = date;
    }

    public Exercise(int id, double minutesExercised, double caloriesBurned, LocalDate date) {
        this.id = id;
        this.minutesExercised = minutesExercised;
        this.caloriesBurned = caloriesBurned;
        this.date = date;
    }

    public int getId() {
        return id;
    }

    public double getMinutesExercised() {
        return minutesExercised;
    }

    public double getCaloriesBurned() {
        return caloriesBurned;
    }

    public void setExerciseT(ExerciseType et){
        this.exerciseT = et;
    }

    public ExerciseType getExerciseT() {
        return exerciseT;
    }

    public LocalDate getDate() {
        return date;
    }

    @Override
    public String toString() {
        return "id = " + id + ", " +
                "mins exercised = " + minutesExercised + ", " +
                "cals bruned = " + caloriesBurned + ", " +
                "ex type = " + exerciseT.getDbID() + ", " +
                "data = " + date + ", ";
    }

    @Override
    public boolean equals(Object obj) {
        if(obj == this){
            return true;
        }else if(obj instanceof Exercise){
            Exercise eObj = (Exercise) obj;
            return id == eObj.id &&
                    minutesExercised == eObj.getMinutesExercised() &&
                    caloriesBurned == eObj.caloriesBurned &&
                    exerciseT == eObj.exerciseT &&
                    date == eObj.date;
        }

        return false;
    }
}
