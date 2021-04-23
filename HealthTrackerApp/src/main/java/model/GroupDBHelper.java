package model;

import java.sql.Array;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class GroupDBHelper {

    /**
     * Main Group Table - Prefix None
     **/
    private static final String TABLE_NAME = "GROUPS";
    private static final String COLUMN_ID = "ID";
    private static final String COLUMN_NAME = "NAME";
    private static final String COLUMN_DESCRIPTION = "DESCRIPTION";
    private static final String COLUMN_SIZE = "SIZE";
    private static final String COLUMN_INVCODE = "CODE";

    /**
     * Group Link Table With Users - Prefix LU
     **/
    private static final String LUTABLE_NAME = "GROUP_MEMBERS";
    private static final String LUCOLUMN_GROUPID = "GROUPID";
    private static final String LUCOLUMN_USERID = "USERID";
    private static final String LUCOLUMN_ROLE = "ROLE";

    /**
     * Group and Goals Linked - Prefix LG
     **/


    private Database db;

    public GroupDBHelper() {
        try {
            db = Database.getInstance();

            //Groups Table
            String createGroupTableSQL = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " (" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT , " + COLUMN_NAME + " TEXT , " + COLUMN_DESCRIPTION + " TEXT , " + COLUMN_SIZE + " INTEGER , " + COLUMN_INVCODE + " TEXT );";
            System.out.println(createGroupTableSQL);
            db.createTable(createGroupTableSQL);

            //Group and Users Link Table
            String createLUTableSQL = "CREATE TABLE IF NOT EXISTS " + LUTABLE_NAME + " (" + LUCOLUMN_GROUPID + " INTEGER NOT NULL REFERENCES GROUPS(ID) , " + LUCOLUMN_USERID + " INTEGER NOT NULL REFERENCES USERS(__id) , " + LUCOLUMN_ROLE + ", PRIMARY KEY(" + LUCOLUMN_GROUPID + "," + LUCOLUMN_USERID + "));";
            System.out.println(createLUTableSQL);
            db.createTable(createLUTableSQL);

            //Group and Goals Link Table
            ////TO ADD


        } catch (SQLException sql) {
            System.out.println("Error Accessing DB");
            sql.printStackTrace();
        } catch (ClassNotFoundException cnfe) {
            System.out.println("JDBC driver not found");
            cnfe.printStackTrace();
        }
    }

    /** GROUP TABLE METHODS **/
    /**
     * This method is used to add a group into the database. It adds a group to the "GROUPS" table and a link to the
     * user that created it in the "GROUP_MEMBERS" table, giving them the OWNER role.
     *
     * @param name    Groups Name
     * @param desc    Groups Description
     * @param size    Groups size, should be initialised as 1.
     * @param invCode - should be checked to be unique prior to saving.
     * @param userID  - ID of user creating group
     * @return saves to database, no return
     */
    public void addGroup(String name, String desc, int size, String invCode, int userID) {
        try {
            // Input data into query removing any quotes in the description of exercise
            String addGroupSQL = "INSERT INTO " + TABLE_NAME + " (" + COLUMN_NAME + " , " + COLUMN_DESCRIPTION + " , " + COLUMN_SIZE + " , " + COLUMN_INVCODE + " ) VALUES( \"" + name + "\" , \"" + desc + "\" , " + size + " , \"" + invCode + "\" )";
            System.out.println(addGroupSQL);
            db.insertData(addGroupSQL);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        Integer groupID = 0;

        //Gets the ID
        String getGroupNameSQL = "SELECT " + COLUMN_ID + " FROM " + TABLE_NAME + " WHERE " + COLUMN_NAME + " = \"" + name + "\";";
        try {
            ResultSet results = db.selectQuery(getGroupNameSQL);
            groupID = results.getInt(COLUMN_ID);
            results.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        if (groupID == 0) {
            System.out.println("ERROR");
        } else {
            String setGroupOwnerSQL = "INSERT INTO " + LUTABLE_NAME + " (" + LUCOLUMN_GROUPID + " , " + LUCOLUMN_USERID + " , " + LUCOLUMN_ROLE + " ) VALUES( " + groupID + " , " + userID + " , \"OWNER\" )";
            try {
                System.out.println(setGroupOwnerSQL);
                db.insertData(setGroupOwnerSQL);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }


    }

    // Method to get NAME from database
    //Using ID
    public String getGroupName(int ID) {
        String groupName = "";
        String getGroupNameSQL = "SELECT " + COLUMN_NAME + " FROM " + TABLE_NAME + " WHERE " + COLUMN_ID + " = " + ID + ";";
        try {
            ResultSet results = db.selectQuery(getGroupNameSQL);
            if (results.isBeforeFirst()) {
                groupName = results.getString(COLUMN_NAME);
            } else {
                System.out.println("AN ERROR HAS OCCURRED");
            }
            results.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return groupName;
    }

    /**
     * This method is used to determine if a group name already exists in the database.
     *
     * @param name Groups Name to check
     * @return Boolean, true if it does, false if it doesn't
     */
    public boolean doesGroupNameExist(String name) {
        String getGroupNameSQL = "SELECT " + COLUMN_ID + " FROM " + TABLE_NAME + " WHERE " + COLUMN_NAME + " = \"" + name + "\";";
        System.out.println(getGroupNameSQL);
        try {
            ResultSet results = db.selectQuery(getGroupNameSQL);
            if (!results.isBeforeFirst()) {
                return false;
            }
            results.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return true;
    }

    /**
     * This method is used to determine if an invite code already exists in the database.
     *
     * @param invCode invite code to check
     * @return Boolean, true if it does, false if it doesn't
     */
    public boolean doesGroupInvCodeExist(String invCode) {
        String getGroupNameSQL = "SELECT " + COLUMN_ID + " FROM " + TABLE_NAME + " WHERE " + COLUMN_INVCODE + " = \"" + invCode + "\";";
        System.out.println(getGroupNameSQL);
        try {
            ResultSet results = db.selectQuery(getGroupNameSQL);
            if (!results.isBeforeFirst()) {
                return false;
            }
            results.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return true;
    }

    /**
     * This method is used to get a list of groupID's that a user belongs in.
     *
     * @param userID ID of the user
     * @return groups a list of groupID's the user is apart of
     */
    public ArrayList getUsersGroupIDs(int userID) {
        ArrayList<Integer> groupIDs = new ArrayList<>();

        String getGroupIDSQL = "SELECT " + LUCOLUMN_GROUPID + " FROM " + LUTABLE_NAME + " WHERE " + LUCOLUMN_USERID + " = " + userID + ";";
        System.out.println(getGroupIDSQL);
        try {
            ResultSet results = db.selectQuery(getGroupIDSQL);

            while (results.next()) {
                int groupID = results.getInt(LUCOLUMN_GROUPID);
                groupIDs.add(groupID);
            }

            results.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return groupIDs;
    }

    /**
     * This method is used to get a group object
     *
     * @param groupID ID of the group
     * @return groups a arraylist of group objects the user is apart of
     */
    public Group getGroup(int groupID) {

        String getGroupIDSQL = "SELECT * FROM " + TABLE_NAME + " WHERE " + COLUMN_ID + " = " + groupID + ";";
        System.out.println(getGroupIDSQL);
        try {
            ResultSet results = db.selectQuery(getGroupIDSQL);

            while (results.next()) {
                int id = results.getInt(COLUMN_ID);
                String name = results.getString(COLUMN_NAME);
                String desc = results.getString(COLUMN_DESCRIPTION);
                int size = results.getInt(COLUMN_SIZE);
                String invCode = results.getString(COLUMN_INVCODE);

                return new Group(id, name, desc, size, invCode);
            }

            results.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String getMembersRole(int groupID, int userID) {
        String role = "";
        String getGroupNameSQL = "SELECT " + LUCOLUMN_ROLE + " FROM " + LUTABLE_NAME + " WHERE " + LUCOLUMN_GROUPID + " = " + groupID + " AND " + LUCOLUMN_USERID + " = " + userID + ";";
        try {
            ResultSet results = db.selectQuery(getGroupNameSQL);
            if (results.isBeforeFirst()) {
                role = results.getString(LUCOLUMN_ROLE);
            } else {
                System.out.println("AN ERROR HAS OCCURRED");
            }
            results.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return role;
    }

    public int GetGroupID(String invCode){
        String getGroupNameSQL = "SELECT " + COLUMN_ID + " FROM " + TABLE_NAME + " WHERE " + COLUMN_INVCODE + " = \"" + invCode + "\";";
        System.out.println(getGroupNameSQL);
        int groupID = 0;
        try {
            ResultSet results = db.selectQuery(getGroupNameSQL);
                groupID = results.getInt(COLUMN_ID);
            results.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        if (groupID == 0) {
            System.out.println("ERROR GETTING GROUP ID");
        }
        return groupID;
    }

    public void AddMember(int groupID, int userID) {
        String setGroupOwnerSQL = "INSERT INTO " + LUTABLE_NAME + " (" + LUCOLUMN_GROUPID + " , " + LUCOLUMN_USERID + " , " + LUCOLUMN_ROLE + " ) VALUES( " + groupID + " , " + userID + " , \"MEMBER\" )";
        try {
            System.out.println(setGroupOwnerSQL);
            db.insertData(setGroupOwnerSQL);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        IncrementGroupSize(groupID);
    }

    public void IncrementGroupSize(int groupID){
        int size = GetGroupSize(groupID) + 1;

        String incrementGroupSizeSQL = "UPDATE " + TABLE_NAME + " SET " + COLUMN_SIZE + " = " + size + " WHERE " + COLUMN_ID + " = " + groupID + ";";
        System.out.println(incrementGroupSizeSQL);
        try {
            db.updateTable(incrementGroupSizeSQL);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public int GetGroupSize(int groupID){
        String getGroupSizeSQL = "SELECT " + COLUMN_SIZE + " FROM " + TABLE_NAME + " WHERE " + COLUMN_ID + " = " + groupID + ";";
        int size = 0;
        try {
            ResultSet results = db.selectQuery(getGroupSizeSQL);
            size = results.getInt(COLUMN_SIZE);
            results.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return size;
    }

    public void LeaveGroup(int groupID, int userID){
        String deleteMemberSQL = "DELETE FROM " + LUTABLE_NAME + " WHERE " + LUCOLUMN_GROUPID + " = " + groupID + " AND " + LUCOLUMN_USERID + " = " + userID + ";";
        try {
            db.deleteData(deleteMemberSQL);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        DecrementGroupSize(groupID);
    }

    public void DecrementGroupSize(int groupID){
        int size = GetGroupSize(groupID) - 1;

        String incrementGroupSizeSQL = "UPDATE " + TABLE_NAME + " SET " + COLUMN_SIZE + " = " + size + " WHERE " + COLUMN_ID + " = " + groupID + ";";
        System.out.println(incrementGroupSizeSQL);
        try {
            db.updateTable(incrementGroupSizeSQL);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public boolean doesMemberAlreadyExistInGroup(int groupID, int userID) {
        String getGroupNameSQL = "SELECT " + LUCOLUMN_ROLE + " FROM " + LUTABLE_NAME + " WHERE " + LUCOLUMN_GROUPID + " = " + groupID + " AND " + LUCOLUMN_USERID + " = " + userID + ";";
        System.out.println(getGroupNameSQL);
        try {
            ResultSet results = db.selectQuery(getGroupNameSQL);
            if (!results.isBeforeFirst()) {
                return false;
            }
            results.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return true;
    }

}

    // Method to get DESCRIPTION from database


    // Method to DELETE GROUP



