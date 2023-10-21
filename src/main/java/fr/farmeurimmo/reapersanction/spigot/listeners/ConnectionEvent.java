package fr.farmeurimmo.reapersanction.spigot.listeners;

import fr.farmeurimmo.reapersanction.core.sanctions.SanctionsManager;
import fr.farmeurimmo.reapersanction.core.storage.MessageManager;
import fr.farmeurimmo.reapersanction.core.storage.SettingsManager;
import fr.farmeurimmo.reapersanction.core.users.User;
import fr.farmeurimmo.reapersanction.core.users.UsersManager;
import fr.farmeurimmo.reapersanction.spigot.ReaperSanction;
import fr.farmeurimmo.reapersanction.utils.TimeConverter;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.potion.PotionEffectType;

public class ConnectionEvent implements Listener {

    @EventHandler(priority = EventPriority.MONITOR)
    public void AsyncPlayer(AsyncPlayerPreLoginEvent e) {
        User user = UsersManager.INSTANCE.getUserAndCreateIfNotExists(e.getUniqueId(), e.getName());
        user.setIp(e.getAddress().getHostAddress());
        String ip = e.getAddress().getHostAddress();
        String partialIp = ip.substring(0, ip.lastIndexOf("."));
        SanctionsManager.INSTANCE.checkForSanctionExpiration(user);
        if (ReaperSanction.INSTANCE.ipblocked.containsKey(partialIp)) {
            e.setKickMessage(SettingsManager.INSTANCE.getSetting("sanctions.banip")
                    .replace("%banner%", user.getBannedBy())
                    .replace("%date%", TimeConverter.getDateFormatted(user.getBannedAt()))
                    .replace("%reason%", user.getBannedReason()));
            e.setLoginResult(AsyncPlayerPreLoginEvent.Result.KICK_BANNED);
            return;
        }
        if (user.isIpBanned()) {
            e.setKickMessage(SettingsManager.INSTANCE.getSetting("sanctions.banip")
                    .replace("%banner%", user.getBannedBy())
                    .replace("%date%", TimeConverter.getDateFormatted(user.getBannedAt()))
                    .replace("%reason%", user.getBannedReason()));
            e.setLoginResult(AsyncPlayerPreLoginEvent.Result.KICK_BANNED);
            return;
        }
        if (user.isPermaBan()) {
            e.setKickMessage(SettingsManager.INSTANCE.getSetting("sanctions.ban")
                    .replace("%banner%", user.getBannedBy())
                    .replace("%date%", TimeConverter.getDateFormatted(user.getBannedAt()))
                    .replace("%reason%", user.getBannedReason()));
            e.setLoginResult(AsyncPlayerPreLoginEvent.Result.KICK_BANNED);
            return;
        }
        if (user.isBanned() && !user.isPermaBan()) {
            e.setKickMessage(SettingsManager.INSTANCE.getSetting("sanctions.tempban")
                    .replace("%banner%", user.getBannedBy())
                    .replace("%date%", TimeConverter.getDateFormatted(user.getBannedAt()))
                    .replace("%reason%", user.getBannedReason())
                    .replace("%expiration%", TimeConverter.getDateFormatted(user.getBannedUntil()))
                    .replace("%duration%", user.getBannedDuration()));
            e.setLoginResult(AsyncPlayerPreLoginEvent.Result.KICK_BANNED);
        }
    }

    @EventHandler
    public void OnLeave(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        if (ReaperSanction.VANISHED.contains(player)) {
            player.removePotionEffect(PotionEffectType.INVISIBILITY);
            ReaperSanction.VANISHED.remove(player);
            player.sendMessage(MessageManager.INSTANCE.getMessage("Vanish-Isoff", false));
        }
    }
}
