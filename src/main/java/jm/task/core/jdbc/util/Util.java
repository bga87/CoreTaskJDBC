package jm.task.core.jdbc.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Util {
    // реализуйте настройку соеденения с БД
    public static Connection getConnection(String dbName) throws SQLException {
        return DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/" + dbName,
                "root",
                "");
    }
}
