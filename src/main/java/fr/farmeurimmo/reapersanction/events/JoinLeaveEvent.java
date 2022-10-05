package main.java.fr.farmeurimmo.reapersanction.events;

import main.java.fr.farmeurimmo.reapersanction.ConfigManager;
import main.java.fr.farmeurimmo.reapersanction.MessageManager;
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

import java.util.HashMap;

@SuppressWarnings("deprecation")
public class JoinLeaveEvent implements Listener {

    @EventHandler(priority = EventPriority.MONITOR)
    public void AsyncPlayer(AsyncPlayerPreLoginEvent e) {
        String ip = e.getAddress().getHostAddress();
        String partialIp = ip.substring(0, ip.lastIndexOf("."));
        if (ReaperSanction.instance.ipblocked.containsKey(partialIp)) {
            e.disallow(Result.KICK_BANNED, ConfigManager.instance.getFromConfigFormatted("BanIp.lines")
                    .replace("%banner%", ConfigManager.instance.getData().getString(
                            ReaperSanction.instance.ipblocked.get(partialIp) + ".ban-ip.banner"))
                    .replace("%date%", ConfigManager.instance.getData().getString(
                            ReaperSanction.instance.ipblocked.get(partialIp) + ".ban-ip.date").replace("T", " "))
                    .replace("%reason%", ConfigManager.instance.getData().getString(
                            ReaperSanction.instance.ipblocked.get(partialIp) + ".ban-ip.reason")));
            return;
        }
        if (ConfigManager.instance.getData().get(e.getName() + ".ban-ip.isipbanned") == null) {
            HashMap<String, Object> values = new HashMap<>();
            values.put(e.getName() + ".ban-ip.isipbanned", false);
            values.put(e.getName() + ".ban-ip.banner", "");
            values.put(e.getName() + ".ban-ip.date", "");
            values.put(e.getName() + ".ban-ip.reason", "");
            values.put(e.getName() + ".ban-ip.ip", "");

            values.put(e.getName() + ".ban.isbanned", false);
            values.put(e.getName() + ".ban.banner", "");
            values.put(e.getName() + ".ban.date", "");
            values.put(e.getName() + ".ban.reason", "");

            values.put(e.getName() + ".mute.ismuted", false);
            values.put(e.getName() + ".mute.date", "");
            values.put(e.getName() + ".mute.reason", "");

            values.put(e.getName() + ".tempban.istempbanned", false);
            values.put(e.getName() + ".tempban.banner", "");
            values.put(e.getName() + ".tempban.date", "");
            values.put(e.getName() + ".tempban.reason", "");
            values.put(e.getName() + ".tempban.duration", "");
            values.put(e.getName() + ".tempban.timemillis", "");

            values.put(e.getName() + ".tempmute.istempmuted", false);
            values.put(e.getName() + ".tempmute.banner", "");
            values.put(e.getName() + ".tempmute.duration", "");
            values.put(e.getName() + ".tempmute.timemillis", "");
            values.put(e.getName() + ".tempmute.reason", "");

            ConfigManager.instance.setAndSaveAsyncData(values);
        }
        if (ConfigManager.instance.getData().getBoolean(e.getName() + ".ban-ip.isipbanned")) {
            e.disallow(Result.KICK_BANNED, ConfigManager.instance.getFromConfigFormatted("BanIp.lines")
                    .replace("%banner%", ConfigManager.instance.getData().getString(e.getName() + ".ban-ip.banner"))
                    .replace("%date%", ConfigManager.instance.getData().getString(e.getName() + ".ban-ip.date").replace("T", " "))
                    .replace("%reason%", ConfigManager.instance.getData().getString(e.getName() + ".ban-ip.reason")));
            return;
        }
        if (ConfigManager.instance.getData().getBoolean(e.getName() + ".ban.isbanned")) {
            e.disallow(Result.KICK_BANNED, ConfigManager.instance.getFromConfigFormatted("Ban.lines")
                    .replace("%banner%", ConfigManager.instance.getData().getString(e.getName() + ".ban.banner"))
                    .replace("%date%", ConfigManager.instance.getData().getString(e.getName() + ".ban.date").replace("T", " "))
                    .replace("%reason%", ConfigManager.instance.getData().getString(e.getName() + ".ban.reason")));
            return;
        }
        if (ConfigManager.instance.getData().getBoolean(e.getName() + ".tempban.istempbanned")) {
            String aaaa = ConfigManager.instance.getData().getString(e.getName() + ".tempban.expiration");
            if (ConfigManager.instance.getData().getLong(e.getName() + ".tempban.timemillis") <= System.currentTimeMillis()) {
                BanRevoker.UnTempBan(e.getName(), Bukkit.getConsoleSender());
            } else {
                e.disallow(Result.KICK_BANNED, ConfigManager.instance.getFromConfigFormatted("TempBan.lines")
                        .replace("%banner%", ConfigManager.instance.getData().getString(e.getName() + ".tempban.banner"))
                        .replace("%date%", ConfigManager.instance.getData().getString(e.getName() + ".tempban.date").replace("T", " "))
                        .replace("%reason%", ConfigManager.instance.getData().getString(e.getName() + ".tempban.reason"))
                        .replace("%expiration%", aaaa)
                        .replace("%duration%", ConfigManager.instance.getData().getString(e.getName() + ".tempban.duration") +
                                ConfigManager.instance.getData().getString(e.getName() + ".tempban.unit").replace("sec", " second(s)").replace("min", " minute(s)")
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
            player.sendMessage(MessageManager.instance.getMessage("Vanish-Isoff"));
        }
    }
}
