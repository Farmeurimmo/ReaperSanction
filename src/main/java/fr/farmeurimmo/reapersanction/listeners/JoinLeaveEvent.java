package fr.farmeurimmo.reapersanction.listeners;

import fr.farmeurimmo.reapersanction.ReaperSanction;
import fr.farmeurimmo.reapersanction.sanctions.SanctionRevoker;
import fr.farmeurimmo.reapersanction.storage.FilesManager;
import fr.farmeurimmo.reapersanction.storage.MessageManager;
import fr.farmeurimmo.reapersanction.users.User;
import fr.farmeurimmo.reapersanction.users.UsersManager;
import fr.farmeurimmo.reapersanction.utils.TimeConverter;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.potion.PotionEffectType;

public class JoinLeaveEvent implements Listener {

    @EventHandler(priority = EventPriority.MONITOR)
    public void AsyncPlayer(AsyncPlayerPreLoginEvent e) {
        User user = UsersManager.instance.getUserAndCreateIfNotExists(e.getUniqueId(), e.getName());
        user.setIp(e.getAddress().getHostAddress());
        String ip = e.getAddress().getHostAddress();
        String partialIp = ip.substring(0, ip.lastIndexOf("."));
        SanctionRevoker.instance.checkForSanctionExpiration(user);
        if (ReaperSanction.instance.ipblocked.containsKey(partialIp)) {
            e.setKickMessage(FilesManager.instance.getFromConfigFormatted("BanIp.lines")
                    .replace("%banner%", user.getBannedBy())
                    .replace("%date%", TimeConverter.getDateFormatted(user.getBannedAt()))
                    .replace("%reason%", user.getBannedReason()));
            e.setLoginResult(AsyncPlayerPreLoginEvent.Result.KICK_BANNED);
            return;
        }
        if (user.isIpBanned()) {
            e.setKickMessage(FilesManager.instance.getFromConfigFormatted("BanIp.lines")
                    .replace("%banner%", user.getBannedBy())
                    .replace("%date%", TimeConverter.getDateFormatted(user.getBannedAt()))
                    .replace("%reason%", user.getBannedReason()));
            e.setLoginResult(AsyncPlayerPreLoginEvent.Result.KICK_BANNED);
            return;
        }
        if (user.isPermaBan()) {
            e.setKickMessage(FilesManager.instance.getFromConfigFormatted("Ban.lines")
                    .replace("%banner%", user.getBannedBy())
                    .replace("%date%", TimeConverter.getDateFormatted(user.getBannedAt()))
                    .replace("%reason%", user.getBannedReason()));
            e.setLoginResult(AsyncPlayerPreLoginEvent.Result.KICK_BANNED);
            return;
        }
        if (user.isBanned() && !user.isPermaBan()) {
            e.setKickMessage(FilesManager.instance.getFromConfigFormatted("TempBan.lines")
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
        if (ReaperSanction.vanished.contains(player)) {
            player.removePotionEffect(PotionEffectType.INVISIBILITY);
            ReaperSanction.vanished.remove(player);
            player.sendMessage(MessageManager.instance.getMessage("Vanish-Isoff"));
        }
    }
}
