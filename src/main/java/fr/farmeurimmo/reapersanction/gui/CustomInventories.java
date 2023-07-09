package main.java.fr.farmeurimmo.reapersanction.gui;

import main.java.fr.farmeurimmo.reapersanction.storage.FilesManager;
import main.java.fr.farmeurimmo.reapersanction.storage.MessageManager;
import main.java.fr.farmeurimmo.reapersanction.utils.ItemStackUtils;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class CustomInventories {

    public static CustomInventories instance;
    public HashMap<InventoryType, CustomInventory> inventories = new HashMap<>();

    public CustomInventories() {
        instance = this;

        applyDefaultInventories();

        FilesManager.instance.setup_inventory_file();
        loadInventories();
        saveInventories();
    }

    public void applyDefaultInventories() {
        for (InventoryType type : InventoryType.values()) {
            switch (type) {
                case MAIN:
                    HashMap<Integer, ItemStack> items = new HashMap<>();
                    HashMap<Integer, ArrayList<String>> actions = new HashMap<>();

                    ArrayList<String> lore = new ArrayList<>();
                    lore.add("§7IP : §e%ip%");

                    items.put(10, ItemStackUtils.getItemStack(Material.GRASS, "§aMutes", null, 1));
                    actions.put(10, new ArrayList<>(Arrays.asList("INT" + ActionGuiInterpreter.SEPARATOR + "GUI" +
                            ActionGuiInterpreter.SEPARATOR + "MUTE" + ActionGuiInterpreter.SEPARATOR + "%player%")));

                    items.put(11, ItemStackUtils.getItemStack(Material.DIAMOND_SWORD, "§aBans", null, 1));
                    actions.put(11, new ArrayList<>(Arrays.asList("INT" + ActionGuiInterpreter.SEPARATOR + "GUI" +
                            ActionGuiInterpreter.SEPARATOR + "BAN" + ActionGuiInterpreter.SEPARATOR + "%player%")));

                    items.put(4, ItemStackUtils.getItemStack(Material.SLIME_BLOCK, "§cKick", null, 1));
                    actions.put(4, new ArrayList<>(Arrays.asList("INT" + ActionGuiInterpreter.SEPARATOR + "GUI" +
                            ActionGuiInterpreter.SEPARATOR + "KICK" + ActionGuiInterpreter.SEPARATOR + "%player%")));

                    items.put(22, ItemStackUtils.getItemStack(Material.BOOK, "§eHistory", null, 1));
                    actions.put(22, new ArrayList<>(Arrays.asList("INT" + ActionGuiInterpreter.SEPARATOR + "GUI" +
                            ActionGuiInterpreter.SEPARATOR + "HISTORY" + ActionGuiInterpreter.SEPARATOR + "%player%")));

                    items.put(15, ItemStackUtils.getItemStack(Material.ANVIL, "§cBan IP", null, 1));
                    actions.put(15, new ArrayList<>(Arrays.asList("INT" + ActionGuiInterpreter.SEPARATOR + "GUI" +
                            ActionGuiInterpreter.SEPARATOR + "BANIP" + ActionGuiInterpreter.SEPARATOR + "%player%")));

                    items.put(16, ItemStackUtils.getItemStack(Material.PAPER, "§cEnd", null, 1));
                    actions.put(16, new ArrayList<>(Arrays.asList("INT" + ActionGuiInterpreter.SEPARATOR + "GUI" +
                            ActionGuiInterpreter.SEPARATOR + "END" + ActionGuiInterpreter.SEPARATOR + "%player%")));

                    items.put(13, ItemStackUtils.getSkull("§6%player%", null, lore));

                    inventories.put(type, new CustomInventory("§4ReaperSanction", 27, items, actions, true, type));
                    break;
                case BAN:
                    //TODO
                case BAN_IP:
                    //TODO
                case KICK:
                    //TODO
                case MUTE:
                    //TODO
                case HISTORY:
                    //TODO
                case END:
                    //TODO
                case REPORT:
                    //TODO
                default:
                    break;
            }
        }
    }

    public CustomInventory getCustomInventory(InventoryType type) {
        return inventories.get(type);
    }

    public void startInventoryOpenProcess(Player p, InventoryType type, String cible) {
        final CustomInventory ci = CustomInventories.instance.getCustomInventory(type);
        if (ci == null) {
            p.sendMessage(MessageManager.prefix +
                    "§cInternal Error: " + type.toString() + " is not found in the inventories list.");
            return;
        }
        new RsGui(p, cible, ci).open(p);
    }

    public void saveInventories() {
        for (CustomInventory inv : inventories.values()) {
            FilesManager.instance.getInventoryData().set(inv.getType() + ".name", inv.getName());
            FilesManager.instance.getInventoryData().set(inv.getType() + ".size", inv.getSize());
            FilesManager.instance.getInventoryData().set(inv.getType() + ".isFill", inv.isFill());
            for (Map.Entry<Integer, ItemStack> entry : inv.getItems().entrySet()) {
                FilesManager.instance.getInventoryData().set(inv.getType() + ".items." + entry.getKey() + ".type", entry.getValue().getType().name());
                FilesManager.instance.getInventoryData().set(inv.getType() + ".items." + entry.getKey() + ".amount", entry.getValue().getAmount());
                FilesManager.instance.getInventoryData().set(inv.getType() + ".items." + entry.getKey() + ".display", entry.getValue().getItemMeta().getDisplayName());
                if (entry.getValue().getItemMeta().getLore() != null) {
                    for (int i = 0; i < entry.getValue().getItemMeta().getLore().size(); i++) {
                        FilesManager.instance.getInventoryData().set(inv.getType() + ".items." + entry.getKey() + ".lore." + i, entry.getValue().getItemMeta().getLore().get(i));
                    }
                }
                if (inv.getActionPerItem().containsKey(entry.getKey())) {
                    int i = 0;
                    for (String action : inv.getActionPerItem().get(entry.getKey())) {
                        FilesManager.instance.getInventoryData().set(inv.getType() + ".items." + entry.getKey() + ".actions." + i, action);
                        i++;
                    }
                }
            }
        }
        FilesManager.instance.saveInventory();
    }

    public void loadInventories() {
        for (InventoryType type : InventoryType.values()) {
            try {
                if (FilesManager.instance.getInventoryData().contains(type + ".name")) {
                    String name = FilesManager.instance.getInventoryData().getString(type + ".name");
                    int size = FilesManager.instance.getInventoryData().getInt(type + ".size");
                    boolean isFill = FilesManager.instance.getInventoryData().getBoolean(type + ".isFill");
                    HashMap<Integer, ItemStack> items = new HashMap<>();
                    HashMap<Integer, ArrayList<String>> actions = new HashMap<>();
                    for (String key : FilesManager.instance.getInventoryData().getConfigurationSection(type + ".items").getKeys(false)) {
                        int slot = Integer.parseInt(key);
                        Material material = Material.valueOf(FilesManager.instance.getInventoryData().getString(type + ".items." + key + ".type"));
                        int amount = FilesManager.instance.getInventoryData().getInt(type + ".items." + key + ".amount");
                        String display = FilesManager.instance.getInventoryData().getString(type + ".items." + key + ".display");
                        ArrayList<String> lore = new ArrayList<>();
                        if (FilesManager.instance.getInventoryData().contains(type + ".items." + key + ".lore")) {
                            for (String loreKey : FilesManager.instance.getInventoryData().getConfigurationSection(type + ".items." + key + ".lore").getKeys(false)) {
                                lore.add(FilesManager.instance.getInventoryData().getString(type + ".items." + key + ".lore." + loreKey));
                            }
                        }
                        ItemStack item = ItemStackUtils.getItemStack(material, display, lore, amount);
                        items.put(slot, item);
                        if (FilesManager.instance.getInventoryData().contains(type + ".items." + key + ".actions")) {
                            ArrayList<String> actionList = new ArrayList<>();
                            for (String actionKey : FilesManager.instance.getInventoryData().getConfigurationSection(type + ".items." + key + ".actions").getKeys(false)) {
                                actionList.add(FilesManager.instance.getInventoryData().getString(type + ".items." + key + ".actions." + actionKey));
                            }
                            actions.put(slot, actionList);
                        }
                    }
                    inventories.put(type, new CustomInventory(name, size, items, actions, isFill, type));
                }
            } catch (Exception ignored) {
            }
        }
    }


}
