package main.java.fr.farmeurimmo.reapersanction.listeners;

import main.java.fr.farmeurimmo.reapersanction.MessageManager;
import main.java.fr.farmeurimmo.reapersanction.users.User;
import main.java.fr.farmeurimmo.reapersanction.users.UsersManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class ChatEvent implements Listener {

    @EventHandler(priority = EventPriority.MONITOR)
    public void AsyncChat(AsyncPlayerChatEvent e) {
        Player player = e.getPlayer();
        User user = UsersManager.instance.getUserAndCreateIfNotExists(player.getUniqueId(), player.getName());
        if (user.isMuted()) {
            e.setCancelled(true);
            if (user.isPermaMuted()) {
                player.sendMessage(MessageManager.prefix +
                        MessageManager.instance.getMessage("PermaMutedPlayerChat")
                                .replace("%player%", player.getName()).replace("%banner%", user.getMutedBy()));
                return;
            }
            player.sendMessage(MessageManager.prefix +
                    MessageManager.instance.getMessage("TempMutedPlayerChat")
                            .replace("%player%", player.getName()).replace("%banner%", user.getMutedBy()));
        }
    }

}
