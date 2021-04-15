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

    /** Group Link Table With Users - Prefix LU **/
    private static final String LUTABLE_NAME = "GROUP_MEMBERS";
    private static final String LUCOLUMN_GROUPID = "GROUPID";
    private static final String LUCOLUMN_USERID = "USERID";
    private static final String LUCOLUMN_ROLE = "ROLE";

    /** Group Invite Codes - Prefix LI **/
    private static final String LITABLE_NAME = "GROUP_INVITE_CODES";
    private static final String LICOLUMN_INVCODE = "CODE";
    private static final String LICOLUMN_GROUPID = "GROUPID";

    /** Group and Goals Linked - Prefix LG **/


    private Database db;

    public GroupDBHelper(){
        try{
            db = Database.getInstance();

            //Groups Table
            String createGroupTableSQL = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " (" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT , " + COLUMN_NAME + " TEXT , " + COLUMN_DESCRIPTION+ " TEXT , " + COLUMN_SIZE + " INTEGER);";
            System.out.println(createGroupTableSQL);
            db.createTable(createGroupTableSQL);

            //Group and Users Link Table
            String createLUTableSQL = "CREATE TABLE IF NOT EXISTS " + LUTABLE_NAME + " (" + LUCOLUMN_GROUPID + " INTEGER NOT NULL REFERENCES GROUPS(ID) , " + LUCOLUMN_USERID + " INTEGER NOT NULL REFERENCES USERS(__id) , " + LUCOLUMN_ROLE + ", PRIMARY KEY("+ LUCOLUMN_GROUPID +","+ LUCOLUMN_USERID +"));";
            System.out.println(createLUTableSQL);
            db.createTable(createLUTableSQL);

            //Group Invite Codes
            String createLITableSQL = "CREATE TABLE IF NOT EXISTS " + LITABLE_NAME + " (" + LICOLUMN_INVCODE +" INTEGER PRIMARY KEY, " + LICOLUMN_GROUPID + " INTERGER NOT NULL REFERENCES GROUPS(ID));";
            System.out.println(createLITableSQL);
            db.createTable(createLITableSQL);

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
     * @return ciphertext that is the encrypted version of the input information.
     */
    public int addGroup(String name, String desc, int size){
        try {
            // Input data into query removing any quotes in the description of exercise
            String addGroupSQL = "INSERT INTO " + TABLE_NAME + " (" + COLUMN_NAME + " , " + COLUMN_DESCRIPTION + " , " + COLUMN_SIZE + " ) VALUES( \"" + name + "\" , \"" + desc + "\" , " + size + " )";
            System.out.println(addGroupSQL);
            db.insertData(addGroupSQL);
        }catch (SQLException e){
            e.printStackTrace();
        }
        Integer groupID = 0;

        String getGroupNameSQL = "SELECT " + COLUMN_ID +  " FROM " + TABLE_NAME + " WHERE " + COLUMN_NAME + " = \"" + name + "\";";
        try {
            ResultSet results = db.selectQuery(getGroupNameSQL);
            groupID = results.getInt(COLUMN_ID);
            results.close();
        }
        catch(SQLException e){
            e.printStackTrace();
        }
        return groupID;
    }

    // Method to get NAME from database
    //Using ID
    public String getGroupName(int ID){
        String groupName = "";
        String getGroupNameSQL = "SELECT " + COLUMN_NAME +  " FROM " + TABLE_NAME + " WHERE " + COLUMN_ID + " = " + ID + ";";
        try {
            ResultSet results = db.selectQuery(getGroupNameSQL);
            groupName = results.getString(COLUMN_NAME);
            results.close();
        }
        catch(SQLException e){
            e.printStackTrace();
        }
        return groupName;
    }

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
