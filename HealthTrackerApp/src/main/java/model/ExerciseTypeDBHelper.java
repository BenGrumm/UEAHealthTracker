package model;

import java.io.*;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class ExerciseTypeDBHelper {

    // CSV with data from https://www.kaggle.com/aadhavvignesh/calories-burned-during-exercise-and-activities

    public static final String ID_COLUMN = "__id";
    public static final String TABLE_NAME = "EXERCISE_TYPES";
    public static final String COLUMN_TYPE = "EXERCISE_TYPE";
    public static final String COLUMN_CALORIES_PER_KG_PER_HOUR = "CALORIES_PER_KG_PER_HOUR";

    private Database db;

    public ExerciseTypeDBHelper(){
        try {
            db = Database.getInstance();

            String createDBIfNotExists = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME +
                                         " (" + ID_COLUMN + " INTEGER PRIMARY KEY AUTOINCREMENT , " +
                                         COLUMN_TYPE + " TEXT , " +
                                         COLUMN_CALORIES_PER_KG_PER_HOUR + " REAL)";

            db.createTable(createDBIfNotExists);
        }catch (SQLException sql){
            System.out.println("Error Accessing DB");
            sql.printStackTrace();
        }catch (ClassNotFoundException cnfe){
            System.out.println("JDBC driver not found");
            cnfe.printStackTrace();
        }
    }

    private static final String getAllExercises = "SELECT * FROM " + TABLE_NAME + " ORDER BY " + ID_COLUMN + " ASC";
    /**
     * Function that uses a final string containing a select everything from statement to select everything from
     * the user exercise types table
     * @return ExerciseType[] - return everything from the exercise type table in an array
     */
    public ExerciseType[] getAllExercises(){
        try {
            ResultSet rs = db.selectQuery(getAllExercises);

            return convertResultSetToExerciseType(rs);
        }catch (SQLException error){
            return null;
        }
    }
    /**
     * Function to convert a ResultSet object to an array of ExerciseType objects
     * @param rs - takes a ResultSet object
     * @return ExerciseType - returns an array of exercise type objects
     * @throws SQLException
     */
    public ExerciseType[] convertResultSetToExerciseType(ResultSet rs) throws SQLException{
        ArrayList<ExerciseType> exercises = new ArrayList<>();

        while(rs.next()){
            int id = rs.getInt(ID_COLUMN);
            String name = rs.getString(COLUMN_TYPE);
            Float caloriesBurnedPerKGH = rs.getFloat(COLUMN_CALORIES_PER_KG_PER_HOUR);

            exercises.add(new ExerciseType(id, name, caloriesBurnedPerKGH));
        }
        return exercises.toArray(new ExerciseType[exercises.size()]);
    }

    private static final String queryWithID = "SELECT * FROM " + TABLE_NAME + " WHERE " + ID_COLUMN + " = %d";
    /**
     * Function to query exercise type table based on record id and return the exercise type object found
     * @param id
     * @return ExerciseType
     */
    public ExerciseType getType(int id){

        try {
            ResultSet rs = db.selectQuery(String.format(queryWithID, id));
        }catch (SQLException sqle){
            return null;
        }
        return null;
    }

    private static final String queryWithName = "SELECT COUNT(*) AS total FROM " + TABLE_NAME + " WHERE " + COLUMN_TYPE
            + " = \"%s\"";

    /**
     * Function to check if an exercise type exists in the table based on its name
     * @param name
     * @return boolean - true if the record exists, false otherwise
     */
    public boolean checkTypeExists(String name){
        try {
            ResultSet rs = db.selectQuery(String.format(queryWithName, name));
            if(rs.getInt("total")>0){
                return true;
            }
            else{
                return false;
            }
        }catch (SQLException sqle){
            System.out.println(sqle);
            return false;
        }
    }

    private static final String exerciseTypeTableLength = "SELECT COUNT(*) AS total FROM " + TABLE_NAME;

    /**
     * Function to return the length of the exercise type table
     * @return int
     */
    public int exerciseTypeTableLength(){
        try {
            ResultSet rs = db.selectQuery(exerciseTypeTableLength);
            return rs.getInt("total");
        }catch (SQLException sqle){
            System.out.println(sqle);
            return 0;
        }
    }

    private static final String addExercise = "INSERT INTO " + TABLE_NAME + " (" + COLUMN_TYPE + ", " +
            COLUMN_CALORIES_PER_KG_PER_HOUR + ") VALUES(\"%s\", %s)";

    /**
     * Function to scan through csv and convert relevant info to database
     */
    public void initialiseExerciseTypeDBFromCSV(){
        File file = new File("src/main/resources/exercise_dataset.csv");
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
                    // Input data into query removing any quotes in the description of exercise
                    String sql = String.format(addExercise, splitLine[0].replaceAll("\"", ""), splitLine[5]);
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
     * Function to add an exercise type object to the exercise type table
     * @param ex, ExerciseType
     */
    public void addExerciseTypeToDB(ExerciseType ex){
        try {
            String sql = String.format(addExercise, ex.getExerciseDescription(), ex.getCaloriesBurned());
            System.out.println(sql);
            db.insertData(sql);
        }catch (SQLException sqle){
            sqle.printStackTrace();
        }
    }

    // Test harness
    public static void main(String[] args) {
        ExerciseTypeDBHelper e = new ExerciseTypeDBHelper();
        // e.initialiseExerciseTypeDBFromCSV();
        for(ExerciseType ex : e.getAllExercises()){
            System.out.println(ex);
        }
        System.out.println(e.exerciseTypeTableLength());
        e.getType(1);
        ExerciseType typeTest = new ExerciseType(1, "run", 100);
        e.addExerciseTypeToDB(typeTest);
        e.checkTypeExists("run");
    }

}
