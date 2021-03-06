package model;



import controllers.Email;

import java.sql.Array;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;

public class GoalDBHelper {
    GroupDBHelper groupDBHelper = new GroupDBHelper();
    UserDBHelper userDBHelper = new UserDBHelper();
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
    private static final String COLUMN_COPIEDFROM = "COPIED_FROM";

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
            String createGoalsTableSQL = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " (" + COLUMN_GOALID + " INTEGER PRIMARY KEY AUTOINCREMENT , " + COLUMN_NAME + " TEXT ,  " + COLUMN_GOALTYPE + " TEXT , " + COLUMN_PROGRESS + " REAL , "+ COLUMN_TARGET + " REAL , " + COLUMN_DATESTART + " DATE , "+ COLUMN_DATEEND + " DATE, " + COLUMN_COPIEDFROM + " INTEGER);";
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

    public boolean removeGoal(int goalID){
        String deleteUserGoalSQL = "DELETE FROM " + GUTABLE_NAME + " WHERE " + GUCOLUMN_GOALID + " = " + goalID + ";";
        try {
            db.deleteData(deleteUserGoalSQL);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        deleteUserGoalSQL = "DELETE FROM " + TABLE_NAME + " WHERE " + COLUMN_GOALID + " = " + goalID + ";";
        try {
            db.deleteData(deleteUserGoalSQL);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;

    }

    public boolean addGoal(String name, Goal.goal goalType, float progress, float target, String userID, int copiedFrom){
        try {
            String sql = "INSERT INTO GOALS(NAME,GOALTYPE,PROGRESS,TARGET,DATE_START,DATE_END,COPIED_FROM)" +
                    " VALUES(" + '"' + name + '"' + ", "
                    + '"' + goalType + '"' + ", "
                    + '"' + progress + '"' + ", "
                    + '"' + target + '"' + ", "
                    + '"' + LocalDate.now() + '"' + ", "
                    + '"' + LocalDate.now() + '"' + ", "
                    + '"' + copiedFrom + '"' + ");";;
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

    public boolean addGoal(String name, Goal.goal goalType, float progress, float target, LocalDate startDate, LocalDate endDate, String userID, int copiedFrom){
        try {
            String sql = "INSERT INTO GOALS(NAME,GOALTYPE,PROGRESS,TARGET,DATE_START,DATE_END,COPIED_FROM)" +
                    " VALUES(" + '"' + name + '"' + ", "
                    + '"' + goalType + '"' + ", "
                    + '"' + progress + '"' + ", "
                    + '"' + target + '"' + ", "
                    + '"' + startDate + '"' + ", "
                    + '"' + endDate + '"' + ", "
                    + '"' + copiedFrom + '"' + ");";
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

    /**
     * MAKE SURE USER HASNT GOT GOAL BEFROE CALLING
     * @param goal
     * @param userID
     */
    public void dupeGroupGoal(Goal goal, Integer userID){
        if(goal.getDateStart().toString().equals("0")){
            addGoal(goal.getName(), goal.getGoalType(), goal.getProgress(), goal.getTarget(), userID.toString(), goal.getGoalID());
        }
        else {
            addGoal(goal.getName(), goal.getGoalType(), goal.getProgress(), goal.getTarget(), goal.getDateStart(), goal.getDateEnd(), userID.toString(), goal.getGoalID());
        }
        System.out.println("Hi");
    }

    /** Method to update goal
     *
     * @param goalID An individual goal to update.
     * @param progress The progress to store, must already be added/subtracted.
     */


    public void updateGoalProgress(int goalID, float progress){

        String getCurrentProgressSQL = "SELECT * FROM " + TABLE_NAME + " WHERE " + COLUMN_GOALID + " = " + goalID + ";";
        System.out.println(getCurrentProgressSQL);
        float prevProgress = 0,target = 0;
        try {
            ResultSet results = db.selectQuery(getCurrentProgressSQL);
            prevProgress = results.getFloat(COLUMN_PROGRESS);
            target = results.getFloat(COLUMN_TARGET);
            results.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }


        String updateGoalSQL = "UPDATE " + TABLE_NAME + " SET " + COLUMN_PROGRESS + " = " + progress + " WHERE " + COLUMN_GOALID + " = " + goalID + ";";
        System.out.println(updateGoalSQL);
        try {
            db.updateTable(updateGoalSQL);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        /** Can use this to prompt users to make new goal**/
        if(isGoalComplete(goalID,prevProgress)){
            //checks group goal, if so alerts members.
            if(getGoalByID(goalID).getCopiedID() != 0){
                int goalIDOfGroup = getGoalByID(goalID).getCopiedID();
                int groupID = getGroupViaGoal(goalIDOfGroup);

                ArrayList<Integer> userIds = groupDBHelper.getUserIDsSubbed(groupID);

                ArrayList<String> usersEmails = new ArrayList<>();

                for(int x=0;x<userIds.size();x++){
                    usersEmails.add(userDBHelper.getUserViaID(userIds.get(x)).getEmail());
                }

                String[] userAddresses = new String[usersEmails.size()];

                for(int x=0;x<userIds.size();x++){
                    userAddresses[x] = usersEmails.get(x);
                    System.out.println(usersEmails);
                }


                Email.emailGroupAboutGoalBeingMet(User.getLoggedIn(),groupDBHelper.getGroup(groupID),userAddresses);

            }
            //check if group goal
            //Send email. List of group members emails, user and group and the goal they are in.
        }

    }

    public boolean isGoalComplete(int goalID, float prevProgress){

        String getGroupNameSQL = "SELECT * FROM " + TABLE_NAME + " WHERE " + COLUMN_GOALID + " = " + goalID + ";";
        System.out.println(getGroupNameSQL);
        float progress = 0,target = 0;
        String goaltype = "";
        try {
            ResultSet results = db.selectQuery(getGroupNameSQL);
            goaltype = results.getString(COLUMN_GOALTYPE);
            progress = results.getFloat(COLUMN_PROGRESS);
            target = results.getFloat(COLUMN_TARGET);

            results.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }


        Goal.goal gt = Goal.goal.valueOf(goaltype);


        if((prevProgress < target && gt.equals(Goal.goal.WEIGHT)) || gt.equals(Goal.goal.STEPS) || gt.equals(Goal.goal.DIET) ){
            if(progress >= target){
                return true;
            }
            else{
                return false;
            }
        }
        else {
            if(progress <= target){
                return true;
            }
            else{
                return false;
            }
        }
    }


    public boolean userHasGoal(int goalID,int userID) {
        ArrayList<Integer> goalIDs = new ArrayList<>();

        String getGoalIDsSQL = "SELECT " + GUCOLUMN_GOALID + " FROM " + GUTABLE_NAME + " WHERE " + GUCOLUMN_USERID + " = " + userID + ";";
        try {
            ResultSet results = db.selectQuery(getGoalIDsSQL);

            while (results.next()) {
                int gID = results.getInt(GGCOLUMN_GOALID);
                goalIDs.add(gID);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }


        boolean ret = false;

        for(int x=0; x< goalIDs.size();x++){
            String getGoalIDSQL = "SELECT " + COLUMN_NAME + " FROM " + TABLE_NAME + " WHERE " + COLUMN_GOALID + " = " + goalIDs.get(x) + " AND " + COLUMN_COPIEDFROM + " = " + goalID + ";";
            System.out.println(getGoalIDSQL);
            try {
                ResultSet results = db.selectQuery(getGoalIDSQL);
                if (!results.isBeforeFirst()) {
                    ret = false;
                }
                else{
                    return true;
                }
                results.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return ret;
    }

    public void removeGroupGoal(int goalID, int groupID){

        String deleteGroupGoalSQL = "DELETE FROM " + GGTABLE_NAME + " WHERE " + GGCOLUMN_GROUPID + " = " + groupID + " AND " + GGCOLUMN_GOALID + " = " + goalID + ";";
        try {
            db.deleteData(deleteGroupGoalSQL);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        String removeGroupGoalFromGoalsSQL = "UPDATE " + TABLE_NAME + " SET " + COLUMN_COPIEDFROM + " = " + 0 + " WHERE " + COLUMN_COPIEDFROM + " = " + goalID + ";";
        System.out.println(removeGroupGoalFromGoalsSQL);
        try {
            db.updateTable(removeGroupGoalFromGoalsSQL);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        //fetch the goalid of the group goal. treat this at the copiedfrom value to check for.
        //Xcascade removes group goal from group goal table.
        //go through the goal table and change all goals that had the copiedfrom value to 0.
    }

    public ArrayList<Goal> getGoalsByUserId(int userID){
        ArrayList<Integer> goalIDs = new ArrayList<>();
        ArrayList<Goal> userGoals = new ArrayList<>();

        String getGoalIdsSQL = "SELECT " + GUCOLUMN_GOALID +" FROM " + GUTABLE_NAME + " WHERE " + GUCOLUMN_USERID + " = " + userID + ";";
        System.out.println(getGoalIdsSQL);
        try {
            ResultSet results = db.selectQuery(getGoalIdsSQL);

            while(results.next()){
                goalIDs.add(results.getInt(GUCOLUMN_GOALID));

            }


            boolean hasDates = true;
            LocalDate startdate = null, enddate = null;

            for(Integer goalIDe : goalIDs){
                String getGoalsById = "SELECT * FROM " + TABLE_NAME + " WHERE " + COLUMN_GOALID + " = " + goalIDe + ";";
                results = db.selectQuery(getGoalsById);
                while (results.next()) {
                    int id = results.getInt(COLUMN_GOALID);
                    String name = results.getString(COLUMN_NAME);
                    String goaltype = results.getString(COLUMN_GOALTYPE);
                    float progress = results.getFloat(COLUMN_PROGRESS);
                    float target = results.getFloat(COLUMN_TARGET);
                    if (results.getString(COLUMN_DATESTART).equals("0")) {
                        hasDates = false;
                    } else {
                        startdate = LocalDate.parse(results.getString(COLUMN_DATESTART));
                    }
                    if (results.getString(COLUMN_DATEEND).equals("0")) {
                        hasDates = false;
                    } else {
                        enddate = LocalDate.parse(results.getString(COLUMN_DATEEND));
                    }
                    int copiedfrom = results.getInt(COLUMN_COPIEDFROM);

                    Goal.goal gt = Goal.goal.valueOf(goaltype);
                    if (hasDates) {
                        userGoals.add(new Goal(id, name, gt, progress, target, startdate, enddate, copiedfrom));

                    }
                    else{
                        userGoals.add(new Goal(id, name, gt, progress, target, copiedfrom));
                    }
                }

            }
            results.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return userGoals;
    }

    /** SEEMS TO WORK, ONLY IF GOALS ARE SAVED AS 0 IN DATE IF THEY DON'T USE THEM **/
    public ArrayList<Goal> getGoalsByGroupId(int groupID){
        ArrayList<Integer> goalIDs = new ArrayList<>();
        ArrayList<Goal> groupGoals = new ArrayList<>();

        String getGoalIDsSQL = "SELECT " + GGCOLUMN_GOALID + " FROM " + GGTABLE_NAME + " WHERE "+ GGCOLUMN_GROUPID + " = " + groupID + ";";
        try {
            ResultSet results = db.selectQuery(getGoalIDsSQL);
            while (results.next()) {
                int goalID = results.getInt(GGCOLUMN_GOALID);
                goalIDs.add(goalID);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        for(int x=0; x< goalIDs.size();x++){
            groupGoals.add(getGoalByID(goalIDs.get(x)));
        }

        return groupGoals;
    }

    public int getGroupViaGoal(int goalID){
        String getGoalIDsSQL = "SELECT " + GGCOLUMN_GROUPID + " FROM " + GGTABLE_NAME + " WHERE "+ GGCOLUMN_GOALID + " = " + goalID + ";";
        int groupID = 0;
        try {
            ResultSet results = db.selectQuery(getGoalIDsSQL);
            groupID = results.getInt(GGCOLUMN_GROUPID);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return groupID;
    }


    public Goal getGoalByID(int goalID){

        String getGoalSQL = "SELECT * FROM " + TABLE_NAME + " WHERE " + COLUMN_GOALID + " = " + goalID + ";";
        System.out.println(getGoalSQL);
        try {
            ResultSet results = db.selectQuery(getGoalSQL);

            boolean hasDates = true;
            LocalDate startdate = null,enddate = null;

            while (results.next()) {
                int id = results.getInt(COLUMN_GOALID);
                String name = results.getString(COLUMN_NAME);
                String goaltype = results.getString(COLUMN_GOALTYPE);
                float progress = results.getFloat(COLUMN_PROGRESS);
                float target = results.getFloat(COLUMN_TARGET);
                if(results.getString(COLUMN_DATESTART).equals("0")){
                    hasDates = false;
                }
                else {
                    startdate = LocalDate.parse(results.getString(COLUMN_DATESTART));
                }
                if(results.getString(COLUMN_DATEEND).equals("0")){
                    hasDates = false;
                }
                else {
                    enddate = LocalDate.parse(results.getString(COLUMN_DATEEND));
                }
                int copiedfrom = results.getInt(COLUMN_COPIEDFROM);

                Goal.goal gt = Goal.goal.valueOf(goaltype);

                if (hasDates) {
                    return new Goal(id, name, gt, progress, target, startdate, enddate, copiedfrom);
                }
                else{
                    return new Goal(id, name, gt, progress, target, copiedfrom);
                }
            }

            results.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }


    public boolean addGroupGoal(String name, Goal.goal goalType, float progress, float target, LocalDate startDate, LocalDate endDate, String groupID, int copiedFrom){
        try {
            String sql = "INSERT INTO GOALS(NAME,GOALTYPE,PROGRESS,TARGET,DATE_START,DATE_END,COPIED_FROM)" +
                    " VALUES(" + '"' + name + '"' + ", "
                    + '"' + goalType + '"' + ", "
                    + '"' + progress + '"' + ", "
                    + '"' + target + '"' + ", "
                    + '"' + startDate + '"' + ", "
                    + '"' + endDate + '"' + ", "
                    + '"' + copiedFrom + '"' + ");";
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

    public int getNumberOfGoalsForAGroup(int groupID){
        ArrayList<Integer> goalIDs = new ArrayList<>();

        String getGoalIDsSQL = "SELECT " + GGCOLUMN_GOALID + " FROM " + GGTABLE_NAME + " WHERE "+ GGCOLUMN_GROUPID + " = " + groupID + ";";
        try {
            ResultSet results = db.selectQuery(getGoalIDsSQL);
            while (results.next()) {
                int goalID = results.getInt(GGCOLUMN_GOALID);
                goalIDs.add(goalID);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return goalIDs.size();
    }


    public static void main(String[] args) {
        GoalDBHelper GDBH = new GoalDBHelper();
/*
        ArrayList<Goal> g = GDBH.getGoalsByGroupId(1);

        for(int x=0; x<g.size(); x++){
            System.out.println(g.get(x).getGoalID()+ ": " + g.get(x).getName());
        }

        GDBH.dupeGroupGoal(g.get(2), 23);
        System.out.println("Group size: " + GDBH.getNumberOfGoalsForAGroup(1));
*/
        GDBH.updateGoalProgress(26,19.05088F);
/*
        System.out.println("is " + GDBH.userHasGoal(3,23));
        System.out.println(GDBH.userHasGoal(3,24));
        */
    }

}