package fr.farmeurimmo.reapersanction.spigot.listeners;

import fr.farmeurimmo.reapersanction.core.Main;
import fr.farmeurimmo.reapersanction.core.storage.MessageManager;
import fr.farmeurimmo.reapersanction.core.users.User;
import fr.farmeurimmo.reapersanction.core.users.UsersManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class ChatEvent implements Listener {

    @EventHandler(priority = EventPriority.MONITOR)
    public void AsyncChat(AsyncPlayerChatEvent e) {
        Player player = e.getPlayer();

        if (Main.INSTANCE.mutedPlayers.contains(player.getUniqueId())) {
            e.getRecipients().clear();
            return;
        }

        User user = UsersManager.INSTANCE.getUserAndCreateIfNotExists(player.getUniqueId(), player.getName());
        if (user.isMuted()) {
            if (!Main.INSTANCE.isProxyMode()) {
                e.setCancelled(true);
                if (user.isPermaMuted()) {
                    player.sendMessage(MessageManager.INSTANCE.getMessage("PermaMutedPlayerChat", true)
                            .replace("%player%", player.getName()).replace("%banner%", user.getMutedBy()));
                    return;
                }
                player.sendMessage(MessageManager.INSTANCE.getMessage("TempMutedPlayerChat", true)
                        .replace("%player%", player.getName()).replace("%banner%", user.getMutedBy()));
            }
        }
    }

}
