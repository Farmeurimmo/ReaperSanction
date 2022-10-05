package main.java.fr.farmeurimmo.reapersanction.sanctions;

import main.java.fr.farmeurimmo.reapersanction.ConfigManager;
import main.java.fr.farmeurimmo.reapersanction.MessageManager;
import main.java.fr.farmeurimmo.reapersanction.ReaperSanction;
import main.java.fr.farmeurimmo.reapersanction.utils.TimeConverter;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ApplySanction {

    public static ApplySanction instance;

    public ApplySanction() {
        instance = this;
    }

    public void ApplyPermaBan(Player player, String reason, String banner, String string) {
        ConfigManager.instance.getData().set(player.getName() + ".ban.banner", banner);
        ConfigManager.instance.getData().set(player.getName() + ".ban.reason", reason);
        ConfigManager.instance.getData().set(player.getName() + ".ban.date", string);
        ConfigManager.instance.getData().set(player.getName() + ".ban.isbanned", true);
        ConfigManager.instance.saveData();
        Bukkit.broadcastMessage(TimeConverter.replaceArgs(MessageManager.prefix
                        + MessageManager.instance.getMessage("PlayerGotPermaBan"),
                "null", "null", player.getName(), banner, reason));
    }

    public void ApplyPermaBanIp(Player player, String reason, String banner, String string) {
        ConfigManager.instance.getData().set(player.getName() + ".ban-ip.banner", banner);
        ConfigManager.instance.getData().set(player.getName() + ".ban-ip.reason", reason);
        ConfigManager.instance.getData().set(player.getName() + ".ban-ip.date", string);
        String ip = player.getAddress().getHostString();
        String partialIp = (ip.contains("l") ? ip : ip.substring(0, ip.lastIndexOf(".")));
        ReaperSanction.instance.ipblocked.put(partialIp, player.getName());
        ConfigManager.instance.getData().set(player.getName() + ".ban-ip.ip", partialIp);
        ConfigManager.instance.getData().set(player.getName() + ".ban-ip.isipbanned", true);
        ConfigManager.instance.saveData();
        Bukkit.broadcastMessage(TimeConverter.replaceArgs(MessageManager.prefix
                        + MessageManager.instance.getMessage("PlayerGotPermaBanIp"),
                "null", "null", player.getName(), banner, reason));
    }

    public void ApplyTempBan(String player, String reason, CommandSender sender, String string, String Expiration, String duration, String type) {
        if (!ConfigManager.instance.getData().getBoolean(player + ".mute.isbanned")) {
            ConfigManager.instance.getData().set(player + ".tempban.banner", sender.getName());
            ConfigManager.instance.getData().set(player + ".tempban.reason", reason);
            ConfigManager.instance.getData().set(player + ".tempban.date", string);
            ConfigManager.instance.getData().set(player + ".tempban.expiration", Expiration);
            ConfigManager.instance.getData().set(player + ".tempban.unit", type);
            long timemillis = System.currentTimeMillis();
            if (type.equalsIgnoreCase("sec")) {
                timemillis = timemillis + Integer.parseInt(duration) * 1000L;
            }
            if (type.equalsIgnoreCase("min")) {
                timemillis = timemillis + Integer.parseInt(duration) * 60000L;
            }
            if (type.equalsIgnoreCase("hour")) {
                timemillis = timemillis + Integer.parseInt(duration) * 360000L;
            }
            if (type.equalsIgnoreCase("day")) {
                timemillis = timemillis + Integer.parseInt(duration) * 86400000L;
            }
            if (type.equalsIgnoreCase("year")) {
                timemillis = timemillis + (long) Integer.parseInt(duration) * 31536000 * 100;
            }
            ConfigManager.instance.getData().set(player + ".tempban.duration", duration);
            ConfigManager.instance.getData().set(player + ".tempban.timemillis", timemillis);
            ConfigManager.instance.getData().set(player + ".tempban.istempbanned", true);
            ConfigManager.instance.saveData();
            Bukkit.broadcastMessage(TimeConverter.replaceArgs(MessageManager.prefix
                            + MessageManager.instance.getMessage("PlayerGotTempBan"),
                    duration, type, player, sender.getName(), reason));
        } else {
            sender.sendMessage(MessageManager.prefix +
                    MessageManager.instance.getMessage("AlreadyBanned"));
        }
    }

    public void ApplyTempMute(String player, String reason, CommandSender sender, String duration, String type) {
        if (!ConfigManager.instance.getData().getBoolean(player + ".mute.ismuted")) {
            ConfigManager.instance.getData().set(player + ".tempmute.banner", sender.getName());
            ConfigManager.instance.getData().set(player + ".tempmute.reason", reason);
            ConfigManager.instance.getData().set(player + ".tempmute.duration", duration);
            ConfigManager.instance.getData().set(player + ".tempmute.unit", type);
            long timemillis = System.currentTimeMillis();
            if (type.equalsIgnoreCase("sec")) {
                timemillis = timemillis + Integer.parseInt(duration) * 1000L;
            }
            if (type.equalsIgnoreCase("min")) {
                timemillis = timemillis + Integer.parseInt(duration) * 60000L;
            }
            if (type.equalsIgnoreCase("hour")) {
                timemillis = timemillis + Integer.parseInt(duration) * 360000L;
            }
            if (type.equalsIgnoreCase("day")) {
                timemillis = timemillis + Integer.parseInt(duration) * 86400000L;
            }
            if (type.equalsIgnoreCase("year")) {
                timemillis = timemillis + (long) Integer.parseInt(duration) * 31536000 * 100;
            }
            ConfigManager.instance.getData().set(player + ".tempmute.timemillis", timemillis);
            ConfigManager.instance.getData().set(player + ".tempmute.istempmuted", true);
            ConfigManager.instance.saveData();
            Bukkit.getPlayer(player).sendMessage(TimeConverter.replaceArgs(MessageManager.prefix
                            + MessageManager.instance.getMessage("MessageToPlayerGotTempMuted"),
                    duration, type, player, sender.getName(), reason));

            Bukkit.broadcastMessage(MessageManager.prefix +
                    TimeConverter.replaceArgs(MessageManager.instance.getMessage("PlayerGotTempMute"),
                            duration, type, player, sender.getName(), reason));
        } else {
            sender.sendMessage(MessageManager.prefix +
                    MessageManager.instance.getMessage("AlreadyMuted"));
        }
    }

    public void ApplyPermaMute(String player, String reason, String banner, CommandSender sender) {
        ConfigManager.instance.getData().set(player + ".mute.banner", banner);
        ConfigManager.instance.getData().set(player + ".mute.reason", reason);
        ConfigManager.instance.getData().set(player + ".mute.ismuted", true);
        ConfigManager.instance.saveData();
        Bukkit.getPlayer(player).sendMessage(MessageManager.prefix + TimeConverter.replaceArgs(MessageManager.instance.getMessage("MessageToPlayerGotPermaMuted"),
                "null", "null", player, sender.getName(), reason));

        Bukkit.broadcastMessage(MessageManager.prefix + TimeConverter.replaceArgs(MessageManager.instance.getMessage("PlayerGotPermaMute"),
                "null", "null", player, sender.getName(), reason));
    }
}
