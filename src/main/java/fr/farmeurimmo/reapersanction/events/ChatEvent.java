package main.java.fr.farmeurimmo.reapersanction.events;

import main.java.fr.farmeurimmo.reapersanction.ConfigManager;
import main.java.fr.farmeurimmo.reapersanction.MessageManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class ChatEvent implements Listener {

    @EventHandler(priority = EventPriority.MONITOR)
    public void AsyncChat(AsyncPlayerChatEvent e) {
        Player player = e.getPlayer();
        if (ConfigManager.instance.getData().getBoolean(e.getPlayer().getName() + ".mute.ismuted")) {
            e.setCancelled(true);
            player.sendMessage(MessageManager.prefix +
                    MessageManager.instance.getMessage("PermaMutedPlayerChat")
                            .replace("%player%", player.getName()).replace("%banner%",
                                    ConfigManager.instance.getData().getString(player.getName() + ".mute.banner")));
            return;
        }
        if (ConfigManager.instance.getData().getBoolean(e.getPlayer().getName() + ".tempmute.istempmuted")) {
            e.setCancelled(true);
            player.sendMessage(MessageManager.prefix +
                    MessageManager.instance.getMessage("TempMutedPlayerChat")
                            .replace("%player%", player.getName()).replace("%banner%",
                                    ConfigManager.instance.getData().getString(player.getName() + ".tempmute.banner")));
        }
    }

}
