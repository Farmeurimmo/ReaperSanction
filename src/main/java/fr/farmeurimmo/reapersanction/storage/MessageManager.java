package main.java.fr.farmeurimmo.reapersanction.storage;

import main.java.fr.farmeurimmo.reapersanction.ReaperSanction;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.HashMap;

public class MessageManager {

    public static final HashMap<String, String> messages = new HashMap<>();
    public static MessageManager instance;
    public static String prefix = "";

    public MessageManager() {
        instance = this;

        int count = 0;
        for (String ignored : FilesManager.instance.messagesData.getKeys(false)) count++;

        load();

        if (count <= 1) regenConfig();
        else readFromFile();

        prefix = getPrefix();
    }

    public String getPrefix() {
        return messages.get("Prefix").replace("&", "§");
    }

    /*public CompletableFuture<String> getMessage(String key) {
        return CompletableFuture.supplyAsync(() -> {
            if (messages.containsKey(key)) {
                return messages.get(key).replace("&", "§");
            } else {
                return "&cAn error has occured while getting the message, please contact the administrator ! (debug: " + key + " not found)";
            }
        });
    }*/

    public String getMessage(String key) {
        return (messages.containsKey(key) && key != null ? messages.get(key).replace("&", "§") :
                "§4An error has occured while getting the message, please contact the administrator ! (debug: " + key + " not found)");
    }

    public void regenConfig() {
        if (FilesManager.instance.messagesFile.exists()) {
            File old = new File(ReaperSanction.instance.getDataFolder(), "OLD-Messages.yml");
            try {
                if (old.exists()) old.delete();
                old.createNewFile();
                FileConfiguration oldConfig = YamlConfiguration.loadConfiguration(old);
                for (String key : FilesManager.instance.messagesData.getKeys(false)) {
                    oldConfig.set(key, FilesManager.instance.messagesData.get(key));
                }
                oldConfig.save(old);
            } catch (Exception e) {
                ReaperSanction.instance.getLogger().info("§c§lError in creation of OLD-Messages.yml");
            }
        }

        for (String key : FilesManager.instance.messagesData.getKeys(false))
            FilesManager.instance.messagesData.set(key, null);
        FilesManager.instance.saveMessages();

        save();
        readFromFile();
    }

    public void load() {
        messages.put("Prefix", "&6[&4ReaperSanction&6] ");
        messages.put("ErrorArg", "&cUsage, /rs <player-name>");
        messages.put("ErrorArgAdminCommands", "&cUsage, /rsadmin <command>");
        messages.put("ErrorBanArg", "&cUsage, /ban <player-name> [Reason]");
        messages.put("ErrorTempBanArg", "&cUsage, /tempban <player-name> <number + sec/min/day/year> [Reason]");
        messages.put("ErrorMuteArg", "&cUsage, /mute <player-name> [Reason]");
        messages.put("ErrorTempMuteArg", "&cUsage, /tempmute <player-name> <number + sec/min/day/year> [Reason]");
        messages.put("ErrorUnMuteArg", "&cUsage, /unmute <player-name>");
        messages.put("ErrorBanIpArg", "&cUsage, /ban-ip <player-name> [Reason]");
        messages.put("ErrorUnBanArg", "&cUsage, /unban <player-name>");
        messages.put("ErrorHistoryArg", "&cUsage, /history <player-name>");
        messages.put("SanctionWaitForApplication", "&6The sanction has been successfuly set !");
        messages.put("SanctionWaitEnd", "&6All sanctions has been removed !");
        messages.put("AlreadyMuted", "&cThis player is already muted !");
        messages.put("AlreadyBanned", "&cThis player is already banned !");
        messages.put("NotBanned", "&cThis player is not banned !");
        messages.put("NotMuted", "&cThis player is not muted !");
        messages.put("PlayerGotPermaBan", "&c%player% got banned by %banner% for %reason% !");
        messages.put("PlayerGotTempBan", "&c%player% got tempbanned for %duration% by %banner% for %reason% !");
        messages.put("PlayerGotTempMute", "&c%player% got tempmuted for %duration% by %banner% for %reason% !");
        messages.put("PlayerGotPermaBanIp", "&c%player% got banned ip by %banner% for %reason% !");
        messages.put("PlayerGotPermaMute", "&c%player% got perma mute by %banner% for %reason% !");
        messages.put("MessageToPlayerGotPermaMuted", "&cYou got perma muted by %banner% !");
        messages.put("MessageToPlayerGotTempMuted", "&cYou got muted for %duration% by %banner% !");
        messages.put("PermaMutedPlayerChat", "&cYou can't talk because you are perma muted by %banner% !");
        messages.put("TempMutedPlayerChat", "&cYou can't talk because you are tempmuted by %banner% !");
        messages.put("MuteEnded", "&aYour mute is now done !");
        messages.put("SuccefullyUnbanned", "&c%player% has been unbanned !");
        messages.put("SuccefullyUnmuted", "&c%player% has been unmuted !");
        messages.put("UnkownReasonSpecified", "N/A");
        messages.put("NoPermission", "&cYou don't have the permission to do this !");
        messages.put("ReloadMessage", "&6Reload done !");
        messages.put("InvalidPlayer", "&6Player unknown or offline !");
        messages.put("Report-Sended", "&eYou have successfuly report &6%player% &efor %reason% &e!");
        messages.put("Report-ErrorArg", "&6Usage, /report <player-name>");
        messages.put("Report-Disabled", "&6This command is disabled !");
        messages.put("Report-Obtain", "&6The player %sender% has report %player% for %reason% !");
        messages.put("Report-PlayerNotonline", "&6This player is not online !");
        messages.put("Vanish-Ison", "§aVanish on");
        messages.put("Vanish-Isoff", "§cVanish off");
        messages.put("NotAvailableInConsole", "&cThis command is not available in console !");
        messages.put("PlayerNoHistoryAvailable", "&cThis player has no history available !");
        messages.put("Command-Disabled", "&cThis command is disabled !");
        messages.put("PlayerGotKicked", "&c%player% got kicked by %banner% for %reason% !");
        messages.put("ErrorKickArg", "&cUsage, /kick <player-name> [Reason]");
    }

    public void readFromFile() {
        for (String key : FilesManager.instance.messagesData.getKeys(false)) {
            messages.put(key, FilesManager.instance.messagesData.getString(key));
        }
        save();
    }

    public void save() {
        for (String key : messages.keySet()) {
            FilesManager.instance.messagesData.set(key, messages.get(key));
        }
        FilesManager.instance.saveMessages();
    }

}
