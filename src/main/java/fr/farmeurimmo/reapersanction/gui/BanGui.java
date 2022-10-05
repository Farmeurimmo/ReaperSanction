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

public class BanGui {

    public static void bangui(Player player, String cible) {
        if (player.hasPermission("mod")) {
            Inventory inv = Bukkit.createInventory(null, 27, "§4ReaperSanction Bans");

            ItemStack custom1 = new ItemStack(Material.DIAMOND_SWORD, 1);
            ItemMeta customS = custom1.getItemMeta();
            customS.setDisplayName(ConfigManager.instance.getFromConfigFormatted("Menu.Bans.DiamondSword.Reason"));
            custom1.setItemMeta(customS);
            inv.setItem(10, custom1);

            ItemStack custom2 = new ItemStack(Material.WOOD_SWORD, 1);
            ItemMeta customa = custom2.getItemMeta();
            customa.setDisplayName(ConfigManager.instance.getFromConfigFormatted("Menu.Bans.WoodSword.Reason"));
            custom2.setItemMeta(customa);
            inv.setItem(11, custom2);

            ItemStack custom3 = new ItemStack(Material.FEATHER, 1);
            ItemMeta customb = custom3.getItemMeta();
            customb.setDisplayName(ConfigManager.instance.getFromConfigFormatted("Menu.Bans.Feather.Reason"));
            custom3.setItemMeta(customb);
            inv.setItem(15, custom3);

            ItemStack custom4 = new ItemStack(Material.IRON_BOOTS, 1);
            ItemMeta customc = custom4.getItemMeta();
            customc.setDisplayName(ConfigManager.instance.getFromConfigFormatted("Menu.Bans.IronBoots.Reason"));
            custom4.setItemMeta(customc);
            inv.setItem(16, custom4);

            ItemStack custom7 = new ItemStack(Material.GOLD_AXE, 1);
            ItemMeta customf = custom7.getItemMeta();
            customf.setDisplayName(ConfigManager.instance.getFromConfigFormatted("Menu.Bans.GoldAxe.Reason"));
            custom7.setItemMeta(customf);
            inv.setItem(4, custom7);

            ItemStack custom8 = new ItemStack(Material.ARMOR_STAND, 1);
            ItemMeta customg = custom8.getItemMeta();
            customg.setDisplayName(ConfigManager.instance.getFromConfigFormatted("Menu.Bans.ArmorStand.Reason"));
            custom8.setItemMeta(customg);
            inv.setItem(22, custom8);

            ItemStack custom9 = new ItemStack(Material.TNT, 1);
            ItemMeta customh = custom9.getItemMeta();
            customh.setDisplayName(ConfigManager.instance.getFromConfigFormatted("Menu.Bans.Tnt.Reason"));
            custom9.setItemMeta(customh);
            inv.setItem(12, custom9);

            ItemStack custom10 = new ItemStack(Material.LEATHER, 1);
            ItemMeta customi = custom10.getItemMeta();
            customi.setDisplayName(ConfigManager.instance.getFromConfigFormatted("Menu.Bans.Leather.Reason"));
            custom10.setItemMeta(customi);
            inv.setItem(14, custom10);

            ItemStack custom11 = new ItemStack(Material.DIAMOND_CHESTPLATE, 1);
            ItemMeta customj = custom11.getItemMeta();
            customj.setDisplayName(ConfigManager.instance.getFromConfigFormatted("Menu.Bans.DiamondChestPlate.Reason"));
            custom11.setItemMeta(customj);
            inv.setItem(9, custom11);

            ItemStack custom12 = new ItemStack(Material.DIRT, 1);
            ItemMeta customk = custom12.getItemMeta();
            customk.setDisplayName(ConfigManager.instance.getFromConfigFormatted("Menu.Bans.Dirt.Reason"));
            custom12.setItemMeta(customk);
            inv.setItem(17, custom12);

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
