package fr.farmeurimmo.reapersanction.spigot.gui;

import fr.farmeurimmo.reapersanction.core.sanctions.SanctionsManager;
import fr.farmeurimmo.reapersanction.core.storage.MessageManager;
import fr.farmeurimmo.reapersanction.core.users.Sanction;
import fr.farmeurimmo.reapersanction.core.users.User;
import fr.farmeurimmo.reapersanction.spigot.ReaperSanction;
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

    public static final String EXPIRED_YES = ReaperSanction.INSTANCE.getConfig().getString("History.isExpired").replace("&", "§");
    public static final String EXPIRED_NO = ReaperSanction.INSTANCE.getConfig().getString("History.isNotExpired").replace("&", "§");
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

        LinkedList<Sanction> historyForPage = CustomInventories.INSTANCE.getContentForPage(userTarget.getHistory(), page);

        for (int i = 0; i < PER_PAGE; i++) {
            if (historyForPage.isEmpty()) break;
            ItemStack item = new ItemStack(Material.PAPER);
            ItemMeta meta = item.getItemMeta();
            Sanction sanction = historyForPage.getLast();
            historyForPage.removeLast();

            meta.setDisplayName(TimeConverter.replaceArgs(ReaperSanction.INSTANCE.getConfig().getString("History.displayname"),
                            sanction.getDuration(), userTarget.getName(), sanction.getBy(), sanction.getReason(), sanction.getAt(), sanction.getUntil())
                    .replace("%sanctiontype%", sanction.getSanctionTypeStr()).replace("&", "§"));
            boolean expired = SanctionsManager.INSTANCE.isSanctionStillActive(sanction, userTarget);
            String expiredStr = expired ? EXPIRED_NO : EXPIRED_YES;

            String lore = TimeConverter.replaceArgs(ReaperSanction.INSTANCE.getConfig().getString("History.lore"),
                            sanction.getDuration(), userTarget.getName(), sanction.getBy(), sanction.getReason(), sanction.getAt(), sanction.getUntil())
                    .replace("%sanctiontype%", sanction.getSanctionTypeStr()).replace("%expired%", expiredStr).replace("&", "§");
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
}
