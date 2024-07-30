package fr.farmeurimmo.reapersanction.core.storage;

import java.util.HashMap;
import java.util.Map;

public class MessageManager {

    public static MessageManager INSTANCE;
    public static String PREFIX = "";
    private final Map<String, String> messages = new HashMap<>();

    public MessageManager() {
        INSTANCE = this;

        int count = FilesManager.INSTANCE.getMessages().size();

        getMessages().putAll(getDefaultMessages());

        if (count <= 1) regen();
        else loadFromMap();

        saveMessages();

        PREFIX = getPrefix();
    }

    public void clearMessages() {
        getMessages().clear();
    }

    public String getPrefix() {
        return getMessages().get("Prefix").replace("&", "§");
    }

    public String getMessage(String key, boolean withPrefix) {
        return (withPrefix ? PREFIX : "") + (getMessages().containsKey(key) && key != null ? getMessages().get(key).replace("&", "§") :
                "§4An error has occured while getting the message, please contact the administrator ! (debug: " + key + " not found)");
    }

    protected void regen() {
        FilesManager.INSTANCE.setupMessages();

        saveMessages();

        loadFromMap();
    }

    public Map<String, String> getDefaultMessages() {
        Map<String, String> toReturn = new HashMap<>();

        toReturn.put("Prefix", "&6[&4ReaperSanction&6] ");
        toReturn.put("ErrorArg", "&cUsage, /rs <player-name>");
        toReturn.put("ErrorArgAdminCommands", "&cUsage, /rsadmin <command>");
        toReturn.put("ErrorBanArg", "&cUsage, /ban <player-name> [Reason]");
        toReturn.put("ErrorTempBanArg", "&cUsage, /tempban <player-name> <number + sec/min/day/year> [Reason]");
        toReturn.put("ErrorMuteArg", "&cUsage, /mute <player-name> [Reason]");
        toReturn.put("ErrorTempMuteArg", "&cUsage, /tempmute <player-name> <number + sec/min/day/year> [Reason]");
        toReturn.put("ErrorUnMuteArg", "&cUsage, /unmute <player-name>");
        toReturn.put("ErrorBanIpArg", "&cUsage, /ban-ip <player-name> [Reason]");
        toReturn.put("ErrorUnBanArg", "&cUsage, /unban <player-name>");
        toReturn.put("ErrorHistoryArg", "&cUsage, /history <player-name>");
        toReturn.put("SanctionWaitForApplication", "&6The sanction has been successfuly set !");
        toReturn.put("SanctionWaitEnd", "&6All sanctions has been removed !");
        toReturn.put("AlreadyMuted", "&cThis player is already muted !");
        toReturn.put("AlreadyBanned", "&cThis player is already banned !");
        toReturn.put("NotBanned", "&cThis player is not banned !");
        toReturn.put("NotMuted", "&cThis player is not muted !");
        toReturn.put("MessageToPlayerGotPermaMuted", "&cYou got perma muted by %banner% !");
        toReturn.put("MessageToPlayerGotTempMuted", "&cYou got muted for %duration% by %banner% !");
        toReturn.put("PermaMutedPlayerChat", "&cYou can't talk because you are perma muted by %banner% !");
        toReturn.put("TempMutedPlayerChat", "&cYou can't talk because you are tempmuted by %banner% !");
        toReturn.put("MuteEnded", "&aYour mute is now done !");
        toReturn.put("SuccessfullyUnbanned", "&c%player% has been unbanned !");
        toReturn.put("SuccessfullyUnmuted", "&c%player% has been unmuted !");
        toReturn.put("UnknownReasonSpecified", "N/A");
        toReturn.put("NoPermission", "&cYou don't have the permission to do this !");
        toReturn.put("ReloadMessage", "&6Reload done !");
        toReturn.put("InvalidPlayer", "&6Player unknown or offline !");
        toReturn.put("Report-Sended", "&eYou have successfuly report &6%player% &efor %reason% &e!");
        toReturn.put("Report-ErrorArg", "&6Usage, /report <player-name>");
        toReturn.put("Report-Disabled", "&6This command is disabled !");
        toReturn.put("Report-Obtain", "&6The player %sender% has report %player% for %reason% !");
        toReturn.put("Report-PlayerNotOnline", "&6This player is not online !");
        toReturn.put("Vanish-Ison", "§aVanish on");
        toReturn.put("Vanish-Isoff", "§cVanish off");
        toReturn.put("NotAvailableInConsole", "&cThis command is not available in console !");
        toReturn.put("PlayerNoHistoryAvailable", "&cThis player has no history available !");
        toReturn.put("Command-Disabled", "&cThis command is disabled !");
        toReturn.put("PlayerGotKicked", "&c%player% got kicked by %banner% for %reason% !");
        toReturn.put("ErrorKickArg", "&cUsage, /kick <player-name> [Reason]");

        return toReturn;
    }

    public void loadFromMap() {
        for (Map.Entry<String, Object> entry : FilesManager.INSTANCE.getMessages().entrySet()) {
            if (entry.getKey() == null) continue;
            if (entry.getKey().equals("infos-file")) continue;
            getMessages().put(entry.getKey(), (String) entry.getValue());
        }
    }

    public Map<String, String> getMessages() {
        return messages;
    }

    protected void saveMessages() {
        Map<String, Object> toSend = new HashMap<>(getMessages());
        FilesManager.INSTANCE.applyInfosFile(toSend);

        FilesManager.INSTANCE.getMessages().putAll(toSend);

        FilesManager.INSTANCE.saveMessages();
    }

}
