package model;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class ExerciseDBHelper {

    private static final String TABLE_NAME = "USER_EXERCISES";

    private static final String COLUMN_ID = "__id";
    private static final String COLUMN_DATE = "DATE";
    private static final String COLUMN_CALORIES_BURNED = "CALORIES_BURNED";
    // Foreign key for stored exercises
    private static final String COLUMN_EXERCISE_ID = "EXERCISE_TYPE";
    private static final String COLUMN_MINUTES_EXERCISE = "MINUTES_EXERCISED";
    // Foreign key for logged in user
    private static final String COLUMN_USER_ID = "USER_ID";

    private Database db;

    public ExerciseDBHelper(){
        try{
            // TODO assuming this can never be called when a user is not stored in current user
            // remove this for
//            User testUser = new User(1, "Caitlin", "Wright", "cwright",
//                    "17cwright@gmail.com", "123", 10, 10, 10, 10,
//                    10, 10, "Female");
//            User.setLoggedIn(testUser);

            db = Database.getInstance();

            String createDBIfNotExists = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME +
                    " (" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT , " +
                    COLUMN_MINUTES_EXERCISE + " REAL , " +
                    COLUMN_EXERCISE_ID + " INTEGER , " +
                    COLUMN_CALORIES_BURNED + " REAL , " +
                    COLUMN_DATE + " DATE , " +
                    COLUMN_USER_ID + " INTEGER , " +
                    " FOREIGN KEY(" + COLUMN_EXERCISE_ID + ") REFERENCES " +
                    ExerciseTypeDBHelper.TABLE_NAME + "(__id) , " +
                    " FOREIGN KEY(" + COLUMN_USER_ID + ") REFERENCES " +
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

    public Exercise[] getDBExercises(){
        return null;
    }

    private static final String exerciseTableLength = "SELECT COUNT(*) AS total FROM " + TABLE_NAME;
    /**
     * Function that uses final string containing select and count sql statement to count how many records are in the
     * users exercises table
     * @return int - return total number of records in table
     */
    public int exerciseTableLength(){
        try {
            ResultSet rs = db.selectQuery(exerciseTableLength);
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

    private static final String withinRangeSQL = "SELECT * FROM " + TABLE_NAME +
            " WHERE " + COLUMN_DATE +
            " BETWEEN '%s' AND '%s' AND " + COLUMN_USER_ID + " = %s" +
            " ORDER BY " + COLUMN_DATE + " %s;";
    /**
     * Inclusive dates
     * @param from
     * @param to
     * @return
     */
    public Exercise[] getExercisesWithinRange(LocalDate from, LocalDate to, Order order){
        try {
            String sql = String.format(withinRangeSQL, from.toString(), to.toString(), User.getLoggedIn().getID(), order.toString());
            System.out.println(sql);
            ResultSet rs = db.selectQuery(sql);

            return convertResultSetToExercise(rs);
        }catch (SQLException error){
            return null;
        }
    }

    private static final String getAllExercises = "SELECT * FROM " + TABLE_NAME +
            " WHERE " + COLUMN_USER_ID + " = %d" +
            " ORDER BY " + COLUMN_DATE + " %s";

    /**
     * Function that uses a final string containing a select everything from statement to select everything from
     * the users exercises table
     * @return Exercise[] - return everything from the users exercises table in an array
     */
    public Exercise[] getAllExercises(Order order){
        try {
            System.out.println(User.loggedIn.getID());
            String sql = String.format(getAllExercises, User.getLoggedIn().getID(), order.toString());
            System.out.println(sql);
            ResultSet rs = db.selectQuery(sql);
            return convertResultSetToExercise(rs);
        }catch (SQLException error){
            error.printStackTrace();
            return null;
        }
    }
    /**
     * Function to convert a ResultSet object to an array of Exercise objects
     * @param rs - takes a ResultSet object
     * @return Exercise - returns an array of exercise objects
     * @throws SQLException
     */
    private Exercise[] convertResultSetToExercise(ResultSet rs) throws SQLException{
        ArrayList<Exercise> exercises = new ArrayList<>();
        ArrayList<Integer> exerciseTypes = new ArrayList<>();

        while(rs.next()){
            int id = rs.getInt(COLUMN_ID);
            double minsExercised = rs.getDouble(COLUMN_MINUTES_EXERCISE);
            double caloriesBurned = rs.getDouble(COLUMN_CALORIES_BURNED);
            // SQLite doesn't have date storage so stored as TEXT
            LocalDate date = LocalDate.parse(rs.getString(COLUMN_DATE));
            exerciseTypes.add(rs.getInt(COLUMN_EXERCISE_ID));

            exercises.add(new Exercise(id, minsExercised, caloriesBurned, date));
        }

        ExerciseTypeDBHelper dbh = new ExerciseTypeDBHelper();

        for(int i = 0; i < exercises.size(); i++){
            exercises.get(i).setExerciseT(dbh.getType(exerciseTypes.get(i)));
        }

        return exercises.toArray(new Exercise[exercises.size()]);
    }


    private static final String addExercise = "INSERT INTO " + TABLE_NAME + " ("
            + COLUMN_MINUTES_EXERCISE + ", "
            + COLUMN_CALORIES_BURNED + ", "
            + COLUMN_DATE + ", "
            + COLUMN_EXERCISE_ID + ", "
            + COLUMN_USER_ID + ") VALUES(%s, %s, '%s', %s, %s)";

    /**
     * Function to add an exercise object to the users exercises table using a final string containing an insert into sql
     * statement
     * @param ex - exercise object
     */
    public boolean addExerciseToDB(Exercise ex){
        try {
            User.getLoggedIn();
            ex.getExerciseT().getDbID();
            String sql = String.format(addExercise, ex.getMinutesExercised(), ex.getCaloriesBurned(), ex.getDate(),
                    ex.getExerciseT().getDbID(), User.getLoggedIn().getID());
            System.out.println(sql);
            db.insertData(sql);
            return true;
        }catch (SQLException sqle){
            sqle.printStackTrace();
            return false;
        }
    }

    private static String removeExercise = "DELETE FROM " + TABLE_NAME + " WHERE " + COLUMN_ID + " = %d;";

    /**
     * Function to remove an exercise from the database
     * @param exercise to remove
     */
    public void removeExerciseFromDB(Exercise exercise){
        try {
            String deleteSql = String.format(removeExercise, exercise.getId());
            System.out.println(deleteSql);
            db.deleteData(deleteSql);
        }catch (SQLException sqle){
            sqle.printStackTrace();
        }
    }

    // Test harness
    public static void main(String[] args) {
        ExerciseDBHelper edbh = new ExerciseDBHelper();
        ExerciseType testExerciseType = new ExerciseType(1, "Run", 30);
        Exercise testExercise = new Exercise(1, 60, 90, testExerciseType, LocalDate.now());
        edbh.addExerciseToDB(testExercise);
        edbh.getAllExercises(Order.ASC);
        edbh.exerciseTableLength();
        edbh.getExercisesWithinRange(LocalDate.now().minusDays(100), LocalDate.now(), Order.ASC);
    }
}
