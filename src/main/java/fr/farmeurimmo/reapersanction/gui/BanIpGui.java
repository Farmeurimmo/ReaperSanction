package main.java.fr.farmeurimmo.reapersanction.gui;

import main.java.fr.farmeurimmo.reapersanction.ConfigManager;
import main.java.fr.farmeurimmo.reapersanction.MessageManager;
import main.java.fr.farmeurimmo.reapersanction.ReaperSanction;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.Arrays;

public class BanIpGui {

    public static void banipgui(Player player, String cible) {
        if (player.hasPermission("mod+")) {
            Inventory inv = Bukkit.createInventory(null, 27, "§4ReaperSanction Bans-ip");

            ItemStack custom1 = new ItemStack(Material.NAME_TAG, 1);
            ItemMeta customS = custom1.getItemMeta();
            customS.setDisplayName(ConfigManager.instance.getFromConfigFormatted("Menu.Banip.NameTag.Reason"));
            custom1.setItemMeta(customS);
            inv.setItem(12, custom1);

            ItemStack custom2 = new ItemStack(Material.CLAY_BALL, 2);
            ItemMeta customa = custom2.getItemMeta();
            customa.setDisplayName(ConfigManager.instance.getFromConfigFormatted("Menu.Banip.ClayBall.Reason"));
            custom2.setItemMeta(customa);
            inv.setItem(14, custom2);

            if (player.hasPermission("mod+")) {
                if (ReaperSanction.instance.getConfig().getBoolean("IP.ShowIpForAdmin")) {
                    ItemStack stack = new ItemStack(Material.SKULL_ITEM, 1, (byte) 3);
                    ItemMeta meta = stack.getItemMeta();
                    ((SkullMeta) meta).setOwner(cible);
                    meta.setDisplayName("§6" + cible);
                    meta.setLore(Arrays.asList(ConfigManager.instance.getFromConfigFormatted("Menu.RsMenu.SkullLore.line1").replace("%displayname%", player.getDisplayName()).replace("%ip%", player.getAddress().getHostName()),
                            ConfigManager.instance.getFromConfigFormatted("Menu.RsMenu.SkullLore.line2").replace("%displayname%", player.getDisplayName()).replace("%ip%", player.getAddress().getHostName())));
                    stack.setItemMeta(meta);
                    inv.setItem(13, stack);
                } else {
                    ItemStack stack = new ItemStack(Material.SKULL_ITEM, 1, (byte) 3);
                    ItemMeta meta = stack.getItemMeta();
                    ((SkullMeta) meta).setOwner(cible);
                    meta.setDisplayName("§6" + cible);
                    meta.setLore(Arrays.asList(ConfigManager.instance.getFromConfigFormatted("Menu.RsMenu.SkullLore.line1").replace("%displayname%", player.getDisplayName()).replace("%ip%", "Disabled"),
                            ConfigManager.instance.getFromConfigFormatted("Menu.RsMenu.SkullLore.line2").replace("%displayname%", player.getDisplayName()).replace("%ip%", "Disabled")));
                    stack.setItemMeta(meta);
                    inv.setItem(13, stack);
                }
            } else {
                if (ReaperSanction.instance.getConfig().getBoolean("IP.ShowIpForMod")) {
                    ItemStack stack = new ItemStack(Material.SKULL_ITEM, 1, (byte) 3);
                    ItemMeta meta = stack.getItemMeta();
                    ((SkullMeta) meta).setOwner(cible);
                    meta.setDisplayName("§6" + cible);
                    meta.setLore(Arrays.asList(ConfigManager.instance.getFromConfigFormatted("Menu.RsMenu.SkullLore.line1").replace("%displayname%", player.getDisplayName()).replace("%ip%", player.getAddress().getHostName()),
                            ConfigManager.instance.getFromConfigFormatted("Menu.RsMenu.SkullLore.line2").replace("%displayname%", player.getDisplayName()).replace("%ip%", player.getAddress().getHostName())));
                    stack.setItemMeta(meta);
                    inv.setItem(13, stack);
                } else {
                    ItemStack stack = new ItemStack(Material.SKULL_ITEM, 1, (byte) 3);
                    ItemMeta meta = stack.getItemMeta();
                    ((SkullMeta) meta).setOwner(cible);
                    meta.setDisplayName("§6" + cible);
                    meta.setLore(Arrays.asList(ConfigManager.instance.getFromConfigFormatted("Menu.RsMenu.SkullLore.line1").replace("%displayname%", player.getDisplayName()).replace("%ip%", "Disabled"),
                            ConfigManager.instance.getFromConfigFormatted("Menu.RsMenu.SkullLore.line2").replace("%displayname%", player.getDisplayName()).replace("%ip%", "Disabled")));
                    stack.setItemMeta(meta);
                    inv.setItem(13, stack);
                }
            }

            ItemStack custom5 = new ItemStack(Material.IRON_DOOR, 1);
            ItemMeta customd = custom5.getItemMeta();
            customd.setDisplayName(ConfigManager.instance.getFromConfigFormatted("Menu.RsMenu.GoBackDoor"));
            custom5.setItemMeta(customd);
            inv.setItem(18, custom5);

            ItemStack custom6 = new ItemStack(Material.IRON_DOOR, 1);
            ItemMeta custome = custom6.getItemMeta();
            custome.setDisplayName(ConfigManager.instance.getFromConfigFormatted("Menu.RsMenu.GoBackDoor"));
            custom6.setItemMeta(custome);
            inv.setItem(26, custom6);

            if (ReaperSanction.instance.getConfig().getBoolean("FillInventoryWithGlassPane")) {
                ItemStack custom0 = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 0);
                ItemMeta meta0 = custom0.getItemMeta();
                meta0.setDisplayName("§6");
                custom0.setItemMeta(meta0);

                for (int i = 0; i < inv.getSize(); i++) {
                    if (inv.getItem(i) == null || inv.getItem(i).getType().equals(Material.AIR)) {
                        inv.setItem(i, custom0);
                    }
                }
            }

            player.openInventory(inv);

        } else {
            player.sendMessage(MessageManager.prefix + MessageManager.instance.getMessage("NoPermission"));
        }
    }

}
