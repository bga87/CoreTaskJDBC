package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.Util;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDaoJDBCImpl implements UserDao {
    private Connection dbConnection;
    private final String dbName = "coretaskdb";
    private final String tableName = "users";

    public UserDaoJDBCImpl() {
        try {
            dbConnection = Util.getConnection(dbName);
        } catch (SQLException ex) {
            System.err.printf("При попытке соединения с БД '%s' возникла ошибка:%n", dbName);
            System.err.println("\t==>" + ex.getMessage() + " Код ошибки: " + ex.getErrorCode());
            System.exit(1);
        }
    }

    public void createUsersTable() {
        try (Statement statement = dbConnection.createStatement()) {
            statement.executeUpdate(
                    "CREATE TABLE IF NOT EXISTS " + tableName + "(id INT AUTO_INCREMENT PRIMARY KEY, " +
                            "name VARCHAR(20) NOT NULL, lastname VARCHAR(35) NOT NULL, age TINYINT(3) UNSIGNED);"
            );
        } catch (SQLException ex) {
            System.err.printf("При попытке создания в БД '%s' таблицы '%s' возникли проблемы:%n", dbName, tableName);
            System.err.println("\t==>" + ex.getMessage() + " Код ошибки: " + ex.getErrorCode());
        }
    }

    public void dropUsersTable() {
        try (Statement statement = dbConnection.createStatement()) {
            statement.executeUpdate("DROP TABLE IF EXISTS " + tableName + ';');
        } catch (SQLException ex) {
            System.err.printf("При попытке удаления из БД '%s' таблицы '%s' возникли проблемы:%n", dbName, tableName);
            System.err.println("\t==>" + ex.getMessage() + " Код ошибки: " + ex.getErrorCode());
        }
    }

    public void saveUser(String name, String lastName, byte age) {
        try (PreparedStatement pStatement =
                     dbConnection.prepareStatement("INSERT INTO " + tableName + "(name, lastname, age) VALUES (?, ?, ?);")
        ) {
            pStatement.setString(1, name);
            pStatement.setString(2, lastName);
            pStatement.setByte(3, age);
            pStatement.executeUpdate();
        } catch (SQLException ex) {
            System.err.printf(
                    "При добавлении в таблицу '%s' пользователя '%s %s' возникли проблемы:%n",
                    tableName, name, lastName
            );
            System.err.println("\t==> " + ex.getMessage() + " Код ошибки: " + ex.getErrorCode());
        }
    }

    public void removeUserById(long id) {
        try (Statement statement = dbConnection.createStatement()) {
            statement.executeUpdate("DELETE FROM " + tableName + " WHERE id = " + id + ';');
        } catch (SQLException ex) {
            System.err.printf("При попытке удаления из таблицы '%s' в БД '%s' пользователя с id=%d возникли проблемы:%n",
                    tableName, dbName, id);
            System.err.println("\t==>" + ex.getMessage() + " Код ошибки: " + ex.getErrorCode());
        }
    }

    public List<User> getAllUsers() {
        List<User> users = new ArrayList<>();

        try (Statement statement = dbConnection.createStatement()) {
            ResultSet queryResult = statement.executeQuery("SELECT * FROM " + tableName + ';');

            while (queryResult.next()) {
                users.add(
                        new User(queryResult.getString("name"),
                                queryResult.getString("lastname"),
                                queryResult.getByte("age"))
                );
            }
        } catch (SQLException ex) {
            System.err.printf("При попытке считывания из БД '%s' таблицы '%s' возникли проблемы:%n", dbName, tableName);
            System.err.println("\t==>" + ex.getMessage() + " Код ошибки: " + ex.getErrorCode());
        }

        return users;
    }

    public void cleanUsersTable() {
        try (Statement statement = dbConnection.createStatement()) {
            statement.executeUpdate("TRUNCATE TABLE " + tableName + ';');
        } catch (SQLException ex) {
            System.err.printf("При попытке очистки таблицы '%s' в БД '%s' возникли проблемы:%n", tableName, dbName);
            System.err.println("\t==>" + ex.getMessage() + " Код ошибки: " + ex.getErrorCode());
        }
    }
}
