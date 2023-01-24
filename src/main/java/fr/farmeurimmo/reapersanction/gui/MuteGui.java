package main.java.fr.farmeurimmo.reapersanction.gui;

import main.java.fr.farmeurimmo.reapersanction.storage.FilesManager;
import main.java.fr.farmeurimmo.reapersanction.storage.MessageManager;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class MuteGui {

    public static MuteGui instance;

    public MuteGui() {
        instance = this;
    }

    public void mutegui(Player player, String cible) {
        if (!player.hasPermission("mod")) {
            player.sendMessage(MessageManager.prefix + MessageManager.instance.getMessage("NoPermission"));
            return;
        }

        Inventory inv = Bukkit.createInventory(null, 27, "§4ReaperSanction Mutes");

        ItemStack custom1 = new ItemStack(Material.BOW, 1);
        ItemMeta customS = custom1.getItemMeta();
        customS.setDisplayName(FilesManager.instance.getFromConfigFormatted("Menu.Mutes.Bow.Reason"));
        custom1.setItemMeta(customS);
        inv.setItem(10, custom1);

        ItemStack custom2 = new ItemStack(Material.DIAMOND_SWORD, 1);
        ItemMeta customa = custom2.getItemMeta();
        customa.setDisplayName(FilesManager.instance.getFromConfigFormatted("Menu.Mutes.DiamondSword.Reason"));
        custom2.setItemMeta(customa);
        inv.setItem(11, custom2);

        ItemStack custom3 = new ItemStack(Material.ANVIL, 1);
        ItemMeta customb = custom3.getItemMeta();
        customb.setDisplayName(FilesManager.instance.getFromConfigFormatted("Menu.Mutes.Anvil.Reason"));
        custom3.setItemMeta(customb);
        inv.setItem(15, custom3);

        ItemStack custom4 = new ItemStack(Material.REDSTONE_BLOCK, 1);
        ItemMeta customc = custom4.getItemMeta();
        customc.setDisplayName(FilesManager.instance.getFromConfigFormatted("Menu.Mutes.RedstoneBlock.Reason"));
        custom4.setItemMeta(customc);
        inv.setItem(16, custom4);

        ItemStack custom7 = new ItemStack(Material.ACTIVATOR_RAIL, 1);
        ItemMeta customf = custom7.getItemMeta();
        customf.setDisplayName(FilesManager.instance.getFromConfigFormatted("Menu.Mutes.ActivatorRail.Reason"));
        custom7.setItemMeta(customf);
        inv.setItem(4, custom7);

        ItemStack custom8 = new ItemStack(Material.ARMOR_STAND, 1);
        ItemMeta customg = custom8.getItemMeta();
        customg.setDisplayName(FilesManager.instance.getFromConfigFormatted("Menu.Mutes.ArmorStand.Reason"));
        custom8.setItemMeta(customg);
        inv.setItem(22, custom8);

        ItemStack custom9 = new ItemStack(Material.COMPASS, 1);
        ItemMeta customh = custom9.getItemMeta();
        customh.setDisplayName(FilesManager.instance.getFromConfigFormatted("Menu.Mutes.Compass.Reason"));
        custom9.setItemMeta(customh);
        inv.setItem(12, custom9);

        ItemStack custom10 = new ItemStack(Material.FLINT_AND_STEEL, 1);
        ItemMeta customi = custom10.getItemMeta();
        customi.setDisplayName(FilesManager.instance.getFromConfigFormatted("Menu.Mutes.FlintAndSteel.Reason"));
        custom10.setItemMeta(customi);
        inv.setItem(14, custom10);

        GuiManager.instance.applyHead(inv, cible, player);
        GuiManager.instance.applyDoorsFromInvSize(inv);
        GuiManager.instance.applyGlass(inv);

        player.openInventory(inv);
    }

}
