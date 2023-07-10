package fr.farmeurimmo.reapersanction.gui;

import fr.farmeurimmo.reapersanction.users.UsersManager;
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
            if (currenttype.name().contains("IRON_DOOR")) {
                CustomInventories.INSTANCE.startInventoryOpenProcess(player, InventoryType.MAIN, HistoryGui.INSTANCE.getPlayerFromGuiName(title));
                return;
            }
            if (currenttype.name().contains("ARROW")) {
                if (current.getItemMeta().getDisplayName().contains("Next")) {
                    HistoryGui.INSTANCE.openHistoryGui(player, UsersManager.INSTANCE.getUser(HistoryGui.INSTANCE.getPlayerFromGuiName(title)),
                            HistoryGui.INSTANCE.getPageFromGuiName(title) + 1);
                } else if (current.getItemMeta().getDisplayName().contains("Previous")) {
                    HistoryGui.INSTANCE.openHistoryGui(player, UsersManager.INSTANCE.getUser(HistoryGui.INSTANCE.getPlayerFromGuiName(title)),
                            HistoryGui.INSTANCE.getPageFromGuiName(title) - 1);
                }
                return;
            }
        }
    }
}

