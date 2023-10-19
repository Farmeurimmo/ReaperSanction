package fr.farmeurimmo.reapersanction.core;

import fr.farmeurimmo.reapersanction.core.storage.*;
import fr.farmeurimmo.reapersanction.core.users.UsersManager;
import org.bukkit.command.ConsoleCommandSender;
import org.slf4j.Logger;

import java.io.File;
import java.util.TimeZone;
import java.util.concurrent.CompletableFuture;

public class Main {

    public static Main INSTANCE;
    private ConsoleCommandSender bukkitConsole;
    private java.util.logging.Logger loggerSpigot;
    private Logger loggerVelocity;
    private int loggerInt = -1;

    public Main(Object logger, Object logger2, int i, File dataFolder) {
        INSTANCE = this;

        if (i == 0) { // Spigot
            bukkitConsole = (ConsoleCommandSender) logger;
            loggerSpigot = (java.util.logging.Logger) logger2;
            loggerInt = 0;
        } else if (i == 1) { // Velocity
            loggerVelocity = (Logger) logger;
            loggerInt = 1;
        } else if (i == 2) { // BungeeCord
            loggerSpigot = (java.util.logging.Logger) logger;
            loggerInt = 2;
        }

        sendLogMessage("§a[CORE]§e Loading ReaperSanction...", 0);
        sendLogMessage("§a[CORE]§6 Booting up files...", 0);
        new FilesManager(dataFolder);
        try {
            new DBCredentialsManager();
        } catch (Exception e) {
            e.printStackTrace();
            sendLogMessage("§c[CORE]§4 Unable to load database credentials, stopping server...", 2);
            shutdown();
            return;
        }
        new LocalStorageManager();
        new SettingsManager();
        new MessageManager();

        sendLogMessage("§a[CORE]§6 Starting users manager...", 0);
        new UsersManager();

        sendLogMessage("§a[CORE]§6 Detecting storage and starting it...", 0);
        String STORAGE_METHOD = DBCredentialsManager.INSTANCE.getMethod();
        if (STORAGE_METHOD.equalsIgnoreCase("MYSQL")) {
            Main.INSTANCE.sendLogMessage("§6Found §bMYSQL§6 storage database, trying to connect...", 0);

            getCredentialsAndInitialize();

            try {
                DatabaseManager.INSTANCE.startConnection();
                DatabaseManager.INSTANCE.loadUsers();
            } catch (Exception e) {
                e.printStackTrace();
                Main.INSTANCE.sendLogMessage("Unable to connect to the database, stopping server...", 2);
                Main.INSTANCE.shutdown();
                return;
            }
        } else {
            Main.INSTANCE.sendLogMessage("§6Found §bYAML§6 storage method, starting it...", 0);
            LocalStorageManager.INSTANCE.setup();
        }

        Main.INSTANCE.sendLogMessage("§a[CORE]§6 Detecting TimeZone...", 0);
        selectTimeZone();

        Main.INSTANCE.sendLogMessage("§a[CORE]§6 Starting Discord Webhook...", 0);
        new WebhookManager();

        //TODO: end message of start

        CompletableFuture.runAsync(() -> new UpdateChecker(89580).checkForUpdate(Main.INSTANCE.getPluginVersion()));
    }

    public void reload() {
        FilesManager.INSTANCE.reloadData();

        selectTimeZone();
        WebhookManager.INSTANCE.setup();
    }

    public String getStorageMethod() {
        return DBCredentialsManager.INSTANCE.getMethod();
    }

    public void sendLogMessage(String message, int type) {
        try {
            switch (loggerInt) {
                case 0:
                    if (type == 1) loggerSpigot.warning(getWithoutColor(message));
                    else if (type == 2) loggerSpigot.severe(getWithoutColor(message));
                    else bukkitConsole.sendMessage("§a" + message);
                    break;
                case 1:
                    if (type == 0) loggerVelocity.info(message);
                    else if (type == 1) loggerVelocity.warn(message);
                    else if (type == 2) loggerVelocity.error(message);
                    else throw new RuntimeException("Can't send message : " + message);
                    break;
                case 2:
                    throw new RuntimeException("Can't send info message : " + message);
            }
        } catch (Exception e) {
            throw new RuntimeException("Can't send info message" + message);
        }
    }

    public String getPluginVersion() {
        switch (loggerInt) {
            case 0:
                return fr.farmeurimmo.reapersanction.spigot.ReaperSanction.INSTANCE.getDescription().getVersion();
            case 1:
                return fr.farmeurimmo.reapersanction.proxy.velocity.ReaperSanction.INSTANCE.getPluginVersion();
            case 2:
                // TODO
            default:
                return "ERROR";
        }
    }

    public void getCredentialsAndInitialize() {
        String db_url = "jdbc:mysql://" + DBCredentialsManager.INSTANCE.getHost() + ":" + DBCredentialsManager.INSTANCE.getPort();
        String db_user = DBCredentialsManager.INSTANCE.getUsername();
        String db_password = DBCredentialsManager.INSTANCE.getPassword();

        new DatabaseManager(db_url, db_user, db_password);
    }

    public void shutdown() {
        switch (loggerInt) {
            case 0:
                fr.farmeurimmo.reapersanction.spigot.ReaperSanction.INSTANCE.getServer().shutdown();
                break;
            case 1:
                fr.farmeurimmo.reapersanction.proxy.velocity.ReaperSanction.INSTANCE.getProxy().shutdown();
                break;
            case 2:
                // TODO
            default:
                throw new RuntimeException("Can't shutdown server");
        }
    }

    public String getWithoutColor(String message) {
        return message.replaceAll("§.", "");
    }

    public void selectTimeZone() {
        sendLogMessage("§6Trying to get the TimeZone from config", 0);
        try {
            TimeZone.setDefault(TimeZone.getTimeZone(SettingsManager.INSTANCE.getSetting("timeZone")));
        } catch (Exception e) {
            sendLogMessage("Unable to get the TimeZone from config, using default one...", 1);
            TimeZone.setDefault(TimeZone.getTimeZone("Europe/Paris"));
        }
        sendLogMessage("§6TimeZone set to : §b" + TimeZone.getDefault().getID(), 0);
    }

    public boolean matchRequirementsToMigrateToMYSQL() {
        return getStorageMethod().equalsIgnoreCase("MYSQL");
    }
}
