package DBConnection;

import java.sql.Connection;
import java.sql.DriverManager;

public class DBConnection {
    // Connection string: তোমার আসল instance name বসানো হলো
    private static final String URL = "jdbc:sqlserver://DESKTOP-1UAP00N\\SQLEXPRESS:1433;databaseName=PayrollDB;encrypt=false";
    
    // তোমার নতুন login credentials
    private static final String USER = "shakib";
    private static final String PASSWORD = "shakib";

    public static Connection getConnection() {
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(URL, USER, PASSWORD);
            System.out.println("✅ Connected to SQL Server successfully!");
        } catch (Exception e) {
            System.out.println("❌ Database connection failed!");
            e.printStackTrace();
        }
        return conn;
    }

    // Test connection
    public static void main(String[] args) {
        Connection testConn = DBConnection.getConnection();
        if (testConn != null) {
            System.out.println("Connection test passed!");
        } else {
            System.out.println("Connection test failed!");
        }
    }
}
