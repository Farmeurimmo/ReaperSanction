package main.java.fr.farmeurimmo.reapersanction.gui;

import main.java.fr.farmeurimmo.reapersanction.storage.FilesManager;
import main.java.fr.farmeurimmo.reapersanction.storage.MessageManager;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

public class GuiManager implements Listener {

    StringBuilder bc = new StringBuilder();
    String part;

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        ItemStack current = event.getCurrentItem();

        if (current == null) {
            return;
        }

        Material currenttype = current.getType();

        if (current.getItemMeta() == null) {
            return;
        }

        int slot = event.getSlot();
        if (slot <= -1) {
            return;
        }

        String title = event.getView().getTitle();

        if (title.length() < 4) {
            return;
        }

        if (event.getInventory().getItem(13) == null) {
            return;
        }

        ItemStack cibleitem = event.getInventory().getItem(13);
        if (!cibleitem.hasItemMeta()) {
            return;
        }
        String cible = cibleitem.getItemMeta().getDisplayName().replace("§6", "");

        switch (title) {
            case "§4ReaperSanction":
                event.setCancelled(true);

                switch (currenttype) {
                    case GRASS:
                        if (player.hasPermission("mod")) {
                            MuteGui.mutegui(player, cible);
                        } else {
                            RsGui.SsMainGui(player, cible);
                            player.sendMessage(MessageManager.prefix + MessageManager.instance.getMessage("NoPermission"));
                        }
                        break;
                    case DIAMOND_SWORD:
                        if (player.hasPermission("mod")) {
                            BanGui.bangui(player, cible);
                        } else {
                            RsGui.SsMainGui(player, cible);
                            player.sendMessage(MessageManager.prefix + MessageManager.instance.getMessage("NoPermission"));
                        }
                        break;
                    case ANVIL:
                        if (player.hasPermission("mod+")) {
                            BanIpGui.banipgui(player, cible);
                        } else {
                            RsGui.SsMainGui(player, cible);
                            player.sendMessage(MessageManager.prefix + MessageManager.instance.getMessage("NoPermission"));
                        }
                        break;
                    case PAPER:
                        if (player.hasPermission("mod+")) {
                            EndGui.endgui(player, cible);
                        } else {
                            RsGui.SsMainGui(player, cible);
                            player.sendMessage(MessageManager.prefix + MessageManager.instance.getMessage("NoPermission"));
                        }
                        break;
                    case BARRIER:
                        event.setCancelled(true);
                        player.sendMessage(MessageManager.prefix + MessageManager.instance.getMessage("NoPermission"));
                        break;
                }
                break;
            case "§4ReaperSanction Mutes":
                event.setCancelled(true);

                if (player.hasPermission("mod")) {
                    switch (currenttype) {
                        case BOW:
                            player.chat("/tempmute " + cible + " " + FilesManager.instance.getFromConfigFormatted("Menu.Mutes.Bow.Duration") + " " + FilesManager.instance.getFromConfigFormatted("Menu.Mutes.Bow.Reason"));
                            player.sendMessage(MessageManager.prefix + MessageManager.instance.getMessage("SanctionWaitForApplication"));
                            player.closeInventory();
                            break;
                        case DIAMOND_SWORD:
                            player.chat("/tempmute " + cible + " " + FilesManager.instance.getFromConfigFormatted("Menu.Mutes.DiamondSword.Duration") + " " + FilesManager.instance.getFromConfigFormatted("Menu.Mutes.DiamondSword.Reason"));
                            player.sendMessage(MessageManager.prefix + MessageManager.instance.getMessage("SanctionWaitForApplication"));
                            player.closeInventory();
                            break;
                        case ANVIL:
                            player.chat("/tempmute " + cible + " " + FilesManager.instance.getFromConfigFormatted("Menu.Mutes.Anvil.Duration") + " " + FilesManager.instance.getFromConfigFormatted("Menu.Mutes.Anvil.Reason"));
                            player.sendMessage(MessageManager.prefix + MessageManager.instance.getMessage("SanctionWaitForApplication"));
                            player.closeInventory();
                            break;
                        case REDSTONE_BLOCK:
                            player.chat("/tempmute " + cible + " " + FilesManager.instance.getFromConfigFormatted("Menu.Mutes.RedstoneBlock.Duration") + " " + FilesManager.instance.getFromConfigFormatted("Menu.Mutes.RedstoneBlock.Reason"));
                            player.sendMessage(MessageManager.prefix + MessageManager.instance.getMessage("SanctionWaitForApplication"));
                            player.closeInventory();
                            break;
                        case ACTIVATOR_RAIL:
                            player.closeInventory();
                            player.chat("/tempmute " + cible + " " + FilesManager.instance.getFromConfigFormatted("Menu.Mutes.ActivatorRail.Duration") + " " + FilesManager.instance.getFromConfigFormatted("Menu.Mutes.ActivatorRail.Reason"));
                            player.sendMessage(MessageManager.prefix + MessageManager.instance.getMessage("SanctionWaitForApplication"));
                            break;
                        case ARMOR_STAND:
                            player.closeInventory();
                            player.chat("/tempmute " + cible + " " + FilesManager.instance.getFromConfigFormatted("Menu.Mutes.ArmorStand.Duration") + " " + FilesManager.instance.getFromConfigFormatted("Menu.Mutes.ArmorStand.Reason"));
                            player.sendMessage(MessageManager.prefix + MessageManager.instance.getMessage("SanctionWaitForApplication"));
                            break;
                        case COMPASS:
                            player.closeInventory();
                            player.chat("/tempmute " + cible + " " + FilesManager.instance.getFromConfigFormatted("Menu.Mutes.Compass.Duration") + " " + FilesManager.instance.getFromConfigFormatted("Menu.Mutes.Compass.Reason"));
                            player.sendMessage(MessageManager.prefix + MessageManager.instance.getMessage("SanctionWaitForApplication"));
                            break;
                        case FLINT_AND_STEEL:
                            player.closeInventory();
                            player.chat("/tempmute " + cible + " " + FilesManager.instance.getFromConfigFormatted("Menu.Mutes.FlintAndSteel.Duration") + " " + FilesManager.instance.getFromConfigFormatted("Menu.Mutes.FlintAndSteel.Reason"));
                            player.sendMessage(MessageManager.prefix + MessageManager.instance.getMessage("SanctionWaitForApplication"));
                            break;
                        default:
                            RsGui.SsMainGui(player, cible);
                            break;
                    }
                } else {
                    RsGui.SsMainGui(player, cible);
                    player.sendMessage(MessageManager.prefix + MessageManager.instance.getMessage("NoPermission"));
                }
                break;
            case "§4ReaperSanction Bans":
                event.setCancelled(true);

                if (player.hasPermission("mod")) {

                    switch (currenttype) {
                        case DIAMOND_SWORD:
                            player.closeInventory();
                            player.chat("/tempban " + cible + " " + FilesManager.instance.getFromConfigFormatted("Menu.Bans.DiamondSword.Duration") + " " + FilesManager.instance.getFromConfigFormatted("Menu.Bans.DiamondSword.Reason"));
                            player.sendMessage(MessageManager.prefix + MessageManager.instance.getMessage("SanctionWaitForApplication"));
                            break;
                        case WOOD_SWORD:
                            player.closeInventory();
                            player.chat("/tempban " + cible + " " + FilesManager.instance.getFromConfigFormatted("Menu.Bans.WoodSword.Duration") + " " + FilesManager.instance.getFromConfigFormatted("Menu.Bans.WoodSword.Reason"));
                            player.sendMessage(MessageManager.prefix + MessageManager.instance.getMessage("SanctionWaitForApplication"));
                            break;
                        case FEATHER:
                            player.closeInventory();
                            player.chat("/tempban " + cible + " " + FilesManager.instance.getFromConfigFormatted("Menu.Bans.Feather.Duration") + " " + FilesManager.instance.getFromConfigFormatted("Menu.Bans.Feather.Reason"));
                            player.sendMessage(MessageManager.prefix + MessageManager.instance.getMessage("SanctionWaitForApplication"));
                            break;
                        case IRON_BOOTS:
                            player.closeInventory();
                            player.chat("/tempban " + cible + " " + FilesManager.instance.getConfig().getString("Menu.Bans.IronBoots.Duration") + " " + FilesManager.instance.getConfig().getString("Menu.Bans.IronBoots.Reason"));
                            player.sendMessage(MessageManager.prefix + MessageManager.instance.getMessage("SanctionWaitForApplication"));
                            break;
                        case GOLD_AXE:
                            player.closeInventory();
                            player.chat("/tempban " + cible + " " + FilesManager.instance.getConfig().getString("Menu.Bans.GoldAxe.Duration") + " " + FilesManager.instance.getConfig().getString("Menu.Bans.GoldAxe.Reason"));
                            player.sendMessage(MessageManager.prefix + MessageManager.instance.getMessage("SanctionWaitForApplication"));
                            break;
                        case ARMOR_STAND:
                            player.closeInventory();
                            player.chat("/tempban " + cible + " " + FilesManager.instance.getConfig().getString("Menu.Bans.ArmorStand.Duration") + " " + FilesManager.instance.getConfig().getString("Menu.Bans.ArmorStand.Reason"));
                            player.sendMessage(MessageManager.prefix + MessageManager.instance.getMessage("SanctionWaitForApplication"));
                            break;
                        case TNT:
                            player.closeInventory();
                            player.chat("/tempban " + cible + " " + FilesManager.instance.getConfig().getString("Menu.Bans.Tnt.Duration") + " " + FilesManager.instance.getConfig().getString("Menu.Bans.Tnt.Reason"));
                            player.sendMessage(MessageManager.prefix + MessageManager.instance.getMessage("SanctionWaitForApplication"));
                            break;
                        case LEATHER:
                            player.closeInventory();
                            player.chat("/tempban " + cible + " " + FilesManager.instance.getConfig().getString("Menu.Bans.Leather.Duration") + " " + FilesManager.instance.getConfig().getString("Menu.Bans.Leather.Reason"));
                            player.sendMessage(MessageManager.prefix + MessageManager.instance.getMessage("SanctionWaitForApplication"));
                            break;
                        case DIAMOND_CHESTPLATE:
                            player.closeInventory();
                            player.chat("/tempban " + cible + " " + FilesManager.instance.getConfig().getString("Menu.Bans.DiamondChestPlate.Duration") + " " + FilesManager.instance.getConfig().getString("Menu.Bans.DiamondChestPlate.Reason"));
                            player.sendMessage(MessageManager.prefix + MessageManager.instance.getMessage("SanctionWaitForApplication"));
                            break;
                        case DIRT:
                            player.closeInventory();
                            player.chat("/tempban " + cible + " " + FilesManager.instance.getConfig().getString("Menu.Bans.Dirt.Duration") + " " + FilesManager.instance.getConfig().getString("Menu.Bans.Dirt.Reason"));
                            player.sendMessage(MessageManager.prefix + MessageManager.instance.getMessage("SanctionWaitForApplication"));
                            break;
                        default:
                            RsGui.SsMainGui(player, cible);
                            break;
                    }
                } else {
                    RsGui.SsMainGui(player, cible);
                    player.sendMessage(MessageManager.prefix + MessageManager.instance.getMessage("NoPermission"));
                }
                break;
            case "§4ReaperSanction Bans-ip":
                event.setCancelled(true);

                if (player.hasPermission("mod+")) {

                    switch (currenttype) {
                        case NAME_TAG:
                            player.closeInventory();
                            player.chat("/ban-ip " + cible + " " + FilesManager.instance.getConfig().getString("Menu.Banip.NameTag.Reason"));
                            player.sendMessage(MessageManager.prefix + MessageManager.instance.getMessage("SanctionWaitForApplication"));
                            break;
                        case CLAY_BALL:
                            player.closeInventory();
                            player.chat("/ban-ip " + cible + " " + FilesManager.instance.getConfig().getString("Menu.Banip.ClayBall.Reason"));
                            player.sendMessage(MessageManager.prefix + MessageManager.instance.getMessage("SanctionWaitForApplication"));
                            break;
                        default:
                            RsGui.SsMainGui(player, cible);
                            break;
                    }
                } else {
                    RsGui.SsMainGui(player, cible);
                    player.sendMessage(MessageManager.prefix + MessageManager.instance.getMessage("NoPermission"));
                }
                break;
            case "§4ReaperSanction Unbans/Unmutes":
                event.setCancelled(true);

                if (player.hasPermission("mod+")) {

                    switch (currenttype) {
                        case BOW:
                            player.closeInventory();
                            player.chat("/unmute " + cible);
                            player.sendMessage(MessageManager.prefix + MessageManager.instance.getMessage("SanctionWaitEnd"));
                            break;
                        case DIAMOND_SWORD:
                            player.closeInventory();
                            player.chat("/unban " + cible);
                            player.chat("/unbanip " + cible);
                            player.sendMessage(MessageManager.prefix + MessageManager.instance.getMessage("SanctionWaitEnd"));
                            break;
                        case ANVIL:
                            player.closeInventory();
                            player.chat("/unmute " + cible);
                            player.chat("/unban " + cible);
                            player.chat("/unbanip " + cible);
                            player.sendMessage(MessageManager.prefix + MessageManager.instance.getMessage("SanctionWaitEnd"));
                            break;
                        default:
                            RsGui.SsMainGui(player, cible);
                            break;
                    }
                }
                break;
            default:
                if (!title.equalsIgnoreCase("§4Report " + cible)) {
                    return;
                }
                event.setCancelled(true);
                String ReportReason = current.getItemMeta().getDisplayName();
                part = ReportReason;

                switch (currenttype) {
                    case GRASS:
                    case DIAMOND_SWORD:
                    case APPLE:
                    case BEACON:
                        sendMessageReported(player, cible, ReportReason);
                        break;
                }
        }
    }

    public void sendMessageReported(Player player, String cible, String ReportReason) {
        player.sendMessage(MessageManager.prefix + MessageManager.instance.getMessage("Report-Sended").replace("%player%", cible).replace("%reason%", ReportReason));
        player.closeInventory();
        for (Player all : Bukkit.getServer().getOnlinePlayers()) {
            bc.append(part).append(" ");
            if (all.hasPermission("reportview")) {
                all.sendMessage(MessageManager.prefix + MessageManager.instance.getMessage("Report-Obtain").replace("%player%", cible).replace("%reason%", ReportReason).replace("%sender%", player.getName()));
                bc.delete(0, 100);
            }
        }
    }

}

