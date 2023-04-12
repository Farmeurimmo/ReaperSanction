package fr.farmeurimmo.reapersanction.gui;

import fr.farmeurimmo.reapersanction.sanctions.SanctionApplier;
import fr.farmeurimmo.reapersanction.storage.FilesManager;
import fr.farmeurimmo.reapersanction.storage.MessageManager;
import fr.farmeurimmo.reapersanction.users.Sanction;
import fr.farmeurimmo.reapersanction.users.User;
import fr.farmeurimmo.reapersanction.utils.TimeConverter;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;

public class HistoryGui {

    public static final String EXPIRED_YES = FilesManager.instance.getFromConfigFormatted("History.isExpired");
    public static final String EXPIRED_NO = FilesManager.instance.getFromConfigFormatted("History.isNotExpired");
    public static HistoryGui instance;
    public static String GUI_NAME = "§c§lHistory of %player% #%page%";
    public static int PER_PAGE = 46;

    public HistoryGui() {
        instance = this;
    }

    public String getPlayerFromGuiName(String guiName) {
        return guiName.split(" ")[2];
    }

    public int getPageFromGuiName(String guiName) {
        return Integer.parseInt(guiName.split(" ")[3].replace("#", ""));
    }

    public void openHistoryGui(Player player, User targetUser, int page) {
        if (!player.hasPermission("mod")) {
            player.sendMessage(MessageManager.prefix + MessageManager.instance.getMessage("NoPermission"));
            return;
        }

        if (page < 1) page = 1;

        Inventory inv = Bukkit.createInventory(null, 54, GUI_NAME.replace("%player%", targetUser.getName()).replace("%page%", String.valueOf(page)));

        if (targetUser.getHistory().size() == 0) {
            player.sendMessage(MessageManager.prefix + MessageManager.instance.getMessage("PlayerNoHistoryAvailable"));
            return;
        }

        LinkedList<Sanction> historyForPage = getContentForPage(targetUser.getHistory(), page);

        for (int i = 0; i < PER_PAGE; i++) {
            if (historyForPage.size() == 0) break;
            ItemStack item = new ItemStack(Material.PAPER);
            ItemMeta meta = item.getItemMeta();
            Sanction sanction = historyForPage.getLast();
            historyForPage.removeLast();
            meta.setDisplayName(TimeConverter.replaceArgs(FilesManager.instance.getFromConfigFormatted("History.displayname"),
                            sanction.getDuration(), targetUser.getName(), sanction.getBy(), sanction.getReason(), sanction.getAt(), sanction.getUntil())
                    .replace("%sanctiontype%", sanction.getSanctionTypeStr()));
            boolean expired = SanctionApplier.instance.isSanctionStillActive(sanction, targetUser);
            String expiredStr = expired ? EXPIRED_NO : EXPIRED_YES;
            String lore = TimeConverter.replaceArgs(FilesManager.instance.getFromConfigFormatted("History.lore"),
                            sanction.getDuration(), targetUser.getName(), sanction.getBy(), sanction.getReason(), sanction.getAt(), sanction.getUntil())
                    .replace("%sanctiontype%", sanction.getSanctionTypeStr()).replace("%expired%", expiredStr);
            ArrayList<String> loreList = new ArrayList<>(Arrays.asList(lore.split("%%")));
            meta.setLore(loreList);
            item.setItemMeta(meta);
            inv.setItem(i, item);
        }

        if (getContentForPage(targetUser.getHistory(), page - 1).size() != 0) {
            ItemStack back = new ItemStack(Material.ARROW);
            ItemMeta backMeta = back.getItemMeta();
            backMeta.setDisplayName("§cPrevious page");
            back.setItemMeta(backMeta);
            inv.setItem(48, back);
        }

        if (getContentForPage(targetUser.getHistory(), page + 1).size() != 0) {
            ItemStack next = new ItemStack(Material.ARROW);
            ItemMeta nextMeta = next.getItemMeta();
            nextMeta.setDisplayName("§aNext page");
            next.setItemMeta(nextMeta);
            inv.setItem(50, next);
        }

        GuiManager.instance.applyDoorsFromInvSize(inv);
        GuiManager.instance.applyGlass(inv);

        player.openInventory(inv);
    }

    public LinkedList<Sanction> getContentForPage(LinkedList<Sanction> history, int page) {
        LinkedList<Sanction> content = new LinkedList<>(history);
        content.descendingIterator();
        if (page < 1) return new LinkedList<>();
        if (page == 1) return content;
        LinkedList<Sanction> toReturn = new LinkedList<>();
        for (int i = 0; i < PER_PAGE * (page - 1); i++) {
            if (content.size() == 0) break;
            content.removeLast();
        }
        for (int i = 0; i < PER_PAGE; i++) {
            if (content.size() == 0) break;
            toReturn.add(content.getFirst());
            content.removeFirst();
        }
        return toReturn;
    }
}
