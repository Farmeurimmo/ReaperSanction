package fr.farmeurimmo.reapersanction.core.sanctions;

import fr.farmeurimmo.reapersanction.core.storage.MessageManager;
import fr.farmeurimmo.reapersanction.core.storage.SettingsManager;
import fr.farmeurimmo.reapersanction.core.storage.WebhookManager;
import fr.farmeurimmo.reapersanction.core.users.Sanction;
import fr.farmeurimmo.reapersanction.core.users.User;
import fr.farmeurimmo.reapersanction.core.users.UsersManager;
import fr.farmeurimmo.reapersanction.utils.TimeConverter;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;

import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public class SanctionApplier {

    public static SanctionApplier INSTANCE;

    public SanctionApplier() {
        INSTANCE = this;
    }

    public void ban(UUID uuid, String playerName, String host, String reason, String banner) {
        User user = UsersManager.INSTANCE.getUserAndCreateIfNotExists(uuid, playerName);
        Sanction sanction = new Sanction(1, reason, banner, System.currentTimeMillis(), -1, true, false, "Permanent");
        user.setBannedAt(sanction.getAt());
        user.setBannedUntil(sanction.getUntil());
        user.setBannedBy(sanction.getBy());
        user.setBannedReason(sanction.getReason());
        user.setIpBanned(sanction.isIp());
        user.setIp(host);
        user.setBannedDuration(sanction.getDuration());
        user.addSanction(sanction);

        CompletableFuture.runAsync(() -> {
            user.requestUserUpdate();

            Bukkit.broadcastMessage(TimeConverter.replaceArgs(MessageManager.INSTANCE.getMessage("PlayerGotPermaBan", true),
                    "null", playerName, banner, reason, user.getBannedAt(), user.getBannedUntil()));

            WebhookManager.INSTANCE.sendDiscordWebHook("ban", banner, playerName, reason, "null");
        });
    }

    public void banIp(UUID uuid, String playerName, String host, String reason, String banner) {
        /*String ip = player.getAddress().getHostString();
        String partialIp = (ip.contains("l") ? ip : ip.substring(0, ip.lastIndexOf(".")));
        ReaperSanction.INSTANCE.ipblocked.put(partialIp, player.getName());*/

        User user = UsersManager.INSTANCE.getUserAndCreateIfNotExists(uuid, playerName);
        Sanction sanction = new Sanction(0, reason, banner, System.currentTimeMillis(), -1, true, true, "Permanent");
        user.setBannedUntil(sanction.getUntil());
        user.setBannedBy(sanction.getBy());
        user.setBannedReason(sanction.getReason());
        user.setBannedAt(sanction.getAt());
        user.setIpBanned(sanction.isIp());
        user.setIp(host);
        user.setBannedDuration(sanction.getDuration());
        user.addSanction(sanction);

        //FIXME: proxy impl

        CompletableFuture.runAsync(() -> {
            user.requestUserUpdate();

            Bukkit.broadcastMessage(TimeConverter.replaceArgs(MessageManager.INSTANCE.getMessage("PlayerGotPermaBanIp", true),
                    "null", playerName, banner, reason, user.getBannedAt(), user.getBannedUntil()));

            WebhookManager.INSTANCE.sendDiscordWebHook("ban", banner, playerName, reason, "null");
        });
    }

    public void tempBan(UUID uuid, String playerName, String host, String reason, CommandSender sender, String duration, String type) {
        User user = UsersManager.INSTANCE.getUserAndCreateIfNotExists(uuid, playerName);

        if (user.isPermaBan()) {
            sender.sendMessage(MessageManager.INSTANCE.getMessage("AlreadyBanned", true));
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
        user.setIp(host);
        user.setBannedDuration(sanction.getDuration());
        user.addSanction(sanction);

        //FIXME: proxy impl

        /*player.kickPlayer(SettingsManager.INSTANCE.getSetting("sanctions.tempban")
                .replace("%banner%", sanction.getBy())
                .replace("%date%", TimeConverter.getDateFormatted(sanction.getAt()))
                .replace("%reason%", reason)
                .replace("%expiration%", TimeConverter.getDateFormatted(sanction.getUntil()))
                .replace("%duration%", sanction.getDuration()));*/

        String finalDuration = duration;
        CompletableFuture.runAsync(() -> {

            user.requestUserUpdate();

            Bukkit.broadcastMessage(TimeConverter.replaceArgs(MessageManager.INSTANCE.getMessage("PlayerGotTempBan", true),
                    finalDuration, playerName, sender.getName(), reason, user.getBannedAt(), user.getBannedUntil()));

            WebhookManager.INSTANCE.sendDiscordWebHook("tempBan", sender.getName(), playerName, reason, finalDuration);
        });
    }

    public void tempMute(UUID uuid, String playerName, String host, String reason, CommandSender sender, String duration, String type) {
        User user = UsersManager.INSTANCE.getUserAndCreateIfNotExists(uuid, playerName);
        if (user.isPermaMuted()) {
            sender.sendMessage(MessageManager.INSTANCE.getMessage("AlreadyMuted", true));
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
        user.setIp(host);
        user.addSanction(sanction);

        String finalDuration = duration;
        CompletableFuture.runAsync(() -> {
            user.requestUserUpdate();

            //FIXME: proxy impl

            /*player.sendMessage(TimeConverter.replaceArgs(MessageManager.INSTANCE.getMessage("MessageToPlayerGotTempMuted", true),
                    finalDuration, playerName, sender.getName(), reason, user.getMutedAt(), user.getMutedUntil()));*/

            Bukkit.broadcastMessage(TimeConverter.replaceArgs(MessageManager.INSTANCE.getMessage("PlayerGotTempMute", true),
                    finalDuration, playerName, sender.getName(), reason, user.getMutedAt(), user.getMutedUntil()));

            WebhookManager.INSTANCE.sendDiscordWebHook("tempMute", sender.getName(), playerName, reason, finalDuration);
        });
    }

    public void mute(UUID uuid, String playerName, String host, String reason, String banner, CommandSender sender) {
        User user = UsersManager.INSTANCE.getUserAndCreateIfNotExists(uuid, playerName);
        Sanction sanction = new Sanction(3, reason, banner, System.currentTimeMillis(), -1, false, false, "Permanent");
        user.setMutedUntil(sanction.getUntil());
        user.setMutedBy(sanction.getBy());
        user.setMutedAt(sanction.getAt());
        user.setMutedReason(sanction.getReason());
        user.setIpBanned(sanction.isIp());
        user.setMutedDuration(sanction.getDuration());
        user.setIp(host);
        user.addSanction(sanction);

        CompletableFuture.runAsync(() -> {
            user.requestUserUpdate();

            //FIXME: proxy impl

            /*player.sendMessage(TimeConverter.replaceArgs(MessageManager.INSTANCE.getMessage("MessageToPlayerGotPermaMuted", true),
                    "null", playerName, sender.getName(), reason, user.getMutedAt(), user.getMutedUntil()));*/

            Bukkit.broadcastMessage(TimeConverter.replaceArgs(MessageManager.INSTANCE.getMessage("PlayerGotPermaMute", true),
                    "null", playerName, sender.getName(), reason, user.getMutedAt(), user.getMutedUntil()));

            WebhookManager.INSTANCE.sendDiscordWebHook("mute", sender.getName(), playerName, reason, "null");
        });
    }

    public void kick(UUID uuid, String playerName, String reason, String banner) {
        String kickMessage = SettingsManager.INSTANCE.getSetting("sanctions.kick")
                .replace("%banner%", banner)
                .replace("%date%", TimeConverter.getDateFormatted(System.currentTimeMillis()))
                .replace("%reason%", reason);
        //FIXME: proxy impl
        //target.kickPlayer(kickMessage);

        User user = UsersManager.INSTANCE.getUser(uuid);
        if (user != null) {
            Sanction sanction = new Sanction(5, reason, banner, System.currentTimeMillis(), -1, false, false, "N/A");
            user.addSanction(sanction);
            CompletableFuture.runAsync(user::requestUserUpdate);
        }

        CompletableFuture.runAsync(() -> {

            Bukkit.broadcastMessage(TimeConverter.replaceArgs(MessageManager.INSTANCE.getMessage("PlayerGotKicked", true),
                    "null", playerName, banner, reason, System.currentTimeMillis(), -1));

            WebhookManager.INSTANCE.sendDiscordWebHook("kick", banner, playerName, reason, "null");
        });
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
