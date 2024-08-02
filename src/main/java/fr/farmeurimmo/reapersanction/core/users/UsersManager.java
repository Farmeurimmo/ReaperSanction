package fr.farmeurimmo.reapersanction.core.users;

import fr.farmeurimmo.reapersanction.core.Main;
import fr.farmeurimmo.reapersanction.core.storage.DatabaseManager;

import java.util.ArrayList;
import java.util.UUID;

public class UsersManager {

    public static UsersManager INSTANCE;
    public ArrayList<User> users = new ArrayList<>();

    public UsersManager() {
        INSTANCE = this;
    }

    public User getUser(UUID uuid) {
        for (User user : users) {
            if (user == null) continue;
            if (user.getUuid() == null) continue;
            if (user.getUuid().equals(uuid)) {
                return user;
            }
        }
        if (Main.INSTANCE.getStorageMethod().equalsIgnoreCase("MYSQL")) return DatabaseManager.INSTANCE.getUser(uuid);
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

    public final ArrayList<User> getUsers() {
        return users;
    }

    public User createUser(UUID uuid, String name) {
        User user = new User(uuid, name);
        users.add(user);
        if (Main.INSTANCE.getStorageMethod().equalsIgnoreCase("MYSQL")) DatabaseManager.INSTANCE.createUser(user);
        return user;
    }

    public User getUserAndCreateIfNotExists(UUID uuid, String name) {
        User user = getUser(uuid);
        if (user == null) user = createUser(uuid, name);
        return user;
    }

    public void replaceUser(User user) {
        for (int i = 0; i < users.size(); i++) {
            if (users.get(i).getUuid().equals(user.getUuid())) {
                users.set(i, user);
                return;
            }
        }
    }
}
