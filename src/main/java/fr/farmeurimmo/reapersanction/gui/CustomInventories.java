package main.java.fr.farmeurimmo.reapersanction.gui;

import main.java.fr.farmeurimmo.reapersanction.storage.FilesManager;
import main.java.fr.farmeurimmo.reapersanction.utils.ItemStackUtils;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;

public class CustomInventories {

    public static CustomInventories instance;
    public HashMap<InventoryType, CustomInventory> inventories = new HashMap<>();

    public CustomInventories() {
        instance = this;

        applyDefaultInventories();

        FilesManager.instance.setup_inventory_file();
        loadInventories();
    }

    public void applyDefaultInventories() {
        for (InventoryType type : InventoryType.values()) {
            if (type == InventoryType.MAIN) {
                HashMap<Integer, ItemStack> items = new HashMap<>();

                ArrayList<String> lore = new ArrayList<>();
                lore.add("§7IP : §e%ip%");

                items.put(10, ItemStackUtils.getItemStack(Material.GRASS, "§aMutes", null));
                items.put(11, ItemStackUtils.getItemStack(Material.DIAMOND_SWORD, "§aBans", null));

                items.put(4, ItemStackUtils.getItemStack(Material.SLIME_BLOCK, "§cKick", null));
                items.put(22, ItemStackUtils.getItemStack(Material.BOOK, "§eHistory", null));

                items.put(15, ItemStackUtils.getItemStack(Material.ANVIL, "§cBan IP", null));
                items.put(16, ItemStackUtils.getItemStack(Material.PAPER, "§cEnd", null));

                items.put(13, ItemStackUtils.getSkull("§6%player%", null, lore));

                inventories.put(type, new CustomInventory("§4ReaperSanction", 27, items, new HashMap<>(), true, type));
            }
        }
    }

    public Inventory getMainInventory(String cible) {
        CustomInventory inv = inventories.get(InventoryType.MAIN);
        inv.applyCible(cible);
        return inv.getInventory();
    }

    public void loadInventories() {
        for (InventoryType type : InventoryType.values()) {
            if (!FilesManager.instance.getInventoryData().isSet(type.getName())) {
                FilesManager.instance.getInventoryData().set(type.getName(), type.getName());
                FilesManager.instance.saveInventory();
            }
        }
    }


}
