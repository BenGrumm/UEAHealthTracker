package model;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;

public class UserDBHelper {

    public static void main(String[] args) throws SQLException {
        UserDBHelper userDBHelper = new UserDBHelper();

        addTest(userDBHelper);

        System.out.println(Arrays.toString(userDBHelper.getAllUsers()));

        for(User user : userDBHelper.getAllUsers()){
            System.out.println(user);
        }
    }

    public static void addTest(UserDBHelper userDBHelper) throws SQLException {
        userDBHelper.addDBUser("John", "Smith", "jsmith1", "JSmith@uea.ac.uk",
                "jsmith1!", 172.5, 10, 1, "male");

        userDBHelper.addDBUser("Johnny", "boolh", "jbole", "pppond@yk.ac.uk",
                "jbol1!", 134.5, 50, 3, "female");
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

    public UserDBHelper() {
        try {
            db = Database.getInstance();

            String createDBIfNotExists = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME +
                    " (" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT , " +
                    COLUMN_FIRSTNAME + " TEXT , " +
                    COLUMN_SURNAME + " TEXT , " +
                    COLUMN_USERNAME + " TEXT , " +
                    COLUMN_PASSWORD + " TEXT , " +
                    COLUMN_EMAIL + " TEXT , " +
                    COLUMN_HEIGHT + " FLOAT , " +
                    COLUMN_WEIGHT_STONE + " INTEGER , " +
                    COLUMN_WEIGHT_POUNDS + " INTEGER , " +
                    COLUMN_GENDER + " TEXT)";

            db.createTable(createDBIfNotExists);

        } catch (SQLException sql) {
            System.out.println("Error Accessing DB");
            sql.printStackTrace();
        } catch (ClassNotFoundException cnfe) {
            System.out.println("JDBC driver not found");
            cnfe.printStackTrace();
        }
    }

    public boolean checkValidAccount(String email, String username){

        String sql = "SELECT " + COLUMN_EMAIL + ',' + COLUMN_USERNAME + " FROM " + TABLE_NAME;
        ResultSet rs = null;
        try {
            rs = db.selectQuery(sql);
            while (rs.next()) {
                if (rs.getString(COLUMN_EMAIL).equals(email) || rs.getString(COLUMN_USERNAME).equals(username)){
                    return false;
                }
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return true;
    }

    public boolean checkValidEmail(String email){

        String sql = "SELECT " + COLUMN_EMAIL + " FROM " + TABLE_NAME;
        ResultSet rs = null;
        try {
            rs = db.selectQuery(sql);
            while (rs.next()) {
                if (rs.getString(COLUMN_EMAIL).equals(email)){
                    return false;
                }
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return true;
    }

    public boolean addDBUser(String firstName, String surname, String username, String email, String password,
                          double height, int weightStone, int weightPounds, String gender) throws SQLException {

        boolean valid = checkValidAccount(email, username);

        if (!valid){
            System.out.println("YOU DIRT! YOU ARE NOT VALID AT ALL!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
            return false;
        }
        else {
            String sql = "INSERT INTO USERS(FIRSTNAME,SURNAME,USERNAME,PASSWORD,EMAIL,HEIGHT,WEIGHT_STONE,WEIGHT_POUNDS,GENDER)" +
                    " VALUES(" + '"' + firstName + '"' + ", " + '"' + surname + '"' + ", " + '"' + username + '"' + ", " + '"'
                    + Security.encrypt(password) + '"' + ", " + '"' + email + '"' + ", " + '"' + height + '"' +
                    ", " + '"' + weightStone + '"' + ", " + '"' + weightPounds + '"' + ", " + '"' + gender + '"' + ");";

            db.insertData(sql);
        }
        return true;
    }

    public User getUserViaEmail(String email) {
        try {
            ResultSet rs = db.selectQuery("SELECT * FROM " + TABLE_NAME + " WHERE EMAIL=" + '"' + email + '"');
            User[] users =  convertResultSetToUser(rs);
            if (users.length != 0){
                return users[0];
            }
            return null;
        } catch (SQLException error) {
            return null;
        }
    }

    public User getUserViaID(int ID) {
        try {
            ResultSet rs = db.selectQuery("SELECT * FROM " + TABLE_NAME + " WHERE __id=" + '"' + ID + '"');
            User[] users =  convertResultSetToUser(rs);
            if (users.length != 0){
                return users[0];
            }
            return null;
        } catch (SQLException error) {
            return null;
        }
    }

    public User getUserViaUsername(String username) {
        try {
            ResultSet rs = db.selectQuery("SELECT * FROM " + TABLE_NAME + " WHERE USERNAME=" + '"' + username + '"');
            User[] users = convertResultSetToUser(rs);
            if (users.length != 0){
                return users[0];
            }
            return null;

        } catch (SQLException error) {
            return null;
        }
    }


    private static final String getAllUsers = "SELECT * FROM " + TABLE_NAME;

    public User[] getAllUsers() {
        try {
            ResultSet rs = db.selectQuery(getAllUsers);
            return convertResultSetToUser(rs);
        } catch (SQLException error) {
            return null;
        }
    }

    public User[] convertResultSetToUser(ResultSet rs) throws SQLException {
        ArrayList<User> users = new ArrayList<>();
        while (rs.next()) {
            int id = rs.getInt(COLUMN_ID);
            String firstname = rs.getString(COLUMN_FIRSTNAME);
            String surname = rs.getString(COLUMN_SURNAME);
            String username = rs.getString(COLUMN_USERNAME);
            String email = rs.getString(COLUMN_EMAIL);
            String password = rs.getString(COLUMN_PASSWORD);
            int height = rs.getInt(COLUMN_HEIGHT);
            int weight_stone = rs.getInt(COLUMN_WEIGHT_POUNDS);
            int weight_pounds = rs.getInt(COLUMN_WEIGHT_POUNDS);
            String gender = rs.getString(COLUMN_GENDER);
            users.add(new User(id, firstname, surname, username, email,Security.decrypt(password), height, weight_stone, weight_pounds, gender));
        }
        return users.toArray(new User[users.size()]);
    }
}
