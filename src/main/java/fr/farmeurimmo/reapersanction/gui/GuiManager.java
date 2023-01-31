package main.java.fr.farmeurimmo.reapersanction.gui;

import main.java.fr.farmeurimmo.reapersanction.ReaperSanction;
import main.java.fr.farmeurimmo.reapersanction.storage.FilesManager;
import main.java.fr.farmeurimmo.reapersanction.storage.MessageManager;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.Arrays;
import java.util.Collections;

public class GuiManager {

    public static GuiManager instance;

    public GuiManager() {
        instance = this;

        new HistoryGui();
        new BanGui();
        new MuteGui();
        new BanIpGui();
        new ReportGui();
        new RsGui();
        new EndGui();
        new KickGui();
    }

    public void applyGlass(Inventory inv) {
        if (!ReaperSanction.instance.getConfig().getBoolean("FillInventoryWithGlassPane")) return;
        ItemStack custom8 = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 0);
        ItemMeta meta8 = custom8.getItemMeta();
        meta8.setDisplayName("§6");
        custom8.setItemMeta(meta8);

        for (int i = 0; i < inv.getSize(); i++) {
            if (inv.getItem(i) == null || inv.getItem(i).getType().equals(Material.AIR)) inv.setItem(i, custom8);
        }
    }

    public void applyDoorsFromInvSize(Inventory inv) {
        ItemStack custom5 = new ItemStack(Material.IRON_DOOR, 1);
        ItemMeta customd = custom5.getItemMeta();
        customd.setDisplayName(FilesManager.instance.getFromConfigFormatted("Menu.RsMenu.GoBackDoor"));
        custom5.setItemMeta(customd);

        int firstItem = 0;
        if (inv.getSize() == 18) firstItem = 9;
        else if (inv.getSize() == 27) firstItem = 18;
        else if (inv.getSize() == 36) firstItem = 27;
        else if (inv.getSize() == 45) firstItem = 36;
        else if (inv.getSize() == 54) firstItem = 45;
        inv.setItem(firstItem, custom5);
        inv.setItem(firstItem + 8, custom5);
    }

    public void applyHead(Inventory inv, String cible, Player player) {
        ItemStack stack = new ItemStack(Material.SKULL_ITEM, 1, (byte) 3);
        ItemMeta meta = stack.getItemMeta();
        ((SkullMeta) meta).setOwner(cible);
        meta.setDisplayName("§6" + cible);
        Player target = Bukkit.getOfflinePlayer(cible).getPlayer();
        if (target == null)
            meta.setLore(Collections.singletonList(MessageManager.instance.getMessage("InvalidPlayer")));
        else {
            String hostname = target.getAddress().getHostName();
            String displayname = target.getDisplayName();
            if (player.hasPermission("mod+")) {
                if (ReaperSanction.instance.getConfig().getBoolean("IP.ShowIpForAdmin"))
                    meta.setLore(Arrays.asList(FilesManager.instance.getFromConfigFormatted("Menu.RsMenu.SkullLore.line1").replace("%displayname%", displayname).replace("%ip%", hostname),
                            FilesManager.instance.getFromConfigFormatted("Menu.RsMenu.SkullLore.line2").replace("%displayname%", displayname).replace("%ip%", hostname)));
                else
                    meta.setLore(Arrays.asList(FilesManager.instance.getFromConfigFormatted("Menu.RsMenu.SkullLore.line1").replace("%displayname%", displayname).replace("%ip%", "Disabled"),
                            FilesManager.instance.getFromConfigFormatted("Menu.RsMenu.SkullLore.line2").replace("%displayname%", displayname).replace("%ip%", "Disabled")));
            } else {
                if (ReaperSanction.instance.getConfig().getBoolean("IP.ShowIpForMod"))
                    meta.setLore(Arrays.asList(FilesManager.instance.getFromConfigFormatted("Menu.RsMenu.SkullLore.line1").replace("%displayname%", displayname).replace("%ip%", hostname),
                            FilesManager.instance.getFromConfigFormatted("Menu.RsMenu.SkullLore.line2").replace("%displayname%", displayname).replace("%ip%", hostname)));
                else
                    meta.setLore(Arrays.asList(FilesManager.instance.getFromConfigFormatted("Menu.RsMenu.SkullLore.line1").replace("%displayname%", displayname).replace("%ip%", "Disabled"),
                            FilesManager.instance.getFromConfigFormatted("Menu.RsMenu.SkullLore.line2").replace("%displayname%", displayname).replace("%ip%", "Disabled")));
            }
        }
        stack.setItemMeta(meta);
        inv.setItem(13, stack);
    }
}
