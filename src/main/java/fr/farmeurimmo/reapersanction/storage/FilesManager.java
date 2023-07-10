package fr.farmeurimmo.reapersanction.storage;

import fr.farmeurimmo.reapersanction.ReaperSanction;
import fr.farmeurimmo.reapersanction.gui.CustomInventories;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.concurrent.CompletableFuture;

public class FilesManager {

    public static FilesManager INSTANCE;
    public FileConfiguration ddata;
    public File dfile;
    public FileConfiguration messagesData;
    public File messagesFile;
    public File inventoryFile;
    public FileConfiguration inventoryData;

    public FilesManager() {
        INSTANCE = this;

        setup();
    }

    public void setup_YAML_Storage() {
        dfile = new File(ReaperSanction.INSTANCE.getDataFolder(), "Sanctions.yml");

        if (!dfile.exists()) {
            try {
                dfile.createNewFile();
            } catch (IOException e) {
                ReaperSanction.INSTANCE.getLogger().info("§c§lError in creation of Sanctions.yml");
            }
        }

        ddata = YamlConfiguration.loadConfiguration(dfile);
    }

    public void setup() {
        messagesFile = new File(ReaperSanction.INSTANCE.getDataFolder(), "Messages.yml");

        if (!messagesFile.exists()) {
            try {
                messagesFile.createNewFile();
            } catch (IOException e) {
                ReaperSanction.INSTANCE.getLogger().info("§c§lError in creation of Messages.yml");
            }
        }

        messagesData = YamlConfiguration.loadConfiguration(messagesFile);
    }

    public void setup_inventory_file() {
        inventoryFile = new File(ReaperSanction.INSTANCE.getDataFolder(), "Inventories.yml");

        if (!inventoryFile.exists()) {
            try {
                inventoryFile.createNewFile();
            } catch (IOException e) {
                ReaperSanction.INSTANCE.getLogger().info("§c§lError in creation of Inventories.yml");
            }
        }

        inventoryData = YamlConfiguration.loadConfiguration(inventoryFile);
    }

    public FileConfiguration getData() {
        return ddata;
    }

    public FileConfiguration getConfig() {
        return ReaperSanction.INSTANCE.getConfig();
    }

    public FileConfiguration getInventoryData() {
        return inventoryData;
    }

    public String getFromConfigFormatted(String key) {
        String toReturn = getConfig().getString(key);
        return (toReturn == null) ? "" : toReturn.replace("&", "§");
    }

    public void deleteAndRecreateDataFile() {
        try {
            dfile.delete();
        } catch (Exception e) {
            ReaperSanction.INSTANCE.getLogger().info("§c§lError in deletion of Sanctions.yml");
        }
        setup_YAML_Storage();
    }

    public void reloadData() {
        try {
            ddata.load(dfile);
        } catch (Exception e) {
            ReaperSanction.INSTANCE.getLogger().info("§c§lError in reloading data for Sanctions.yml!");
            e.printStackTrace();
        }
        try {
            MessageManager.messages.clear();
            messagesData.load(messagesFile);
            MessageManager.INSTANCE.load();
            MessageManager.INSTANCE.readFromFile();
        } catch (Exception e) {
            ReaperSanction.INSTANCE.getLogger().info("§c§lError in reloading data for Messages.yml!");
            e.printStackTrace();
        }
        ReaperSanction.INSTANCE.reloadConfig();
        try {
            inventoryData.load(inventoryFile);
        } catch (Exception e) {
            ReaperSanction.INSTANCE.getLogger().info("§c§lError in reloading data for Inventories.yml!");
            e.printStackTrace();
        }
        CustomInventories.INSTANCE.loadInventories();
    }

    public void saveData() {
        try {
            ddata.save(dfile);
        } catch (IOException e) {
            ReaperSanction.INSTANCE.getLogger().info("§c§lError in save for Sanctions.yml!");
        }
    }

    public void saveMessages() {
        try {
            messagesData.save(messagesFile);
        } catch (IOException e) {
            ReaperSanction.INSTANCE.getLogger().info("§c§lError in save for Messages.yml!");
        }
    }

    public void saveInventory() {
        try {
            inventoryData.save(inventoryFile);
        } catch (IOException e) {
            ReaperSanction.INSTANCE.getLogger().info("§c§lError in save for Inventories.yml!");
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

    public void setAndSaveAsyncDataBlockThread(HashMap<String, Object> values) {
        for (String key : values.keySet()) {
            ddata.set(key, values.get(key));
        }
        saveData();
    }

}
