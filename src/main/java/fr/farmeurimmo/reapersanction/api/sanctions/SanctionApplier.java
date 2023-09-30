package fr.farmeurimmo.reapersanction.api.sanctions;

import fr.farmeurimmo.reapersanction.api.storage.FilesManager;
import fr.farmeurimmo.reapersanction.api.storage.MessageManager;
import fr.farmeurimmo.reapersanction.api.users.Sanction;
import fr.farmeurimmo.reapersanction.api.users.User;
import fr.farmeurimmo.reapersanction.api.users.UsersManager;
import fr.farmeurimmo.reapersanction.spigot.ReaperSanction;
import fr.farmeurimmo.reapersanction.utils.DiscordWebhook;
import fr.farmeurimmo.reapersanction.utils.TimeConverter;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;

import java.awt.*;
import java.io.IOException;
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

            if (!ReaperSanction.INSTANCE.isDiscordWebhookActive()) return;

            DiscordWebhook webhook_message = new DiscordWebhook(ReaperSanction.DISCORD_WEBHOOK_URL);
            String desc = replaceArgs(ReaperSanction.INSTANCE.getConfig().getString("Discord.ban.desc"), banner, playerName, "null", reason);
            String thumbnail = replaceArgs(ReaperSanction.INSTANCE.getConfig().getString("Discord.ban.thumbnail"), banner, playerName, "null", reason);
            String author_name = replaceArgs(ReaperSanction.INSTANCE.getConfig().getString("Discord.ban.author.name"), banner, playerName, "null", reason);
            String author_url = replaceArgs(ReaperSanction.INSTANCE.getConfig().getString("Discord.ban.author.url"), banner, playerName, "null", reason);
            String author_icon_url = replaceArgs(ReaperSanction.INSTANCE.getConfig().getString("Discord.ban.author.icon_url"), banner, playerName, "null", reason);
            String color = ReaperSanction.INSTANCE.getConfig().getString("Discord.ban.color");
            webhook_message.addEmbed(new DiscordWebhook.EmbedObject()
                    .setDescription(desc)
                    .setThumbnail(thumbnail)
                    .setAuthor(author_name, author_url, author_icon_url)
                    .setFooter(TimeConverter.getDateFormatted(System.currentTimeMillis()), "")
                    .setColor(Color.decode(color)));

            sendDiscordWebHook(webhook_message);
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

        CompletableFuture.runAsync(() -> {
            user.requestUserUpdate();

            Bukkit.broadcastMessage(TimeConverter.replaceArgs(MessageManager.INSTANCE.getMessage("PlayerGotPermaBanIp", true),
                    "null", playerName, banner, reason, user.getBannedAt(), user.getBannedUntil()));

            if (!ReaperSanction.INSTANCE.isDiscordWebhookActive()) return;

            DiscordWebhook webhook_message = new DiscordWebhook(ReaperSanction.DISCORD_WEBHOOK_URL);
            String desc = replaceArgs(ReaperSanction.INSTANCE.getConfig().getString("Discord.ban_ip.desc"), banner, playerName, "null", reason);
            String thumbnail = replaceArgs(ReaperSanction.INSTANCE.getConfig().getString("Discord.ban_ip.thumbnail"), banner, playerName, "null", reason);
            String author_name = replaceArgs(ReaperSanction.INSTANCE.getConfig().getString("Discord.ban_ip.author.name"), banner, playerName, "null", reason);
            String author_url = replaceArgs(ReaperSanction.INSTANCE.getConfig().getString("Discord.ban_ip.author.url"), banner, playerName, "null", reason);
            String author_icon_url = replaceArgs(ReaperSanction.INSTANCE.getConfig().getString("Discord.ban_ip.author.icon_url"), banner, playerName, "null", reason);
            String color = ReaperSanction.INSTANCE.getConfig().getString("Discord.ban_ip.color");
            webhook_message.addEmbed(new DiscordWebhook.EmbedObject()
                    .setDescription(desc)
                    .setThumbnail(thumbnail)
                    .setAuthor(author_name, author_url, author_icon_url)
                    .setFooter(TimeConverter.getDateFormatted(System.currentTimeMillis()), "")
                    .setColor(Color.decode(color)));

            sendDiscordWebHook(webhook_message);
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

        /*player.kickPlayer(FilesManager.INSTANCE.getFromConfigFormatted("TempBan.lines")
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

            if (!ReaperSanction.INSTANCE.isDiscordWebhookActive()) return;

            DiscordWebhook webhook_message = new DiscordWebhook(ReaperSanction.DISCORD_WEBHOOK_URL);
            String desc = replaceArgs(ReaperSanction.INSTANCE.getConfig().getString("Discord.tempban.desc"), sender.getName(), playerName, finalDuration, reason);
            String thumbnail = replaceArgs(ReaperSanction.INSTANCE.getConfig().getString("Discord.tempban.thumbnail"), sender.getName(), playerName, finalDuration, reason);
            String author_name = replaceArgs(ReaperSanction.INSTANCE.getConfig().getString("Discord.tempban.author.name"), sender.getName(), playerName, finalDuration, reason);
            String author_url = replaceArgs(ReaperSanction.INSTANCE.getConfig().getString("Discord.tempban.author.url"), sender.getName(), playerName, finalDuration, reason);
            String author_icon_url = replaceArgs(ReaperSanction.INSTANCE.getConfig().getString("Discord.tempban.author.icon_url"), sender.getName(), playerName, finalDuration, reason);
            String color = ReaperSanction.INSTANCE.getConfig().getString("Discord.tempban.color");
            webhook_message.addEmbed(new DiscordWebhook.EmbedObject()
                    .setDescription(desc)
                    .setThumbnail(thumbnail)
                    .setAuthor(author_name, author_url, author_icon_url)
                    .setFooter(TimeConverter.getDateFormatted(System.currentTimeMillis()), "")
                    .setColor(Color.decode(color)));

            sendDiscordWebHook(webhook_message);
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

            if (!ReaperSanction.INSTANCE.isDiscordWebhookActive()) return;

            DiscordWebhook webhook_message = new DiscordWebhook(ReaperSanction.DISCORD_WEBHOOK_URL);
            String desc = replaceArgs(ReaperSanction.INSTANCE.getConfig().getString("Discord.tempmute.desc"), sender.getName(), playerName, finalDuration, reason);
            String thumbnail = replaceArgs(ReaperSanction.INSTANCE.getConfig().getString("Discord.tempmute.thumbnail"), sender.getName(), playerName, finalDuration, reason);
            String author_name = replaceArgs(ReaperSanction.INSTANCE.getConfig().getString("Discord.tempmute.author.name"), sender.getName(), playerName, finalDuration, reason);
            String author_url = replaceArgs(ReaperSanction.INSTANCE.getConfig().getString("Discord.tempmute.author.url"), sender.getName(), playerName, finalDuration, reason);
            String author_icon_url = replaceArgs(ReaperSanction.INSTANCE.getConfig().getString("Discord.tempmute.author.icon_url"), sender.getName(), playerName, finalDuration, reason);
            String color = ReaperSanction.INSTANCE.getConfig().getString("Discord.tempmute.color");
            webhook_message.addEmbed(new DiscordWebhook.EmbedObject()
                    .setDescription(desc)
                    .setThumbnail(thumbnail)
                    .setAuthor(author_name, author_url, author_icon_url)
                    .setFooter(TimeConverter.getDateFormatted(System.currentTimeMillis()), "")
                    .setColor(Color.decode(color)));

            sendDiscordWebHook(webhook_message);
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

            if (!ReaperSanction.INSTANCE.isDiscordWebhookActive()) return;

            DiscordWebhook webhook_message = new DiscordWebhook(ReaperSanction.DISCORD_WEBHOOK_URL);
            String desc = replaceArgs(ReaperSanction.INSTANCE.getConfig().getString("Discord.mute.desc"), sender.getName(), playerName, "null", reason);
            String thumbnail = replaceArgs(ReaperSanction.INSTANCE.getConfig().getString("Discord.mute.thumbnail"), sender.getName(), playerName, "null", reason);
            String author_name = replaceArgs(ReaperSanction.INSTANCE.getConfig().getString("Discord.mute.author.name"), sender.getName(), playerName, "null", reason);
            String author_url = replaceArgs(ReaperSanction.INSTANCE.getConfig().getString("Discord.mute.author.url"), sender.getName(), playerName, "null", reason);
            String author_icon_url = replaceArgs(ReaperSanction.INSTANCE.getConfig().getString("Discord.mute.author.icon_url"), sender.getName(), playerName, "null", reason);
            String color = ReaperSanction.INSTANCE.getConfig().getString("Discord.mute.color");
            webhook_message.addEmbed(new DiscordWebhook.EmbedObject()
                    .setDescription(desc)
                    .setThumbnail(thumbnail)
                    .setAuthor(author_name, author_url, author_icon_url)
                    .setFooter(TimeConverter.getDateFormatted(System.currentTimeMillis()), "")
                    .setColor(Color.decode(color)));

            sendDiscordWebHook(webhook_message);
        });
    }

    public void kick(UUID uuid, String playerName, String reason, String banner) {
        String kickMessage = FilesManager.INSTANCE.getFromConfigFormatted("Kick.lines")
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

            if (!ReaperSanction.INSTANCE.isDiscordWebhookActive()) return;

            DiscordWebhook webhook_message = new DiscordWebhook(ReaperSanction.DISCORD_WEBHOOK_URL);
            String desc = replaceArgs(ReaperSanction.INSTANCE.getConfig().getString("Discord.kick.desc"), banner, playerName, "null", reason);
            String thumbnail = replaceArgs(ReaperSanction.INSTANCE.getConfig().getString("Discord.kick.thumbnail"), banner, playerName, "null", reason);
            String author_name = replaceArgs(ReaperSanction.INSTANCE.getConfig().getString("Discord.kick.author.name"), banner, playerName, "null", reason);
            String author_url = replaceArgs(ReaperSanction.INSTANCE.getConfig().getString("Discord.kick.author.url"), banner, playerName, "null", reason);
            String author_icon_url = replaceArgs(ReaperSanction.INSTANCE.getConfig().getString("Discord.kick.author.icon_url"), banner, playerName, "null", reason);
            String color = ReaperSanction.INSTANCE.getConfig().getString("Discord.kick.color");
            webhook_message.addEmbed(new DiscordWebhook.EmbedObject()
                    .setDescription(desc)
                    .setThumbnail(thumbnail)
                    .setAuthor(author_name, author_url, author_icon_url)
                    .setFooter(TimeConverter.getDateFormatted(System.currentTimeMillis()), "")
                    .setColor(Color.decode(color)));

            sendDiscordWebHook(webhook_message);
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

    public String replaceArgs(String str, String author, String target, String duration, String reason) {
        return str
                .replace("%author%", author)
                .replace("%target%", target)
                .replace("%duration%", duration)
                .replace("%reason%", reason)
                .replace("%server_name%", ReaperSanction.INSTANCE.getServerName());
    }

    public void sendDiscordWebHook(DiscordWebhook dwh) {
        try {
            dwh.setTts(false);
            dwh.setUsername("ReaperSanction - " + ReaperSanction.INSTANCE.getVersion());
            dwh.setAvatarUrl("https://cdn.farmeurimmo.fr/img/reaper-solution.jpg");
            dwh.execute();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
