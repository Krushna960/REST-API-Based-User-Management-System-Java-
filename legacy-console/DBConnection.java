import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Manages MySQL database connection using JDBC.
 * Update URL, USER, and PASSWORD to match your local MySQL setup.
 */
public class DBConnection {

    private static final String URL =
            "jdbc:mysql://localhost:3306/user_management_db?useSSL=false&serverTimezone=UTC";
    private static final String USER = "root";
    private static final String PASSWORD = "your_password";

    private DBConnection() {
        // Utility class - prevent instantiation
    }

    /**
     * Creates and returns a new JDBC connection.
     * Caller should close the connection (e.g. via try-with-resources).
     */
    public static Connection getConnection() throws SQLException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new SQLException("MySQL JDBC Driver not found. Add mysql-connector-j to classpath.", e);
        }
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}
