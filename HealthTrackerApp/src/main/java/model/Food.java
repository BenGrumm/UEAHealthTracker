package model;

import java.time.LocalDate;

public class Food {
    private static int id = 0;
    private String foodName;
    private int calories;
    private String meal;
    private LocalDate dateConsumed;

    public Food(String foodName, int calories, String meal, LocalDate dateConsumed) {
        id++;
        this.foodName = foodName;
        this.calories = calories;
        this.meal = meal;
        this.dateConsumed = dateConsumed;
    }

    public int getId() {
        return id;
    }

    public String getFoodName() {
        return foodName;
    }

    public int getCalories() {
        return calories;
    }

    public String getMeal() {
        return meal;
    }

    public LocalDate getDateConsumed() {
        return dateConsumed;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setFoodName(String foodName) {
        this.foodName = foodName;
    }

    public void setCalories(int calories) {
        this.calories = calories;
    }

    public void setMeal(String meal) {
        this.meal = meal;
    }

    public void setDateConsumed(LocalDate dateConsumed) {
        this.dateConsumed = dateConsumed;
    }
}
