package jm.task.core.jdbc.util;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
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

    public static EntityManagerFactory getEntityManagerFactory() {
        return Persistence.createEntityManagerFactory("jm.task.core.jdbc.jpa");
    }
}
