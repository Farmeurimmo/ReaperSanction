package main.java.fr.farmeurimmo.reapersanction;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.concurrent.CompletableFuture;

public class ConfigManager {

    public static ConfigManager instance;
    public FileConfiguration ddata;
    public File dfile;
    public FileConfiguration messagesData;
    public File messagesFile;

    public ConfigManager() {
        instance = this;

        setup();
    }

    public void setup() {
        dfile = new File(ReaperSanction.instance.getDataFolder(), "Sanctions.yml");

        if (!dfile.exists()) {
            try {
                dfile.createNewFile();
            } catch (IOException e) {
                ReaperSanction.instance.getLogger().info("§c§lError in creation of Sanctions.yml");
            }
        }

        ddata = YamlConfiguration.loadConfiguration(dfile);

        messagesFile = new File(ReaperSanction.instance.getDataFolder(), "Messages.yml");

        if (!messagesFile.exists()) {
            try {
                messagesFile.createNewFile();
            } catch (IOException e) {
                ReaperSanction.instance.getLogger().info("§c§lError in creation of Messages.yml");
            }
        }

        messagesData = YamlConfiguration.loadConfiguration(messagesFile);
    }

    public FileConfiguration getData() {
        return ddata;
    }

    public FileConfiguration getConfig() {
        return ReaperSanction.instance.getConfig();
    }

    public String getFromConfigFormatted(String key) {
        return getConfig().getString(key).replace("&", "§");
    }

    public void reloadData() {
        try {
            ddata.load(dfile);
        } catch (Exception e) {
            ReaperSanction.instance.getLogger().info("§c§lError in reloading data for Sanctions.yml!");
            e.printStackTrace();
        }
        try {
            MessageManager.messages.clear();
            messagesData.load(messagesFile);
            MessageManager.instance.load();
            MessageManager.instance.readFromFile();
        } catch (Exception e) {
            ReaperSanction.instance.getLogger().info("§c§lError in reloading data for Messages.yml!");
            e.printStackTrace();
        }
        ReaperSanction.instance.reloadConfig();
    }

    public void saveData() {
        try {
            ddata.save(dfile);
        } catch (IOException e) {
            ReaperSanction.instance.getLogger().info("§c§lError in save for Sanctions.yml!");
        }
    }

    public void saveMessages() {
        try {
            messagesData.save(messagesFile);
        } catch (IOException e) {
            ReaperSanction.instance.getLogger().info("§c§lError in save for Messages.yml!");
        }
    }

    public FileConfiguration getMessagesData() {
        return messagesData;
    }

    public void setAndSaveAsyncMessages(HashMap<String, Object> values) {
        CompletableFuture.runAsync(() -> {
            for (String key : values.keySet()) {
                ddata.set(key, values.get(key));
            }
            saveData();
        });
    }

    public void setAndSaveAsyncData(HashMap<String, Object> values) {
        CompletableFuture.runAsync(() -> {
            for (String key : values.keySet()) {
                ddata.set(key, values.get(key));
            }
            saveData();
        });
    }

}
