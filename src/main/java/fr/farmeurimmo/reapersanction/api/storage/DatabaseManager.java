package fr.farmeurimmo.reapersanction.api.storage;

import fr.farmeurimmo.reapersanction.api.users.User;
import fr.farmeurimmo.reapersanction.api.users.UsersManager;
import fr.farmeurimmo.reapersanction.server.spigot.ReaperSanction;

import java.sql.*;
import java.util.ArrayList;
import java.util.UUID;

public class DatabaseManager {

    public static DatabaseManager INSTANCE;
    private final String host, user, password;
    public Connection connection;

    public DatabaseManager(String host, String user, String password) {
        INSTANCE = this;

        this.host = host;
        this.user = user;
        this.password = password;
    }

    public void startConnection() throws Exception {
        try {
            connection = getConnection();
            ReaperSanction.INSTANCE.getLogger().info("§a§lSuccessfully connected to the database !");
        } catch (SQLException e) {
            throw new Exception("Unable to connect to the database");
        }

        connection.prepareStatement("CREATE DATABASE IF NOT EXISTS reapersanction").executeUpdate();
        connection.prepareStatement("USE reapersanction").executeUpdate();
        connection.prepareStatement("CREATE TABLE IF NOT EXISTS users (uuid VARCHAR(36) primary key, name VARCHAR(24), mutedAt BIGINT, mutedUntil BIGINT, " +
                "mutedFor LONGTEXT, mutedBy VARCHAR(24), mutedDuration LONGTEXT, bannedUntil BIGINT, bannedAt BIGINT, bannedBy VARCHAR(24), bannedFor LONGTEXT, ipBanned BOOL, " +
                "bannedDuration LONGTEXT, ip VARCHAR(32), history LONGTEXT)").executeUpdate();
    }

    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(host, user, password);
    }

    public void createUser(User user) {
        try {
            PreparedStatement statement = connection.prepareStatement(
                    "INSERT IGNORE INTO users (uuid, name, mutedAt, mutedUntil, mutedFor, mutedBy, mutedDuration, bannedUntil, bannedAt, bannedBy, bannedFor, ipBanned, bannedDuration, ip, history) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
            statement.setString(1, user.getUuid().toString());
            statement.setString(2, user.getName());
            statement.setLong(3, user.getMutedAt());
            statement.setLong(4, user.getMutedUntil());
            statement.setString(5, user.getMutedReason());
            statement.setString(6, user.getMutedBy());
            statement.setString(7, user.getMutedDuration());
            statement.setLong(8, user.getBannedUntil());
            statement.setLong(9, user.getBannedAt());
            statement.setString(10, user.getBannedBy());
            statement.setString(11, user.getBannedReason());
            statement.setBoolean(12, user.isIpBanned());
            statement.setString(13, user.getBannedDuration());
            statement.setString(14, user.getIp());
            statement.setString(15, User.getHistoryAsString(user.getHistory()));

            statement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updatePlayer(User user) {
        try {
            PreparedStatement statement = connection.prepareStatement(
                    "UPDATE users SET name = ?, mutedAt = ?, mutedUntil = ?, mutedFor = ?, mutedBy = ?, mutedDuration = ?, bannedUntil = ?, bannedAt = ?, bannedBy = ?, bannedFor = ?, ipBanned = ?, bannedDuration = ?, ip = ?, history = ? WHERE uuid = ?");
            statement.setString(1, user.getName());
            statement.setLong(2, user.getMutedAt());
            statement.setLong(3, user.getMutedUntil());
            statement.setString(4, user.getMutedReason());
            statement.setString(5, user.getMutedBy());
            statement.setString(6, user.getMutedDuration());
            statement.setLong(7, user.getBannedUntil());
            statement.setLong(8, user.getBannedAt());
            statement.setString(9, user.getBannedBy());
            statement.setString(10, user.getBannedReason());
            statement.setBoolean(11, user.isIpBanned());
            statement.setString(12, user.getBannedDuration());
            statement.setString(13, user.getIp());
            statement.setString(14, User.getHistoryAsString(user.getHistory()));
            statement.setString(15, user.getUuid().toString());

            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateAllUsersFromMigratation() {
        for (User user : UsersManager.INSTANCE.users) {
            createUser(user);
            updatePlayer(user);
        }
    }

    public void loadUsers() {
        try {
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM users");
            ResultSet resultSet = statement.executeQuery();

            ArrayList<User> users = new ArrayList<>();

            while (resultSet.next()) {
                users.add(new User(UUID.fromString(resultSet.getString("uuid")), resultSet.getString("name"), resultSet.getLong("mutedUntil"),
                        resultSet.getString("mutedFor"), resultSet.getString("mutedBy"), resultSet.getLong("mutedAt"),
                        resultSet.getString("mutedDuration"), resultSet.getLong("bannedUntil"), resultSet.getString("bannedFor"),
                        resultSet.getString("bannedBy"), resultSet.getLong("bannedAt"), resultSet.getBoolean("ipBanned"),
                        resultSet.getString("bannedDuration"), resultSet.getString("ip"), User.getHistoryFromString(resultSet.getString("history"))));
            }

            UsersManager.INSTANCE.users = users;
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public User getUser(UUID uuid) {
        try {
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM users WHERE uuid = ?");
            statement.setString(1, uuid.toString());
            statement.executeQuery();

            ResultSet resultSet = statement.getResultSet();

            if (resultSet.next()) {
                return new User(UUID.fromString(resultSet.getString("uuid")), resultSet.getString("name"), resultSet.getLong("mutedUntil"),
                        resultSet.getString("mutedFor"), resultSet.getString("mutedBy"), resultSet.getLong("mutedAt"),
                        resultSet.getString("mutedDuration"), resultSet.getLong("bannedUntil"), resultSet.getString("bannedFor"),
                        resultSet.getString("bannedBy"), resultSet.getLong("bannedAt"), resultSet.getBoolean("ipBanned"),
                        resultSet.getString("bannedDuration"), resultSet.getString("ip"), User.getHistoryFromString(resultSet.getString("history")));
            }

            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
