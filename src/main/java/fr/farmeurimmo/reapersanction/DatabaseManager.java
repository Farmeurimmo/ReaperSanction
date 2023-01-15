package main.java.fr.farmeurimmo.reapersanction;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseManager {

    public static DatabaseManager instance;
    private final String host, user, password;
    public Connection connection;

    public DatabaseManager(String host, String user, String password) throws Exception {
        instance = this;

        this.host = host;
        this.user = user;
        this.password = password;

        try {
            connection = getConnection();
        } catch (SQLException e) {
            throw new Exception("Unable to connect to the database");
        }

        connection.prepareStatement("CREATE DATABASE IF NOT EXISTS reapersanction").executeUpdate();
        connection.prepareStatement("USE reapersanction").executeUpdate();
        connection.prepareStatement("CREATE TABLE IF NOT EXISTS users (uuid VARCHAR(36) primary key, mute JSON, ban JSON, history JSON)").executeUpdate();
    }

    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(host, user, password);
    }

}
