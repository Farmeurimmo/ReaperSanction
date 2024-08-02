package fr.farmeurimmo.reapersanction.proxy.velocity.listeners;

import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.connection.LoginEvent;
import com.velocitypowered.api.event.player.PlayerChatEvent;
import com.velocitypowered.api.event.player.ServerPostConnectEvent;
import com.velocitypowered.api.proxy.Player;
import fr.farmeurimmo.reapersanction.core.Main;
import fr.farmeurimmo.reapersanction.core.sanctions.SanctionsManager;
import fr.farmeurimmo.reapersanction.core.storage.MessageManager;
import fr.farmeurimmo.reapersanction.core.storage.SettingsManager;
import fr.farmeurimmo.reapersanction.core.users.User;
import fr.farmeurimmo.reapersanction.core.users.UsersManager;
import fr.farmeurimmo.reapersanction.proxy.velocity.cpm.CPMManager;
import fr.farmeurimmo.reapersanction.utils.TimeConverter;
import net.kyori.adventure.text.Component;

public class PlayerListener {

    @Subscribe
    public void onPreLogin(LoginEvent e) {
        Player p = e.getPlayer();

        User user = UsersManager.INSTANCE.getUserAndCreateIfNotExists(p.getUniqueId(), p.getUsername());
        user.setIp(p.getRemoteAddress().getAddress().getHostAddress());
        String ip = p.getRemoteAddress().getAddress().getHostAddress();
        String partialIp = ip.substring(0, ip.lastIndexOf("."));

        SanctionsManager.INSTANCE.checkForSanctionExpiration(user);

        if (Main.INSTANCE.ipblocked.containsKey(partialIp)) {
            user = Main.INSTANCE.ipblocked.get(partialIp);
            e.setResult(LoginEvent.ComponentResult.denied(Component.text(SettingsManager.INSTANCE.getSanctionMessage("banip")
                    .replace("%banner%", user.getBannedBy())
                    .replace("%date%", TimeConverter.getDateFormatted(user.getBannedAt()))
                    .replace("%reason%", user.getBannedReason()))));
            return;
        }
        if (user.isIpBanned()) {
            e.setResult(LoginEvent.ComponentResult.denied(Component.text(SettingsManager.INSTANCE.getSanctionMessage("banip")
                    .replace("%banner%", user.getBannedBy())
                    .replace("%date%", TimeConverter.getDateFormatted(user.getBannedAt()))
                    .replace("%reason%", user.getBannedReason()))));
            return;
        }
        if (user.isPermaBan()) {
            e.setResult(LoginEvent.ComponentResult.denied(Component.text(SettingsManager.INSTANCE.getSanctionMessage("ban")
                    .replace("%banner%", user.getBannedBy())
                    .replace("%date%", TimeConverter.getDateFormatted(user.getBannedAt()))
                    .replace("%reason%", user.getBannedReason()))));
            return;
        }
        if (user.isBanned() && !user.isPermaBan()) {
            e.setResult(LoginEvent.ComponentResult.denied(Component.text(SettingsManager.INSTANCE.getSanctionMessage("tempban")
                    .replace("%banner%", user.getBannedBy())
                    .replace("%date%", TimeConverter.getDateFormatted(user.getBannedAt()))
                    .replace("%reason%", user.getBannedReason())
                    .replace("%until%", TimeConverter.getDateFormatted(user.getBannedUntil()))
                    .replace("%duration%", user.getBannedDuration()))));
        }
    }

    @Subscribe
    public void onPlayerChat(PlayerChatEvent e) {
        Player p = e.getPlayer();
        User user = UsersManager.INSTANCE.getUserAndCreateIfNotExists(p.getUniqueId(), p.getUsername());
        if (user.isMuted()) {
            if (user.isPermaMuted()) {
                p.sendMessage(Component.text(MessageManager.INSTANCE.getMessage("PermaMutedPlayerChat", true)
                        .replace("%player%", p.getUsername()).replace("%banner%", user.getMutedBy())));
                return;
            }
            p.sendMessage(Component.text(MessageManager.INSTANCE.getMessage("TempMutedPlayerChat", true)
                    .replace("%player%", p.getUsername()).replace("%banner%", user.getMutedBy())));
        }
    }

    @Subscribe
    public void onPlayerConnectedToServer(ServerPostConnectEvent e) {
        Player p = e.getPlayer();

        User user = UsersManager.INSTANCE.getUserAndCreateIfNotExists(p.getUniqueId(), p.getUsername());
        if (user.isMuted()) {
            CPMManager.INSTANCE.sendPluginMessage(p, "nowmuted", p.getUniqueId().toString());
        }
    }
}
