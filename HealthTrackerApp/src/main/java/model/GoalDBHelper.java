package model;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class GoalDBHelper {

    /**
     * Goals Table - Prefix none
     **/
    private static final String TABLE_NAME = "GOALS";
    private static final String COLUMN_GOALID = "GOALID";
    private static final String COLUMN_NAME = "NAME";
    private static final String COLUMN_GOALTYPE = "GOALTYPE";
    private static final String COLUMN_DATESTART = "DATE_START";
    private static final String COLUMN_DATEEND = "DATE_END";

    /**
     * Goals - User Table - Prefix GU
     **/
    private static final String GUTABLE_NAME = "USERS_GOALS";
    private static final String GUCOLUMN_USERID = "USERID";
    private static final String GUCOLUMN_GOALID = "GOALID";

    /**
     * Goals - Groups - Prefix GG
     **/
    private static final String GGTABLE_NAME = "GROUP_GOALS";
    private static final String GGCOLUMN_GROUPID = "GROUPID";
    private static final String GGCOLUMN_GOALID = "GOALID";

    private Database db;

    public GoalDBHelper() {
        try {
            db = Database.getInstance();

            //Create Goals Database Table
            String createGoalsTableSQL = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " (" + COLUMN_GOALID + " INTEGER PRIMARY KEY AUTOINCREMENT , " + COLUMN_NAME + " TEXT ,  " + COLUMN_GOALTYPE + " TEXT , " + COLUMN_DATESTART + " DATE , "+ COLUMN_DATEEND + " DATE);";
            System.out.println(createGoalsTableSQL);
            db.createTable(createGoalsTableSQL);

            //Create User Goals link table
            String createGUTableSQL = "CREATE TABLE IF NOT EXISTS " + GUTABLE_NAME + " (" + GUCOLUMN_USERID + " INTEGER NOT NULL REFERENCES USERS(__id) , " + GUCOLUMN_GOALID + " INTEGER NOT NULL REFERENCES GOALS(__id), PRIMARY KEY(" + GUCOLUMN_USERID + "," + GUCOLUMN_GOALID + "));";
            System.out.println(createGUTableSQL);
            db.createTable(createGUTableSQL);

            //Create Group Goals link table
            String createGGTableSQL = "CREATE TABLE IF NOT EXISTS " + GGTABLE_NAME + " (" + GGCOLUMN_GROUPID + " INTEGER NOT NULL REFERENCES GROUPS(ID) , " + GGCOLUMN_GOALID + " INTEGER NOT NULL REFERENCES GOALS(__id), PRIMARY KEY(" + GGCOLUMN_GROUPID + "," + GGCOLUMN_GOALID + "));";
            System.out.println(createGGTableSQL);
            db.createTable(createGGTableSQL);

        } catch (SQLException sql) {
            System.out.println("Error Accessing DB");
            sql.printStackTrace();
        } catch (ClassNotFoundException cnfe) {
            System.out.println("JDBC driver not found");
            cnfe.printStackTrace();
        }
    }

    public static void main(String[] args) {
        GoalDBHelper GDBH = new GoalDBHelper();
    }

}