package fr.farmeurimmo.reapersanction.core.storage;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SettingsManager {

    public static SettingsManager INSTANCE;
    private final Map<String, Object> settings = new HashMap<>();

    public SettingsManager() {
        INSTANCE = this;

        settings.putAll(getDefaultSettings());

        loadSettings();

        saveMessages();
    }

    public Map<String, Object> getSettings() {
        return settings;
    }

    public String getSetting(String key) {
        String toReturn = getSettings().get(key).toString();
        return (toReturn == null) ? "" : toReturn.replace("&", "§");
    }

    public Map<String, Object> getDefaultSettings() {
        Map<String, Object> toReturn = new HashMap<>();

        FilesManager.INSTANCE.applyInfosFile(toReturn);

        Map<String, Object> updates = new HashMap<>();
        updates.put("check", true);
        updates.put("channel", "RELEASE-CANDIDATE");
        toReturn.put("updates", updates);

        Map<String, Object> ip = new HashMap<>();
        ip.put("showForAdmin", true);
        ip.put("showForModerator", true);
        toReturn.put("ip", ip);

        toReturn.put("timeZone", "Europe/Paris");
        toReturn.put("hourFormat", "HH:mm:ss");
        toReturn.put("dateFormat", "dd/MM/yyyy");
        toReturn.put("timeFormat", "dd/MM/yyyy HH:mm:ss");

        toReturn.put("context", "global");
        toReturn.put("proxy", false);

        //FIXME: add %until% for temporary sanctions
        Map<String, Object> sanctions = new HashMap<>();
        sanctions.put("banip", Arrays.asList("&cConnection refused", " ",
                "&4You are permanently ip banned from this server", " ", "&6Date of ban : &e%date%", "&6By : &e%banner%",
                "&6Reason : &e%reason%"));
        sanctions.put("ban", Arrays.asList("&cConnection refused", " ",
                "&4You are permanently banned from this server", " ", "&6Date of ban : &e%date%",
                "&6By : &e%banner%", "&6Reason : &e%reason%"));
        sanctions.put("tempban", Arrays.asList("&cConnection refused", " ",
                "&4You are temp banned from this server", " ", "&6Date of ban : &e%date%", "&6By : &e%banner%",
                "&6Reason : &e%reason%", "&6Until : &e%until%"));
        sanctions.put("kick", Arrays.asList("&cConnection stopped", " ",
                "&4You have been kicked from this server", " ", "&6Date of kick : &e%date%", "&6By : &e%banner%",
                "&6Reason : &e%reason%"));
        toReturn.put("sanctions", sanctions);

        Map<String, Object> vanish = new HashMap<>();
        vanish.put("status", true);
        vanish.put("changeGamemode", true);
        vanish.put("gamemode", 2);
        vanish.put("fly", true);
        vanish.put("exitGamemode", true);
        vanish.put("exitGamemodeType", 0);
        toReturn.put("vanish", vanish);

        Map<String, Object> report = new HashMap<>();
        report.put("status", true);
        toReturn.put("report", report);

        return toReturn;
    }

    public String getSanctionMessage(String type) {
        Map<String, Object> sanctions = (Map<String, Object>) getSettings().get("sanctions");
        if (sanctions == null) return "";
        if (sanctions.get(type) == null) return "";
        return String.join("\n", (List<String>) sanctions.get(type)).replace("&", "§");
    }

    protected void loadSettings() {
        for (Map.Entry<String, Object> entry : FilesManager.INSTANCE.getSettings().entrySet()) {
            if (entry.getKey() == null) continue;
            if (entry.getKey().equals("infos-file")) continue;
            String path = entry.getKey() + ".";
            if (entry.getValue() instanceof Map) {
                for (Map.Entry<String, Object> entry1 : ((Map<String, Object>) entry.getValue()).entrySet()) {
                    if (entry1.getKey() == null) continue;
                    settings.put(path + entry1.getKey(), entry1.getValue());
                }
                continue;
            }
            settings.put(entry.getKey(), entry.getValue());
        }
    }

    protected void saveMessages() {
        HashMap<String, Object> toSend = new HashMap<>(getSettings());
        FilesManager.INSTANCE.applyInfosFile(toSend);

        FilesManager.INSTANCE.setSettings(toSend);
    }


}
