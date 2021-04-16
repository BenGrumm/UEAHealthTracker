package model;

import java.sql.ResultSet;
import java.sql.SQLException;

public class GroupDBHelper {

    /** Main Group Table - Prefix None **/
    private static final String TABLE_NAME = "GROUPS";
    private static final String COLUMN_ID = "ID";
    private static final String COLUMN_NAME = "NAME";
    private static final String COLUMN_DESCRIPTION = "DESCRIPTION";
    private static final String COLUMN_SIZE = "SIZE";
    private static final String COLUMN_INVCODE = "CODE";

    /** Group Link Table With Users - Prefix LU **/
    private static final String LUTABLE_NAME = "GROUP_MEMBERS";
    private static final String LUCOLUMN_GROUPID = "GROUPID";
    private static final String LUCOLUMN_USERID = "USERID";
    private static final String LUCOLUMN_ROLE = "ROLE";

    /** Group and Goals Linked - Prefix LG **/


    private Database db;

    public GroupDBHelper(){
        try{
            db = Database.getInstance();

            //Groups Table
            String createGroupTableSQL = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " (" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT , " + COLUMN_NAME + " TEXT , " + COLUMN_DESCRIPTION+ " TEXT , " + COLUMN_SIZE + " INTEGER , " + COLUMN_INVCODE + " TEXT );";
            System.out.println(createGroupTableSQL);
            db.createTable(createGroupTableSQL);

            //Group and Users Link Table
            String createLUTableSQL = "CREATE TABLE IF NOT EXISTS " + LUTABLE_NAME + " (" + LUCOLUMN_GROUPID + " INTEGER NOT NULL REFERENCES GROUPS(ID) , " + LUCOLUMN_USERID + " INTEGER NOT NULL REFERENCES USERS(__id) , " + LUCOLUMN_ROLE + ", PRIMARY KEY("+ LUCOLUMN_GROUPID +","+ LUCOLUMN_USERID +"));";
            System.out.println(createLUTableSQL);
            db.createTable(createLUTableSQL);

            //Group and Goals Link Table
            ////TO ADD


        }catch (SQLException sql){
            System.out.println("Error Accessing DB");
            sql.printStackTrace();
        }catch (ClassNotFoundException cnfe){
            System.out.println("JDBC driver not found");
            cnfe.printStackTrace();
        }
    }

    /** GROUP TABLE METHODS **/
    /**
     * This method is used to add a group into the database.
     * @param name Groups Name
     * @param desc Groups Description
     * @param size Groups size, should be initialised as 1.
     * @param invCode - should be checked to be unique prior to saving.
     * @param userID - ID of user creating group
     * @return saves to database, no return
     */
    public void addGroup(String name, String desc, int size,String invCode, int userID){
        try {
            // Input data into query removing any quotes in the description of exercise
            String addGroupSQL = "INSERT INTO " + TABLE_NAME + " (" + COLUMN_NAME + " , " + COLUMN_DESCRIPTION + " , " + COLUMN_SIZE + " , " + COLUMN_INVCODE + " ) VALUES( \"" + name + "\" , \"" + desc + "\" , " + size + " , \"" + invCode + "\" )";
            System.out.println(addGroupSQL);
            db.insertData(addGroupSQL);
        }catch (SQLException e){
            e.printStackTrace();
        }
        Integer groupID = 0;

        //Gets the ID
        String getGroupNameSQL = "SELECT " + COLUMN_ID +  " FROM " + TABLE_NAME + " WHERE " + COLUMN_NAME + " = \"" + name + "\";";
        try {
            ResultSet results = db.selectQuery(getGroupNameSQL);
            groupID = results.getInt(COLUMN_ID);
            results.close();
        }
        catch(SQLException e){
            e.printStackTrace();
        }

        if(groupID == 0){
            System.out.println("ERROR");
        }
        else{
            String setGroupOwnerSQL = "INSERT INTO " + LUTABLE_NAME + " (" + LUCOLUMN_GROUPID + " , " + LUCOLUMN_USERID + " , " + LUCOLUMN_ROLE + " ) VALUES( " + groupID + " , " + userID  + " , \"OWNER\" )";
            try {
                System.out.println(setGroupOwnerSQL);
                db.insertData(setGroupOwnerSQL);
            }
            catch(SQLException e){
                e.printStackTrace();
            }
        }




    }

    // Method to get NAME from database
    //Using ID
    public String getGroupName(int ID){
        String groupName = "";
        String getGroupNameSQL = "SELECT " + COLUMN_NAME + " FROM " + TABLE_NAME + " WHERE " + COLUMN_ID + " = " + ID + ";";
        try {
            ResultSet results = db.selectQuery(getGroupNameSQL);
            if (results.isBeforeFirst()) {
                groupName = results.getString(COLUMN_NAME);
            }
            else{
                System.out.println("AN ERROR HAS OCCURRED");
            }
            results.close();
        }
        catch(SQLException e){
            e.printStackTrace();
        }
        return groupName;
    }

    /**
     * This method is used to determine if a group name already exists in the database.
     * @param name Groups Name to check
     * @return Boolean, true if it does, false if it doesn't
     */
    public boolean doesGroupNameExist(String name){
        String getGroupNameSQL = "SELECT " + COLUMN_ID +  " FROM " + TABLE_NAME + " WHERE " + COLUMN_NAME + " = \"" + name + "\";";
        System.out.println(getGroupNameSQL);
        try {
            ResultSet results = db.selectQuery(getGroupNameSQL);
            if(!results.isBeforeFirst()){
                return false;
            }
            results.close();
        }
        catch(SQLException e){
            e.printStackTrace();
        }
        return true;
    }

    /**
     * This method is used to determine if an invite code already exists in the database.
     * @param invCode invite code to check
     * @return Boolean, true if it does, false if it doesn't
     */
    public boolean doesGroupInvCodeExist(String invCode){
        String getGroupNameSQL = "SELECT " + COLUMN_ID +  " FROM " + TABLE_NAME + " WHERE " + COLUMN_INVCODE + " = \"" + invCode + "\";";
        System.out.println(getGroupNameSQL);
        try {
            ResultSet results = db.selectQuery(getGroupNameSQL);
            if(!results.isBeforeFirst()){
                return false;
            }
            results.close();
        }
        catch(SQLException e){
            e.printStackTrace();
        }
        return true;
    }



    // Method to get DESCRIPTION from database

    // Method to get SIZE from database

    // Method to INCREMENT SIZE of a group

    // Method to DECREMENT SIZE of group

    // Method to DELETE GROUP

    /** Group and User Table Methods **/

    /** Group invite Codes Methods**/
    //Check if code exist

    //Add invite code

    //remove invite code



}
