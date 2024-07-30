package fr.farmeurimmo.reapersanction.core.sanctions;

import com.velocitypowered.api.command.SimpleCommand;
import fr.farmeurimmo.reapersanction.core.Main;
import fr.farmeurimmo.reapersanction.core.ServerType;
import fr.farmeurimmo.reapersanction.core.storage.MessageManager;
import fr.farmeurimmo.reapersanction.core.storage.WebhookManager;
import fr.farmeurimmo.reapersanction.core.users.Sanction;
import fr.farmeurimmo.reapersanction.core.users.User;
import fr.farmeurimmo.reapersanction.core.users.UsersManager;
import fr.farmeurimmo.reapersanction.proxy.velocity.ReaperSanction;
import fr.farmeurimmo.reapersanction.proxy.velocity.cpm.CPMManager;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public class SanctionsManager {

    public static SanctionsManager INSTANCE;

    public SanctionsManager() {
        INSTANCE = this;
    }

    public void checkForUsersExpiration() {
        for (User user : UsersManager.INSTANCE.users) {
            checkForSanctionExpiration(user);
        }
    }

    public Sanction ban(UUID uuid, String playerName, String host, String reason, String banner) {
        User user = UsersManager.INSTANCE.getUserAndCreateIfNotExists(uuid, playerName);
        Sanction sanction = new Sanction(1, reason, banner, System.currentTimeMillis(), -1, true, false, "Permanent");
        user.applyBan(sanction, host);

        CompletableFuture.runAsync(() -> {
            user.requestUserUpdate();

            WebhookManager.INSTANCE.sendDiscordWebHook("ban", banner, playerName, reason, "null");
        });

        return sanction;
    }

    public Sanction banIp(UUID uuid, String playerName, String host, String reason, String banner) {
        /*String ip = player.getAddress().getHostString();
        String partialIp = (ip.contains("l") ? ip : ip.substring(0, ip.lastIndexOf(".")));
        ReaperSanction.INSTANCE.ipblocked.put(partialIp, player.getName());*/

        User user = UsersManager.INSTANCE.getUserAndCreateIfNotExists(uuid, playerName);
        Sanction sanction = new Sanction(0, reason, banner, System.currentTimeMillis(), -1, true, true, "Permanent");
        user.applyBan(sanction, host);

        CompletableFuture.runAsync(() -> {
            user.requestUserUpdate();

            WebhookManager.INSTANCE.sendDiscordWebHook("ban", banner, playerName, reason, "null");
        });

        return sanction;
    }

    public Sanction tempBan(UUID uuid, String playerName, String host, String reason, String sender, String duration, String type) {
        User user = UsersManager.INSTANCE.getUserAndCreateIfNotExists(uuid, playerName);

        long timeMillis = getMillisOfEmission(System.currentTimeMillis(), duration, type);

        duration = duration + type.replace("sec", " second(s)").replace("min", " minute(s)")
                .replace("day", " day(s)").replace("hour", " hour(s)").replace("year", " year(s)");
        Sanction sanction = new Sanction(2, reason, sender, System.currentTimeMillis(), timeMillis, true, false, duration);
        user.applyBan(sanction, host);

        String finalDuration = duration;
        CompletableFuture.runAsync(() -> {
            user.requestUserUpdate();

            WebhookManager.INSTANCE.sendDiscordWebHook("tempBan", sender, playerName, reason, finalDuration);
        });

        return sanction;
    }

    public Sanction tempMute(UUID uuid, String playerName, String host, String reason, String sender, String duration, String type) {
        User user = UsersManager.INSTANCE.getUserAndCreateIfNotExists(uuid, playerName);
        long timeMillis = getMillisOfEmission(System.currentTimeMillis(), duration, type);
        duration = duration + type.replace("sec", " second(s)").replace("min", " minute(s)")
                .replace("day", " day(s)").replace("hour", " hour(s)").replace("year", " year(s)");
        Sanction sanction = new Sanction(4, reason, sender, System.currentTimeMillis(), timeMillis, false, false, duration);
        user.applyMute(sanction, host);

        String finalDuration = duration;
        CompletableFuture.runAsync(() -> {
            user.requestUserUpdate();

            WebhookManager.INSTANCE.sendDiscordWebHook("tempMute", sender, playerName, reason, finalDuration);
        });

        return sanction;
    }

    public Sanction mute(UUID uuid, String playerName, String host, String reason, String banner) {
        User user = UsersManager.INSTANCE.getUserAndCreateIfNotExists(uuid, playerName);
        Sanction sanction = new Sanction(3, reason, banner, System.currentTimeMillis(), -1, false, false, "Permanent");
        user.applyMute(sanction, host);

        CompletableFuture.runAsync(() -> {
            user.requestUserUpdate();

            WebhookManager.INSTANCE.sendDiscordWebHook("mute", banner, playerName, reason, "null");
        });

        return sanction;
    }

    public Sanction kick(UUID uuid, String playerName, String reason, String banner) {
        User user = UsersManager.INSTANCE.getUserAndCreateIfNotExists(uuid, playerName);
        Sanction sanction = new Sanction(5, reason, banner, System.currentTimeMillis(), -1, false, false, "N/A");
        user.addSanction(sanction);

        CompletableFuture.runAsync(() -> {
            user.requestUserUpdate();

            WebhookManager.INSTANCE.sendDiscordWebHook("kick", banner, playerName, reason, "null");
        });

        return sanction;
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

    public void checkForSanctionExpiration(User user) {
        if (user.isMuted() && !user.isPermaMuted())
            if (user.getMutedUntil() != -1 && user.getMutedUntil() < System.currentTimeMillis())
                revokeMute(user, "CONSOLE");
        if (user.isBanned() && !user.isPermaBan() && !user.isIpBanned())
            if (user.getBannedUntil() != -1 && user.getBannedUntil() < System.currentTimeMillis()) revokeBan(user);
    }

    public void revokeMute(User user, String by) {
        if (user.isMuted()) {
            //TODO: msg for expiration
            WebhookManager.INSTANCE.sendDiscordWebHook("unmute", by, user.getName(),
                    by.equalsIgnoreCase("CONSOLE") ? "EXPIRATION" : "command", "...");
        }

        user.setMutedAt(0);
        user.setMutedDuration("");
        user.setMutedUntil(0);
        user.setMutedReason("");
        user.setMutedBy("");
        user.requestUserUpdate();

        if (Main.INSTANCE.getServerType() == ServerType.SPIGOT) {
            Player player = Bukkit.getPlayer(user.getUuid());
            if (player != null) {
                player.sendMessage(MessageManager.INSTANCE.getMessage("MuteEnded", true));
            }
        } else if (Main.INSTANCE.getServerType() == ServerType.VELOCITY) {
            for (com.velocitypowered.api.proxy.Player p : ReaperSanction.INSTANCE.getProxy().getAllPlayers()) {
                CPMManager.INSTANCE.sendPluginMessage(p, "unmuted", user.getUuid().toString());
                break;
            }
        }
    }

    public void revokeMuteAdmin(User user, CommandSender requester) {
        if (!user.isMuted()) {
            requester.sendMessage(MessageManager.INSTANCE.getMessage("NotMuted", true));
            return;
        }
        revokeMute(user, requester.getName());
        requester.sendMessage(MessageManager.INSTANCE.getMessage("SuccessfullyUnmuted", true)
                .replace("%player%", user.getName()));
    }

    public void revokeBan(User user) {
        if (user.isBanned()) {
            WebhookManager.INSTANCE.sendDiscordWebHook("unban", user.getBannedBy(), user.getName(),
                    user.getBannedReason(), user.getBannedDuration());
        }

        user.setBannedAt(0);
        user.setBannedDuration("");
        user.setBannedUntil(0);
        user.setBannedReason("");
        user.setBannedBy("");
        user.setIpBanned(false);
        user.requestUserUpdate();
    }

    public void revokeBanAdmin(User user, CommandSender requester) {
        if (!user.isBanned()) {
            requester.sendMessage(MessageManager.INSTANCE.getMessage("NotBanned", true));
            return;
        }
        revokeBan(user);
        requester.sendMessage(MessageManager.INSTANCE.getMessage("SuccessfullyUnbanned", true).replace("%player%", user.getName()));
    }

    public void revokeBanAdmin(User user, SimpleCommand.Invocation invocation) {
        if (!user.isBanned()) {
            invocation.source().sendMessage(MessageManager.INSTANCE.getComponent("NotBanned", true));
            return;
        }
        revokeBan(user);
        invocation.source().sendMessage(Component.text(MessageManager.INSTANCE.getMessage("SuccessfullyUnbanned", true).replace("%player%", user.getName())));
    }

    public void revokeMuteAdmin(User user, SimpleCommand.Invocation invocation) {
        if (!user.isMuted()) {
            invocation.source().sendMessage(MessageManager.INSTANCE.getComponent("NotMuted", true));
            return;
        }
        revokeMute(user, invocation.alias());
        invocation.source().sendMessage(Component.text(MessageManager.INSTANCE.getMessage("SuccessfullyUnmuted", true)
                .replace("%player%", user.getName())));
    }
}
