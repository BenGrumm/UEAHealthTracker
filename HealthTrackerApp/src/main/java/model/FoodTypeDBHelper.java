package model;

import java.io.File;
import java.io.FileNotFoundException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Scanner;

public class FoodTypeDBHelper {
    public static final String ID_COLUMN = "__id";
    public static final String TABLE_NAME = "FOOD_TYPES";
    public static final String COLUMN_TYPE = "FOOD_TYPE";
    public static final String COLUMN_CALORIES_PER_100G = "CALORIES_PER_100G";

    private Database db;

    public FoodTypeDBHelper(){
        try {
            db = Database.getInstance();

            String createDBIfNotExists = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME +
                    " (" + ID_COLUMN + " INTEGER PRIMARY KEY AUTOINCREMENT , " +
                    COLUMN_TYPE + " TEXT , " +
                    COLUMN_CALORIES_PER_100G + " REAL)";

            db.createTable(createDBIfNotExists);
        }catch (SQLException sql){
            System.out.println("Error Accessing DB");
            sql.printStackTrace();
        }catch (ClassNotFoundException cnfe){
            System.out.println("JDBC driver not found");
            cnfe.printStackTrace();
        }
    }

    private static final String getAllFood = "SELECT * FROM " + TABLE_NAME + " ORDER BY " + ID_COLUMN + " ASC";
    /**
     * Function that uses a final string containing a select everything from statement to select everything from
     * the food types table
     * @return FoodType[] - return everything from the food types table in an array
     */
    public FoodType[] getAllFoods(){
        try {
            ResultSet rs = db.selectQuery(getAllFood);

            return convertResultSetToFoodType(rs);
        }catch (SQLException error){
            return null;
        }
    }

    /**
     * Function to convert a ResultSet object to an array of FoodType objects
     * @param rs - takes a ResultSet object
     * @return FoodType[] - returns an array of food type objects
     * @throws SQLException
     */
    private FoodType[] convertResultSetToFoodType(ResultSet rs) throws SQLException{
        ArrayList<FoodType> foods = new ArrayList<>();
        while(rs.next()){
            int id = rs.getInt(ID_COLUMN);
            String name = rs.getString(COLUMN_TYPE);
            Float caloriesPer100g = rs.getFloat(COLUMN_CALORIES_PER_100G);

            foods.add(new FoodType(id, name, caloriesPer100g));
        }
        return foods.toArray(new FoodType[foods.size()]);
    }

    private static final String addFood = "INSERT INTO " + TABLE_NAME + " (" + COLUMN_TYPE + ", " +
            COLUMN_CALORIES_PER_100G + ") VALUES(\"%s\", %s)";
    /**
     * Function to scan through csv and convert relevant info to database
     */
    public void initialiseFoodTypeDBFromCSV(){
        File file = new File("src/main/resources/food_dataset.csv");
        try {
            Scanner fileScanner = new Scanner(file);

            // Throwaway 1st line
            if(fileScanner.hasNextLine()){
                fileScanner.nextLine();
            }

            Scanner lineScanner;

            while(fileScanner.hasNextLine()){

                String[] splitLine = fileScanner.nextLine().split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)", -1);

                try {
                    // Input data into query removing any quotes in the description of food
                    String sql = String.format(addFood, splitLine[1].replaceAll("\"", ""), splitLine[2]);
                    System.out.println(sql);
                    db.insertData(sql);
                }catch (SQLException sqle){
                    sqle.printStackTrace();
                }

            }

        }catch (FileNotFoundException e){
            e.printStackTrace();
        }
    }

    /**
     * Function to add a foodtype object to the food types table using a final string containing an insert into sql
     * statement
     * @param foodType - foodType object
     */
    public void addFoodTypeToDB(FoodType foodType){
        try {
            String sql = String.format(addFood, foodType.getFoodDescription(), foodType.getCaloriesPer100g());
            System.out.println(sql);
            db.insertData(sql);
        }catch (SQLException sqle){
            sqle.printStackTrace();
        }
    }

    private static final String queryWithName = "SELECT COUNT(*) AS total FROM " + TABLE_NAME + " WHERE " + COLUMN_TYPE + " = \"%s\"";
    /**
     * Function that uses final string containing select and count sql statement to count how many records are in the
     * users foods table with the same name as the passed in food type
     * @param name - String, a food object name
     * @return boolean - true if contains matching record, false otherwise
     */
    public boolean checkTypeExists(String name){
        try {
            //System.out.println(String.format(queryWithName, name));
            ResultSet rs = db.selectQuery(String.format(queryWithName, name));
            if(rs.getInt("total")>0){
                //System.out.println("WHAT YOU'VE ENTERED ALREADY EXISTS");
                return true;
            }
            else{
                //System.out.println("WHAT YOU'VE ENTERED DOES NOT EXIST");
                return false;
            }
        }catch (SQLException sqle){
            //System.out.println(sqle);
            return false;
        }
    }

    private static final String foodTypeTableLength = "SELECT COUNT(*) AS total FROM " + TABLE_NAME;
    /**
     * Function that uses final string containing select and count sql statement to count how many records are in the
     * food types table
     * @return int - return total number of records in table
     */
    public int foodTypeTableLength(){
        try {
            ResultSet rs = db.selectQuery(foodTypeTableLength);
            return rs.getInt("total");
        }catch (SQLException sqle){
            System.out.println(sqle);
            return 0;
        }
    }

    private static final String queryWithID = "SELECT * FROM " + TABLE_NAME + " WHERE " + ID_COLUMN + " = %d";
    /**
     * Function to query food type table based on record id and return the food type object found
     * @param id
     * @return FoodType
     */
    public FoodType getType(int id){

        try {
            ResultSet rs = db.selectQuery(String.format(queryWithID, id));
            FoodType[] ft = convertResultSetToFoodType(rs);
            if(ft.length >= 1){
                return ft[0];
            }else{
                System.out.println(id + " not found");
                return null;
            }
        }catch (SQLException sqle){
            return null;
        }
    }

    // Test harness
    public static void main(String[] args) {
        FoodTypeDBHelper db = new FoodTypeDBHelper();
        // initialise also tests add to db function
        db.initialiseFoodTypeDBFromCSV();
        // get all foods also tests convert result set function
//        System.out.println(db.getAllFoods());
//        System.out.println(db.checkTypeExists("bread"));
//        System.out.println(db.foodTypeTableLength());
    }
}
