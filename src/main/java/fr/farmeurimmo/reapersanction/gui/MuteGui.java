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

public class MuteGui {

    public static void mutegui(Player player, String cible) {
        if (player.hasPermission("mod")) {
            Inventory inv = Bukkit.createInventory(null, 27, "§4ReaperSanction Mutes");

            ItemStack custom1 = new ItemStack(Material.BOW, 1);
            ItemMeta customS = custom1.getItemMeta();
            customS.setDisplayName("§6" + ConfigManager.instance.getFromConfigFormatted("Menu.Mutes.Bow.Reason"));
            custom1.setItemMeta(customS);
            inv.setItem(10, custom1);

            ItemStack custom2 = new ItemStack(Material.DIAMOND_SWORD, 1);
            ItemMeta customa = custom2.getItemMeta();
            customa.setDisplayName("§6" + ConfigManager.instance.getFromConfigFormatted("Menu.Mutes.DiamondSword.Reason"));
            custom2.setItemMeta(customa);
            inv.setItem(11, custom2);

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
                if (ReaperSanction.instance.getConfig().getBoolean("IP.ShowIpForMod") == true) {
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

            ItemStack custom3 = new ItemStack(Material.ANVIL, 1);
            ItemMeta customb = custom3.getItemMeta();
            customb.setDisplayName("§6" + ConfigManager.instance.getFromConfigFormatted("Menu.Mutes.Anvil.Reason"));
            custom3.setItemMeta(customb);
            inv.setItem(15, custom3);

            ItemStack custom4 = new ItemStack(Material.REDSTONE_BLOCK, 1);
            ItemMeta customc = custom4.getItemMeta();
            customc.setDisplayName("§6" + ConfigManager.instance.getFromConfigFormatted("Menu.Mutes.RedstoneBlock.Reason"));
            custom4.setItemMeta(customc);
            inv.setItem(16, custom4);

            ItemStack custom7 = new ItemStack(Material.ACTIVATOR_RAIL, 1);
            ItemMeta customf = custom7.getItemMeta();
            customf.setDisplayName("§6" + ConfigManager.instance.getFromConfigFormatted("Menu.Mutes.ActivatorRail.Reason"));
            custom7.setItemMeta(customf);
            inv.setItem(4, custom7);

            ItemStack custom8 = new ItemStack(Material.ARMOR_STAND, 1);
            ItemMeta customg = custom8.getItemMeta();
            customg.setDisplayName("§6" + ConfigManager.instance.getFromConfigFormatted("Menu.Mutes.ArmorStand.Reason"));
            custom8.setItemMeta(customg);
            inv.setItem(22, custom8);

            ItemStack custom9 = new ItemStack(Material.COMPASS, 1);
            ItemMeta customh = custom9.getItemMeta();
            customh.setDisplayName("§6" + ConfigManager.instance.getFromConfigFormatted("Menu.Mutes.Compass.Reason"));
            custom9.setItemMeta(customh);
            inv.setItem(12, custom9);

            ItemStack custom10 = new ItemStack(Material.FLINT_AND_STEEL, 1);
            ItemMeta customi = custom10.getItemMeta();
            customi.setDisplayName("§6" + ConfigManager.instance.getFromConfigFormatted("Menu.Mutes.FlintAndSteel.Reason"));
            custom10.setItemMeta(customi);
            inv.setItem(14, custom10);

            ItemStack custom5 = new ItemStack(Material.IRON_DOOR, 1);
            ItemMeta customd = custom5.getItemMeta();
            customd.setDisplayName("§6" + ConfigManager.instance.getFromConfigFormatted("Menu.RsMenu.GoBackDoor"));
            custom5.setItemMeta(customd);
            inv.setItem(18, custom5);

            ItemStack custom6 = new ItemStack(Material.IRON_DOOR, 1);
            ItemMeta custome = custom6.getItemMeta();
            custome.setDisplayName("§6" + ConfigManager.instance.getFromConfigFormatted("Menu.RsMenu.GoBackDoor"));
            custom6.setItemMeta(custome);
            inv.setItem(26, custom6);

            if (ReaperSanction.instance.getConfig().getBoolean("FillInventoryWithGlassPane") == true) {
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
            player.sendMessage(MessageManager.prefix +
                    MessageManager.instance.getMessage("NoPermission"));
        }
    }

}
