package model;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

public class FoodDBHelper {

    private static final String TABLE_NAME = "USER_FOOD_ENTRIES";

    private static final String COLUMN_ID = "__id";
    private static final String COLUMN_NAME = "NAME";
    private static final String COLUMN_CALORIES = "CALORIES";
    private static final String COLUMN_MEAL = "MEAL";
    private static final String COLUMN_SERVING = "SERVING_IN_GRAMS";
    private static final String COLUMN_DATE = "DATE";
    // Foreign key for stored foods
    private static final String COLUMN_FOOD_ID = "FOOD_TYPE";

    private Database db;

    public FoodDBHelper(){
        try{
            db = Database.getInstance();

            String createDBIfNotExists = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME +
                    " (" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT , " +
                    COLUMN_NAME + " TEXT , " +
                    COLUMN_CALORIES + " REAL , " +
                    COLUMN_MEAL + " REAL , " +
                    COLUMN_SERVING + " REAL , " +
                    COLUMN_DATE + " TEXT , " +
                    COLUMN_FOOD_ID + " INTEGER , "+
                    " FOREIGN KEY(" + COLUMN_FOOD_ID + ") REFERENCES " +
                    FoodTypeDBHelper.TABLE_NAME + "(__id))";
            db.createTable(createDBIfNotExists);

        }catch (SQLException sql){
            System.out.println("Error Accessing DB");
            sql.printStackTrace();
        }catch (ClassNotFoundException cnfe){
            System.out.println("JDBC driver not found");
            cnfe.printStackTrace();
        }
    }

    private static final String addFood = "INSERT INTO " + TABLE_NAME + " ("
            + COLUMN_NAME + ", "
            + COLUMN_CALORIES + ", "
            + COLUMN_MEAL + ", "
            + COLUMN_SERVING + ", "
            + COLUMN_DATE + ", "
            + COLUMN_FOOD_ID + ") VALUES(\"%s\", %s, \"%s\", %s, %s, %s)";

    /**
     * Function that uses final string containing insert into sql statement to insert food objects into the users foods
     * table
     * @param food
     */
    public void addFoodToDB(Food food){
        try {
            String sql = String.format(addFood, food.getFoodName(), food.getCalories(), food.getMeal(),
                    food.getServingInGrams(), food.getDateConsumed(), food.getFoodType().getDbID());
            System.out.println(sql);
            db.insertData(sql);
        }catch (SQLException sqle){
            sqle.printStackTrace();
        }
    }

    private static final String foodTableLength = "SELECT COUNT(*) AS total FROM " + TABLE_NAME;
    /**
     * Function that uses final string containing select and count sql statement to count how many records are in the
     * users foods table
     */
    public int foodTableLength(){
        try {
            ResultSet rs = db.selectQuery(foodTableLength);
            return rs.getInt("total");
        }catch (SQLException sqle){
            System.out.println(sqle);
            return 0;
        }
    }

    // test harness
    public static void main(String[] args) {
        FoodDBHelper db = new FoodDBHelper();
        FoodType testType = new FoodType(1, "cheese", 100);
        Food testFood = new Food(1, "cheese", 100, "breakfast", 100,
                LocalDate.now(), testType);
        db.addFoodToDB(testFood);
        System.out.println(db.foodTableLength());

    }
}
