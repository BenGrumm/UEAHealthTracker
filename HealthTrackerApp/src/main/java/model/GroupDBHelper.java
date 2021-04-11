package model;

import java.sql.ResultSet;
import java.sql.SQLException;

public class GroupDBHelper {

    private static final String TABLE_NAME = "GROUPS";
    private static final String COLUMN_ID = "ID";
    private static final String COLUMN_NAME = "NAME";
    private static final String COLUMN_DESCRIPTION = "DESCRIPTION";
    private static final String COLUMN_SIZE = "SIZE";

    private Database db;

    public GroupDBHelper(){
        try{
            db = Database.getInstance();

            String createTableSQL = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " (" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT , " + COLUMN_NAME + " TEXT , " + COLUMN_DESCRIPTION+ " TEXT , " + COLUMN_SIZE + " INTEGER)";
            System.out.println(createTableSQL);

            db.createTable(createTableSQL);

        }catch (SQLException sql){
            System.out.println("Error Accessing DB");
            sql.printStackTrace();
        }catch (ClassNotFoundException cnfe){
            System.out.println("JDBC driver not found");
            cnfe.printStackTrace();
        }
    }


    /** Method to add group to database **/
    //Returns ID when stored succesfully
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

    /** Method to get NAME from database **/
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



    /** Method to get DESCRIPTION from database **/

    /** Method to get SIZE from database **/

    /** Method to INCREMENT SIZE of a group**/

    /** Method to DECREMENT SIZE of group **/

    /** Method to DELETE GROUP **/
}
