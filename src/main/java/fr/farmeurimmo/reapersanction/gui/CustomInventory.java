package main.java.fr.farmeurimmo.reapersanction.gui;

import main.java.fr.farmeurimmo.reapersanction.storage.MessageManager;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.HashMap;
import java.util.List;

enum InventoryType {
    MAIN("Main"),
    BAN("Ban"),
    MUTE("Mute"),
    KICK("Kick"),
    BAN_IP("BanIP"),
    END("End"),
    REPORT("Report"),
    HISTORY("History");

    final String name;

    InventoryType(String i) {
        this.name = i;
    }

    public String getName() {
        return name;
    }
}

public class CustomInventory {

    private String name;
    private int size;
    private HashMap<Integer, ItemStack> items;
    private HashMap<Integer, InventoryAction> actionPerItem;
    private boolean isFill;
    private InventoryType type;

    public CustomInventory(String name, int size, HashMap<Integer, ItemStack> items, HashMap<Integer, InventoryAction> actionPerItem, boolean isFill, InventoryType type) {
        this.name = name;
        this.size = size;
        this.items = items;
        this.actionPerItem = actionPerItem;
        this.isFill = isFill;
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public int getSize() {
        return size;
    }

    public HashMap<Integer, ItemStack> getItems() {
        return items;
    }

    public boolean isFill() {
        return isFill;
    }

    public InventoryType getType() {
        return type;
    }

    public HashMap<Integer, InventoryAction> getActionPerItem() {
        return actionPerItem;
    }

    public void applyCible(String cible) {
        this.name = this.name.replace("%player%", cible);
        Player target = Bukkit.getPlayer(cible);
        String ip = target != null ? target.getAddress().getAddress().getHostAddress() : MessageManager.instance.getMessage("InvalidPlayer");
        for (int i : items.keySet()) {
            ItemStack item = items.get(i);
            ItemMeta meta = item.getItemMeta();
            if (item.getType() == Material.SKULL_ITEM) ((SkullMeta) meta).setOwner(cible);
            if (meta.getDisplayName() != null) meta.setDisplayName(meta.getDisplayName().replace("%player%", cible));
            if (meta.getLore() != null) {
                List<String> lore = meta.getLore();
                lore.replaceAll(s -> s.replace("%player%", cible));
                lore.replaceAll(s -> s.replace("%ip%", ip));
                meta.setLore(lore);
            }
            item.setItemMeta(meta);
        }
    }

    public Inventory getInventory() {
        Inventory inv = Bukkit.createInventory(null, size, name);
        for (int i : items.keySet()) inv.setItem(i, items.get(i));
        return inv;
    }

}
