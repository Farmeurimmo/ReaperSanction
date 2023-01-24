package main.java.fr.farmeurimmo.reapersanction.gui;

import main.java.fr.farmeurimmo.reapersanction.storage.FilesManager;
import main.java.fr.farmeurimmo.reapersanction.storage.MessageManager;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class BanIpGui {

    public static BanIpGui instance;

    public BanIpGui() {
        instance = this;
    }

    public void banipgui(Player player, String cible) {
        if (!player.hasPermission("mod+")) {
            player.sendMessage(MessageManager.prefix + MessageManager.instance.getMessage("NoPermission"));
            return;
        }

        Inventory inv = Bukkit.createInventory(null, 27, "§4ReaperSanction Bans-ip");

        ItemStack custom1 = new ItemStack(Material.NAME_TAG, 1);
        ItemMeta customS = custom1.getItemMeta();
        customS.setDisplayName(FilesManager.instance.getFromConfigFormatted("Menu.Banip.NameTag.Reason"));
        custom1.setItemMeta(customS);
        inv.setItem(12, custom1);

        ItemStack custom2 = new ItemStack(Material.CLAY_BALL, 2);
        ItemMeta customa = custom2.getItemMeta();
        customa.setDisplayName(FilesManager.instance.getFromConfigFormatted("Menu.Banip.ClayBall.Reason"));
        custom2.setItemMeta(customa);
        inv.setItem(14, custom2);

        GuiManager.instance.applyHead(inv, cible, player);
        GuiManager.instance.applyDoorsFromInvSize(inv);
        GuiManager.instance.applyGlass(inv);

        player.openInventory(inv);

    }

}
