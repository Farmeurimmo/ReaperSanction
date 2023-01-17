package main.java.fr.farmeurimmo.reapersanction;

import main.java.fr.farmeurimmo.reapersanction.users.User;

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
            System.out.println("§a§lSuccessfully connected to the database !");
        } catch (SQLException e) {
            throw new Exception("Unable to connect to the database");
        }

        connection.prepareStatement("CREATE DATABASE IF NOT EXISTS reapersanction").executeUpdate();
        connection.prepareStatement("USE reapersanction").executeUpdate();
        connection.prepareStatement("CREATE TABLE IF NOT EXISTS users (uuid VARCHAR(36) primary key, name VARCHAR(24), mutedAt BIGINT, mutedUntil BIGINT, mutedFor LONGTEXT," +
                "mutedBy VARCHAR(24), bannedUntil BIGINT, bannedAt BIGINT, bannedBy VARCHAR(24), bannedFor LONGTEXT, ipBanned BOOL, ip VARCHAR(32), history JSON)").executeUpdate();
    }

    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(host, user, password);
    }

    public void createUser(User user) {
        // TODO
    }

    public void updatePlayer(User user) {

    }
}
