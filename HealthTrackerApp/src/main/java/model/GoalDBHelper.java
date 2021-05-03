package model;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;

public class GoalDBHelper {

    /**
     * Goals Table - Prefix none
     **/
    private static final String TABLE_NAME = "GOALS";
    private static final String COLUMN_GOALID = "GOALID";
    private static final String COLUMN_NAME = "NAME";
    private static final String COLUMN_GOALTYPE = "GOALTYPE";
    private static final String COLUMN_PROGRESS = "PROGRESS";
    private static final String COLUMN_TARGET = "TARGET";
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
            String createGoalsTableSQL = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " (" + COLUMN_GOALID + " INTEGER PRIMARY KEY AUTOINCREMENT , " + COLUMN_NAME + " TEXT ,  " + COLUMN_GOALTYPE + " TEXT , " + COLUMN_PROGRESS + " REAL , "+ COLUMN_TARGET + " REAL , " + COLUMN_DATESTART + " DATE , "+ COLUMN_DATEEND + " DATE);";
            System.out.println(createGoalsTableSQL);
            db.createTable(createGoalsTableSQL);

            //Create User Goals link table
            String createGUTableSQL = "CREATE TABLE IF NOT EXISTS " + GUTABLE_NAME + " (" + GUCOLUMN_USERID + " INTEGER NOT NULL REFERENCES USERS(__id) , " + GUCOLUMN_GOALID + " INTEGER NOT NULL REFERENCES GOALS(GOALID), PRIMARY KEY(" + GUCOLUMN_USERID + "," + GUCOLUMN_GOALID + "));";
            System.out.println(createGUTableSQL);
            db.createTable(createGUTableSQL);

            //Create Group Goals link table
            String createGGTableSQL = "CREATE TABLE IF NOT EXISTS " + GGTABLE_NAME + " (" + GGCOLUMN_GROUPID + " INTEGER NOT NULL REFERENCES GROUPS(ID) , " + GGCOLUMN_GOALID + " INTEGER NOT NULL REFERENCES GOALS(GOALID), PRIMARY KEY(" + GGCOLUMN_GROUPID + "," + GGCOLUMN_GOALID + "));";
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

    public boolean addGoal(String name, Goal.goal goalType, float progress, float target, LocalDate startDate, LocalDate endDate, String userID){
        try {
            String sql = "INSERT INTO GOALS(NAME,GOALTYPE,PROGRESS,TARGET,DATE_START,DATE_END)" +
                    " VALUES(" + '"' + name + '"' + ", "
                    + '"' + goalType + '"' + ", "
                    + '"' + progress + '"' + ", "
                    + '"' + target + '"' + ", "
                    + '"' + startDate + '"' + ", "
                    + '"' + endDate + '"' + ");";
            db.insertData(sql);

        }catch (SQLException e){
            e.printStackTrace();
        }
        try{
            String getGoalIdSQL = "SELECT * FROM GOALS WHERE GOALID=(SELECT max(GOALID) FROM GOALS);";
            String goalID = "";
            ResultSet results = db.selectQuery(getGoalIdSQL);
            goalID = String.valueOf(results.getInt("GOALID"));
            results.close();
            String sql = "INSERT INTO USERS_GOALS(USERID,GOALID)" +
                    " VALUES(" + '"' + userID + '"' + ", "
                    + '"' + goalID + '"' + ");";
            db.insertData(sql);
        }catch(SQLException e){

            e.printStackTrace();
        }
        return true;
    }

    public boolean addGroupGoal(String name, Goal.goal goalType, float progress, float target, LocalDate startDate, LocalDate endDate, String groupID){
        try {
            String sql = "INSERT INTO GOALS(NAME,GOALTYPE,PROGRESS,TARGET,DATE_START,DATE_END)" +
                    " VALUES(" + '"' + name + '"' + ", "
                    + '"' + goalType + '"' + ", "
                    + '"' + progress + '"' + ", "
                    + '"' + target + '"' + ", "
                    + '"' + startDate + '"' + ", "
                    + '"' + endDate + '"' + ");";
            db.insertData(sql);

        }catch (SQLException e){
            e.printStackTrace();
        }
        try{
            String getGoalIdSQL = "SELECT * FROM GOALS WHERE GOALID=(SELECT max(GOALID) FROM GOALS);";
            String goalID = "";
            ResultSet results = db.selectQuery(getGoalIdSQL);
            goalID = String.valueOf(results.getInt("GOALID"));
            results.close();
            String sql = "INSERT INTO GROUP_GOALS(GROUPID,GOALID)" +
                    " VALUES(" + '"' + groupID + '"' + ", "
                    + '"' + goalID + '"' + ");";
            db.insertData(sql);
        }catch(SQLException e){
            e.printStackTrace();
        }
        return true;
    }

    public static void main(String[] args) {
        GoalDBHelper GDBH = new GoalDBHelper();
    }

}