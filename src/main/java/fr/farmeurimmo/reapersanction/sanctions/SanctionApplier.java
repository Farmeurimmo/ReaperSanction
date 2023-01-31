package main.java.fr.farmeurimmo.reapersanction.sanctions;

import main.java.fr.farmeurimmo.reapersanction.storage.FilesManager;
import main.java.fr.farmeurimmo.reapersanction.storage.MessageManager;
import main.java.fr.farmeurimmo.reapersanction.users.Sanction;
import main.java.fr.farmeurimmo.reapersanction.users.User;
import main.java.fr.farmeurimmo.reapersanction.users.UsersManager;
import main.java.fr.farmeurimmo.reapersanction.utils.TimeConverter;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Objects;

public class SanctionApplier {

    public static SanctionApplier instance;

    public SanctionApplier() {
        instance = this;
    }

    public void ApplyPermaBan(Player player, String reason, String banner) {
        User user = UsersManager.instance.getUserAndCreateIfNotExists(player.getUniqueId(), player.getName());
        Sanction sanction = new Sanction(1, reason, banner, System.currentTimeMillis(), -1, true, false, "Permanent");
        user.setBannedAt(sanction.getAt());
        user.setBannedUntil(sanction.getUntil());
        user.setBannedBy(sanction.getBy());
        user.setBannedReason(sanction.getReason());
        user.setIpBanned(sanction.isIp());
        user.setIp(player.getAddress().getAddress().getHostAddress());
        user.setBannedDuration(sanction.getDuration());
        user.addSanction(sanction);
        user.requestUserUpdate();

        Bukkit.broadcastMessage(TimeConverter.replaceArgs(MessageManager.prefix
                        + MessageManager.instance.getMessage("PlayerGotPermaBan"),
                "null", player.getName(), banner, reason, user.getBannedAt(), user.getBannedUntil()));
    }

    public void ApplyPermaBanIp(Player player, String reason, String banner) {
        /*String ip = player.getAddress().getHostString();
        String partialIp = (ip.contains("l") ? ip : ip.substring(0, ip.lastIndexOf(".")));
        ReaperSanction.instance.ipblocked.put(partialIp, player.getName());*/

        User user = UsersManager.instance.getUserAndCreateIfNotExists(player.getUniqueId(), player.getName());
        Sanction sanction = new Sanction(0, reason, banner, System.currentTimeMillis(), -1, true, true, "Permanent");
        user.setBannedUntil(sanction.getUntil());
        user.setBannedBy(sanction.getBy());
        user.setBannedReason(sanction.getReason());
        user.setBannedAt(sanction.getAt());
        user.setIpBanned(sanction.isIp());
        user.setIp(player.getAddress().getAddress().getHostAddress());
        user.setBannedDuration(sanction.getDuration());
        user.addSanction(sanction);
        user.requestUserUpdate();

        Bukkit.broadcastMessage(TimeConverter.replaceArgs(MessageManager.prefix
                        + MessageManager.instance.getMessage("PlayerGotPermaBanIp"),
                "null", player.getName(), banner, reason, user.getBannedAt(), user.getBannedUntil()));
    }

    public void ApplyTempBan(Player player, String reason, CommandSender sender, String duration, String type) {
        User user = UsersManager.instance.getUserAndCreateIfNotExists(player.getUniqueId(), player.getName());

        if (user.isPermaBan()) {
            sender.sendMessage(MessageManager.prefix +
                    MessageManager.instance.getMessage("AlreadyBanned"));
            return;
        }
        long timemillis = getMillisOfEmission(System.currentTimeMillis(), duration, type);

        duration = duration + type.replace("sec", " second(s)").replace("min", " minute(s)")
                .replace("day", " day(s)").replace("hour", " hour(s)").replace("year", " year(s)");
        Sanction sanction = new Sanction(2, reason, sender.getName(), System.currentTimeMillis(), timemillis, true, false, duration);
        user.setBannedBy(sanction.getBy());
        user.setBannedUntil(sanction.getUntil());
        user.setBannedAt(sanction.getAt());
        user.setIpBanned(sanction.isIp());
        user.setBannedReason(sanction.getReason());
        user.setIp(player.getAddress().getAddress().getHostAddress());
        user.setBannedDuration(sanction.getDuration());
        user.addSanction(sanction);
        user.requestUserUpdate();

        Bukkit.broadcastMessage(TimeConverter.replaceArgs(MessageManager.prefix
                        + MessageManager.instance.getMessage("PlayerGotTempBan"),
                duration, player.getName(), sender.getName(), reason, user.getBannedAt(), user.getBannedUntil()));

        player.kickPlayer(FilesManager.instance.getFromConfigFormatted("TempBan.lines")
                .replace("%banner%", sanction.getBy())
                .replace("%date%", TimeConverter.getDateFormatted(sanction.getAt()))
                .replace("%reason%", reason)
                .replace("%expiration%", TimeConverter.getDateFormatted(sanction.getUntil()))
                .replace("%duration%", sanction.getDuration()));
    }

    public void ApplyTempMute(Player player, String reason, CommandSender sender, String duration, String type) {
        User user = UsersManager.instance.getUserAndCreateIfNotExists(player.getUniqueId(), player.getName());
        if (user.isPermaMuted()) {
            sender.sendMessage(MessageManager.prefix +
                    MessageManager.instance.getMessage("AlreadyMuted"));
            return;
        }
        long timemillis = getMillisOfEmission(System.currentTimeMillis(), duration, type);
        duration = duration + type.replace("sec", " second(s)").replace("min", " minute(s)")
                .replace("day", " day(s)").replace("hour", " hour(s)").replace("year", " year(s)");
        Sanction sanction = new Sanction(4, reason, sender.getName(), System.currentTimeMillis(), timemillis, false, false, duration);
        user.setMutedBy(sanction.getBy());
        user.setMutedUntil(sanction.getUntil());
        user.setMutedReason(sanction.getReason());
        user.setMutedAt(sanction.getAt());
        user.setIpBanned(sanction.isIp());
        user.setMutedDuration(sanction.getDuration());
        user.setIp(player.getAddress().getAddress().getHostAddress());
        user.addSanction(sanction);
        user.requestUserUpdate();

        player.sendMessage(TimeConverter.replaceArgs(MessageManager.prefix
                        + MessageManager.instance.getMessage("MessageToPlayerGotTempMuted"),
                duration, player.getName(), sender.getName(), reason, user.getMutedAt(), user.getMutedUntil()));

        Bukkit.broadcastMessage(MessageManager.prefix +
                TimeConverter.replaceArgs(MessageManager.instance.getMessage("PlayerGotTempMute"),
                        duration, player.getName(), sender.getName(), reason, user.getMutedAt(), user.getMutedUntil()));
    }

    public void ApplyPermaMute(Player player, String reason, String banner, CommandSender sender) {
        User user = UsersManager.instance.getUserAndCreateIfNotExists(player.getUniqueId(), player.getName());
        Sanction sanction = new Sanction(3, reason, banner, System.currentTimeMillis(), -1, false, false, "Permanent");
        user.setMutedUntil(sanction.getUntil());
        user.setMutedBy(sanction.getBy());
        user.setMutedAt(sanction.getAt());
        user.setMutedReason(sanction.getReason());
        user.setIpBanned(sanction.isIp());
        user.setMutedDuration(sanction.getDuration());
        user.setIp(player.getAddress().getAddress().getHostAddress());
        user.addSanction(sanction);
        user.requestUserUpdate();

        player.sendMessage(MessageManager.prefix + TimeConverter.replaceArgs(MessageManager.instance.getMessage("MessageToPlayerGotPermaMuted"),
                "null", player.getName(), sender.getName(), reason, user.getMutedAt(), user.getMutedUntil()));

        Bukkit.broadcastMessage(MessageManager.prefix + TimeConverter.replaceArgs(MessageManager.instance.getMessage("PlayerGotPermaMute"),
                "null", player.getName(), sender.getName(), reason, user.getMutedAt(), user.getMutedUntil()));
    }

    public void kickPlayer(Player target, String reason, String banner) {
        target.kickPlayer(reason);
        User user = UsersManager.instance.getUser(target.getUniqueId());
        if (user != null) {
            Sanction sanction = new Sanction(5, reason, banner, System.currentTimeMillis(), -1, false, false, "N/A");
            user.addSanction(sanction);
        }
        Bukkit.broadcastMessage(MessageManager.prefix + TimeConverter.replaceArgs(MessageManager.instance.getMessage("PlayerGotKicked"),
                "null", target.getName(), banner, reason, System.currentTimeMillis(), -1));
    }

    public boolean isSanctionStillActive(Sanction sanction, User user) {
        if (sanction.getType() == 1 || sanction.getType() == 2 || sanction.getType() == 0) {
            if (user.getBannedUntil() < 0) return false;
            if (sanction.getUntil() < System.currentTimeMillis()) return false;
            if (user.getBannedUntil() == sanction.getUntil() && user.getBannedAt() == sanction.getAt() &&
                    Objects.equals(user.getBannedReason(), sanction.getReason()) && Objects.equals(user.getBannedBy(), sanction.getBy()))
                return true;
        }
        if (sanction.getType() == 3 || sanction.getType() == 4) {
            if (user.getMutedUntil() < 0) return false;
            if (sanction.getUntil() < System.currentTimeMillis()) return false;
            return user.getMutedUntil() == sanction.getUntil() && user.getMutedAt() == sanction.getAt() &&
                    Objects.equals(user.getMutedReason(), sanction.getReason()) && Objects.equals(user.getMutedBy(), sanction.getBy());
        }
        return false;
    }

    public long getMillisOfEmission(long until, String duration, String type) {
        if (type.equalsIgnoreCase("sec")) until += Integer.parseInt(duration) * 1000L;
        if (type.equalsIgnoreCase("min")) until += Integer.parseInt(duration) * 60000L;
        if (type.equalsIgnoreCase("hour")) until += Integer.parseInt(duration) * 360000L;
        if (type.equalsIgnoreCase("day")) until += Integer.parseInt(duration) * 86400000L;
        if (type.equalsIgnoreCase("year")) until += Integer.parseInt(duration) * 31536000000L;
        return until;
    }
}
