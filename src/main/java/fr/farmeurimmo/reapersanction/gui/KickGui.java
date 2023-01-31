package main.java.fr.farmeurimmo.reapersanction.gui;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

public class KickGui {

    public static KickGui instance;

    public KickGui() {
        instance = this;
    }

    public void openKickGui(Player player, String target) {
        Inventory inv = Bukkit.createInventory(null, 27, "§4ReaperSanction Kick");

        GuiManager.instance.applyDoorsFromInvSize(inv);
        GuiManager.instance.applyHead(inv, target, player);
        GuiManager.instance.applyGlass(inv);

        player.openInventory(inv);
    }

}
