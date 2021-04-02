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

    public int getId() {
        return id;
    }

    public double getMinutesExercised() {
        return minutesExercised;
    }

    public double getCaloriesBurned() {
        return caloriesBurned;
    }

    public ExerciseType getExerciseT() {
        return exerciseT;
    }

    public LocalDate getDate() {
        return date;
    }
}
