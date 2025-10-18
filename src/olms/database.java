package olms;

import java.sql.*;

public class database {

    private static final String URL = "jdbc:mysql://localhost:3306/olms_db";
    private static final String USER = "root"; // change if needed
    private static final String PASSWORD = "1234"; // your MySQL password if any

    public static Connection getConnection() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            return DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (SQLException e) {
            System.out.println("❌ Database connection failed: " + e.getMessage());
        } catch (ClassNotFoundException e) {
            System.out.println("❌ MySQL JDBC Driver not found!");
        }
        return null;
    }
}
