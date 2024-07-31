package fr.farmeurimmo.reapersanction.proxy.bungeecord.listeners;

import fr.farmeurimmo.reapersanction.core.Main;
import fr.farmeurimmo.reapersanction.core.sanctions.SanctionsManager;
import fr.farmeurimmo.reapersanction.core.storage.MessageManager;
import fr.farmeurimmo.reapersanction.core.storage.SettingsManager;
import fr.farmeurimmo.reapersanction.core.users.User;
import fr.farmeurimmo.reapersanction.core.users.UsersManager;
import fr.farmeurimmo.reapersanction.proxy.bungeecord.cpm.CPMManager;
import fr.farmeurimmo.reapersanction.utils.TimeConverter;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.ChatEvent;
import net.md_5.bungee.api.event.LoginEvent;
import net.md_5.bungee.api.event.ServerConnectedEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class PlayerListener implements Listener {

    @EventHandler
    public void onPreLogin(LoginEvent e) {
        User user = UsersManager.INSTANCE.getUserAndCreateIfNotExists(e.getConnection().getUniqueId(), e.getConnection().getName());

        user.setIp(e.getConnection().getAddress().getAddress().getHostAddress());
        String ip = e.getConnection().getAddress().getAddress().getHostAddress();
        String partialIp = ip.substring(0, ip.lastIndexOf("."));

        SanctionsManager.INSTANCE.checkForSanctionExpiration(user);

        if (Main.INSTANCE.ipblocked.containsKey(partialIp)) {
            e.setCancelled(true);
            e.setReason(new TextComponent(SettingsManager.INSTANCE.getSanctionMessage("banip")
                    .replace("%banner%", user.getBannedBy())
                    .replace("%date%", TimeConverter.getDateFormatted(user.getBannedAt()))
                    .replace("%reason%", user.getBannedReason())));
            return;
        }
        if (user.isIpBanned()) {
            e.setCancelled(true);
            e.setReason(new TextComponent(SettingsManager.INSTANCE.getSanctionMessage("banip")
                    .replace("%banner%", user.getBannedBy())
                    .replace("%date%", TimeConverter.getDateFormatted(user.getBannedAt()))
                    .replace("%reason%", user.getBannedReason())));
            return;
        }
        if (user.isPermaBan()) {
            e.setCancelled(true);
            e.setReason(new TextComponent(SettingsManager.INSTANCE.getSanctionMessage("ban")
                    .replace("%banner%", user.getBannedBy())
                    .replace("%date%", TimeConverter.getDateFormatted(user.getBannedAt()))
                    .replace("%reason%", user.getBannedReason())));
            return;
        }
        if (user.isBanned()) {
            e.setCancelled(true);
            e.setReason(new TextComponent(SettingsManager.INSTANCE.getSanctionMessage("tempban")
                    .replace("%banner%", user.getBannedBy())
                    .replace("%date%", TimeConverter.getDateFormatted(user.getBannedAt()))
                    .replace("%reason%", user.getBannedReason())
                    .replace("%until%", TimeConverter.getDateFormatted(user.getBannedUntil()))
                    .replace("%duration%", user.getBannedDuration())));
        }
    }

    @EventHandler
    public void onPlayerChat(ChatEvent e) {
        ProxiedPlayer p = (ProxiedPlayer) e.getSender();
        User user = UsersManager.INSTANCE.getUserAndCreateIfNotExists(p.getUniqueId(), p.getName());

        if (e.getMessage().startsWith("/"))
            return;

        SanctionsManager.INSTANCE.checkForSanctionExpiration(user);

        if (user.isMuted()) {
            e.setCancelled(true);
            if (user.isPermaMuted()) {
                p.sendMessage(new TextComponent(MessageManager.INSTANCE.getMessage("PermaMutedPlayerChat", true)
                        .replace("%player%", p.getName()).replace("%banner%", user.getMutedBy())));
                return;
            }
            p.sendMessage(new TextComponent(MessageManager.INSTANCE.getMessage("TempMutedPlayerChat", true)
                    .replace("%player%", p.getName()).replace("%banner%", user.getMutedBy())));
        }
    }

    @EventHandler
    public void onPlayerConnectedToServer(ServerConnectedEvent e) {
        ProxiedPlayer p = e.getPlayer();

        User user = UsersManager.INSTANCE.getUserAndCreateIfNotExists(p.getUniqueId(), p.getName());
        if (user.isMuted()) {
            CPMManager.INSTANCE.sendPluginMessage(p, "nowmuted", p.getUniqueId().toString());
        }
    }
}
