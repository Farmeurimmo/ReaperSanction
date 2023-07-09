package main.java.fr.farmeurimmo.reapersanction.gui;

import fr.mrmicky.fastinv.FastInv;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Map;

public class RsGui extends FastInv {

    public RsGui(Player player, String cible, CustomInventory ci) {
        super(ci.getSize(), ci.getName());

        ci.applyCible(cible);

        for (Map.Entry<Integer, ItemStack> entry : ci.getItems().entrySet()) {
            setItem(entry.getKey(), entry.getValue(), e -> {
                if (ci.getActionPerItem().containsKey(entry.getKey())) {
                    for (String action : ci.getActionPerItem().get(entry.getKey())) {
                        //TODO: Parse action and execute it
                    }
                }
            });
        }
    }
}
