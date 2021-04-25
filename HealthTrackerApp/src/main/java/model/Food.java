package model;

import java.time.LocalDate;

public class Food {
    private int id;
    private String foodName;
    private double calories;
    private String meal;
    private double servingInGrams;
    private LocalDate dateConsumed;
    private FoodType foodType;

    public Food(int id, String foodName, double calories, String meal, double servingInGrams, LocalDate dateConsumed, FoodType foodType) {
        this.id = id;
        this.foodName = foodName;
        this.calories = calories;
        this.meal = meal;
        this.servingInGrams = servingInGrams;
        this.dateConsumed = dateConsumed;
        this.foodType = foodType;
    }

    public int getId() {
        return id;
    }


    public String getFoodName() {
        return foodName;
    }

    public double getCalories() {
        return calories;
    }

    public String getMeal() {
        return meal;
    }

    public double getServingInGrams() { return servingInGrams; }

    public LocalDate getDateConsumed() {
        return dateConsumed;
    }

    public FoodType getFoodType(){ return foodType; }

    public void setId(int id) {
        this.id = id;
    }

    public void setFoodName(String foodName) {
        this.foodName = foodName;
    }

    public void setCalories(double calories) {
        this.calories = calories;
    }

    public void setMeal(String meal) {
        this.meal = meal;
    }

    public void setDateConsumed(LocalDate dateConsumed) {
        this.dateConsumed = dateConsumed;
    }
}
