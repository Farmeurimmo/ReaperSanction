package main.java.fr.farmeurimmo.reapersanction.gui;

import main.java.fr.farmeurimmo.reapersanction.storage.FilesManager;
import main.java.fr.farmeurimmo.reapersanction.storage.MessageManager;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class RsGui {

    public static RsGui instance;

    public RsGui() {
        instance = this;
    }

    public void ssMainGui(Player player, String cible) {
        if (!player.hasPermission("mod")) {
            player.sendMessage(MessageManager.prefix + MessageManager.instance.getMessage("NoPermission"));
            return;
        }

        Inventory inv = Bukkit.createInventory(null, 27, "§4ReaperSanction");

        ItemStack custom1 = new ItemStack(Material.GRASS, 1);
        ItemMeta customS = custom1.getItemMeta();
        customS.setDisplayName(
                FilesManager.instance.getFromConfigFormatted("Menu.RsMenu.GrassBlock"));
        custom1.setItemMeta(customS);
        inv.setItem(10, custom1);

        ItemStack custom2 = new ItemStack(Material.DIAMOND_SWORD, 1);
        ItemMeta customa = custom2.getItemMeta();
        customa.setDisplayName(
                FilesManager.instance.getFromConfigFormatted("Menu.RsMenu.DiamondSword"));
        custom2.setItemMeta(customa);
        inv.setItem(11, custom2);

        ItemStack history = new ItemStack(Material.BOOK, 1);
        ItemMeta historyMeta = history.getItemMeta();
        historyMeta.setDisplayName(
                FilesManager.instance.getFromConfigFormatted("Menu.RsMenu.History"));
        history.setItemMeta(historyMeta);
        inv.setItem(22, history);

        ItemStack custom3 = new ItemStack(Material.BARRIER, 1);
        ItemMeta customb = custom3.getItemMeta();
        customb.setDisplayName(
                FilesManager.instance.getFromConfigFormatted("Menu.RsMenu.Barrier"));
        custom3.setItemMeta(customb);
        inv.setItem(15, custom3);

        ItemStack custom4 = new ItemStack(Material.BARRIER, 1);
        ItemMeta customc = custom4.getItemMeta();
        customc.setDisplayName(
                FilesManager.instance.getFromConfigFormatted("Menu.RsMenu.Barrier"));
        custom4.setItemMeta(customc);
        inv.setItem(16, custom4);

        ItemStack kick = new ItemStack(Material.SLIME_BLOCK, 1);
        ItemMeta kickMeta = kick.getItemMeta();
        kickMeta.setDisplayName(
                FilesManager.instance.getFromConfigFormatted("Menu.RsMenu.Kick"));
        kick.setItemMeta(kickMeta);
        inv.setItem(4, kick);

        if (player.hasPermission("mod") && player.hasPermission("mod+")) {

            ItemStack custom5 = new ItemStack(Material.ANVIL, 1);
            ItemMeta customd = custom5.getItemMeta();
            customd.setDisplayName(
                    FilesManager.instance.getFromConfigFormatted("Menu.RsMenu.Anvil"));
            custom5.setItemMeta(customd);
            inv.setItem(15, custom5);

            ItemStack custom6 = new ItemStack(Material.PAPER, 1);
            ItemMeta custome = custom6.getItemMeta();
            custome.setDisplayName(
                    FilesManager.instance.getFromConfigFormatted("Menu.RsMenu.Paper"));
            custom6.setItemMeta(custome);
            inv.setItem(16, custom6);

        }

        GuiManager.instance.applyHead(inv, cible, player);
        GuiManager.instance.applyGlass(inv);

        player.openInventory(inv);
    }
}
