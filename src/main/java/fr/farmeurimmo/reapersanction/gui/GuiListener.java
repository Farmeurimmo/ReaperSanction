package main.java.fr.farmeurimmo.reapersanction.gui;

import main.java.fr.farmeurimmo.reapersanction.storage.MessageManager;
import main.java.fr.farmeurimmo.reapersanction.users.UsersManager;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

public class GuiListener implements Listener {

    @EventHandler
    public void onInventoryClick(InventoryClickEvent e) {
        Player player = (Player) e.getWhoClicked();
        ItemStack current = e.getCurrentItem();

        if (current == null) return;

        Material currenttype = current.getType();

        if (current.getItemMeta() == null) return;
        if (e.getSlot() <= -1) return;

        String title = e.getView().getTitle();

        if (title.length() < 4) return;

        if (title.contains("§c§lHistory of ")) {
            e.setCancelled(true);
            if (currenttype == Material.IRON_DOOR) {
                CustomInventories.instance.startInventoryOpenProcess(player, InventoryType.MAIN, HistoryGui.instance.getPlayerFromGuiName(title));
                return;
            }
            if (currenttype == Material.ARROW) {
                if (current.getItemMeta().getDisplayName().contains("Next")) {
                    HistoryGui.instance.openHistoryGui(player, UsersManager.instance.getUser(HistoryGui.instance.getPlayerFromGuiName(title)),
                            HistoryGui.instance.getPageFromGuiName(title) + 1);
                } else if (current.getItemMeta().getDisplayName().contains("Previous")) {
                    HistoryGui.instance.openHistoryGui(player, UsersManager.instance.getUser(HistoryGui.instance.getPlayerFromGuiName(title)),
                            HistoryGui.instance.getPageFromGuiName(title) - 1);
                }
                return;
            }
        }
    }

    public void sendMessageReported(Player player, String cible, String ReportReason) {
        player.sendMessage(MessageManager.prefix + MessageManager.instance.getMessage("Report-Sended").replace("%player%", cible).replace("%reason%", ReportReason));
        player.closeInventory();
        for (Player all : Bukkit.getServer().getOnlinePlayers()) {
            if (all.hasPermission("reportview"))
                all.sendMessage(MessageManager.prefix + MessageManager.instance.getMessage("Report-Obtain").replace("%player%", cible).replace("%reason%", ReportReason).replace("%sender%", player.getName()));
        }
    }

}

