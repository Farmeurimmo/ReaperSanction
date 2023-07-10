package fr.farmeurimmo.reapersanction.users;

import fr.farmeurimmo.reapersanction.ReaperSanction;
import fr.farmeurimmo.reapersanction.storage.DatabaseManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.UUID;

public class UsersManager {

    public static UsersManager INSTANCE;
    public ArrayList<User> users = new ArrayList<>();

    public UsersManager() {
        INSTANCE = this;
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
        if (ReaperSanction.storageMethod.equalsIgnoreCase("MYSQL")) return DatabaseManager.INSTANCE.getUser(uuid);
        return null;
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
        if (ReaperSanction.storageMethod.equalsIgnoreCase("MYSQL")) DatabaseManager.INSTANCE.createUser(user);
        return user;
    }

    public User getUserAndCreateIfNotExists(UUID uuid, String name) {
        User user = getUser(uuid);
        if (user == null) user = createUser(uuid, name);
        return user;
    }
}
