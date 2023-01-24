package main.java.fr.farmeurimmo.reapersanction.storage;

import main.java.fr.farmeurimmo.reapersanction.sanctions.SanctionApplier;
import main.java.fr.farmeurimmo.reapersanction.users.Sanction;
import main.java.fr.farmeurimmo.reapersanction.users.User;
import main.java.fr.farmeurimmo.reapersanction.users.UsersManager;
import org.bukkit.Bukkit;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.UUID;

public class LocalStorageManager {

    public static LocalStorageManager instance;

    public LocalStorageManager() {
        instance = this;
    }

    public void setup() {
        FilesManager.instance.setup_YAML_Storage();
        if (isAnOldYAMLFile()) convertFromOldStorageMethod();
        else loadUsers();
    }

    public boolean isAnOldYAMLFile() {
        for (String key : FilesManager.instance.getData().getKeys(false)) {
            if (FilesManager.instance.getData().isSet(key + ".tempmute")) {
                return true;
            }
        }
        return false;
    }

    public ArrayList<User> getUsersFromOldYAML() {
        ArrayList<User> users = new ArrayList<>();
        for (String name : FilesManager.instance.getData().getKeys(false)) {
            if (name == null) continue;
            UUID uuid;
            if (FilesManager.instance.getData().get(name + ".uuid") != null)
                uuid = UUID.fromString(FilesManager.instance.getData().getString(name + ".uuid"));
            else uuid = Bukkit.getOfflinePlayer(name).getUniqueId();
            if (uuid == null) continue;
            long mutedUntil = 0;
            long mutedAt = 0;
            String mutedReason = "";
            String mutedBy = "";
            String mutedDuration = "";
            String mutedType = "";
            if (FilesManager.instance.getData().getBoolean(name + ".tempmute.istempmuted")) {
                mutedUntil = FilesManager.instance.getData().getLong(name + ".tempmute.timemillis");
                mutedReason = FilesManager.instance.getData().getString(name + ".tempmute.reason");
                mutedBy = FilesManager.instance.getData().getString(name + ".tempmute.banner");
                mutedDuration = FilesManager.instance.getData().getString(name + ".tempmute.duration");
                mutedType = FilesManager.instance.getData().getString(name + ".tempmute.unit");
                mutedAt = SanctionApplier.instance.getMillisOfEmission(mutedUntil, mutedDuration, mutedType);
            }
            if (FilesManager.instance.getData().getBoolean(name + ".mute.ismuted")) {
                mutedUntil = -1;
                mutedReason = FilesManager.instance.getData().getString(name + ".mute.reason");
                mutedBy = "";
                mutedAt = -1;
                mutedDuration = "Permanent";
                mutedType = "";
            }
            mutedDuration += mutedType;
            long bannedUntil = 0;
            long bannedAt = 0;
            String bannedReason = "";
            String bannedBy = "";
            String bannedDuration = "";
            String bannedType = "";
            boolean isIpBanned = false;
            if (FilesManager.instance.getData().getBoolean(name + ".tempban.istempbanned")) {
                bannedUntil = FilesManager.instance.getData().getLong(name + ".tempban.timemillis");
                bannedReason = FilesManager.instance.getData().getString(name + ".tempban.reason");
                bannedBy = FilesManager.instance.getData().getString(name + ".tempban.banner");
                bannedDuration = FilesManager.instance.getData().getString(name + ".tempban.duration");
                bannedType = FilesManager.instance.getData().getString(name + ".tempban.unit");
                bannedAt = SanctionApplier.instance.getMillisOfEmission(bannedUntil, bannedDuration, bannedType);
            }
            if (FilesManager.instance.getData().getBoolean(name + ".ban.isbanned")) {
                bannedUntil = 0;
                bannedReason = FilesManager.instance.getData().getString(name + ".ban.reason");
                bannedBy = FilesManager.instance.getData().getString(name + ".ban.banner");
                bannedAt = 0;
                bannedDuration = "Permanent";
                bannedType = "";
            }
            if (FilesManager.instance.getData().getBoolean(name + ".ipban.isipbanned")) {
                bannedUntil = -1;
                bannedReason = FilesManager.instance.getData().getString(name + ".ipban.reason");
                bannedBy = FilesManager.instance.getData().getString(name + ".ipban.banner");
                bannedAt = -1;
                bannedDuration = "Permanent";
                bannedType = "";
                isIpBanned = true;
            }
            String ip = FilesManager.instance.getData().getString(name + ".ip");
            LinkedList<Sanction> history = new LinkedList<>();
            bannedDuration += bannedType;

            users.add(new User(uuid, name, mutedUntil, mutedReason, mutedBy, mutedAt, mutedDuration, bannedUntil, bannedReason, bannedBy, bannedAt, isIpBanned, bannedDuration, ip, history));
        }
        return users;
    }

    public void convertFromOldStorageMethod() {
        ArrayList<User> users = getUsersFromOldYAML();
        FilesManager.instance.deleteAndRecreateDataFile();
        UsersManager.instance.users = users;
        saveAllUsers(true);
    }

    public void saveUser(User user, boolean async) {
        HashMap<String, Object> data = new HashMap<>();
        data.put(user.getUuid().toString() + ".name", user.getName());
        data.put(user.getUuid().toString() + ".muted.until", user.getMutedUntil());
        data.put(user.getUuid().toString() + ".muted.at", user.getMutedAt());
        data.put(user.getUuid().toString() + ".muted.by", user.getMutedBy());
        data.put(user.getUuid().toString() + ".muted.reason", user.getMutedReason());
        data.put(user.getUuid().toString() + ".muted.duration", user.getMutedDuration());
        data.put(user.getUuid().toString() + ".banned.until", user.getBannedUntil());
        data.put(user.getUuid().toString() + ".banned.at", user.getBannedAt());
        data.put(user.getUuid().toString() + ".banned.by", user.getBannedBy());
        data.put(user.getUuid().toString() + ".banned.reason", user.getBannedReason());
        data.put(user.getUuid().toString() + ".banned.duration", user.getBannedDuration());
        data.put(user.getUuid().toString() + ".banned.isBanIp", user.isIpBanned());
        data.put(user.getUuid().toString() + ".ip", user.getIp());
        data.put(user.getUuid().toString() + ".history", User.getHistoryAsString(user.getHistory()));

        if (async) FilesManager.instance.setAndSaveAsyncData(data);
        else FilesManager.instance.setAndSaveAsyncDataBlockThread(data);
    }

    public void saveAllUsers(boolean async) {
        for (User user : UsersManager.instance.users) {
            saveUser(user, async);
        }
    }

    public void loadUsers() {
        ArrayList<User> users = new ArrayList<>();
        for (String str : FilesManager.instance.getData().getKeys(false)) {
            if (str == null) continue;
            String name = FilesManager.instance.getData().getString(str + ".name");
            UUID uuid = UUID.fromString(str);
            long mutedUntil = FilesManager.instance.getData().getLong(str + ".muted.until");
            String mutedReason = FilesManager.instance.getData().getString(str + ".muted.reason");
            String mutedBy = FilesManager.instance.getData().getString(str + ".muted.by");
            long mutedAt = FilesManager.instance.getData().getLong(str + ".muted.at");
            String mutedDuration = FilesManager.instance.getData().getString(str + ".muted.duration");
            long bannedUntil = FilesManager.instance.getData().getLong(str + ".banned.until");
            String bannedReason = FilesManager.instance.getData().getString(str + ".banned.reason");
            String bannedBy = FilesManager.instance.getData().getString(str + ".banned.by");
            long bannedAt = FilesManager.instance.getData().getLong(str + ".banned.at");
            String bannedDuration = FilesManager.instance.getData().getString(str + ".banned.duration");
            boolean isIpBanned = FilesManager.instance.getData().getBoolean(str + ".banned.isBanIp");
            String ip = FilesManager.instance.getData().getString(str + ".ip");
            LinkedList<Sanction> history = User.getHistoryFromString(FilesManager.instance.getData().getString(str + ".history"));

            users.add(new User(uuid, name, mutedUntil, mutedReason, mutedBy, mutedAt, mutedDuration, bannedUntil, bannedReason, bannedBy, bannedAt, isIpBanned, bannedDuration, ip, history));
        }
        UsersManager.instance.users = users;
    }
}
