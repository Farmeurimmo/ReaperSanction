package main.java.fr.farmeurimmo.reapersanction.gui;

import main.java.fr.farmeurimmo.reapersanction.storage.FilesManager;
import main.java.fr.farmeurimmo.reapersanction.storage.MessageManager;
import main.java.fr.farmeurimmo.reapersanction.users.User;
import main.java.fr.farmeurimmo.reapersanction.users.UsersManager;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

public class GuiListener implements Listener {

    @EventHandler
    public void onInventoryClick(InventoryClickEvent e) {
        Player player = (Player) e.getWhoClicked();
        ItemStack current = e.getCurrentItem();

        if (current == null) return;

        Material currenttype = current.getType();

        if (current.getItemMeta() == null) return;
        if (e.getSlot() <= -1) return;

        String title = e.getView().getTitle();

        if (title.length() < 4) return;

        if (title.contains("§c§lHistory of ")) {
            e.setCancelled(true);
            if (currenttype == Material.IRON_DOOR) {
                RsGui.instance.ssMainGui(player, HistoryGui.instance.getPlayerFromGuiName(title));
                return;
            }
            if (currenttype == Material.ARROW) {
                if (current.getItemMeta().getDisplayName().contains("Next")) {
                    HistoryGui.instance.openHistoryGui(player, UsersManager.instance.getUser(HistoryGui.instance.getPlayerFromGuiName(title)),
                            HistoryGui.instance.getPageFromGuiName(title) + 1);
                } else if (current.getItemMeta().getDisplayName().contains("Previous")) {
                    HistoryGui.instance.openHistoryGui(player, UsersManager.instance.getUser(HistoryGui.instance.getPlayerFromGuiName(title)),
                            HistoryGui.instance.getPageFromGuiName(title) - 1);
                }
                return;
            }
        }

        if (e.getInventory().getItem(13) == null) return;

        ItemStack cibleitem = e.getInventory().getItem(13);
        if (!cibleitem.hasItemMeta()) return;
        String cible = cibleitem.getItemMeta().getDisplayName().replace("§6", "");

        User targetUser = UsersManager.instance.getUser(cible);

        switch (title) {
            case "§4ReaperSanction":
                e.setCancelled(true);

                if (!player.hasPermission("mod")) {
                    player.sendMessage(MessageManager.prefix + MessageManager.instance.getMessage("NoPermission"));
                    return;
                }

                switch (currenttype) {
                    case GRASS:
                        MuteGui.instance.mutegui(player, cible);
                        break;
                    case DIAMOND_SWORD:
                        BanGui.instance.bangui(player, cible);
                        break;
                    case ANVIL:
                        BanIpGui.instance.banipgui(player, cible);
                        break;
                    case PAPER:
                        if (!player.hasPermission("mod+")) {
                            RsGui.instance.ssMainGui(player, cible);
                            player.sendMessage(MessageManager.prefix + MessageManager.instance.getMessage("NoPermission"));
                            break;
                        }
                        EndGui.instance.endgui(player, cible);
                        break;
                    case BOOK:
                        if (!player.hasPermission("mod+")) {
                            RsGui.instance.ssMainGui(player, cible);
                            player.sendMessage(MessageManager.prefix + MessageManager.instance.getMessage("NoPermission"));
                            break;
                        }
                        if (targetUser == null) break;
                        HistoryGui.instance.openHistoryGui(player, targetUser, 0);
                        break;
                }
                break;
            case "§4ReaperSanction Mutes":
                e.setCancelled(true);

                if (!player.hasPermission("mod")) {
                    RsGui.instance.ssMainGui(player, cible);
                    player.sendMessage(MessageManager.prefix + MessageManager.instance.getMessage("NoPermission"));
                    return;
                }

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
                        RsGui.instance.ssMainGui(player, cible);
                        break;
                }
                break;
            case "§4ReaperSanction Bans":
                e.setCancelled(true);

                if (!player.hasPermission("mod")) {
                    RsGui.instance.ssMainGui(player, cible);
                    player.sendMessage(MessageManager.prefix + MessageManager.instance.getMessage("NoPermission"));
                    return;
                }

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
                        RsGui.instance.ssMainGui(player, cible);
                        break;
                }
                break;
            case "§4ReaperSanction Bans-ip":
                e.setCancelled(true);

                if (!player.hasPermission("mod+")) {
                    RsGui.instance.ssMainGui(player, cible);
                    player.sendMessage(MessageManager.prefix + MessageManager.instance.getMessage("NoPermission"));
                    break;
                }

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
                        RsGui.instance.ssMainGui(player, cible);
                        break;
                }
                break;
            case "§4ReaperSanction Unbans/Unmutes":
                e.setCancelled(true);

                if (!player.hasPermission("mod+")) break;

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
                        RsGui.instance.ssMainGui(player, cible);
                        break;
                }
                break;
            default:
                if (!title.equalsIgnoreCase("§4Report " + cible)) return;
                e.setCancelled(true);
                String ReportReason = current.getItemMeta().getDisplayName();

                switch (currenttype) {
                    case GRASS:
                    case DIAMOND_SWORD:
                    case APPLE:
                    case BEACON: {
                        sendMessageReported(player, cible, ReportReason);
                        break;
                    }
                }
        }
    }

    public void sendMessageReported(Player player, String cible, String ReportReason) {
        player.sendMessage(MessageManager.prefix + MessageManager.instance.getMessage("Report-Sended").replace("%player%", cible).replace("%reason%", ReportReason));
        player.closeInventory();
        for (Player all : Bukkit.getServer().getOnlinePlayers()) {
            if (all.hasPermission("reportview"))
                all.sendMessage(MessageManager.prefix + MessageManager.instance.getMessage("Report-Obtain").replace("%player%", cible).replace("%reason%", ReportReason).replace("%sender%", player.getName()));
        }
    }

}

