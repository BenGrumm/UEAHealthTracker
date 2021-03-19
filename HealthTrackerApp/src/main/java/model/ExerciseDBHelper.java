package model;

import java.sql.SQLException;

public class ExerciseDBHelper {

    public static void main(String[] args) {
        ExerciseDBHelper edbh = new ExerciseDBHelper();
    }

    private static final String TABLE_NAME = "USER_EXERCISES";

    private static final String COLUMN_DATE = "DATE";
    private static final String COLUMN_INTENSITY_LEVEL = "INTENSITY_LEVEL";
    // Foreign key for stored exercises
    private static final String COLUMN_EXERCISE_ID = "EXERCISE_TYPE";
    private static final String COLUMN_MINUTES_EXERCISE = "MINUTES_EXERCISED";

    private Database db;

    public ExerciseDBHelper(){
        try{
            db = Database.getInstance();

            String createDBIfNotExists = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME +
                                         " (__id INTEGER PRIMARY KEY AUTOINCREMENT , " +
                                         COLUMN_MINUTES_EXERCISE + " REAL , " +
                                         COLUMN_EXERCISE_ID + " INTEGER , " +
                                         COLUMN_INTENSITY_LEVEL + " INTEGER , " +
                                         COLUMN_DATE + " TEXT , " +
                                         " FOREIGN KEY(" + COLUMN_EXERCISE_ID + ") REFERENCES " + ExerciseTypeDBHelper.TABLE_NAME + "(__id))";

            db.createTable(createDBIfNotExists);

        }catch (SQLException sql){
            System.out.println("Error Accessing DB");
            sql.printStackTrace();
        }catch (ClassNotFoundException cnfe){
            System.out.println("JDBC driver not found");
            cnfe.printStackTrace();
        }
    }

    public Exercise[] getDBExercises(){
        return null;
    }

    public boolean addExerciseToDB(Exercise ex){
        return false;
    }
}
