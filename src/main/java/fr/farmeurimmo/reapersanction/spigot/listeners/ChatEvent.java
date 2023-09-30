package fr.farmeurimmo.reapersanction.spigot.listeners;

import fr.farmeurimmo.reapersanction.api.storage.MessageManager;
import fr.farmeurimmo.reapersanction.api.users.User;
import fr.farmeurimmo.reapersanction.api.users.UsersManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class ChatEvent implements Listener {

    @EventHandler(priority = EventPriority.MONITOR)
    public void AsyncChat(AsyncPlayerChatEvent e) {
        Player player = e.getPlayer();
        User user = UsersManager.INSTANCE.getUserAndCreateIfNotExists(player.getUniqueId(), player.getName());
        if (user.isMuted()) {
            e.setCancelled(true);
            if (user.isPermaMuted()) {
                player.sendMessage(MessageManager.prefix +
                        MessageManager.INSTANCE.getMessage("PermaMutedPlayerChat")
                                .replace("%player%", player.getName()).replace("%banner%", user.getMutedBy()));
                return;
            }
            player.sendMessage(MessageManager.prefix +
                    MessageManager.INSTANCE.getMessage("TempMutedPlayerChat")
                            .replace("%player%", player.getName()).replace("%banner%", user.getMutedBy()));
        }
    }

}
