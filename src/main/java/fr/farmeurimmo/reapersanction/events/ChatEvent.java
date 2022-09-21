package main.java.fr.farmeurimmo.reapersanction.events;

import main.java.fr.farmeurimmo.reapersanction.ReaperSanction;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class ChatEvent implements Listener {

    @EventHandler(priority = EventPriority.MONITOR)
    public void AsyncChat(AsyncPlayerChatEvent e) {
        Player player = e.getPlayer();
        if (ReaperSanction.instance.getData().getBoolean(e.getPlayer().getName() + ".mute.ismuted")) {
            e.setCancelled(true);
            player.sendMessage(ReaperSanction.instance.Preffix +
                    ReaperSanction.instance.getConfig().getString("ReaperSanction.Settings.PermaMutedPlayerChat").replace("&", "§")
                            .replace("%player%", player.getName()).replace("%banner%",
                                    ReaperSanction.instance.getData().getString(player.getName() + ".mute.banner").replace("&", "§")));
            return;
        }
        if (ReaperSanction.instance.getData().getBoolean(e.getPlayer().getName() + ".tempmute.istempmuted")) {
            e.setCancelled(true);
            player.sendMessage(ReaperSanction.instance.Preffix +
                    ReaperSanction.instance.getConfig().getString("ReaperSanction.Settings.TempMutedPlayerChat").replace("&", "§")
                            .replace("%player%", player.getName()).replace("%banner%",
                                    ReaperSanction.instance.getData().getString(player.getName() + ".tempmute.banner").replace("&", "§")));
        }
    }

}
