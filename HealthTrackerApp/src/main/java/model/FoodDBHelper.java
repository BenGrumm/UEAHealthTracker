package model;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;

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
    // Foreign key for logged in user
    private static final String COLUMN_USER_ID = "USER_ID";

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
                    COLUMN_DATE + " DATE , " +
                    COLUMN_FOOD_ID + " INTEGER , "+
                    COLUMN_USER_ID + " INTEGER , " +
                    " FOREIGN KEY(" + COLUMN_FOOD_ID + ") REFERENCES " +
                    FoodTypeDBHelper.TABLE_NAME + "(__id) , " +
                    "FOREIGN KEY(" + COLUMN_USER_ID + ") REFERENCES " +
                    UserDBHelper.getTableName() + "(__id))" ;
            System.out.println(createDBIfNotExists);
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
            + COLUMN_FOOD_ID + ", "
            + COLUMN_USER_ID + ") VALUES(\"%s\", %s, \"%s\", %s, '%s', %s, %s)";

    /**
     * Function that uses final string containing insert into sql statement to insert food objects into the users foods
     * table
     * @param food
     */
    public void addFoodToDB(Food food){
        try {
            // CURRENTLY HOLDS TEST DATA - once log in process is fully funcitonal, just take the
            // user.getLoggedIn().getID() rather than using this test user
//            User testUser = new User(1, "Caitlin", "Wright", "cwright",
//                    "17cwright@gmail.com", "123", 10, 10, 10, 10,
//                    10, 10, "Female");
//            User.setLoggedIn(testUser);
            String sql = String.format(addFood, food.getFoodName(), food.getCalories(), food.getMeal(),
                    food.getServingInGrams(), food.getDateConsumed(), food.getFoodType().getDbID(),
                    User.getLoggedIn().getID());
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

    public enum Order{
        ASC,
        DESC
    }

    public static final String sqlGetAll = "SELECT * FROM " + TABLE_NAME +
            " WHERE " + COLUMN_USER_ID + " = %d" +
            " ORDER BY " + COLUMN_DATE + " %s";

    public Food[] getAllFoods(Order order){
        try {
            System.out.println(User.loggedIn.getID());
            String sql = String.format(sqlGetAll, User.getLoggedIn().getID(), order.toString());
            System.out.println(sql);
            ResultSet rs = db.selectQuery(sql);
            return convertResultSetToFoods(rs);
        }catch (SQLException error){
            error.printStackTrace();
            return null;
        }
    }

    private static final String withinRangeSQL = "SELECT * FROM " + TABLE_NAME +
            " WHERE " + COLUMN_DATE +
            " BETWEEN '%s' AND '%s' AND " + COLUMN_USER_ID + " = %s" +
            " ORDER BY " + COLUMN_DATE + " %s;";

    public Food[] getFoodsWithinRange(LocalDate dateFrom, LocalDate dateTo, Order order){
        try {
            String sql = String.format(withinRangeSQL, dateFrom.toString(), dateTo.toString(), User.getLoggedIn().getID(), order.toString());
            System.out.println(sql);
            ResultSet rs = db.selectQuery(sql);

            return convertResultSetToFoods(rs);
        }catch (SQLException error){
            return null;
        }
    }

    private static String removeFood = "DELETE FROM " + TABLE_NAME + " WHERE " + COLUMN_ID + " = %d;";

    /**
     * Function to remove a food from the database
     * @param food to remove
     */
    public void removeFoodFromDB(Food food){
        try {
            String deleteSql = String.format(removeFood, food.getId());
            System.out.println(deleteSql);
            db.deleteData(deleteSql);
        }catch (SQLException sqle){
            sqle.printStackTrace();
        }
    }

    /**
     *
     * @param resultSet
     * @return
     * @throws SQLException
     */
    public Food[] convertResultSetToFoods(ResultSet resultSet) throws SQLException{
        ArrayList<Food> foods = new ArrayList<>();
        ArrayList<Integer> ftID = new ArrayList<>();

        while(resultSet.next()){
            int id = resultSet.getInt(COLUMN_ID);
            String foodName = resultSet.getString(COLUMN_NAME);
            double caloriesConsumed = resultSet.getDouble(COLUMN_CALORIES);
            String meal = resultSet.getString(COLUMN_MEAL);
            double serving = resultSet.getDouble(COLUMN_SERVING);
            LocalDate date = LocalDate.parse(resultSet.getString(COLUMN_DATE));
            ftID.add(resultSet.getInt(COLUMN_FOOD_ID));

            foods.add(new Food(id, foodName, caloriesConsumed, meal, serving, date));
        }

        FoodTypeDBHelper dbh = new FoodTypeDBHelper();

        for(int i = 0; i < foods.size(); i++){
            foods.get(i).setFoodType(dbh.getType(ftID.get(i)));
        }

        return foods.toArray(new Food[foods.size()]);
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
