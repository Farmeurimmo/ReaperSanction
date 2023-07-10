package fr.farmeurimmo.reapersanction.gui;

import fr.farmeurimmo.reapersanction.storage.FilesManager;
import fr.farmeurimmo.reapersanction.storage.MessageManager;
import fr.farmeurimmo.reapersanction.utils.ItemStackUtils;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.ArrayList;
import java.util.Collections;
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
            HashMap<Integer, ItemStack> items = new HashMap<>();
            HashMap<Integer, ArrayList<String>> actions = new HashMap<>();

            ArrayList<String> lore = new ArrayList<>();
            lore.add("§7IP : §e%ip%");
            switch (type) {
                case MAIN: {
                    items.put(10, ItemStackUtils.getItemStack(Material.GRASS, "§aMutes", null, 1));
                    actions.put(10, new ArrayList<>(Collections.singletonList("INT" + ActionGuiInterpreter.SEPARATOR + "GUI" +
                            ActionGuiInterpreter.SEPARATOR + "MUTE" + ActionGuiInterpreter.SEPARATOR + "%player%")));

                    items.put(11, ItemStackUtils.getItemStack(Material.DIAMOND_SWORD, "§aBans", null, 1));
                    actions.put(11, new ArrayList<>(Collections.singletonList("INT" + ActionGuiInterpreter.SEPARATOR + "GUI" +
                            ActionGuiInterpreter.SEPARATOR + "BAN" + ActionGuiInterpreter.SEPARATOR + "%player%")));

                    items.put(4, ItemStackUtils.getItemStack(Material.SLIME_BLOCK, "§cKick", null, 1));
                    actions.put(4, new ArrayList<>(Collections.singletonList("INT" + ActionGuiInterpreter.SEPARATOR + "GUI" +
                            ActionGuiInterpreter.SEPARATOR + "KICK" + ActionGuiInterpreter.SEPARATOR + "%player%")));

                    items.put(22, ItemStackUtils.getItemStack(Material.BOOK, "§eHistory", null, 1));
                    actions.put(22, new ArrayList<>(Collections.singletonList("INT" + ActionGuiInterpreter.SEPARATOR + "GUI_DYN" +
                            ActionGuiInterpreter.SEPARATOR + "HISTORY" + ActionGuiInterpreter.SEPARATOR + "%player%")));

                    items.put(15, ItemStackUtils.getItemStack(Material.ANVIL, "§cBan IP", null, 1));
                    actions.put(15, new ArrayList<>(Collections.singletonList("INT" + ActionGuiInterpreter.SEPARATOR + "GUI" +
                            ActionGuiInterpreter.SEPARATOR + "BAN_IP" + ActionGuiInterpreter.SEPARATOR + "%player%")));

                    items.put(16, ItemStackUtils.getItemStack(Material.PAPER, "§cEnd", null, 1));
                    actions.put(16, new ArrayList<>(Collections.singletonList("INT" + ActionGuiInterpreter.SEPARATOR + "GUI" +
                            ActionGuiInterpreter.SEPARATOR + "END" + ActionGuiInterpreter.SEPARATOR + "%player%")));

                    items.put(13, ItemStackUtils.getSkull("§6%player%", null, lore));

                    inventories.put(type, new CustomInventory("§4ReaperSanction", 27, items, actions, true, type));
                    break;
                }
                case BAN: {
                    items.put(4, ItemStackUtils.getItemStack(Material.GOLD_AXE, "Incorrect build", null, 1));
                    actions.put(4, new ArrayList<>(Collections.singletonList("INT" + ActionGuiInterpreter.SEPARATOR + "TEMPBAN" +
                            ActionGuiInterpreter.SEPARATOR + "%player%" + ActionGuiInterpreter.SEPARATOR + "5day" +
                            ActionGuiInterpreter.SEPARATOR + items.get(4).getItemMeta().getDisplayName())));

                    items.put(10, ItemStackUtils.getItemStack(Material.DIAMOND_CHESTPLATE, "Other", null, 1));
                    actions.put(10, new ArrayList<>(Collections.singletonList("INT" + ActionGuiInterpreter.SEPARATOR + "TEMPBAN" +
                            ActionGuiInterpreter.SEPARATOR + "%player%" + ActionGuiInterpreter.SEPARATOR + "7day" +
                            ActionGuiInterpreter.SEPARATOR + items.get(10).getItemMeta().getDisplayName())));

                    items.put(11, ItemStackUtils.getItemStack(Material.DIAMOND_SWORD, "Kill aura", null, 1));
                    actions.put(11, new ArrayList<>(Collections.singletonList("INT" + ActionGuiInterpreter.SEPARATOR + "TEMPBAN" +
                            ActionGuiInterpreter.SEPARATOR + "%player%" + ActionGuiInterpreter.SEPARATOR + "7day" +
                            ActionGuiInterpreter.SEPARATOR + items.get(11).getItemMeta().getDisplayName())));

                    items.put(12, ItemStackUtils.getItemStack(Material.WOOD_SWORD, "CPS", null, 1));
                    actions.put(12, new ArrayList<>(Collections.singletonList("INT" + ActionGuiInterpreter.SEPARATOR + "TEMPBAN" +
                            ActionGuiInterpreter.SEPARATOR + "%player%" + ActionGuiInterpreter.SEPARATOR + "7day" +
                            ActionGuiInterpreter.SEPARATOR + items.get(12).getItemMeta().getDisplayName())));

                    items.put(14, ItemStackUtils.getItemStack(Material.NAME_TAG, "Incorrect player name", null, 1));
                    actions.put(14, new ArrayList<>(Collections.singletonList("INT" + ActionGuiInterpreter.SEPARATOR + "TEMPBAN" +
                            ActionGuiInterpreter.SEPARATOR + "%player%" + ActionGuiInterpreter.SEPARATOR + "10year" +
                            ActionGuiInterpreter.SEPARATOR + items.get(14).getItemMeta().getDisplayName())));

                    items.put(15, ItemStackUtils.getItemStack(Material.LEATHER, "Incorrect skin", null, 1));
                    actions.put(15, new ArrayList<>(Collections.singletonList("INT" + ActionGuiInterpreter.SEPARATOR + "TEMPBAN" +
                            ActionGuiInterpreter.SEPARATOR + "%player%" + ActionGuiInterpreter.SEPARATOR + "1day" +
                            ActionGuiInterpreter.SEPARATOR + items.get(15).getItemMeta().getDisplayName())));

                    items.put(16, ItemStackUtils.getItemStack(Material.FEATHER, "Fly", null, 1));
                    actions.put(16, new ArrayList<>(Collections.singletonList("INT" + ActionGuiInterpreter.SEPARATOR + "TEMPBAN" +
                            ActionGuiInterpreter.SEPARATOR + "%player%" + ActionGuiInterpreter.SEPARATOR + "7day" +
                            ActionGuiInterpreter.SEPARATOR + items.get(16).getItemMeta().getDisplayName())));

                    items.put(22, ItemStackUtils.getItemStack(Material.ARMOR_STAND, "Other hacks", null, 1));
                    actions.put(22, new ArrayList<>(Collections.singletonList("INT" + ActionGuiInterpreter.SEPARATOR + "TEMPBAN" +
                            ActionGuiInterpreter.SEPARATOR + "%player%" + ActionGuiInterpreter.SEPARATOR + "7day" +
                            ActionGuiInterpreter.SEPARATOR + items.get(22).getItemMeta().getDisplayName())));

                    items.put(13, ItemStackUtils.getSkull("§6%player%", null, lore));

                    items.put(18, ItemStackUtils.getItemStack(Material.IRON_DOOR, "§cBack", null, 1));
                    actions.put(18, new ArrayList<>(Collections.singletonList("INT" + ActionGuiInterpreter.SEPARATOR + "GUI" +
                            ActionGuiInterpreter.SEPARATOR + "MAIN" + ActionGuiInterpreter.SEPARATOR + "%player%")));

                    items.put(26, ItemStackUtils.getItemStack(Material.BARRIER, "§cClose", null, 1));
                    actions.put(26, new ArrayList<>(Collections.singletonList("INT" + ActionGuiInterpreter.SEPARATOR + "CLOSE")));

                    inventories.put(type, new CustomInventory("§4ReaperSanction Bans", 27, items, actions, true, type));
                    break;
                }
                case BAN_IP: {
                    items.put(12, ItemStackUtils.getItemStack(Material.NAME_TAG, "Usurpation", null, 1));
                    actions.put(12, new ArrayList<>(Collections.singletonList("INT" + ActionGuiInterpreter.SEPARATOR + "BAN_IP" +
                            ActionGuiInterpreter.SEPARATOR + "%player%" + ActionGuiInterpreter.SEPARATOR +
                            items.get(12).getItemMeta().getDisplayName())));

                    items.put(14, ItemStackUtils.getItemStack(Material.CLAY_BALL, "Other", null, 2));
                    actions.put(14, new ArrayList<>(Collections.singletonList("INT" + ActionGuiInterpreter.SEPARATOR + "BAN_IP" +
                            ActionGuiInterpreter.SEPARATOR + "%player%" + ActionGuiInterpreter.SEPARATOR +
                            items.get(14).getItemMeta().getDisplayName())));

                    items.put(13, ItemStackUtils.getSkull("§6%player%", null, lore));

                    items.put(18, ItemStackUtils.getItemStack(Material.IRON_DOOR, "§cBack", null, 1));
                    actions.put(18, new ArrayList<>(Collections.singletonList("INT" + ActionGuiInterpreter.SEPARATOR + "GUI" +
                            ActionGuiInterpreter.SEPARATOR + "MAIN" + ActionGuiInterpreter.SEPARATOR + "%player%")));

                    items.put(26, ItemStackUtils.getItemStack(Material.BARRIER, "§cClose", null, 1));
                    actions.put(26, new ArrayList<>(Collections.singletonList("INT" + ActionGuiInterpreter.SEPARATOR + "CLOSE")));

                    inventories.put(type, new CustomInventory("§4ReaperSanction Bans IP", 27, items, actions, true, type));
                    break;
                }
                case KICK: {
                    items.put(12, ItemStackUtils.getItemStack(Material.TNT, "Other", null, 1));
                    actions.put(12, new ArrayList<>(Collections.singletonList("INT" + ActionGuiInterpreter.SEPARATOR + "KICK" +
                            ActionGuiInterpreter.SEPARATOR + "%player%" +
                            ActionGuiInterpreter.SEPARATOR + items.get(12).getItemMeta().getDisplayName())));

                    items.put(14, ItemStackUtils.getItemStack(Material.FEATHER, "Non specified reason", null, 1));
                    actions.put(14, new ArrayList<>(Collections.singletonList("INT" + ActionGuiInterpreter.SEPARATOR + "KICK" +
                            ActionGuiInterpreter.SEPARATOR + "%player%" +
                            ActionGuiInterpreter.SEPARATOR + items.get(14).getItemMeta().getDisplayName())));

                    items.put(13, ItemStackUtils.getSkull("§6%player%", null, lore));

                    items.put(18, ItemStackUtils.getItemStack(Material.IRON_DOOR, "§cBack", null, 1));
                    actions.put(18, new ArrayList<>(Collections.singletonList("INT" + ActionGuiInterpreter.SEPARATOR + "GUI" +
                            ActionGuiInterpreter.SEPARATOR + "MAIN" + ActionGuiInterpreter.SEPARATOR + "%player%")));

                    items.put(26, ItemStackUtils.getItemStack(Material.BARRIER, "§cClose", null, 1));
                    actions.put(26, new ArrayList<>(Collections.singletonList("INT" + ActionGuiInterpreter.SEPARATOR + "CLOSE")));

                    inventories.put(type, new CustomInventory("§4ReaperSanction Kicks", 27, items, actions, true, type));
                    break;
                }
                case MUTE: {
                    items.put(4, ItemStackUtils.getItemStack(Material.DETECTOR_RAIL, "Incitment to crime", null, 1));
                    actions.put(4, new ArrayList<>(Collections.singletonList("INT" + ActionGuiInterpreter.SEPARATOR + "TEMPMUTE" +
                            ActionGuiInterpreter.SEPARATOR + "%player%" + ActionGuiInterpreter.SEPARATOR + "1hour" +
                            ActionGuiInterpreter.SEPARATOR + items.get(4).getItemMeta().getDisplayName())));

                    items.put(10, ItemStackUtils.getItemStack(Material.BOW, "Spam", null, 1));
                    actions.put(10, new ArrayList<>(Collections.singletonList("INT" + ActionGuiInterpreter.SEPARATOR + "TEMPMUTE" +
                            ActionGuiInterpreter.SEPARATOR + "%player%" + ActionGuiInterpreter.SEPARATOR + "20min" +
                            ActionGuiInterpreter.SEPARATOR + items.get(10).getItemMeta().getDisplayName())));

                    items.put(11, ItemStackUtils.getItemStack(Material.DIAMOND_SWORD, "Flood", null, 1));
                    actions.put(11, new ArrayList<>(Collections.singletonList("INT" + ActionGuiInterpreter.SEPARATOR + "TEMPMUTE" +
                            ActionGuiInterpreter.SEPARATOR + "%player%" + ActionGuiInterpreter.SEPARATOR + "20min" +
                            ActionGuiInterpreter.SEPARATOR + items.get(11).getItemMeta().getDisplayName())));

                    items.put(12, ItemStackUtils.getItemStack(Material.COMPASS, "Pub", null, 1));
                    actions.put(12, new ArrayList<>(Collections.singletonList("INT" + ActionGuiInterpreter.SEPARATOR + "TEMPMUTE" +
                            ActionGuiInterpreter.SEPARATOR + "%player%" + ActionGuiInterpreter.SEPARATOR + "1day" +
                            ActionGuiInterpreter.SEPARATOR + items.get(12).getItemMeta().getDisplayName())));

                    items.put(13, ItemStackUtils.getSkull("§6%player%", null, lore));

                    items.put(14, ItemStackUtils.getItemStack(Material.FLINT_AND_STEEL, "Threat", null, 1));
                    actions.put(14, new ArrayList<>(Collections.singletonList("INT" + ActionGuiInterpreter.SEPARATOR + "TEMPMUTE" +
                            ActionGuiInterpreter.SEPARATOR + "%player%" + ActionGuiInterpreter.SEPARATOR + "1day" +
                            ActionGuiInterpreter.SEPARATOR + items.get(14).getItemMeta().getDisplayName())));

                    items.put(15, ItemStackUtils.getItemStack(Material.ANVIL, "Insults", null, 1));
                    actions.put(15, new ArrayList<>(Collections.singletonList("INT" + ActionGuiInterpreter.SEPARATOR + "TEMPMUTE" +
                            ActionGuiInterpreter.SEPARATOR + "%player%" + ActionGuiInterpreter.SEPARATOR + "30min" +
                            ActionGuiInterpreter.SEPARATOR + items.get(15).getItemMeta().getDisplayName())));

                    items.put(16, ItemStackUtils.getItemStack(Material.REDSTONE_BLOCK, "Provocation", null, 1));
                    actions.put(16, new ArrayList<>(Collections.singletonList("INT" + ActionGuiInterpreter.SEPARATOR + "TEMPMUTE" +
                            ActionGuiInterpreter.SEPARATOR + "%player%" + ActionGuiInterpreter.SEPARATOR + "30min" +
                            ActionGuiInterpreter.SEPARATOR + items.get(16).getItemMeta().getDisplayName())));

                    items.put(22, ItemStackUtils.getItemStack(Material.ARMOR_STAND, "Sanction evading", null, 1));
                    actions.put(22, new ArrayList<>(Collections.singletonList("INT" + ActionGuiInterpreter.SEPARATOR + "TEMPMUTE" +
                            ActionGuiInterpreter.SEPARATOR + "%player%" + ActionGuiInterpreter.SEPARATOR + "30min" +
                            ActionGuiInterpreter.SEPARATOR + items.get(22).getItemMeta().getDisplayName())));

                    items.put(18, ItemStackUtils.getItemStack(Material.IRON_DOOR, "§cBack", null, 1));
                    actions.put(18, new ArrayList<>(Collections.singletonList("INT" + ActionGuiInterpreter.SEPARATOR + "GUI" +
                            ActionGuiInterpreter.SEPARATOR + "MAIN" + ActionGuiInterpreter.SEPARATOR + "%player%")));

                    items.put(26, ItemStackUtils.getItemStack(Material.BARRIER, "§cClose", null, 1));
                    actions.put(26, new ArrayList<>(Collections.singletonList("INT" + ActionGuiInterpreter.SEPARATOR + "CLOSE")));

                    inventories.put(type, new CustomInventory("§4ReaperSanction Mutes", 27, items, actions, true, type));
                    break;
                }
                case END: {
                    items.put(4, ItemStackUtils.getItemStack(Material.BOW, "Unmute", null, 1));
                    actions.put(4, new ArrayList<>(Collections.singletonList("INT" + ActionGuiInterpreter.SEPARATOR + "UNMUTE" +
                            ActionGuiInterpreter.SEPARATOR + "%player%")));

                    items.put(12, ItemStackUtils.getItemStack(Material.DIAMOND_SWORD, "Unban", null, 1));
                    actions.put(12, new ArrayList<>(Collections.singletonList("INT" + ActionGuiInterpreter.SEPARATOR + "UNBAN" +
                            ActionGuiInterpreter.SEPARATOR + "%player%")));

                    items.put(14, ItemStackUtils.getItemStack(Material.ANVIL, "UnbanIP", null, 1));
                    actions.put(14, new ArrayList<>(Collections.singletonList("INT" + ActionGuiInterpreter.SEPARATOR + "UNBAN_IP" +
                            ActionGuiInterpreter.SEPARATOR + "%player%")));

                    items.put(18, ItemStackUtils.getItemStack(Material.IRON_DOOR, "§cBack", null, 1));
                    actions.put(18, new ArrayList<>(Collections.singletonList("INT" + ActionGuiInterpreter.SEPARATOR + "GUI" +
                            ActionGuiInterpreter.SEPARATOR + "MAIN" + ActionGuiInterpreter.SEPARATOR + "%player%")));

                    items.put(26, ItemStackUtils.getItemStack(Material.BARRIER, "§cClose", null, 1));
                    actions.put(26, new ArrayList<>(Collections.singletonList("INT" + ActionGuiInterpreter.SEPARATOR + "CLOSE")));

                    items.put(13, ItemStackUtils.getSkull("§6%player%", null, lore));

                    inventories.put(type, new CustomInventory("§4ReaperSanction End", 27, items, actions, true, type));
                    break;
                }
                case REPORT: {
                    items.put(10, ItemStackUtils.getItemStack(Material.NAME_TAG, "Incorrect skin/name", null, 1));
                    actions.put(10, new ArrayList<>(Collections.singletonList("INT" + ActionGuiInterpreter.SEPARATOR + "REPORT" +
                            ActionGuiInterpreter.SEPARATOR + items.get(10).getItemMeta().getDisplayName() +
                            ActionGuiInterpreter.SEPARATOR + "%player%")));

                    items.put(11, ItemStackUtils.getItemStack(Material.DIAMOND_SWORD, "Cheating", null, 1));
                    actions.put(11, new ArrayList<>(Collections.singletonList("INT" + ActionGuiInterpreter.SEPARATOR + "REPORT" +
                            ActionGuiInterpreter.SEPARATOR + items.get(11).getItemMeta().getDisplayName() +
                            ActionGuiInterpreter.SEPARATOR + "%player%")));

                    items.put(15, ItemStackUtils.getItemStack(Material.APPLE, "Cross team", null, 1));
                    actions.put(15, new ArrayList<>(Collections.singletonList("INT" + ActionGuiInterpreter.SEPARATOR + "REPORT" +
                            ActionGuiInterpreter.SEPARATOR + items.get(15).getItemMeta().getDisplayName() +
                            ActionGuiInterpreter.SEPARATOR + "%player%")));

                    items.put(16, ItemStackUtils.getItemStack(Material.ANVIL, "Other", null, 1));
                    actions.put(16, new ArrayList<>(Collections.singletonList("INT" + ActionGuiInterpreter.SEPARATOR + "REPORT" +
                            ActionGuiInterpreter.SEPARATOR + items.get(16).getItemMeta().getDisplayName() +
                            ActionGuiInterpreter.SEPARATOR + "%player%")));

                    items.put(13, ItemStackUtils.getSkull("Report %player%", null, null));

                    inventories.put(type, new CustomInventory("§4ReaperSanction Report", 27, items, actions, true, type));
                    break;
                }
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
        new Gui(p, cible, ci).open(p);
    }

    public void saveInventories() {
        for (CustomInventory inv : inventories.values()) {
            FilesManager.instance.getInventoryData().set(inv.getType() + ".name", inv.getName());
            FilesManager.instance.getInventoryData().set(inv.getType() + ".size", inv.getSize());
            FilesManager.instance.getInventoryData().set(inv.getType() + ".isFill", inv.isFill());
            for (Map.Entry<Integer, ItemStack> entry : inv.getItems().entrySet()) {
                FilesManager.instance.getInventoryData().set(inv.getType() + ".items." + entry.getKey() + ".type", entry.getValue().getType().name());
                if (entry.getValue().getType() == Material.SKULL_ITEM) {
                    SkullMeta meta = (SkullMeta) entry.getValue().getItemMeta();
                    FilesManager.instance.getInventoryData().set(inv.getType() + ".items." + entry.getKey() + ".owner", meta.getOwner());
                }
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
                        ItemStack item;
                        if (material != Material.SKULL_ITEM) {
                            item = ItemStackUtils.getItemStack(material, display, lore, amount);
                        } else {
                            String owner = FilesManager.instance.getInventoryData().getString(type + ".items." + key + ".owner");
                            item = ItemStackUtils.getSkull(display, owner, lore);
                        }
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
