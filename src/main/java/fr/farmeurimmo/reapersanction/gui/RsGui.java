package main.java.fr.farmeurimmo.reapersanction.gui;

import main.java.fr.farmeurimmo.reapersanction.ReaperSanction;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.Arrays;

public class RsGui {

    public static void SsMainGui(Player player, String target) {
        Inventory inv = Bukkit.createInventory(null, 27, "§4ReaperSanction");

        ItemStack custom1 = new ItemStack(Material.GRASS, 1);
        ItemMeta customS = custom1.getItemMeta();
        customS.setDisplayName(
                ReaperSanction.instance.getConfig().getString("ReaperSanction.Settings.Menu.RsMenu.GrassBlock").replace("&", "§"));
        custom1.setItemMeta(customS);
        inv.setItem(10, custom1);

        ItemStack custom2 = new ItemStack(Material.DIAMOND_SWORD, 1);
        ItemMeta customa = custom2.getItemMeta();
        customa.setDisplayName(
                ReaperSanction.instance.getConfig().getString("ReaperSanction.Settings.Menu.RsMenu.DiamondSword").replace("&", "§"));
        custom2.setItemMeta(customa);
        inv.setItem(11, custom2);

        if (player.hasPermission("mod+")) {
            if (ReaperSanction.instance.getConfig().getBoolean("ReaperSanction.Settings.IP.ShowIpForAdmin")) {
                ItemStack stack = new ItemStack(Material.SKULL_ITEM, 1, (byte) 3);
                ItemMeta meta = stack.getItemMeta();
                ((SkullMeta) meta).setOwner(target);
                meta.setDisplayName("§6" + target);
                meta.setLore(Arrays.asList(
                        ReaperSanction.instance.getConfig().getString("ReaperSanction.Settings.Menu.RsMenu.SkullLore.line1").replace("&", "§").replace("%displayname%", player.getDisplayName()).replace("%ip%", player.getAddress().getHostName()),
                        ReaperSanction.instance.getConfig().getString("ReaperSanction.Settings.Menu.RsMenu.SkullLore.line2").replace("&", "§").replace("%displayname%", player.getDisplayName()).replace("%ip%", player.getAddress().getHostName())));
                stack.setItemMeta(meta);
                inv.setItem(13, stack);
            } else {
                ItemStack stack = new ItemStack(Material.SKULL_ITEM, 1, (byte) 3);
                ItemMeta meta = stack.getItemMeta();
                ((SkullMeta) meta).setOwner(target);
                meta.setDisplayName("§6" + target);
                meta.setLore(Arrays.asList(
                        ReaperSanction.instance.getConfig().getString("ReaperSanction.Settings.Menu.RsMenu.SkullLore.line1").replace("&", "§").replace("%displayname%", player.getDisplayName()).replace("%ip%", "Disabled"),
                        ReaperSanction.instance.getConfig().getString("ReaperSanction.Settings.Menu.RsMenu.SkullLore.line2").replace("&", "§").replace("%displayname%", player.getDisplayName()).replace("%ip%", "Disabled")));
                stack.setItemMeta(meta);
                inv.setItem(13, stack);
            }
        } else {
            if (ReaperSanction.instance.getConfig().getBoolean("ReaperSanction.Settings.IP.ShowIpForMod")) {
                ItemStack stack = new ItemStack(Material.SKULL_ITEM, 1, (byte) 3);
                ItemMeta meta = stack.getItemMeta();
                ((SkullMeta) meta).setOwner(target);
                meta.setDisplayName("§6" + target);
                meta.setLore(Arrays.asList(
                        ReaperSanction.instance.getConfig().getString("ReaperSanction.Settings.Menu.RsMenu.SkullLore.line1").replace("&", "§").replace("%displayname%", player.getDisplayName()).replace("%ip%", player.getAddress().getHostName()),
                        ReaperSanction.instance.getConfig().getString("ReaperSanction.Settings.Menu.RsMenu.SkullLore.line2").replace("&", "§").replace("%displayname%", player.getDisplayName()).replace("%ip%", player.getAddress().getHostName())));
                stack.setItemMeta(meta);
                inv.setItem(13, stack);
            } else {
                ItemStack stack = new ItemStack(Material.SKULL_ITEM, 1, (byte) 3);
                ItemMeta meta = stack.getItemMeta();
                ((SkullMeta) meta).setOwner(target);
                meta.setDisplayName("§6" + target);
                meta.setLore(Arrays.asList(
                        ReaperSanction.instance.getConfig().getString("ReaperSanction.Settings.Menu.RsMenu.SkullLore.line1").replace("&", "§").replace("%displayname%", player.getDisplayName()).replace("%ip%", "Disabled"),
                        ReaperSanction.instance.getConfig().getString("ReaperSanction.Settings.Menu.RsMenu.SkullLore.line2").replace("&", "§").replace("%displayname%", player.getDisplayName()).replace("%ip%", "Disabled")));
                stack.setItemMeta(meta);
                inv.setItem(13, stack);
            }
        }

        ItemStack custom3 = new ItemStack(Material.BARRIER, 1);
        ItemMeta customb = custom3.getItemMeta();
        customb.setDisplayName(
                ReaperSanction.instance.getConfig().getString("ReaperSanction.Settings.Menu.RsMenu.Barrier").replace("&", "§"));
        custom3.setItemMeta(customb);
        inv.setItem(15, custom3);

        ItemStack custom4 = new ItemStack(Material.BARRIER, 1);
        ItemMeta customc = custom4.getItemMeta();
        customc.setDisplayName(
                ReaperSanction.instance.getConfig().getString("ReaperSanction.Settings.Menu.RsMenu.Barrier").replace("&", "§"));
        custom4.setItemMeta(customc);
        inv.setItem(16, custom4);

        if (player.hasPermission("mod") && player.hasPermission("mod+")) {

            ItemStack custom5 = new ItemStack(Material.ANVIL, 1);
            ItemMeta customd = custom5.getItemMeta();
            customd.setDisplayName(
                    ReaperSanction.instance.getConfig().getString("ReaperSanction.Settings.Menu.RsMenu.Anvil").replace("&", "§"));
            custom5.setItemMeta(customd);
            inv.setItem(15, custom5);

            ItemStack custom6 = new ItemStack(Material.PAPER, 1);
            ItemMeta custome = custom6.getItemMeta();
            custome.setDisplayName(
                    ReaperSanction.instance.getConfig().getString("ReaperSanction.Settings.Menu.RsMenu.Paper").replace("&", "§"));
            custom6.setItemMeta(custome);
            inv.setItem(16, custom6);

        }
        if (ReaperSanction.instance.getConfig().getBoolean("ReaperSanction.Settings.FillInventoryWithGlassPane")) {
            ItemStack custom8 = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 0);
            ItemMeta meta8 = custom8.getItemMeta();
            meta8.setDisplayName("§6");
            custom8.setItemMeta(meta8);

            for (int i = 0; i < inv.getSize(); i++) {
                if (inv.getItem(i) == null || inv.getItem(i).getType().equals(Material.AIR)) {
                    inv.setItem(i, custom8);
                }
            }
        }
        player.openInventory(inv);
    }
}
