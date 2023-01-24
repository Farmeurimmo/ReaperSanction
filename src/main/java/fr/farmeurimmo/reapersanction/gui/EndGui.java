package main.java.fr.farmeurimmo.reapersanction.gui;

import main.java.fr.farmeurimmo.reapersanction.storage.MessageManager;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class EndGui {

    public static EndGui instance;

    public EndGui() {
        instance = this;
    }

    public void endgui(Player player, String cible) {
        if (!player.hasPermission("mod+")) {
            player.sendMessage(MessageManager.prefix + MessageManager.instance.getMessage("NoPermission"));
            return;
        }

        Inventory inv = Bukkit.createInventory(null, 27, "§4ReaperSanction Unbans/Unmutes");

        ItemStack custom1 = new ItemStack(Material.BOW, 1);
        ItemMeta customS = custom1.getItemMeta();
        customS.setDisplayName("§6Unmutes");
        custom1.setItemMeta(customS);
        inv.setItem(12, custom1);

        ItemStack custom2 = new ItemStack(Material.DIAMOND_SWORD, 1);
        ItemMeta customa = custom2.getItemMeta();
        customa.setDisplayName("§6Unban and unbanip");
        custom2.setItemMeta(customa);
        inv.setItem(14, custom2);

        ItemStack custom3 = new ItemStack(Material.ANVIL, 1);
        ItemMeta customb = custom3.getItemMeta();
        customb.setDisplayName("§6Unbans and Unmute");
        custom3.setItemMeta(customb);
        inv.setItem(22, custom3);

        GuiManager.instance.applyHead(inv, cible, player);
        GuiManager.instance.applyDoorsFromInvSize(inv);
        GuiManager.instance.applyGlass(inv);

        player.openInventory(inv);
    }

}
