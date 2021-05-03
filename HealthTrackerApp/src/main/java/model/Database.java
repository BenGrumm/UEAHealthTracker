package model;

import java.sql.*;

public class Database {

    private static final int iTimeout = 30;
    private static final String dbName = "HealthTrackerDB";
    private static Database db;

    private Connection conn;
    private Statement stmt;
    public ResultSet keys;

    public static void main(String[] args) {
        try {
            Database db = new Database("test");
            db.close();
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }

    protected static Database getInstance() throws ClassNotFoundException, SQLException{
//        if(db == null || db.stmt.isClosed() || db.conn.isClosed()){
//            db = new Database(dbName);
//        }

        return new Database(dbName);
    }

    private Database(String dbName) throws ClassNotFoundException, SQLException{
        openDatabase(dbName);
    }

    /**
     * Function to open sqlite database using JDBC driver. If name passed doesnt exist one will be created.
     * @param dbName name of database to open (if it doesn't exist already one will be created)
     * @throws ClassNotFoundException thrown when Class.forName is used to check if the JDBC driver is in project
     * @throws SQLException thrown when error connecting / creating statement / setting query timeout
     */
    protected void openDatabase(String dbName) throws ClassNotFoundException, SQLException {
        // Register driver
        String sDriverName = "org.sqlite.JDBC";
        Class.forName(sDriverName);

        // From relative path in HealthTrackerApp folder (not from where file is stored)
        String dbLocation = "src/main/resources/" + dbName + ".db";
        String sJDBC = "jdbc:sqlite";
        String sDbUrl = sJDBC + ":" + dbLocation;

        conn = DriverManager.getConnection(sDbUrl);

        stmt = conn.createStatement();
        stmt.setQueryTimeout(iTimeout);
    }

    /**
     * Function to create an new empty table in opened database
     * @param sqlStatement sql statement with table name, columns, etc.
     * @throws SQLException thrown if error adding sql
     */
    protected void createTable(String sqlStatement) throws SQLException{
        stmt.executeUpdate(sqlStatement);
    }

    /**
     *
     * @param selectQuery
     * @return set of results from the query
     * @throws SQLException
     */
    protected ResultSet selectQuery(String selectQuery) throws SQLException{
        return stmt.executeQuery(selectQuery);
    }

    /**
     * Insert data into currently opened db
     * @param insertQuery query with insert statement
     * @return true if insert updated 1 row in db
     * @throws SQLException error executing query
     */
    protected boolean insertData(String insertQuery) throws SQLException{
        int numRowsAffected = stmt.executeUpdate(insertQuery);

        //George code for finding the id of the record just inserted etc etc its the cool way.
        keys = stmt.getGeneratedKeys();

        return numRowsAffected == 0;
    }

    protected void updateTable(String updateQuery) throws SQLException{
        stmt.executeUpdate(updateQuery);
    }

    protected void deleteData(String deleteQuery) throws SQLException{
        stmt.executeUpdate(deleteQuery);
    }

    /**
     * Function to close any existing connection to the database
     * @throws SQLException
     */
    protected void close() throws SQLException{
        if(!stmt.isClosed()){
            stmt.close();
        }
        if(!conn.isClosed()){
            stmt.close();
        }
    }

}
