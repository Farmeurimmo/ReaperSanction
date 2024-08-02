package fr.farmeurimmo.reapersanction.core.storage;

import fr.farmeurimmo.reapersanction.core.Main;
import fr.farmeurimmo.reapersanction.core.users.Sanction;
import fr.farmeurimmo.reapersanction.core.users.User;
import fr.farmeurimmo.reapersanction.core.users.UsersManager;
import fr.farmeurimmo.reapersanction.utils.Parser;

import java.util.*;

public class LocalStorageManager {

    public static LocalStorageManager INSTANCE;

    public LocalStorageManager() {
        INSTANCE = this;
    }

    public void setup() {
        FilesManager.INSTANCE.setupSanctions();

        loadUsers();
    }

    public void saveUser(User user) {
        HashMap<String, Object> toSend = new HashMap<>();

        Map<String, Object> data = new HashMap<>();
        data.put("name", user.getName());

        Map<String, Object> muted = new HashMap<>();
        muted.put("until", user.getMutedUntil());
        muted.put("at", user.getMutedAt());
        muted.put("by", user.getMutedBy());
        muted.put("reason", user.getMutedReason());
        muted.put("duration", user.getMutedDuration());
        data.put("muted", muted);

        Map<String, Object> banned = new HashMap<>();
        banned.put("until", user.getBannedUntil());
        banned.put("at", user.getBannedAt());
        banned.put("by", user.getBannedBy());
        banned.put("reason", user.getBannedReason());
        banned.put("duration", user.getBannedDuration());
        banned.put("isBanIp", user.isIpBanned());
        data.put("banned", banned);

        data.put("ip", user.getIp());
        data.put("history", User.getHistoryAsMap(user.getHistory()));

        toSend.put(user.getUuid().toString(), data);

        FilesManager.INSTANCE.setSanctions(toSend);

        Main.INSTANCE.updateBlockedIps();
    }

    public void saveAllUsers() {
        for (User user : UsersManager.INSTANCE.users) {
            saveUser(user);
        }
    }

    public void loadUsers() {
        ArrayList<User> users = new ArrayList<>();
        for (Map.Entry<String, Object> entry : FilesManager.INSTANCE.getSanctions().entrySet()) {
            if (entry.getKey() == null) continue;
            Map<String, Object> data = (Map<String, Object>) entry.getValue();
            String name = (String) data.get("name");
            UUID uuid = UUID.fromString(entry.getKey());

            Map<String, Object> muted = (Map<String, Object>) data.get("muted");
            long mutedUntil = Parser.PARSE_LONG(muted.get("until"));
            String mutedReason = (String) muted.get("reason");
            String mutedBy = (String) muted.get("by");
            long mutedAt = Parser.PARSE_LONG(muted.get("at"));
            String mutedDuration = (String) muted.get("duration");

            Map<String, Object> banned = (Map<String, Object>) data.get("banned");
            long bannedUntil = Parser.PARSE_LONG(banned.get("until"));
            String bannedReason = (String) banned.get("reason");
            String bannedBy = (String) banned.get("by");
            long bannedAt = Parser.PARSE_LONG(banned.get("at"));
            String bannedDuration = (String) banned.get("duration");
            boolean isIpBanned = Parser.PARSE_BOOLEAN(banned.get("isBanIp"));

            String ip = (String) data.get("ip");

            LinkedList<Sanction> history = new LinkedList<>();

            try {
                history = User.getHistoryFromMap((Map<String, Object>) data.get("history"));
            } catch (Exception ignored) {
                try {
                    history = User.getHistoryFromString((String) data.get("history"));
                } catch (Exception ignored2) {
                }
            }

            users.add(new User(uuid, name, mutedUntil, mutedReason, mutedBy, mutedAt, mutedDuration, bannedUntil, bannedReason, bannedBy, bannedAt, isIpBanned, bannedDuration, ip, history));
        }
        UsersManager.INSTANCE.users = users;
        Main.INSTANCE.updateBlockedIps();
    }
}
