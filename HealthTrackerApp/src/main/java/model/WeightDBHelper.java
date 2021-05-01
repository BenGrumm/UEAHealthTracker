package model;

import java.sql.SQLException;

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

    public static void main(String[] args) {
        WeightDBHelper WDBH = new WeightDBHelper();
    }

}