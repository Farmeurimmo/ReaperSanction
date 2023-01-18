package main.java.fr.farmeurimmo.reapersanction.listeners;

import main.java.fr.farmeurimmo.reapersanction.ConfigManager;
import main.java.fr.farmeurimmo.reapersanction.MessageManager;
import main.java.fr.farmeurimmo.reapersanction.ReaperSanction;
import main.java.fr.farmeurimmo.reapersanction.sanctions.SanctionRevoker;
import main.java.fr.farmeurimmo.reapersanction.users.User;
import main.java.fr.farmeurimmo.reapersanction.users.UsersManager;
import main.java.fr.farmeurimmo.reapersanction.utils.TimeConverter;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerPreLoginEvent.Result;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.potion.PotionEffectType;

@SuppressWarnings("deprecation")
public class JoinLeaveEvent implements Listener {

    @EventHandler(priority = EventPriority.MONITOR)
    public void AsyncPlayer(AsyncPlayerPreLoginEvent e) {
        User user = UsersManager.instance.getUserAndCreateIfNotExists(e.getUniqueId(), e.getName());
        user.setIp(e.getAddress().getHostAddress());
        String ip = e.getAddress().getHostAddress();
        String partialIp = ip.substring(0, ip.lastIndexOf("."));
        SanctionRevoker.instance.checkForSanctionExpiration(user);
        if (ReaperSanction.instance.ipblocked.containsKey(partialIp)) {
            e.disallow(Result.KICK_BANNED, ConfigManager.instance.getFromConfigFormatted("BanIp.lines")
                    .replace("%banner%", user.getBannedBy())
                    .replace("%date%", TimeConverter.getDateFormatted(user.getBannedAt()))
                    .replace("%reason%", user.getBannedReason()));
            return;
        }
        if (user.isIpBanned()) {
            e.disallow(Result.KICK_BANNED, ConfigManager.instance.getFromConfigFormatted("BanIp.lines")
                    .replace("%banner%", user.getBannedBy())
                    .replace("%date%", TimeConverter.getDateFormatted(user.getBannedAt()))
                    .replace("%reason%", user.getBannedReason()));
            return;
        }
        if (user.isPermaBan()) {
            e.disallow(Result.KICK_BANNED, ConfigManager.instance.getFromConfigFormatted("Ban.lines")
                    .replace("%banner%", user.getBannedBy())
                    .replace("%date%", TimeConverter.getDateFormatted(user.getBannedAt()))
                    .replace("%reason%", user.getBannedReason()));
            return;
        }
        if (user.isBanned() && !user.isPermaBan()) {
            e.disallow(Result.KICK_BANNED, ConfigManager.instance.getFromConfigFormatted("TempBan.lines")
                    .replace("%banner%", user.getBannedBy())
                    .replace("%date%", TimeConverter.getDateFormatted(user.getBannedAt()))
                    .replace("%reason%", user.getBannedReason())
                    .replace("%expiration%", TimeConverter.getDateFormatted(user.getBannedUntil()))
                    .replace("%duration%", user.getBannedDuration()));
        }
    }

    @EventHandler
    public void OnLeave(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        User user = UsersManager.instance.getUser(player.getUniqueId());
        if (ReaperSanction.vanished.contains(player)) {
            player.removePotionEffect(PotionEffectType.INVISIBILITY);
            ReaperSanction.vanished.remove(player);
            player.sendMessage(MessageManager.instance.getMessage("Vanish-Isoff"));
        }
    }
}
