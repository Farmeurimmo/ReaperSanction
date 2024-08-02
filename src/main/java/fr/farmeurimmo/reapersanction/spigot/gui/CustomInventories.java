package fr.farmeurimmo.reapersanction.spigot.gui;

import fr.farmeurimmo.reapersanction.core.storage.FilesManager;
import fr.farmeurimmo.reapersanction.core.storage.MessageManager;
import fr.farmeurimmo.reapersanction.core.users.Sanction;
import fr.farmeurimmo.reapersanction.core.users.User;
import fr.farmeurimmo.reapersanction.spigot.utils.ItemStackUtils;
import fr.farmeurimmo.reapersanction.utils.Parser;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.*;

public class CustomInventories {

    public static CustomInventories INSTANCE;
    public HashMap<InventoryType, CustomInventory> inventories = new HashMap<>();

    public CustomInventories() {
        INSTANCE = this;

        applyDefaultInventories();

        FilesManager.INSTANCE.setupInventories();
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
                    items.put(4, ItemStackUtils.getItemStack(Material.DETECTOR_RAIL, "Incitement to crime", null, 1));
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
                case HISTORY: {
                    items.put(53, ItemStackUtils.getItemStack(Material.IRON_DOOR, "§cBack", null, 1));
                    actions.put(53, new ArrayList<>(Collections.singletonList("INT" + ActionGuiInterpreter.SEPARATOR + "GUI" +
                            ActionGuiInterpreter.SEPARATOR + "MAIN" + ActionGuiInterpreter.SEPARATOR + "%player%")));

                    items.put(48, ItemStackUtils.getItemStack(Material.ARROW, "§cPrevious page", null, 1));
                    actions.put(48, new ArrayList<>(Collections.singletonList("INT" + ActionGuiInterpreter.SEPARATOR +
                            "GUI_DYN" + ActionGuiInterpreter.SEPARATOR + "HISTORY" + ActionGuiInterpreter.SEPARATOR +
                            "%player%" + ActionGuiInterpreter.SEPARATOR + "%previous_page%")));

                    items.put(50, ItemStackUtils.getItemStack(Material.ARROW, "§cNext page", null, 1));
                    actions.put(50, new ArrayList<>(Collections.singletonList("INT" + ActionGuiInterpreter.SEPARATOR +
                            "GUI_DYN" + ActionGuiInterpreter.SEPARATOR + "HISTORY" + ActionGuiInterpreter.SEPARATOR +
                            "%player%" + ActionGuiInterpreter.SEPARATOR + "%next_page%")));

                    inventories.put(type, new CustomInventory("§c§lHistory of %player% #%page%", 54, items, actions, true, type));
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
        final CustomInventory ci = CustomInventories.INSTANCE.getCustomInventory(type);
        if (ci == null) {
            p.sendMessage(MessageManager.PREFIX +
                    "§cInternal Error: " + type.toString() + " is not found in the inventories list.");
            return;
        }
        new Gui(p, cible, ci).open(p);
    }

    public void startInventoryOpenOfHistoryGui(Player p, User user, int page) {
        final CustomInventory ci = CustomInventories.INSTANCE.getCustomInventory(InventoryType.HISTORY);
        if (ci == null) {
            p.sendMessage(MessageManager.PREFIX +
                    "§cInternal Error: HISTORY is not found in the inventories list.");
            return;
        }
        if (page < 1) page = 1;

        if (user.getHistory().isEmpty()) {
            p.sendMessage(MessageManager.INSTANCE.getMessage("PlayerNoHistoryAvailable", true));
            p.closeInventory();
            return;
        }

        while (true) {
            if (getContentForPage(user.getHistory(), page).isEmpty()) {
                page--;
            } else break;
        }

        new HistoryGui(ci, p, user, page).open(p);
    }

    public LinkedList<Sanction> getContentForPage(LinkedList<Sanction> history, int page) {
        LinkedList<Sanction> content = new LinkedList<>(history);
        content.descendingIterator();
        if (page < 1) return new LinkedList<>();
        if (page == 1) return content;
        LinkedList<Sanction> toReturn = new LinkedList<>();
        for (int i = 0; i < HistoryGui.PER_PAGE * (page - 1); i++) {
            if (content.isEmpty()) break;
            content.removeLast();
        }
        for (int i = 0; i < HistoryGui.PER_PAGE; i++) {
            if (content.isEmpty()) break;
            toReturn.add(content.getFirst());
            content.removeFirst();
        }
        return toReturn;
    }

    public void saveInventories() {
        for (CustomInventory inv : inventories.values()) {
            Map<String, Object> map = new HashMap<>();

            map.put("name", inv.getName());
            map.put("size", inv.getSize());
            map.put("isFill", inv.isFill());

            Map<String, Object> items = new HashMap<>();
            for (Map.Entry<Integer, ItemStack> entry : inv.getItems().entrySet()) {
                Map<String, Object> itemMap = new HashMap<>();

                itemMap.put("type", entry.getValue().getType().name());
                if (entry.getValue().getType() == Material.SKULL_ITEM) {
                    SkullMeta meta = (SkullMeta) entry.getValue().getItemMeta();
                    itemMap.put("owner", meta.getOwner());
                }
                itemMap.put("amount", entry.getValue().getAmount());
                itemMap.put("display", entry.getValue().getItemMeta().getDisplayName());
                if (entry.getValue().getItemMeta().getLore() != null) {
                    Map<String, Object> lore = new HashMap<>();
                    for (int i = 0; i < entry.getValue().getItemMeta().getLore().size(); i++) {
                        lore.put(i + "", entry.getValue().getItemMeta().getLore().get(i));
                    }
                    itemMap.put("lore", lore);
                }
                if (inv.getActionPerItem().containsKey(entry.getKey())) {
                    Map<String, Object> actions = new HashMap<>();
                    int i = 0;
                    for (String action : inv.getActionPerItem().get(entry.getKey())) {
                        actions.put(i + "", action);
                        i++;
                    }
                    itemMap.put("actions", actions);
                }
                items.put(entry.getKey().toString(), itemMap);
            }
            map.put("items", items);

            FilesManager.INSTANCE.getInventories().put(inv.getType().toString(), map);
        }
        FilesManager.INSTANCE.saveInventories();
    }

    public void loadInventories() {
        for (InventoryType type : InventoryType.values()) {
            try {
                Map<String, Object> map = (Map<String, Object>) FilesManager.INSTANCE.getInventories().get(type.toString());
                if (map == null) continue;
                if (map.isEmpty()) continue;

                String name = (String) map.get("name");
                int size = Parser.PARSE_INT(map.get("size"));

                boolean isFill = Parser.PARSE_BOOLEAN(map.get("isFill"));
                HashMap<Integer, ItemStack> items = new HashMap<>();
                HashMap<Integer, ArrayList<String>> actions = new HashMap<>();
                Map<String, Object> itemsMap = (Map<String, Object>) map.get("items");
                for (String key : itemsMap.keySet()) {
                    Map<String, Object> itemMap = (Map<String, Object>) itemsMap.get(key);
                    int slot = Parser.PARSE_INT(key);
                    Material material = Material.valueOf((String) itemMap.get("type"));
                    int amount = Parser.PARSE_INT(itemMap.get("amount"));
                    String display = (String) itemMap.get("display");
                    ArrayList<String> lore = new ArrayList<>();
                    if (itemMap.containsKey("lore")) {
                        Map<String, Object> loreMap = (Map<String, Object>) itemMap.get("lore");
                        for (Map.Entry<String, Object> entry : loreMap.entrySet()) {
                            lore.add((String) entry.getValue());
                        }
                    }
                    ItemStack item;
                    if (material != Material.SKULL_ITEM) {
                        item = ItemStackUtils.getItemStack(material, display, lore, amount);
                    } else {
                        String owner = (String) itemMap.get("owner");
                        item = ItemStackUtils.getSkull(display, owner, lore);
                    }
                    items.put(slot, item);
                    if (itemMap.containsKey("actions")) {
                        ArrayList<String> actionList = new ArrayList<>();
                        Map<String, Object> actionsMap = (Map<String, Object>) itemMap.get("actions");
                        for (Map.Entry<String, Object> entry : actionsMap.entrySet()) {
                            actionList.add((String) entry.getValue());
                        }
                        actions.put(slot, actionList);
                    }
                }
                inventories.put(type, new CustomInventory(name, size, items, actions, isFill, type));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


}
