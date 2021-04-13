package model;

public class FoodType {
    private int dbID;
    private String foodDescription;
    private double caloriesPer100g;

    public FoodType(int dbID, String foodDescription, double caloriesPer100g) {
        this.dbID = dbID;
        this.foodDescription = foodDescription;
        this.caloriesPer100g = caloriesPer100g;
    }

    public int getDbID() {
        return dbID;
    }

    public String getFoodDescription() {
        return foodDescription;
    }

    public double getCaloriesPer100g() {
        return caloriesPer100g;
    }

    @Override
    public String toString() {
        return foodDescription;
    }
}
