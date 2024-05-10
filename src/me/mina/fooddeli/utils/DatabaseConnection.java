package me.mina.fooddeli.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import static me.mina.fooddeli.utils.Constants.*;

public class DatabaseConnection {

    private static volatile DatabaseConnection instance;
    private Connection connection;

    private DatabaseConnection() {
        try {
            this.connection = DriverManager.getConnection(
                    JDBC_DRIVER, JDBC_USER, JDBC_PWD);
        } catch (SQLException e) {
            throw new RuntimeException("Error connecting to the database", e);
        }
    }

    public static DatabaseConnection getInstance() {
        if (instance == null) {
            //Use synchronized block to be thread-safe
            synchronized (DatabaseConnection.class) {
                if (instance == null) {
                    instance = new DatabaseConnection();
                }
            }
        }
        return instance;
    }

    public Connection getConnection() {
        return connection;
    }

    // Include a method to close the connection
    public void closeConnection() {
        try {
            if (connection != null) {
                connection.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
