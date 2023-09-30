package fr.farmeurimmo.reapersanction.spigot.listeners;

import fr.farmeurimmo.reapersanction.api.sanctions.SanctionRevoker;
import fr.farmeurimmo.reapersanction.api.storage.FilesManager;
import fr.farmeurimmo.reapersanction.api.storage.MessageManager;
import fr.farmeurimmo.reapersanction.api.users.User;
import fr.farmeurimmo.reapersanction.api.users.UsersManager;
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
        SanctionRevoker.INSTANCE.checkForSanctionExpiration(user);
        if (ReaperSanction.INSTANCE.ipblocked.containsKey(partialIp)) {
            e.setKickMessage(FilesManager.INSTANCE.getFromConfigFormatted("BanIp.lines")
                    .replace("%banner%", user.getBannedBy())
                    .replace("%date%", TimeConverter.getDateFormatted(user.getBannedAt()))
                    .replace("%reason%", user.getBannedReason()));
            e.setLoginResult(AsyncPlayerPreLoginEvent.Result.KICK_BANNED);
            return;
        }
        if (user.isIpBanned()) {
            e.setKickMessage(FilesManager.INSTANCE.getFromConfigFormatted("BanIp.lines")
                    .replace("%banner%", user.getBannedBy())
                    .replace("%date%", TimeConverter.getDateFormatted(user.getBannedAt()))
                    .replace("%reason%", user.getBannedReason()));
            e.setLoginResult(AsyncPlayerPreLoginEvent.Result.KICK_BANNED);
            return;
        }
        if (user.isPermaBan()) {
            e.setKickMessage(FilesManager.INSTANCE.getFromConfigFormatted("Ban.lines")
                    .replace("%banner%", user.getBannedBy())
                    .replace("%date%", TimeConverter.getDateFormatted(user.getBannedAt()))
                    .replace("%reason%", user.getBannedReason()));
            e.setLoginResult(AsyncPlayerPreLoginEvent.Result.KICK_BANNED);
            return;
        }
        if (user.isBanned() && !user.isPermaBan()) {
            e.setKickMessage(FilesManager.INSTANCE.getFromConfigFormatted("TempBan.lines")
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
