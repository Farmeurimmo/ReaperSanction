package main.java.fr.farmeurimmo.reapersanction.gui;

import main.java.fr.farmeurimmo.reapersanction.ReaperSanction;
import main.java.fr.farmeurimmo.reapersanction.storage.FilesManager;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

public class ReportGui {

    public static void MakeReportGui(Player player, String target) {
        Inventory inv = Bukkit.createInventory(null, 27, "§4Report " + target);

        ItemStack custom1 = new ItemStack(Material.GRASS, 1);
        ItemMeta customS = custom1.getItemMeta();
        customS.setDisplayName(FilesManager.instance.getFromConfigFormatted("Report.Reason.grass"));
        custom1.setItemMeta(customS);
        inv.setItem(10, custom1);

        ItemStack custom2 = new ItemStack(Material.DIAMOND_SWORD, 1);
        ItemMeta customa = custom2.getItemMeta();
        customa.setDisplayName(FilesManager.instance.getFromConfigFormatted("Report.Reason.DiamondSword"));
        custom2.setItemMeta(customa);
        inv.setItem(11, custom2);

        ItemStack stack = new ItemStack(Material.SKULL_ITEM, 1, (byte) 3);
        ItemMeta meta = stack.getItemMeta();
        ((SkullMeta) meta).setOwner(target);
        meta.setDisplayName("§6" + target);
        stack.setItemMeta(meta);
        inv.setItem(13, stack);

        ItemStack custom3 = new ItemStack(Material.APPLE, 1);
        ItemMeta customb = custom3.getItemMeta();
        customb.setDisplayName(FilesManager.instance.getFromConfigFormatted("Report.Reason.apple"));
        custom3.setItemMeta(customb);
        inv.setItem(15, custom3);

        ItemStack custom4 = new ItemStack(Material.BEACON, 1);
        ItemMeta customc = custom4.getItemMeta();
        customc.setDisplayName(FilesManager.instance.getFromConfigFormatted("Report.Reason.beacon"));
        custom4.setItemMeta(customc);
        inv.setItem(16, custom4);

        if (ReaperSanction.instance.getConfig().getBoolean("FillInventoryWithGlassPane")) {
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
