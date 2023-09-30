package fr.farmeurimmo.reapersanction.spigot.gui;

import fr.farmeurimmo.reapersanction.api.storage.MessageManager;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CustomInventory {

    private final int size;
    private final InventoryType type;
    private String name;
    private HashMap<Integer, ItemStack> items;
    private HashMap<Integer, ArrayList<String>> actionPerItem;
    private boolean isFill;

    public CustomInventory(String name, int size, HashMap<Integer, ItemStack> items, HashMap<Integer, ArrayList<String>> actionPerItem, boolean isFill, InventoryType type) {
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

    public HashMap<Integer, ArrayList<String>> getActionPerItem() {
        return actionPerItem;
    }

    public void applyCible(String cible) {
        this.name = this.name.replace("%player%", cible);
        Player target = Bukkit.getPlayer(cible);
        String ip = target != null ? target.getAddress().getAddress().getHostAddress() : MessageManager.INSTANCE.getMessage("InvalidPlayer", false);
        for (int i : items.keySet()) {
            ItemStack item = items.get(i);
            ItemMeta meta = item.getItemMeta();
            if (item.getType() == Material.SKULL_ITEM) ((SkullMeta) meta).setOwner(cible);
            item.setItemMeta(meta);
            if (meta.getDisplayName() != null) meta.setDisplayName(meta.getDisplayName().replace("%player%", cible));
            if (meta.getLore() != null) {
                List<String> lore = meta.getLore();
                lore.replaceAll(s -> s.replace("%player%", cible));
                lore.replaceAll(s -> s.replace("%ip%", ip));
                meta.setLore(lore);
            }
            item.setItemMeta(meta);
        }
        for (int i : actionPerItem.keySet()) {
            ArrayList<String> actions = actionPerItem.get(i);
            for (int j = 0; j < actions.size(); j++) {
                String action = actions.get(j);
                action = action.replace("%player%", cible);
                action = action.replace("%ip%", ip);
                actions.set(j, action);
            }
            actionPerItem.put(i, actions);
        }
    }

    public void applyPage(int page) {
        if (page < 1) page = 1;
        for (int i : items.keySet()) {
            ItemStack item = items.get(i);
            ItemMeta meta = item.getItemMeta();
            if (meta.getDisplayName() != null)
                meta.setDisplayName(meta.getDisplayName().replace("%previous_page%", String.valueOf(page - 1)));
            if (meta.getDisplayName() != null)
                meta.setDisplayName(meta.getDisplayName().replace("%next_page%", String.valueOf(page + 1)));
            if (meta.getDisplayName() != null)
                meta.setDisplayName(meta.getDisplayName().replace("%page%", String.valueOf(page)));
            if (meta.getLore() != null) {
                List<String> lore = meta.getLore();
                int finalPage = page;
                lore.replaceAll(s -> s.replace("%previous_page%", String.valueOf(finalPage - 1)));
                lore.replaceAll(s -> s.replace("%next_page%", String.valueOf(finalPage + 1)));
                lore.replaceAll(s -> s.replace("%page%", String.valueOf(finalPage)));
                meta.setLore(lore);
            }
            item.setItemMeta(meta);
        }
        for (int i : actionPerItem.keySet()) {
            ArrayList<String> actions = actionPerItem.get(i);
            for (int j = 0; j < actions.size(); j++) {
                String action = actions.get(j);
                action = action.replace("%previous_page%", String.valueOf(page - 1));
                action = action.replace("%next_page%", String.valueOf(page + 1));
                action = action.replace("%page%", String.valueOf(page));
                actions.set(j, action);
            }
            actionPerItem.put(i, actions);
        }
    }

}
