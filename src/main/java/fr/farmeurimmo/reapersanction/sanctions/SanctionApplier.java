package main.java.fr.farmeurimmo.reapersanction.sanctions;

import main.java.fr.farmeurimmo.reapersanction.MessageManager;
import main.java.fr.farmeurimmo.reapersanction.users.Sanction;
import main.java.fr.farmeurimmo.reapersanction.users.User;
import main.java.fr.farmeurimmo.reapersanction.users.UsersManager;
import main.java.fr.farmeurimmo.reapersanction.utils.TimeConverter;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SanctionApplier {

    public static SanctionApplier instance;

    public SanctionApplier() {
        instance = this;
    }

    public void ApplyPermaBan(Player player, String reason, String banner) {
        User user = UsersManager.instance.getUserAndCreateIfNotExists(player.getUniqueId(), player.getName());
        Sanction sanction = new Sanction(reason, banner, System.currentTimeMillis(), -1, true, false);
        user.setBannedAt(sanction.getAt());
        user.setBannedUntil(sanction.getUntil());
        user.setBannedBy(sanction.getBy());
        user.setBannedReason(sanction.getReason());
        user.setIpBanned(sanction.isIp());
        user.setIp(player.getAddress().getAddress().getHostAddress());
        user.addSanction(sanction);
        user.requestUserUpdate();

        Bukkit.broadcastMessage(TimeConverter.replaceArgs(MessageManager.prefix
                        + MessageManager.instance.getMessage("PlayerGotPermaBan"),
                "null", "null", player.getName(), banner, reason));
    }

    public void ApplyPermaBanIp(Player player, String reason, String banner, String string) {
        /*String ip = player.getAddress().getHostString();
        String partialIp = (ip.contains("l") ? ip : ip.substring(0, ip.lastIndexOf(".")));
        ReaperSanction.instance.ipblocked.put(partialIp, player.getName());*/

        User user = UsersManager.instance.getUserAndCreateIfNotExists(player.getUniqueId(), player.getName());
        Sanction sanction = new Sanction(reason, banner, System.currentTimeMillis(), -1, true, true);
        user.setBannedUntil(sanction.getUntil());
        user.setBannedBy(sanction.getBy());
        user.setBannedReason(sanction.getReason());
        user.setBannedAt(sanction.getAt());
        user.setIpBanned(sanction.isIp());
        user.setIp(player.getAddress().getAddress().getHostAddress());
        user.addSanction(sanction);
        user.requestUserUpdate();

        Bukkit.broadcastMessage(TimeConverter.replaceArgs(MessageManager.prefix
                        + MessageManager.instance.getMessage("PlayerGotPermaBanIp"),
                "null", "null", player.getName(), banner, reason));
    }

    public void ApplyTempBan(Player player, String reason, CommandSender sender, String duration, String type) {
        User user = UsersManager.instance.getUserAndCreateIfNotExists(player.getUniqueId(), player.getName());

        if (user.isPermaBan()) {
            sender.sendMessage(MessageManager.prefix +
                    MessageManager.instance.getMessage("AlreadyBanned"));
        }
        long timemillis = getDurationFromType(duration, type);

        Sanction sanction = new Sanction(reason, sender.getName(), System.currentTimeMillis(), timemillis, false, false);
        user.setBannedBy(sanction.getBy());
        user.setBannedUntil(sanction.getUntil());
        user.setBannedReason(sanction.getReason());
        user.setBannedAt(sanction.getAt());
        user.setIpBanned(sanction.isIp());
        user.setIp(player.getAddress().getAddress().getHostAddress());
        user.addSanction(sanction);
        user.requestUserUpdate();

        Bukkit.broadcastMessage(TimeConverter.replaceArgs(MessageManager.prefix
                        + MessageManager.instance.getMessage("PlayerGotTempBan"),
                duration, type, player.getName(), sender.getName(), reason));
    }

    public void ApplyTempMute(Player player, String reason, CommandSender sender, String duration, String type) {
        User user = UsersManager.instance.getUserAndCreateIfNotExists(player.getUniqueId(), player.getName());
        if (user.isPermaMuted()) {
            sender.sendMessage(MessageManager.prefix +
                    MessageManager.instance.getMessage("AlreadyMuted"));
            return;
        }
        long timemillis = getDurationFromType(duration, type);
        Sanction sanction = new Sanction(reason, sender.getName(), System.currentTimeMillis(), timemillis, false, false);
        user.setMutedBy(sanction.getBy());
        user.setMutedUntil(sanction.getUntil());
        user.setMutedReason(sanction.getReason());
        user.setMutedAt(sanction.getAt());
        user.setIpBanned(sanction.isIp());
        user.setIp(player.getAddress().getAddress().getHostAddress());
        user.addSanction(sanction);
        user.requestUserUpdate();

        player.sendMessage(TimeConverter.replaceArgs(MessageManager.prefix
                        + MessageManager.instance.getMessage("MessageToPlayerGotTempMuted"),
                duration, type, player.getName(), sender.getName(), reason));

        Bukkit.broadcastMessage(MessageManager.prefix +
                TimeConverter.replaceArgs(MessageManager.instance.getMessage("PlayerGotTempMute"),
                        duration, type, player.getName(), sender.getName(), reason));
    }

    public void ApplyPermaMute(Player player, String reason, String banner, CommandSender sender) {
        User user = UsersManager.instance.getUserAndCreateIfNotExists(player.getUniqueId(), player.getName());
        Sanction sanction = new Sanction(reason, banner, System.currentTimeMillis(), -1, false, false);
        user.setMutedUntil(sanction.getUntil());
        user.setMutedBy(sanction.getBy());
        user.setMutedReason(sanction.getReason());
        user.setMutedAt(sanction.getAt());
        user.setIpBanned(sanction.isIp());
        user.setIp(player.getAddress().getAddress().getHostAddress());
        user.addSanction(sanction);
        user.requestUserUpdate();

        player.sendMessage(MessageManager.prefix + TimeConverter.replaceArgs(MessageManager.instance.getMessage("MessageToPlayerGotPermaMuted"),
                "null", "null", player.getName(), sender.getName(), reason));

        Bukkit.broadcastMessage(MessageManager.prefix + TimeConverter.replaceArgs(MessageManager.instance.getMessage("PlayerGotPermaMute"),
                "null", "null", player.getName(), sender.getName(), reason));
    }

    public long getDurationFromType(String duration, String type) {
        long timemillis = System.currentTimeMillis();
        if (type.equalsIgnoreCase("sec")) timemillis += Integer.parseInt(duration) * 1000L;
        if (type.equalsIgnoreCase("min")) timemillis += Integer.parseInt(duration) * 60000L;
        if (type.equalsIgnoreCase("hour")) timemillis += Integer.parseInt(duration) * 360000L;
        if (type.equalsIgnoreCase("day")) timemillis += Integer.parseInt(duration) * 86400000L;
        if (type.equalsIgnoreCase("year")) timemillis += Integer.parseInt(duration) * 31536000000L;
        return timemillis;
    }
}
