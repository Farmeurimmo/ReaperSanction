package fr.farmeurimmo.reapersanction.gui;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class KickGui {

    public static KickGui instance;

    public KickGui() {
        instance = this;
    }

    public void openKickGui(Player player, String target) {
        Inventory inv = Bukkit.createInventory(null, 27, "§4ReaperSanction Kick");

        GuiManager.instance.applyDoorsFromInvSize(inv);
        GuiManager.instance.applyHead(inv, target, player);
        GuiManager.instance.applyGlass(inv);

        ItemStack bedrock = new ItemStack(Material.BEDROCK);
        ItemMeta bedrockMeta = bedrock.getItemMeta();
        bedrockMeta.setDisplayName("§cWill be available in the custom gui update");
        bedrock.setItemMeta(bedrockMeta);
        inv.setItem(13, bedrock);

        player.openInventory(inv);
    }

}
