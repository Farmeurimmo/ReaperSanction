package fr.farmeurimmo.reapersanction.api;

import fr.farmeurimmo.reapersanction.api.storage.FilesManager;
import fr.farmeurimmo.reapersanction.api.storage.LocalStorageManager;
import fr.farmeurimmo.reapersanction.api.storage.SettingsManager;
import fr.farmeurimmo.reapersanction.api.users.UsersManager;
import org.bukkit.command.ConsoleCommandSender;
import org.slf4j.Logger;

public class Main {

    public static Main INSTANCE;
    private ConsoleCommandSender bukkitConsole;
    private java.util.logging.Logger loggerSpigot;
    private Logger loggerVelocity;
    private int loggerInt = -1;

    public Main(Object logger, Object logger2, int i) {
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
        new FilesManager();
        new LocalStorageManager();
        new SettingsManager();


        sendLogMessage("§a[CORE]§6 Starting users manager...", 0);
        new UsersManager();
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

    public String getWithoutColor(String message) {
        return message.replaceAll("§.", "");
    }
}
