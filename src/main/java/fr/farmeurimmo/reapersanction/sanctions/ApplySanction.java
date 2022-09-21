package main.java.fr.farmeurimmo.reapersanction.sanctions;

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
        ReaperSanction.instance.getData().set(player.getName() + ".ban.banner", banner);
        ReaperSanction.instance.getData().set(player.getName() + ".ban.reason", reason);
        ReaperSanction.instance.getData().set(player.getName() + ".ban.date", string);
        ReaperSanction.instance.getData().set(player.getName() + ".ban.isbanned", true);
        ReaperSanction.instance.saveData();
        Bukkit.broadcastMessage(TimeConverter.replaceArgs(ReaperSanction.instance.Preffix
                        + ReaperSanction.instance.getConfig().getString("ReaperSanction.Settings.PlayerGotPermaBan"),
                "null", "null", player.getName(), banner, reason));
    }

    public void ApplyPermaBanIp(Player player, String reason, String banner, String string) {
        ReaperSanction.instance.getData().set(player.getName() + ".ban-ip.banner", banner);
        ReaperSanction.instance.getData().set(player.getName() + ".ban-ip.reason", reason);
        ReaperSanction.instance.getData().set(player.getName() + ".ban-ip.date", string);
        String ip = player.getAddress().getHostString();
        String partialIp = ip.substring(0, ip.lastIndexOf("."));
        ReaperSanction.instance.ipblocked.put(partialIp, player.getName());
        ReaperSanction.instance.getData().set(player.getName() + ".ban-ip.ip", partialIp);
        ReaperSanction.instance.getData().set(player.getName() + ".ban-ip.isipbanned", true);
        ReaperSanction.instance.saveData();
        Bukkit.broadcastMessage(TimeConverter.replaceArgs(ReaperSanction.instance.Preffix
                        + ReaperSanction.instance.getConfig().getString("ReaperSanction.Settings.PlayerGotPermaBanIp"),
                "null", "null", player.getName(), banner, reason));
    }

    public void ApplyTempBan(String player, String reason, CommandSender sender, String string, String Expiration, String duration, String type) {
        if (!ReaperSanction.instance.getData().getBoolean(player + ".mute.isbanned")) {
            ReaperSanction.instance.getData().set(player + ".tempban.banner", sender.getName());
            ReaperSanction.instance.getData().set(player + ".tempban.reason", reason);
            ReaperSanction.instance.getData().set(player + ".tempban.date", string);
            ReaperSanction.instance.getData().set(player + ".tempban.expiration", Expiration);
            ReaperSanction.instance.getData().set(player + ".tempban.unit", type);
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
            ReaperSanction.instance.getData().set(player + ".tempban.duration", duration);
            ReaperSanction.instance.getData().set(player + ".tempban.timemillis", timemillis);
            ReaperSanction.instance.getData().set(player + ".tempban.istempbanned", true);
            ReaperSanction.instance.saveData();
            Bukkit.broadcastMessage(TimeConverter.replaceArgs(ReaperSanction.instance.Preffix
                            + ReaperSanction.instance.getConfig().getString("ReaperSanction.Settings.PlayerGotTempBan"),
                    duration, type, player, sender.getName(), reason));
        } else {
            sender.sendMessage(ReaperSanction.instance.Preffix +
                    ReaperSanction.instance.getConfig().getString("ReaperSanction.Settings.AlreadyBanned").replace("&", "§"));
        }
    }

    public void ApplyTempMute(String player, String reason, CommandSender sender, String duration, String type) {
        if (!ReaperSanction.instance.getData().getBoolean(player + ".mute.ismuted")) {
            ReaperSanction.instance.getData().set(player + ".tempmute.banner", sender.getName());
            ReaperSanction.instance.getData().set(player + ".tempmute.reason", reason);
            ReaperSanction.instance.getData().set(player + ".tempmute.duration", duration);
            ReaperSanction.instance.getData().set(player + ".tempmute.unit", type);
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
            ReaperSanction.instance.getData().set(player + ".tempmute.timemillis", timemillis);
            ReaperSanction.instance.getData().set(player + ".tempmute.istempmuted", true);
            ReaperSanction.instance.saveData();
            Bukkit.getPlayer(player).sendMessage(TimeConverter.replaceArgs(ReaperSanction.instance.Preffix
                            + ReaperSanction.instance.getConfig().getString("ReaperSanction.Settings.MessageToPlayerGotTempMuted"),
                    duration, type, player, sender.getName(), reason));

            Bukkit.broadcastMessage(ReaperSanction.instance.Preffix +
                    TimeConverter.replaceArgs(ReaperSanction.instance.getConfig().getString("ReaperSanction.Settings.PlayerGotTempMute"),
                            duration, type, player, sender.getName(), reason));
        } else {
            sender.sendMessage(ReaperSanction.instance.Preffix +
                    ReaperSanction.instance.getConfig().getString("ReaperSanction.Settings.AlreadyMuted").replace("&", "§"));
        }
    }

    public void ApplyPermaMute(String player, String reason, String banner, CommandSender sender) {
        ReaperSanction.instance.getData().set(player + ".mute.banner", banner);
        ReaperSanction.instance.getData().set(player + ".mute.reason", reason);
        ReaperSanction.instance.getData().set(player + ".mute.ismuted", true);
        ReaperSanction.instance.saveData();
        Bukkit.getPlayer(player).sendMessage(ReaperSanction.instance.Preffix + TimeConverter.replaceArgs(ReaperSanction.instance.getConfig()
                        .getString("ReaperSanction.Settings.MessageToPlayerGotPermaMuted"),
                "null", "null", player, sender.getName(), reason));

        Bukkit.broadcastMessage(ReaperSanction.instance.Preffix + TimeConverter.replaceArgs(ReaperSanction.instance.getConfig()
                        .getString("ReaperSanction.Settings.PlayerGotPermaMute"),
                "null", "null", player, sender.getName(), reason));
    }
}
