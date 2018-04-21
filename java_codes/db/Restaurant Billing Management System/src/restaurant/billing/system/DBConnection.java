
// password hash
// sql injection

package restaurant.billing.system;

import java.sql.*;

public class DBConnection {
    
    private static final String DB_NAME = "restaurant";
    private static final String DB_USER_NAME = "root";
    private static final String DB_PASSWORD = "";
    
    private Connection conn;
    
    public DBConnection() {
        createConnection();
    }
    
    private void createConnection() {
        conn = null;
        try {
            Class.forName("com.mysql.jdbc.Driver");
            conn = DriverManager.getConnection("jdbc:mysql://localhost/" + DB_NAME, DB_USER_NAME, DB_PASSWORD);
        } catch (ClassNotFoundException | SQLException e) {
            System.out.println("Open not successful.");
        }
    }
    
    public Connection getConnection() {
        return conn;
    }
    
    public ResultSet executeQuery(String sql) {
        ResultSet result = null;
        try {
            Statement st = conn.createStatement();
            result = st.executeQuery(sql);
        } catch (SQLException e) {
            System.out.println("execute query error.\n" + sql);
            e.printStackTrace();
        }
        return result;
    }
    
    //for exporting file using insert
    public void executeQuery(String sql, boolean noResult) {
        try {
            Statement st = conn.createStatement();
            st.executeQuery(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    public boolean executeUpdate(String sql) {
        try {
            Statement st = conn.createStatement();
            st.executeUpdate(sql);
            return true;
        } catch (SQLException e) {
            System.out.println(sql);
            e.printStackTrace();
            return false;
        }
    }
    
    public boolean alreadyExists(String sql) {
        try {
            ResultSet result = executeQuery(sql);
            return result.next();
        } catch (SQLException e) {
            System.out.println("error checking if already exists.");
        }
        return false;
    }
    
    public void close() {
          try {
            conn.close();
        } catch (SQLException e) {
            System.out.println("Closing Error.");
        } catch (NullPointerException e) {
            System.out.println("Attempting to close null conn.");
        }
    }
    
}
