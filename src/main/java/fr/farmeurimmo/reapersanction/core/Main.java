package fr.farmeurimmo.reapersanction.core;

import fr.farmeurimmo.reapersanction.core.sanctions.SanctionsManager;
import fr.farmeurimmo.reapersanction.core.storage.*;
import fr.farmeurimmo.reapersanction.core.update.UpdateChecker;
import fr.farmeurimmo.reapersanction.core.users.UsersManager;
import org.bukkit.command.ConsoleCommandSender;
import org.slf4j.Logger;

import java.io.File;
import java.util.TimeZone;
import java.util.concurrent.CompletableFuture;

public class Main {

    public static Main INSTANCE;
    private final ServerType serverType;
    private ConsoleCommandSender bukkitConsole;
    private java.util.logging.Logger loggerSpigot;
    private Logger loggerVelocity;

    public Main(Object logger, Object logger2, File dataFolder) {
        INSTANCE = this;

        serverType = getServerType();

        switch (serverType) {
            case SPIGOT:
                if (logger instanceof ConsoleCommandSender && logger2 instanceof java.util.logging.Logger) {
                    bukkitConsole = (ConsoleCommandSender) logger;
                    loggerSpigot = (java.util.logging.Logger) logger2;
                }
                break;
            case BUNGEECORD:
                if (logger instanceof java.util.logging.Logger) {
                    loggerSpigot = (java.util.logging.Logger) logger;
                }
                break;
            case VELOCITY:
                if (logger instanceof Logger) {
                    loggerVelocity = (Logger) logger;
                }
                break;
            default:
                throw new RuntimeException("Can't determine server type");
        }

        sendLogMessage("§a[CORE]§e Loading ReaperSanction on §b" + serverType.name() + "§e server...", 0);
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

        sendLogMessage("§a[CORE]§6 Detecting storage...", 0);
        String STORAGE_METHOD = DBCredentialsManager.INSTANCE.getMethod();
        if (STORAGE_METHOD.equalsIgnoreCase("MYSQL")) {
            Main.INSTANCE.sendLogMessage("§a[CORE]§6 Found §bMYSQL§6 storage database, trying to connect...", 0);

            getCredentialsAndInitialize();

            try {
                DatabaseManager.INSTANCE.startConnection();
                DatabaseManager.INSTANCE.loadUsers();
            } catch (Exception e) {
                e.printStackTrace();
                Main.INSTANCE.sendLogMessage("§a[CORE]§6 Unable to connect to the database, stopping server...", 2);
                Main.INSTANCE.shutdown();
                return;
            }
        } else {
            Main.INSTANCE.sendLogMessage("§a[CORE]§6 Found §bYAML§6 storage method, starting it...", 0);
            LocalStorageManager.INSTANCE.setup();
        }

        Main.INSTANCE.sendLogMessage("§a[CORE]§6 Detecting TimeZone...", 0);
        selectTimeZone();

        Main.INSTANCE.sendLogMessage("§a[CORE]§6 Starting sanctions manager...", 0);
        new SanctionsManager();

        Main.INSTANCE.sendLogMessage("§a[CORE]§6 Starting Discord Webhook...", 0);
        new WebhookManager();

        CompletableFuture.runAsync(() -> new UpdateChecker(89580).checkForUpdate(Main.INSTANCE.getPluginVersion()));
    }

    public void endOfStart() {
        sendLogMessage("§a[CORE]§a ReaperSanction is now enabled", 0);
        sendLogMessage("§eOfficial website : §bhttps://reaper.farmeurimmo.fr/reapersanction", 0);
    }

    public void disable() {
        sendLogMessage("§c[CORE]§4 ReaperSanction is now disabled", 0);
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
            switch (serverType) {
                case SPIGOT:
                    if (type == 1) loggerSpigot.warning(getWithoutColor(message));
                    else if (type == 2) loggerSpigot.severe(getWithoutColor(message));
                    else bukkitConsole.sendMessage("§a" + message);
                    break;
                case VELOCITY:
                    if (type == 0) loggerVelocity.info(message);
                    else if (type == 1) loggerVelocity.warn(message);
                    else if (type == 2) loggerVelocity.error(message);
                    else throw new RuntimeException("Can't send message : " + message);
                    break;
                default:
                    throw new RuntimeException("Can't send info message : " + message);
            }
        } catch (Exception e) {
            throw new RuntimeException("Can't send info message" + message);
        }
    }

    public String getPluginVersion() {
        switch (serverType) {
            case SPIGOT:
                return fr.farmeurimmo.reapersanction.spigot.ReaperSanction.INSTANCE.getDescription().getVersion();
            case VELOCITY:
                return fr.farmeurimmo.reapersanction.proxy.velocity.ReaperSanction.INSTANCE.getPluginVersion();
            case BUNGEECORD:
                // TODO
            default:
                return "ERROR";
        }
    }

    public ServerType getServerType() {
        return ServerType.getServerType();
    }

    public void getCredentialsAndInitialize() {
        String db_url = "jdbc:mysql://" + DBCredentialsManager.INSTANCE.getHost() + ":" + DBCredentialsManager.INSTANCE.getPort();
        String db_user = DBCredentialsManager.INSTANCE.getUsername();
        String db_password = DBCredentialsManager.INSTANCE.getPassword();

        new DatabaseManager(db_url, db_user, db_password);
    }

    public void shutdown() {
        switch (serverType) {
            case SPIGOT:
                fr.farmeurimmo.reapersanction.spigot.ReaperSanction.INSTANCE.getServer().shutdown();
                break;
            case VELOCITY:
                fr.farmeurimmo.reapersanction.proxy.velocity.ReaperSanction.INSTANCE.getProxy().shutdown();
                break;
            case BUNGEECORD:
                // TODO
            default:
                throw new RuntimeException("Can't shutdown server");
        }
    }

    public String getWithoutColor(String message) {
        return message.replaceAll("§.", "");
    }

    public void selectTimeZone() {
        try {
            TimeZone.setDefault(TimeZone.getTimeZone(SettingsManager.INSTANCE.getSetting("timeZone")));
        } catch (Exception e) {
            sendLogMessage("Unable to get the TimeZone from config, using default one...", 1);
            TimeZone.setDefault(TimeZone.getTimeZone("Europe/Paris"));
        }
        sendLogMessage("§a[CORE]§6 TimeZone set to : §b" + TimeZone.getDefault().getID(), 0);
    }

    public boolean matchRequirementsToMigrateToMYSQL() {
        return getStorageMethod().equalsIgnoreCase("MYSQL");
    }
}