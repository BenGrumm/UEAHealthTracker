package model;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class ExerciseDBHelper {

    public static void main(String[] args) {
        ExerciseDBHelper edbh = new ExerciseDBHelper();
    }

    public static void populateRandomData(){

    }

    private static final String TABLE_NAME = "USER_EXERCISES";

    private static final String COLUMN_ID = "__id";
    private static final String COLUMN_DATE = "DATE";
    private static final String COLUMN_CALORIES_BURNED = "CALORIES_BURNED";
    // Foreign key for stored exercises
    private static final String COLUMN_EXERCISE_ID = "EXERCISE_TYPE";
    private static final String COLUMN_MINUTES_EXERCISE = "MINUTES_EXERCISED";

    private Database db;

    public ExerciseDBHelper(){
        try{
            db = Database.getInstance();

            String createDBIfNotExists = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME +
                                         " (" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT , " +
                                         COLUMN_MINUTES_EXERCISE + " REAL , " +
                                         COLUMN_EXERCISE_ID + " INTEGER , " +
                                         COLUMN_CALORIES_BURNED + " REAL , " +
                                         COLUMN_DATE + " TEXT , " +
                                         " FOREIGN KEY(" + COLUMN_EXERCISE_ID + ") REFERENCES " +
                                         ExerciseTypeDBHelper.TABLE_NAME + "(__id))";
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

    private static final String withinRangeSQL = "SELECT * FROM " + TABLE_NAME + " WHERE " + COLUMN_DATE + " BETWEEN '%s' AND '%s' ORDER BY " + COLUMN_DATE + " ASC;";

    /**
     * Inclusive dates
     * @param from
     * @param to
     * @return
     */
    public Exercise[] getExercisesWithinRange(LocalDate from, LocalDate to){
        try {
            ResultSet rs = db.selectQuery(String.format(withinRangeSQL, from.toString(), to.toString()));

            return convertResultSetToExercise(rs);
        }catch (SQLException error){
            return null;
        }
    }

    private static final String getAllExercises = "SELECT * FROM " + TABLE_NAME + " ORDER BY " + COLUMN_DATE + " ASC";
    public Exercise[] getAllExercises(){
        try {
            ResultSet rs = db.selectQuery(getAllExercises);

            return convertResultSetToExercise(rs);
        }catch (SQLException error){
            return null;
        }
    }

    public Exercise[] convertResultSetToExercise(ResultSet rs) throws SQLException{
        ArrayList<Exercise> exercises = new ArrayList<>();
        ExerciseTypeDBHelper dbh = new ExerciseTypeDBHelper();

        while(rs.next()){
            int id = rs.getInt(COLUMN_ID);
            double minsExercised = rs.getDouble(COLUMN_MINUTES_EXERCISE);
            double caloriesBurned = rs.getDouble(COLUMN_CALORIES_BURNED);
            // SQLite doesn't have date storage so stored as TEXT
            LocalDate date = LocalDate.parse(rs.getString(COLUMN_DATE));
            ExerciseType et = dbh.getType(rs.getInt(COLUMN_EXERCISE_ID));

            exercises.add(new Exercise(id, minsExercised, caloriesBurned, et, date));
        }

        return exercises.toArray(new Exercise[exercises.size()]);
    }


    private static final String addExercise = "INSERT INTO " + TABLE_NAME + " ("
            + COLUMN_MINUTES_EXERCISE + ", "
            + COLUMN_CALORIES_BURNED + ", "
            + COLUMN_DATE + ", "
            + COLUMN_EXERCISE_ID + ") VALUES(%s, %s, %s, %s)";

    public void addExerciseToDB(Exercise ex){
        try {
            String sql = String.format(addExercise, ex.getMinutesExercised(), ex.getCaloriesBurned(), ex.getDate(),
                    ex.getExerciseT().getDbID());
            System.out.println(sql);
            db.insertData(sql);
        }catch (SQLException sqle){
            sqle.printStackTrace();
        }
    }
}
