package main.java.fr.farmeurimmo.reapersanction.events;

import main.java.fr.farmeurimmo.reapersanction.ReaperSanction;
import main.java.fr.farmeurimmo.reapersanction.sanctions.BanRevoker;
import org.bukkit.Bukkit;
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
        String ip = e.getAddress().getHostAddress();
        String partialIp = ip.substring(0, ip.lastIndexOf("."));
        if (ReaperSanction.instance.ipblocked.containsKey(partialIp)) {
            e.disallow(Result.KICK_BANNED, ReaperSanction.instance.getConfig().getString("ReaperSanction.Settings.BanIp.lines").replace("&", "§")
                    .replace("%banner%", ReaperSanction.instance.getData().getString(
                            ReaperSanction.instance.ipblocked.get(partialIp) + ".ban-ip.banner"))
                    .replace("%date%", ReaperSanction.instance.getData().getString(
                            ReaperSanction.instance.ipblocked.get(partialIp) + ".ban-ip.date").replace("T", " "))
                    .replace("%reason%", ReaperSanction.instance.getData().getString(
                            ReaperSanction.instance.ipblocked.get(partialIp) + ".ban-ip.reason")));
            return;
        }
        if (ReaperSanction.instance.getData().get(e.getName() + ".ban-ip.isipbanned") == null) {
            ReaperSanction.instance.getData().set(e.getName() + ".ban-ip.banner", "");
            ReaperSanction.instance.getData().set(e.getName() + ".ban-ip.date", "");
            ReaperSanction.instance.getData().set(e.getName() + ".ban-ip.reason", "");
            ReaperSanction.instance.getData().set(e.getName() + ".ban-ip.ip", "");
            ReaperSanction.instance.getData().set(e.getName() + ".ban-ip.isipbanned", false);

            ReaperSanction.instance.getData().set(e.getName() + ".ban.banner", "");
            ReaperSanction.instance.getData().set(e.getName() + ".ban.date", "");
            ReaperSanction.instance.getData().set(e.getName() + ".ban.reason", "");
            ReaperSanction.instance.getData().set(e.getName() + ".ban.isbanned", false);

            ReaperSanction.instance.getData().set(e.getName() + ".tempban.banner", "");
            ReaperSanction.instance.getData().set(e.getName() + ".tempban.date", "");
            ReaperSanction.instance.getData().set(e.getName() + ".tempban.timemillis", "");
            ReaperSanction.instance.getData().set(e.getName() + ".tempban.duration", "");
            ReaperSanction.instance.getData().set(e.getName() + ".tempban.reason", "");
            ReaperSanction.instance.getData().set(e.getName() + ".tempban.istempbanned", false);

            ReaperSanction.instance.getData().set(e.getName() + ".mute.ismuted", false);
            ReaperSanction.instance.getData().set(e.getName() + ".mute.banner", "");
            ReaperSanction.instance.getData().set(e.getName() + ".mute.reason", "");

            ReaperSanction.instance.getData().set(e.getName() + ".tempmute.istempmuted", false);
            ReaperSanction.instance.getData().set(e.getName() + ".tempmute.banner", "");
            ReaperSanction.instance.getData().set(e.getName() + ".tempmute.timemillis", "");
            ReaperSanction.instance.getData().set(e.getName() + ".tempmute.reason", "");
            ReaperSanction.instance.getData().set(e.getName() + ".tempmute.duration", "");


            ReaperSanction.instance.saveData();
        }
        if (ReaperSanction.instance.getData().getBoolean(e.getName() + ".ban-ip.isipbanned")) {
            e.disallow(Result.KICK_BANNED, ReaperSanction.instance.getConfig().getString("ReaperSanction.Settings.BanIp.lines").replace("&", "§")
                    .replace("%banner%", ReaperSanction.instance.getData().getString(e.getName() + ".ban-ip.banner"))
                    .replace("%date%", ReaperSanction.instance.getData().getString(e.getName() + ".ban-ip.date").replace("T", " "))
                    .replace("%reason%", ReaperSanction.instance.getData().getString(e.getName() + ".ban-ip.reason")));
            return;
        }
        if (ReaperSanction.instance.getData().getBoolean(e.getName() + ".ban.isbanned")) {
            e.disallow(Result.KICK_BANNED, ReaperSanction.instance.getConfig().getString("ReaperSanction.Settings.Ban.lines").replace("&", "§")
                    .replace("%banner%", ReaperSanction.instance.getData().getString(e.getName() + ".ban.banner"))
                    .replace("%date%", ReaperSanction.instance.getData().getString(e.getName() + ".ban.date").replace("T", " "))
                    .replace("%reason%", ReaperSanction.instance.getData().getString(e.getName() + ".ban.reason")));
            return;
        }
        if (ReaperSanction.instance.getData().getBoolean(e.getName() + ".tempban.istempbanned")) {
            String aaaa = ReaperSanction.instance.getData().getString(e.getName() + ".tempban.expiration");
            if (ReaperSanction.instance.getData().getLong(e.getName() + ".tempban.timemillis") <= System.currentTimeMillis()) {
                BanRevoker.UnTempBan(e.getName(), Bukkit.getConsoleSender());
            } else {
                e.disallow(Result.KICK_BANNED, ReaperSanction.instance.getConfig().getString("ReaperSanction.Settings.TempBan.lines").replace("&", "§")
                        .replace("%banner%", ReaperSanction.instance.getData().getString(e.getName() + ".tempban.banner"))
                        .replace("%date%", ReaperSanction.instance.getData().getString(e.getName() + ".tempban.date").replace("T", " "))
                        .replace("%reason%", ReaperSanction.instance.getData().getString(e.getName() + ".tempban.reason"))
                        .replace("%expiration%", aaaa)
                        .replace("%duration%", ReaperSanction.instance.getData().getString(e.getName() + ".tempban.duration") +
                                ReaperSanction.instance.getData().getString(e.getName() + ".tempban.unit").replace("sec", " second(s)").replace("min", " minute(s)")
                                        .replace("day", " day(s)").replace("hour", " hour(s)").replace("year", " year(s)")));
            }
        }
    }

    @EventHandler
    public void OnLeave(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        if (ReaperSanction.vanished.contains(player)) {
            player.removePotionEffect(PotionEffectType.INVISIBILITY);
            ReaperSanction.vanished.remove(player);
            player.sendMessage(ReaperSanction.instance.getConfig().getString("ReaperSanction.Settings.Vanish.Isoff").replace("&", "§"));
        }
    }
}
