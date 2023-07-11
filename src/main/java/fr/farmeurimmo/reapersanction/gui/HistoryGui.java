package fr.farmeurimmo.reapersanction.gui;

import fr.farmeurimmo.reapersanction.sanctions.SanctionApplier;
import fr.farmeurimmo.reapersanction.storage.FilesManager;
import fr.farmeurimmo.reapersanction.storage.MessageManager;
import fr.farmeurimmo.reapersanction.users.Sanction;
import fr.farmeurimmo.reapersanction.users.User;
import fr.farmeurimmo.reapersanction.utils.TimeConverter;
import fr.mrmicky.fastinv.FastInv;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Map;

public class HistoryGui extends FastInv {

    public static final String EXPIRED_YES = FilesManager.INSTANCE.getFromConfigFormatted("History.isExpired");
    public static final String EXPIRED_NO = FilesManager.INSTANCE.getFromConfigFormatted("History.isNotExpired");
    public static int PER_PAGE = 45;

    public HistoryGui(CustomInventory ci, Player p, User userTarget, int page) {
        super(ci.getSize(), ci.getName().replace("%player%", userTarget.getName()).replace("%page%", String.valueOf(page)));

        if (!p.hasPermission("mod")) {
            p.sendMessage(MessageManager.prefix + MessageManager.INSTANCE.getMessage("NoPermission"));
            p.closeInventory();
            return;
        }

        if (page < 1) page = 1;

        p.sendMessage("page = " + page);

        if (userTarget.getHistory().size() == 0) {
            p.sendMessage(MessageManager.prefix + MessageManager.INSTANCE.getMessage("PlayerNoHistoryAvailable"));
            p.closeInventory();
            return;
        }

        ci.applyCible(userTarget.getName());
        ci.applyPage(page);

        LinkedList<Sanction> historyForPage = getContentForPage(userTarget.getHistory(), page);

        for (int i = 0; i < PER_PAGE; i++) {
            if (historyForPage.size() == 0) break;
            ItemStack item = new ItemStack(Material.PAPER);
            ItemMeta meta = item.getItemMeta();
            Sanction sanction = historyForPage.getLast();
            historyForPage.removeLast();
            meta.setDisplayName(TimeConverter.replaceArgs(FilesManager.INSTANCE.getFromConfigFormatted("History.displayname"),
                            sanction.getDuration(), userTarget.getName(), sanction.getBy(), sanction.getReason(), sanction.getAt(), sanction.getUntil())
                    .replace("%sanctiontype%", sanction.getSanctionTypeStr()));
            boolean expired = SanctionApplier.INSTANCE.isSanctionStillActive(sanction, userTarget);
            String expiredStr = expired ? EXPIRED_NO : EXPIRED_YES;
            String lore = TimeConverter.replaceArgs(FilesManager.INSTANCE.getFromConfigFormatted("History.lore"),
                            sanction.getDuration(), userTarget.getName(), sanction.getBy(), sanction.getReason(), sanction.getAt(), sanction.getUntil())
                    .replace("%sanctiontype%", sanction.getSanctionTypeStr()).replace("%expired%", expiredStr);
            ArrayList<String> loreList = new ArrayList<>(Arrays.asList(lore.split("%%")));
            meta.setLore(loreList);
            item.setItemMeta(meta);
            setItem(i, item);
        }

        for (Map.Entry<Integer, ItemStack> entry : ci.getItems().entrySet()) {
            setItem(entry.getKey(), entry.getValue(), e -> {
                if (ci.getActionPerItem().containsKey(entry.getKey())) {
                    for (String action : ci.getActionPerItem().get(entry.getKey())) {
                        ActionGuiInterpreter.INSTANCE.interpretAction(action, p);
                    }
                }
            });
        }
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
