package fr.farmeurimmo.reapersanction.spigot.gui;

import fr.farmeurimmo.reapersanction.core.sanctions.SanctionsManager;
import fr.farmeurimmo.reapersanction.core.storage.MessageManager;
import fr.farmeurimmo.reapersanction.core.users.Sanction;
import fr.farmeurimmo.reapersanction.core.users.User;
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

    //FIXME
    public static final String EXPIRED_YES = /*FilesManager.INSTANCE.getFromConfigFormatted("History.isExpired");*/"broken";
    public static final String EXPIRED_NO = /*FilesManager.INSTANCE.getFromConfigFormatted("History.isNotExpired");*/"broken";
    public static int PER_PAGE = 45;

    public HistoryGui(CustomInventory ci, Player p, User userTarget, int page) {
        super(ci.getSize(), ci.getName().replace("%player%", userTarget.getName()).replace("%page%", String.valueOf(page)));

        if (!p.hasPermission("mod")) {
            p.sendMessage(MessageManager.INSTANCE.getMessage("NoPermission", true));
            p.closeInventory();
            return;
        }

        if (page < 1) page = 1;

        if (userTarget.getHistory().isEmpty()) {
            p.sendMessage(MessageManager.INSTANCE.getMessage("PlayerNoHistoryAvailable", true));
            p.closeInventory();
            return;
        }

        ci.applyCible(userTarget.getName());
        ci.applyPage(page);

        LinkedList<Sanction> historyForPage = getContentForPage(userTarget.getHistory(), page);

        for (int i = 0; i < PER_PAGE; i++) {
            if (historyForPage.isEmpty()) break;
            ItemStack item = new ItemStack(Material.PAPER);
            ItemMeta meta = item.getItemMeta();
            Sanction sanction = historyForPage.getLast();
            historyForPage.removeLast();

            //FIXME
            meta.setDisplayName(/*TimeConverter.replaceArgs(FilesManager.INSTANCE.getFromConfigFormatted("History.displayname"),
                            sanction.getDuration(), userTarget.getName(), sanction.getBy(), sanction.getReason(), sanction.getAt(), sanction.getUntil())
                    .replace("%sanctiontype%", sanction.getSanctionTypeStr())*/"broken");
            boolean expired = SanctionsManager.INSTANCE.isSanctionStillActive(sanction, userTarget);
            String expiredStr = expired ? EXPIRED_NO : EXPIRED_YES;

            //FIXME
            String lore = /*TimeConverter.replaceArgs(FilesManager.INSTANCE.getFromConfigFormatted("History.lore"),
                            sanction.getDuration(), userTarget.getName(), sanction.getBy(), sanction.getReason(), sanction.getAt(), sanction.getUntil())
                    .replace("%sanctiontype%", sanction.getSanctionTypeStr()).replace("%expired%", expiredStr);*/
                    "broken";
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
            if (content.isEmpty()) break;
            content.removeLast();
        }
        for (int i = 0; i < PER_PAGE; i++) {
            if (content.isEmpty()) break;
            toReturn.add(content.getFirst());
            content.removeFirst();
        }
        return toReturn;
    }
}
