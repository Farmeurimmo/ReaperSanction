package fr.farmeurimmo.reapersanction.core.storage;

import fr.farmeurimmo.reapersanction.core.Main;
import fr.farmeurimmo.reapersanction.spigot.ReaperSanction;
import fr.farmeurimmo.reapersanction.utils.DiscordWebhook;
import fr.farmeurimmo.reapersanction.utils.TimeConverter;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class WebhookManager {

    public static WebhookManager INSTANCE;
    private Map<String, Object> webhooks;

    public WebhookManager() {
        INSTANCE = this;

        setup();
    }

    @NotNull
    private static Map<String, Object> getStringObjectMap() {
        Map<String, Object> author = new HashMap<>();
        author.put("name", "%author%");
        author.put("icon_url", "https://minotar.net/avatar/%author%/100.png");
        author.put("url", "https://namemc.com/profile/%author%");

        Map<String, Object> common = new HashMap<>();
        common.put("active", false);
        common.put("author", author);
        common.put("color", "#29ad91");
        common.put("thumbnail", "https://minotar.net/avatar/%target%/100.png");
        common.put("webhook_url", "");
        return common;
    }

    public void setup() {
        webhooks = getDefaults();

        loadFromMap();

        save();
    }

    protected void save() {
        Map<String, Object> discord = new HashMap<>();

        for (Map.Entry<String, Object> method : webhooks.entrySet()) {
            Map<String, Object> sa;
            try {
                sa = (Map<String, Object>) method.getValue();
            } catch (Exception e) {
                continue;
            }
            if (sa == null) continue;
            for (Map.Entry<String, Object> s : sa.entrySet()) {
                Map<String, Object> sanction = (Map<String, Object>) s.getValue();
                if (sanction == null) continue;
                sanction.put("active", sanction.get("active"));
                sanction.put("desc", sanction.get("desc"));
                sanction.put("color", sanction.get("color"));
                sanction.put("thumbnail", sanction.get("thumbnail"));
                sanction.put("webhook_url", sanction.get("webhook_url"));

                Map<String, Object> author;
                try {
                    author = (Map<String, Object>) sanction.get("author");
                } catch (Exception e) {
                    author = new HashMap<>();
                }
                author.put("name", author.get("name"));
                author.put("icon_url", author.get("icon_url"));
                author.put("url", author.get("url"));
                sanction.put("author", author);

                discord.put(s.getKey(), sanction);
            }
        }

        Map<String, Object> discordFinal = new HashMap<>();
        discordFinal.put("discord", discord);

        FilesManager.INSTANCE.applyInfosFile(discordFinal);
        FilesManager.INSTANCE.getWebhooks().putAll(discordFinal);
        FilesManager.INSTANCE.saveWebhooks();
    }

    public Map<String, Object> getDefaults() {
        Map<String, Object> toReturn = new HashMap<>();

        Map<String, Object> discord = new HashMap<>();

        Map<String, Object> tempMute = new HashMap<>();
        tempMute.put("desc", "**Author ·** ``%author%``\\\\n**Target ·** ``%target%``\\\\n**Reason ·** ``%reason%``\\\\n**SanctionType ·** ``TempMute``\\\\n**Duration ·** ``%duration%``\\\\n**Server ·** ``%server_name%``");
        tempMute.putAll(getStringObjectMap());
        discord.put("tempMute", tempMute);

        Map<String, Object> tempBan = new HashMap<>();
        tempBan.put("desc", "**Author ·** ``%author%``\\\\n**Target ·** ``%target%``\\\\n**Reason ·** ``%reason%``\\\\n**SanctionType ·** ``TempBan``\\\\n**Duration ·** ``%duration%``\\\\n**Server ·** ``%server_name%``");
        tempBan.putAll(getStringObjectMap());
        discord.put("tempBan", tempBan);

        Map<String, Object> mute = new HashMap<>();
        mute.put("desc", "**Author ·** ``%author%``\\\\n**Target ·** ``%target%``\\\\n**Reason ·** ``%reason%``\\\\n**SanctionType ·** ``Mute``\\\\n**Server ·** ``%server_name%``");
        mute.putAll(getStringObjectMap());
        discord.put("mute", mute);

        Map<String, Object> ban = new HashMap<>();
        ban.put("desc", "**Author ·** ``%author%``\\\\n**Target ·** ``%target%``\\\\n**Reason ·** ``%reason%``\\\\n**SanctionType ·** ``Ban``\\\\n**Server ·** ``%server_name%``");
        ban.putAll(getStringObjectMap());
        discord.put("ban", ban);

        Map<String, Object> kick = new HashMap<>();
        kick.put("desc", "**Author ·** ``%author%``\\\\n**Target ·** ``%target%``\\\\n**Reason ·** ``%reason%``\\\\n**SanctionType ·** ``Kick``\\\\n**Server ·** ``%server_name%``");
        kick.putAll(getStringObjectMap());
        discord.put("kick", kick);

        Map<String, Object> unban = new HashMap<>();
        unban.put("desc", "**Author ·** ``%author%``\\\\n**Target ·** ``%target%``\\\\n**Reason ·** ``%reason%``\\\\n**SanctionType ·** ``Unban``\\\\n**Server ·** ``%server_name%``");
        unban.putAll(getStringObjectMap());
        discord.put("unban", unban);

        Map<String, Object> unmute = new HashMap<>();
        unmute.put("desc", "**Author ·** ``%author%``\\\\n**Target ·** ``%target%``\\\\n**Reason ·** ``%reason%``\\\\n**SanctionType ·** ``Unmute``\\\\n**Server ·** ``%server_name%``");
        unmute.putAll(getStringObjectMap());
        discord.put("unmute", unmute);

        Map<String, Object> report = new HashMap<>();
        report.put("desc", "**Author ·** ``%author%``\\\\n**Target ·** ``%target%``\\\\n**Reason ·** ``%reason%``\\\\n**SanctionType ·** ``Unban``\\\\n**Server ·** ``%server_name%``");
        report.putAll(getStringObjectMap());
        discord.put("report", report);

        toReturn.put("discord", discord);

        return toReturn;
    }

    public DiscordWebhook getWebhook(String type, String banner, String playerName, String reason, String duration) {
        try {
            Map<String, Object> discord = (Map<String, Object>) webhooks.get("discord");
            if (discord == null) return null;

            Map<String, Object> sanction = (Map<String, Object>) discord.get(type);
            if (sanction == null) return null;

            if (sanction.get("active") == null) return null;
            if (!(boolean) sanction.get("active")) return null;

            DiscordWebhook webhook_message = new DiscordWebhook(sanction.get("webhook_url").toString());
            String desc = replaceArgs((String) sanction.get("desc"), banner, playerName, duration, reason);
            String thumbnail = replaceArgs((String) sanction.get("thumbnail"), banner, playerName, duration, reason);
            String author_name, author_url, author_icon_url;
            author_name = author_url = author_icon_url = "";
            Map<String, Object> author = (Map<String, Object>) sanction.get("author");
            if (author != null) {
                author_name = replaceArgs((String) author.get("name"), banner, playerName, duration, reason);
                author_url = replaceArgs((String) author.get("url"), banner, playerName, duration, reason);
                author_icon_url = replaceArgs((String) author.get("icon_url"), banner, playerName, duration, reason);
            }

            desc = desc.replaceAll("\\\\n", "n");

            String color = (String) sanction.get("color");
            webhook_message.addEmbed(new DiscordWebhook.EmbedObject()
                    .setDescription(desc)
                    .setThumbnail(thumbnail)
                    .setAuthor(author_name, author_url, author_icon_url)
                    .setFooter(TimeConverter.getDateFormatted(System.currentTimeMillis()), "")
                    .setColor(Color.decode(color)));
            return webhook_message;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public String replaceArgs(String str, String author, String target, String duration, String reason) {
        return str
                .replace("%author%", author)
                .replace("%target%", target)
                .replace("%duration%", duration)
                .replace("%reason%", reason)
                .replace("%server_name%", ReaperSanction.INSTANCE.getServerName());
    }

    public void sendDiscordWebHook(String type, String banner, String playerName, String reason, String duration) {
        DiscordWebhook webhook = getWebhook(type, banner, playerName, reason, duration);
        if (webhook == null) return;
        try {
            webhook.setTts(false);
            webhook.setUsername("ReaperSanction - " + Main.INSTANCE.getPluginVersion());
            webhook.setAvatarUrl("https://cdn.farmeurimmo.fr/img/reaper-solution.jpg");
            webhook.execute();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadFromMap() {
        try {
            Map<String, Object> discord;
            try {
                discord = (Map<String, Object>) FilesManager.INSTANCE.getWebhooks().get("discord");
            } catch (Exception e) {
                return;
            }

            if (discord == null) return;

            discord.remove("infos-file");

            Map<String, Object> sanctions = new HashMap<>();
            for (Map.Entry<String, Object> s : discord.entrySet()) {
                Map<String, Object> sanction = (Map<String, Object>) s.getValue();
                sanctions.put(s.getKey(), sanction);
            }

            webhooks.put("discord", sanctions);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
