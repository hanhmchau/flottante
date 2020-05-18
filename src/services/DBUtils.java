package services;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

class DBUtils {
    static Connection getMyConnection() {
        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            return DriverManager.getConnection("jdbc:sqlserver://localhost:1433;database=Flottante", "sa", "1234");
        } catch (ClassNotFoundException | SQLException ex) {
            ex.printStackTrace();
            return null;
        }
    }
}
