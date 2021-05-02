package model;


import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLOutput;
import java.time.LocalDate;
import java.util.ArrayList;

public class WeightDBHelper {

    /**
     * Weight Table - Prefix none
     **/
    private static final String TABLE_NAME = "WEIGHT";
    private static final String COLUMN_WEIGHTID = "WEIGHTID";
    private static final String COLUMN_USERID = "USERID";
    private static final String COLUMN_WEIGHTSTONE = "WEIGHTSTONE";
    private static final String COLUMN_WEIGHTPOUNDS = "WEIGHTPOUND";
    private static final String COLUMN_DATE = "DATE";


    private Database db;

    public WeightDBHelper() {
        try {
            db = Database.getInstance();

            //Create Goals Database Table
            String createWeightTableSQL = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " (" + COLUMN_WEIGHTID + " INTEGER PRIMARY KEY AUTOINCREMENT , " + COLUMN_USERID + " INTEGER NOT NULL REFERENCES USERS(__id) ,  " + COLUMN_WEIGHTSTONE + " INTEGER , " + COLUMN_WEIGHTPOUNDS + " INTEGER , "+ COLUMN_DATE + " DATE);";
            System.out.println(createWeightTableSQL);
            db.createTable(createWeightTableSQL);


        } catch (SQLException sql) {
            System.out.println("Error Accessing DB");
            sql.printStackTrace();
        } catch (ClassNotFoundException cnfe) {
            System.out.println("JDBC driver not found");
            cnfe.printStackTrace();
        }
    }

    //Add weight, pass USERweight

    public void addWeight(UserWeight uw, int userID) {
        try {
            // Input data into query removing any quotes in the description of exercise
            String addWeightSQL = "INSERT INTO " + TABLE_NAME + " (" + COLUMN_USERID + " , " + COLUMN_WEIGHTSTONE + " , " + COLUMN_WEIGHTPOUNDS + " , " + COLUMN_DATE + " ) VALUES( " + userID + " , " + uw.getStones() + " , " + uw.getPounds() + " , \"" + uw.getDateRecorded() + "\" )";
            System.out.println(addWeightSQL);
            db.insertData(addWeightSQL);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public ArrayList<UserWeight> getUsersWeights(int userID) {
        ArrayList<UserWeight> UsersWeights = new ArrayList<>();

        String getGroupIDSQL = "SELECT * FROM " + TABLE_NAME + " WHERE " + COLUMN_USERID + " =" + userID + ";";
        System.out.println(getGroupIDSQL);
        try {
            ResultSet results = db.selectQuery(getGroupIDSQL);

            while (results.next()) {
                int stones = results.getInt(COLUMN_WEIGHTSTONE);
                int pounds = results.getInt(COLUMN_WEIGHTPOUNDS);
                LocalDate recordedDate = LocalDate.parse(results.getString(COLUMN_DATE));
                UserWeight uw = new UserWeight(stones,pounds,recordedDate);
                UsersWeights.add(uw);
            }
            results.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return UsersWeights;
    }

    private static final String withinRangeSQL = "SELECT * FROM " + TABLE_NAME +
            " WHERE " + COLUMN_DATE +
            " BETWEEN '%s' AND '%s' AND " + COLUMN_USERID + " = %s" +
            " ORDER BY " + COLUMN_DATE + " ASC;";

    public ArrayList<UserWeight> getWeightsWithinRange(LocalDate from, LocalDate to){
        try {
            ArrayList<UserWeight> UsersWeights = new ArrayList<>();
            String sql = String.format(withinRangeSQL, from.toString(), to.toString(), User.getLoggedIn().getID());
            System.out.println(sql);
            ResultSet results = db.selectQuery(sql);

            while (results.next()) {
                int stones = results.getInt(COLUMN_WEIGHTSTONE);
                int pounds = results.getInt(COLUMN_WEIGHTPOUNDS);
                LocalDate recordedDate = LocalDate.parse(results.getString(COLUMN_DATE));
                UserWeight uw = new UserWeight(stones,pounds,recordedDate);
                UsersWeights.add(uw);
            }
            return UsersWeights;
        }catch (SQLException error){
            return null;
        }
    }



    //Get users weights and dates, pass userID

    //Testing
    public static void main(String[] args) {
        WeightDBHelper WDBH = new WeightDBHelper();
        UserWeight uw = new UserWeight(5,6);

        WDBH.addWeight(uw,1);


        ArrayList<UserWeight> uws = new ArrayList<>();
        uws = WDBH.getUsersWeights(1);

        for (UserWeight weight:uws) {
            System.out.println(weight.getStones() + "," + weight.getPounds() + "," + weight.getDateRecorded());
        }
    }



}