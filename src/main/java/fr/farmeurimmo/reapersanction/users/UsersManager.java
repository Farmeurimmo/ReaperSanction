package main.java.fr.farmeurimmo.reapersanction.users;

import main.java.fr.farmeurimmo.reapersanction.DatabaseManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.UUID;

public class UsersManager {

    public static UsersManager instance;
    public ArrayList<User> users;

    public UsersManager() {
        instance = this;

        users = DatabaseManager.instance.getUsers();
    }

    public void checkForOnlinePlayersIfTheyAreUsers() {
        for (Player p : Bukkit.getOnlinePlayers()) {
            getUserAndCreateIfNotExists(p.getUniqueId(), p.getName());
        }
    }

    public User getUser(UUID uuid) {
        for (User user : users) {
            if (user.getUuid().equals(uuid)) {
                return user;
            }
        }
        return DatabaseManager.instance.getUser(uuid);
    }

    public User getUser(String name) {
        for (User user : users) {
            if (user.getName().equals(name)) {
                return user;
            }
        }
        return null;
    }

    public User createUser(UUID uuid, String name) {
        User user = new User(uuid, name);
        users.add(user);
        DatabaseManager.instance.createUser(user);
        return user;
    }

    public User getUserAndCreateIfNotExists(UUID uuid, String name) {
        User user = getUser(uuid);
        if (user == null) user = createUser(uuid, name);
        return user;
    }
}
