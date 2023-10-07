package fr.farmeurimmo.reapersanction.api.storage;

import fr.farmeurimmo.reapersanction.spigot.ReaperSanction;
import fr.farmeurimmo.reapersanction.spigot.gui.CustomInventories;
import org.bukkit.configuration.file.FileConfiguration;
import org.yaml.snakeyaml.Yaml;

import java.io.*;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public class FilesManager {

    public static FilesManager INSTANCE;
    private Map<String, Object> sanctions = new HashMap<>();
    private Map<String, Object> messages = new HashMap<>();
    private Map<String, Object> inventories = new HashMap<>();

    public FilesManager() {
        INSTANCE = this;

        setupMessages();
    }

    public void setupSanctions() {
        try (InputStream inputStream = Files.newInputStream(getSanctionsFile().toPath())) {
            sanctions = (Map<String, Object>) new Yaml().load(inputStream);
        } catch (IOException ignored) {
        }
        if (sanctions == null) sanctions = new HashMap<>();
    }

    public void setupMessages() {
        try (InputStream inputStream = Files.newInputStream(getMessagesFile().toPath())) {
            messages = (Map<String, Object>) new Yaml().load(inputStream);
        } catch (IOException ignored) {
        }
        if (messages == null) messages = new HashMap<>();
    }

    public void setupInventories() {
        try (InputStream inputStream = Files.newInputStream(getInventoryFile().toPath())) {
            inventories = (Map<String, Object>) new Yaml().load(inputStream);
        } catch (IOException ignored) {
        }
        if (inventories == null) inventories = new HashMap<>();
    }

    public String getFromConfigFormatted(String key) {
        String toReturn = ReaperSanction.INSTANCE.getConfig().getString(key);
        return (toReturn == null) ? "" : toReturn.replace("&", "§");
    }

    public void deleteAndRecreateSanctionFile() {
        try {
            getSanctionsFile().delete();
        } catch (Exception e) {
            ReaperSanction.INSTANCE.getLogger().info("§c§lError in deletion of Sanctions.yml");
        }
        setupSanctions();
    }

    public void reloadData() {
        try {
            setupSanctions();
        } catch (Exception e) {
            ReaperSanction.INSTANCE.getLogger().info("§c§lError in reloading data for Sanctions.yml!");
            e.printStackTrace();
        }
        try {
            MessageManager.INSTANCE.clearMessages();
            setupMessages();
            MessageManager.INSTANCE.getMessages().putAll(MessageManager.INSTANCE.getDefaultMessages());
            MessageManager.INSTANCE.readFromFile();
        } catch (Exception e) {
            ReaperSanction.INSTANCE.getLogger().info("§c§lError in reloading data for Messages.yml!");
            e.printStackTrace();
        }
        ReaperSanction.INSTANCE.reloadConfig();
        try {
            setupInventories();
        } catch (Exception e) {
            ReaperSanction.INSTANCE.getLogger().info("§c§lError in reloading data for Inventories.yml!");
            e.printStackTrace();
        }
        CustomInventories.INSTANCE.loadInventories();
    }

    public void saveSanctions() {
        try (Writer writer = new FileWriter(getSanctionsFile())) {
            new Yaml().dump(sanctions, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void saveMessages() {
        try (Writer writer = new FileWriter(getMessagesFile())) {
            new Yaml().dump(messages, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void saveInventories() {
        try (Writer writer = new FileWriter(getInventoryFile())) {
            new Yaml().dump(inventories, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Map<String, Object> getSanctions() {
        return sanctions;
    }

    public void setSanctions(HashMap<String, Object> values) {
        for (String key : values.keySet()) {
            getSanctions().put(key, values.get(key));
        }
        saveSanctions();
    }

    public Map<String, Object> getMessages() {
        return messages;
    }

    public Map<String, Object> getInventories() {
        return inventories;
    }

    public void setMessagesAsync(HashMap<String, Object> values) {
        CompletableFuture.runAsync(() -> {
            for (String key : values.keySet()) {
                getSanctions().put(key, values.get(key));
            }
            saveMessages();
        });
    }

    public void setSanctionsAsync(HashMap<String, Object> values) {
        CompletableFuture.runAsync(() -> {
            setSanctions(values);
        });
    }

    public File getSanctionsFile() {
        File sanction = new File(ReaperSanction.INSTANCE.getDataFolder(), "Sanctions.yml");
        if (!sanction.exists()) {
            try {
                sanction.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return sanction;
    }

    public File getMessagesFile() {
        File messages = new File(ReaperSanction.INSTANCE.getDataFolder(), "Messages.yml");
        if (!messages.exists()) {
            try {
                messages.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return messages;
    }

    public FileConfiguration getConfig() {
        return ReaperSanction.INSTANCE.getConfig();
    }

    public File getInventoryFile() {
        File inventory = new File(ReaperSanction.INSTANCE.getDataFolder(), "Inventories.yml");
        if (!inventory.exists()) {
            try {
                inventory.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return inventory;
    }

    public Map<String, Object> getSanctionsKeys(String startWith) {
        Map<String, Object> toReturn = new HashMap<>();
        for (String key : getSanctions().keySet()) {
            if (key.startsWith(startWith)) {
                toReturn.put(key, getSanctions().get(key));
            }
        }
        return toReturn;
    }

}
