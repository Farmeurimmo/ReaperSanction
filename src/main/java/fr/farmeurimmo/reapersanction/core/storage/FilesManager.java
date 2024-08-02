package fr.farmeurimmo.reapersanction.core.storage;

import fr.farmeurimmo.reapersanction.core.Main;
import fr.farmeurimmo.reapersanction.spigot.ReaperSanction;
import fr.farmeurimmo.reapersanction.spigot.gui.CustomInventories;
import fr.farmeurimmo.reapersanction.utils.TimeConverter;
import org.yaml.snakeyaml.Yaml;

import java.io.*;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;

public class FilesManager {

    public static FilesManager INSTANCE;
    private final File dataFolder;
    private Map<String, Object> sanctions = new HashMap<>();
    private Map<String, Object> messages = new HashMap<>();
    private Map<String, Object> inventories = new HashMap<>();
    private Map<String, Object> settings = new HashMap<>();
    private Map<String, Object> dbCredentials = new HashMap<>();
    private Map<String, Object> webhooks = new HashMap<>();

    public FilesManager(File dataFolder) {
        INSTANCE = this;

        this.dataFolder = dataFolder;
        if (!dataFolder.exists()) dataFolder.mkdirs();

        setupMessages();
        setupConfig();
        setupDbCredentials();
        setupWebhooks();
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

    public void setupConfig() {
        try (InputStream inputStream = Files.newInputStream(getSettingsFile().toPath())) {
            settings = (Map<String, Object>) new Yaml().load(inputStream);
        } catch (IOException ignored) {
        }
        if (settings == null) settings = new HashMap<>();
    }

    public void setupDbCredentials() {
        try (InputStream inputStream = Files.newInputStream(getDbCredentialsFile().toPath())) {
            dbCredentials = (Map<String, Object>) new Yaml().load(inputStream);
        } catch (IOException ignored) {
        }
        if (dbCredentials == null) dbCredentials = new HashMap<>();
    }

    public void setupWebhooks() {
        try (InputStream inputStream = Files.newInputStream(getWebhooksFile().toPath())) {
            webhooks = (Map<String, Object>) new Yaml().load(inputStream);
        } catch (IOException ignored) {
        }
        if (webhooks == null) webhooks = new HashMap<>();
    }

    public void deleteAndRecreateSanctionFile() {
        try {
            getSanctionsFile().delete();
        } catch (Exception e) {
            Main.INSTANCE.sendLogMessage("§c§lError in deletion of Sanctions.yml", 1);
        }
        setupSanctions();
    }

    public void reloadData() {
        try {
            setupSanctions();
        } catch (Exception e) {
            Main.INSTANCE.sendLogMessage("§c§lError in reloading data for Sanctions.yml!", 1);
            e.printStackTrace();
        }
        try {
            setupConfig();
        } catch (Exception e) {
            Main.INSTANCE.sendLogMessage("§c§lError in reloading data for Settings.yml!", 1);
        }
        try {
            setupDbCredentials();
        } catch (Exception e) {
            Main.INSTANCE.sendLogMessage("§c§lError in reloading data for Storage.yml!", 1);
        }
        try {
            setupWebhooks();
        } catch (Exception e) {
            Main.INSTANCE.sendLogMessage("§c§lError in reloading data for Webhooks.yml!", 1);
        }
        try {
            MessageManager.INSTANCE.clearMessages();
            setupMessages();
            MessageManager.INSTANCE.getMessages().putAll(MessageManager.INSTANCE.getDefaultMessages());
            MessageManager.INSTANCE.loadFromMap();
        } catch (Exception e) {
            Main.INSTANCE.sendLogMessage("§c§lError in reloading data for Messages.yml!", 1);
            e.printStackTrace();
        }
        ReaperSanction.INSTANCE.reloadConfig();
        try {
            setupInventories();
        } catch (Exception e) {
            Main.INSTANCE.sendLogMessage("§c§lError in reloading data for Inventories.yml!", 1);
            e.printStackTrace();
        }
        CustomInventories.INSTANCE.loadInventories();
    }

    protected void saveFile(File file, Map<String, Object> map) {
        try (Writer writer = new FileWriter(file)) {
            new Yaml().dump(map, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void saveSanctions() {
        saveFile(getSanctionsFile(), sanctions);
    }

    public void saveMessages() {
        saveFile(getMessagesFile(), messages);
    }

    public void saveInventories() {
        saveFile(getInventoryFile(), inventories);
    }

    public void saveSettings() {
        saveFile(getSettingsFile(), settings);
    }

    public void saveDbCredentials() {
        saveFile(getDbCredentialsFile(), dbCredentials);
    }

    public void saveWebhooks() {
        saveFile(getWebhooksFile(), webhooks);
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

    public Map<String, Object> getSettings() {
        return settings;
    }

    public void setSettings(HashMap<String, Object> values) {
        for (String key : values.keySet()) {
            getSettings().put(key, values.get(key));
        }
        saveSettings();
    }

    public Map<String, Object> getDbCredentials() {
        return dbCredentials;
    }

    public Map<String, Object> getWebhooks() {
        return webhooks;
    }

    protected File getFileOrCreateIt(File folder, String fileName) {
        File file = new File(folder, fileName);
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                Main.INSTANCE.sendLogMessage("§c§lError in creation of " + fileName, 1);
            }
        }
        return file;
    }

    public File getSanctionsFile() {
        return getFileOrCreateIt(dataFolder, "Sanctions.yml");
    }

    public File getMessagesFile() {
        return getFileOrCreateIt(dataFolder, "Messages.yml");
    }

    public File getInventoryFile() {
        return getFileOrCreateIt(dataFolder, "Inventories.yml");
    }

    public File getSettingsFile() {
        return getFileOrCreateIt(dataFolder, "Settings.yml");
    }

    public File getDbCredentialsFile() {
        return getFileOrCreateIt(dataFolder, "Storage.yml");
    }

    public File getWebhooksFile() {
        return getFileOrCreateIt(dataFolder, "Webhooks.yml");
    }

    public void applyInfosFile(Map<String, Object> map) {
        Map<String, Object> version = new HashMap<>();
        version.put("last-update-of-missing", TimeConverter.getDateFormatted(System.currentTimeMillis()));
        version.put("plugin-version", Main.INSTANCE.getPluginVersion());
        version.put("documentation", "https://farmeurimmo.fr/projects/reapersanction");

        map.put("infos-file", version);
    }

}
