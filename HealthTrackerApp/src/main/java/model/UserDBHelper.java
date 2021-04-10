package model;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;

public class UserDBHelper {

    public static void main(String[] args) throws SQLException {
        UserDBHelper userDBHelper = new UserDBHelper();

        userDBHelper.addDBUser("John","Smith","jsmith1","JSmith@uea.ac.uk",
                "jsmith1!",172.5, 10,1,"male");

        System.out.println(Arrays.toString(userDBHelper.getAllUsers()));
    }

    private static final String TABLE_NAME = "USERS";
    private static final String COLUMN_ID = "__id";
    private static final String COLUMN_FIRSTNAME = "FIRSTNAME";
    private static final String COLUMN_SURNAME = "SURNAME";
    private static final String COLUMN_USERNAME = "USERNAME";
    private static final String COLUMN_PASSWORD = "PASSWORD";
    private static final String COLUMN_EMAIL = "EMAIL";
    private static final String COLUMN_HEIGHT = "HEIGHT";
    private static final String COLUMN_WEIGHT_STONE = "WEIGHT_STONE";
    private static final String COLUMN_WEIGHT_POUNDS = "WEIGHT_POUNDS";
    private static final String COLUMN_GENDER = "GENDER";

    private Database db;

    public UserDBHelper(){
        try{
            db = Database.getInstance();

            String createDBIfNotExists = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME +
                    " (" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT , " +
                    COLUMN_FIRSTNAME + " TEXT , " +
                    COLUMN_SURNAME + " TEXT , " +
                    COLUMN_USERNAME + " TEXT , " +
                    COLUMN_PASSWORD + " TEXT , " +
                    COLUMN_EMAIL + "TEXT , " +
                    COLUMN_HEIGHT + "INTEGER , " +
                    COLUMN_WEIGHT_STONE + "INTEGER , " +
                    COLUMN_WEIGHT_POUNDS + "INTEGER , " +
                    COLUMN_GENDER + "INTEGER";

            //db.createTable(createDBIfNotExists);

        }catch (SQLException sql){
            System.out.println("Error Accessing DB");
            sql.printStackTrace();
        }catch (ClassNotFoundException cnfe){
            System.out.println("JDBC driver not found");
            cnfe.printStackTrace();
        }
    }

    public void addDBUser(String firstName, String surname, String username, String email, String password,
                          double height, int weightStone, int weightPounds, String gender) throws SQLException {

        String sql = "INSERT INTO USERS(FIRSTNAME,SURNAME,USERNAME,EMAIL,PASSWORD,HEIGHT,WEIGHT_STONE,WEIGHT_POUNDS,GENDER)" +
                " VALUES(" + '"' + firstName + '"' + ", " + '"'+ surname+ '"' + ", " + '"'+ username+ '"' + ", " + '"'
                + email+ '"' + ", "+ '"' + password+ '"' + ", "+ '"' + height+ '"' +
                ", "+ '"' + weightStone+ '"' + ", "+ '"' + weightPounds+ '"' + ", "+ '"' + gender+ '"' + ");";

        db.insertData(sql);

    }


    public Exercise[] getDBUsers(){
        return null;
    }


    /*
    public Exercise[] getExercisesWithinRange(LocalDate from, LocalDate to){
        try {
            ResultSet rs = db.selectQuery(String.format(withinRangeSQL, from.toString(), to.toString()));
            return convertResultSetToExercise(rs);
        }catch (SQLException error){
            return null;
        }
    }
    */

    private static final String getAllUsers = "SELECT * FROM " + TABLE_NAME;
    public User[] getAllUsers(){
        try {
            ResultSet rs = db.selectQuery(getAllUsers);
            return convertResultSetToUsers(rs);
        }catch (SQLException error){
            return null;
        }
    }

    public User[] convertResultSetToUsers(ResultSet rs) throws SQLException{
        ArrayList<User> users = new ArrayList<>();

        while(rs.next()){
            int id = rs.getInt(COLUMN_ID);
            String firstname = rs.getString(COLUMN_FIRSTNAME);
            String surname = rs.getString(COLUMN_SURNAME);
            String username = rs.getString(COLUMN_USERNAME);
            String password = rs.getString(COLUMN_PASSWORD);
            String email = rs.getString(COLUMN_EMAIL);
            int height = rs.getInt(COLUMN_HEIGHT);
            int weight_stone = rs.getInt(COLUMN_WEIGHT_POUNDS);
            int weight_pounds = rs.getInt(COLUMN_WEIGHT_POUNDS);
            String gender = rs.getString(COLUMN_GENDER);


            users.add(new User(id, firstname,surname,username,password,email,height,weight_stone,weight_pounds,gender));
        }

        return users.toArray(new User[users.size()]);
    }

    public boolean addExerciseToDB(Exercise ex){
        return false;
    }
}
