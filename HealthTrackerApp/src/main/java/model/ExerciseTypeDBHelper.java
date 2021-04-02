package model;

import java.io.*;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class ExerciseTypeDBHelper {

    // CSV with data from https://www.kaggle.com/aadhavvignesh/calories-burned-during-exercise-and-activities

    public static final String ID_COLUMN = "__id";
    public static final String TABLE_NAME = "EXERCISE_TYPES";
    public static final String COLUMN_TYPE = "EXERCISE_TYPE";
    public static final String COLUMN_CALORIES_PER_KG_PER_HOUR = "CALORIES_PER_KG_PER_HOUR";

    private Database db;

    public static void main(String[] args) {
        ExerciseTypeDBHelper e = new ExerciseTypeDBHelper();
        // e.initialiseExerciseTypeDBFromCSV();
    }

    public ExerciseTypeDBHelper(){
        try {
            db = Database.getInstance();

            String createDBIfNotExists = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME +
                                         " (" + ID_COLUMN + " INTEGER PRIMARY KEY AUTOINCREMENT , " +
                                         COLUMN_TYPE + " TEXT , " +
                                         COLUMN_CALORIES_PER_KG_PER_HOUR + " REAL)";

            db.createTable(createDBIfNotExists);
        }catch (ClassNotFoundException cnfe){

        }catch (SQLException sqle){

        }
    }

    private static final String queryWithID = "SELECT * FROM " + TABLE_NAME + " WHERE " + ID_COLUMN + " = %d";

    public ExerciseType getType(int id){

        try {
            ResultSet rs = db.selectQuery(String.format(queryWithID, id));
        }catch (SQLException sqle){
            return null;
        }

        return null;
    }

    private static final String addExercise = "INSERT INTO " + TABLE_NAME + " (" + COLUMN_TYPE + ", " + COLUMN_CALORIES_PER_KG_PER_HOUR + ") VALUES(\"%s\", %s)";

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

}
