package main.java.fr.farmeurimmo.reapersanction.gui;

import main.java.fr.farmeurimmo.reapersanction.ReaperSanction;
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
                            player.sendMessage(ReaperSanction.instance.Preffix + ReaperSanction.instance.getConfig().getString("ReaperSanction.Settings.NoPermission").replace("&", "§"));
                        }
                        break;
                    case DIAMOND_SWORD:
                        if (player.hasPermission("mod")) {
                            BanGui.bangui(player, cible);
                        } else {
                            RsGui.SsMainGui(player, cible);
                            player.sendMessage(ReaperSanction.instance.Preffix + ReaperSanction.instance.getConfig().getString("ReaperSanction.Settings.NoPermission").replace("&", "§"));
                        }
                        break;
                    case ANVIL:
                        if (player.hasPermission("mod+")) {
                            BanIpGui.banipgui(player, cible);
                        } else {
                            RsGui.SsMainGui(player, cible);
                            player.sendMessage(ReaperSanction.instance.Preffix + ReaperSanction.instance.getConfig().getString("ReaperSanction.Settings.NoPermission").replace("&", "§"));
                        }
                        break;
                    case PAPER:
                        if (player.hasPermission("mod+")) {
                            EndGui.endgui(player, cible);
                        } else {
                            RsGui.SsMainGui(player, cible);
                            player.sendMessage(ReaperSanction.instance.Preffix + ReaperSanction.instance.getConfig().getString("ReaperSanction.Settings.NoPermission").replace("&", "§"));
                        }
                        break;
                    case BARRIER:
                        event.setCancelled(true);
                        player.sendMessage(ReaperSanction.instance.Preffix + ReaperSanction.instance.getConfig().getString("ReaperSanction.Settings.NoPermission").replace("&", "§"));
                        break;
                }
                break;
            case "§4ReaperSanction Mutes":
                event.setCancelled(true);

                if (player.hasPermission("mod")) {
                    switch (currenttype) {
                        case BOW:
                            player.chat("/tempmute " + cible + " " + ReaperSanction.instance.getConfig().getString("ReaperSanction.Settings.Menu.Mutes.Bow.Duration") + " " + ReaperSanction.instance.getConfig().getString("ReaperSanction.Settings.Menu.Mutes.Bow.Reason"));
                            player.sendMessage(ReaperSanction.instance.Preffix + ReaperSanction.instance.getConfig().getString("ReaperSanction.Settings.SanctionWaitForApplication").replace("&", "§"));
                            player.closeInventory();
                            break;
                        case DIAMOND_SWORD:
                            player.chat("/tempmute " + cible + " " + ReaperSanction.instance.getConfig().getString("ReaperSanction.Settings.Menu.Mutes.DiamondSword.Duration") + " " + ReaperSanction.instance.getConfig().getString("ReaperSanction.Settings.Menu.Mutes.DiamondSword.Reason"));
                            player.sendMessage(ReaperSanction.instance.Preffix + ReaperSanction.instance.getConfig().getString("ReaperSanction.Settings.SanctionWaitForApplication").replace("&", "§"));
                            player.closeInventory();
                            break;
                        case ANVIL:
                            player.chat("/tempmute " + cible + " " + ReaperSanction.instance.getConfig().getString("ReaperSanction.Settings.Menu.Mutes.Anvil.Duration") + " " + ReaperSanction.instance.getConfig().getString("ReaperSanction.Settings.Menu.Mutes.Anvil.Reason"));
                            player.sendMessage(ReaperSanction.instance.Preffix + ReaperSanction.instance.getConfig().getString("ReaperSanction.Settings.SanctionWaitForApplication").replace("&", "§"));
                            player.closeInventory();
                            break;
                        case REDSTONE_BLOCK:
                            player.chat("/tempmute " + cible + " " + ReaperSanction.instance.getConfig().getString("ReaperSanction.Settings.Menu.Mutes.RedstoneBlock.Duration") + " " + ReaperSanction.instance.getConfig().getString("ReaperSanction.Settings.Menu.Mutes.RedstoneBlock.Reason"));
                            player.sendMessage(ReaperSanction.instance.Preffix + ReaperSanction.instance.getConfig().getString("ReaperSanction.Settings.SanctionWaitForApplication").replace("&", "§"));
                            player.closeInventory();
                            break;
                        case ACTIVATOR_RAIL:
                            player.closeInventory();
                            player.chat("/tempmute " + cible + " " + ReaperSanction.instance.getConfig().getString("ReaperSanction.Settings.Menu.Mutes.ActivatorRail.Duration") + " " + ReaperSanction.instance.getConfig().getString("ReaperSanction.Settings.Menu.Mutes.ActivatorRail.Reason"));
                            player.sendMessage(ReaperSanction.instance.Preffix + ReaperSanction.instance.getConfig().getString("ReaperSanction.Settings.SanctionWaitForApplication").replace("&", "§"));
                            break;
                        case ARMOR_STAND:
                            player.closeInventory();
                            player.chat("/tempmute " + cible + " " + ReaperSanction.instance.getConfig().getString("ReaperSanction.Settings.Menu.Mutes.ArmorStand.Duration") + " " + ReaperSanction.instance.getConfig().getString("ReaperSanction.Settings.Menu.Mutes.ArmorStand.Reason"));
                            player.sendMessage(ReaperSanction.instance.Preffix + ReaperSanction.instance.getConfig().getString("ReaperSanction.Settings.SanctionWaitForApplication").replace("&", "§"));
                            break;
                        case COMPASS:
                            player.closeInventory();
                            player.chat("/tempmute " + cible + " " + ReaperSanction.instance.getConfig().getString("ReaperSanction.Settings.Menu.Mutes.Compass.Duration") + " " + ReaperSanction.instance.getConfig().getString("ReaperSanction.Settings.Menu.Mutes.Compass.Reason"));
                            player.sendMessage(ReaperSanction.instance.Preffix + ReaperSanction.instance.getConfig().getString("ReaperSanction.Settings.SanctionWaitForApplication").replace("&", "§"));
                            break;
                        case FLINT_AND_STEEL:
                            player.closeInventory();
                            player.chat("/tempmute " + cible + " " + ReaperSanction.instance.getConfig().getString("ReaperSanction.Settings.Menu.Mutes.FlintAndSteel.Duration") + " " + ReaperSanction.instance.getConfig().getString("ReaperSanction.Settings.Menu.Mutes.FlintAndSteel.Reason"));
                            player.sendMessage(ReaperSanction.instance.Preffix + ReaperSanction.instance.getConfig().getString("ReaperSanction.Settings.SanctionWaitForApplication").replace("&", "§"));
                            break;
                        default:
                            RsGui.SsMainGui(player, cible);
                            break;
                    }
                } else {
                    RsGui.SsMainGui(player, cible);
                    player.sendMessage(ReaperSanction.instance.Preffix + ReaperSanction.instance.getConfig().getString("ReaperSanction.Settings.NoPermission").replace("&", "§"));
                }
                break;
            case "§4ReaperSanction Bans":
                event.setCancelled(true);

                if (player.hasPermission("mod")) {

                    switch (currenttype) {
                        case DIAMOND_SWORD:
                            player.closeInventory();
                            player.chat("/tempban " + cible + " " + ReaperSanction.instance.getConfig().getString("ReaperSanction.Settings.Menu.Bans.DiamondSword.Duration") + " " + ReaperSanction.instance.getConfig().getString("ReaperSanction.Settings.Menu.Bans.DiamondSword.Reason"));
                            player.sendMessage(ReaperSanction.instance.Preffix + ReaperSanction.instance.getConfig().getString("ReaperSanction.Settings.SanctionWaitForApplication").replace("&", "§"));
                            break;
                        case WOOD_SWORD:
                            player.closeInventory();
                            player.chat("/tempban " + cible + " " + ReaperSanction.instance.getConfig().getString("ReaperSanction.Settings.Menu.Bans.WoodSword.Duration") + " " + ReaperSanction.instance.getConfig().getString("ReaperSanction.Settings.Menu.Bans.WoodSword.Reason"));
                            player.sendMessage(ReaperSanction.instance.Preffix + ReaperSanction.instance.getConfig().getString("ReaperSanction.Settings.SanctionWaitForApplication").replace("&", "§"));
                            break;
                        case FEATHER:
                            player.closeInventory();
                            player.chat("/tempban " + cible + " " + ReaperSanction.instance.getConfig().getString("ReaperSanction.Settings.Menu.Bans.Feather.Duration") + " " + ReaperSanction.instance.getConfig().getString("ReaperSanction.Settings.Menu.Bans.Feather.Reason"));
                            player.sendMessage(ReaperSanction.instance.Preffix + ReaperSanction.instance.getConfig().getString("ReaperSanction.Settings.SanctionWaitForApplication").replace("&", "§"));
                            break;
                        case IRON_BOOTS:
                            player.closeInventory();
                            player.chat("/tempban " + cible + " " + ReaperSanction.instance.getConfig().getString("ReaperSanction.Settings.Menu.Bans.IronBoots.Duration") + " " + ReaperSanction.instance.getConfig().getString("ReaperSanction.Settings.Menu.Bans.IronBoots.Reason"));
                            player.sendMessage(ReaperSanction.instance.Preffix + ReaperSanction.instance.getConfig().getString("ReaperSanction.Settings.SanctionWaitForApplication").replace("&", "§"));
                            break;
                        case GOLD_AXE:
                            player.closeInventory();
                            player.chat("/tempban " + cible + " " + ReaperSanction.instance.getConfig().getString("ReaperSanction.Settings.Menu.Bans.GoldAxe.Duration") + " " + ReaperSanction.instance.getConfig().getString("ReaperSanction.Settings.Menu.Bans.GoldAxe.Reason"));
                            player.sendMessage(ReaperSanction.instance.Preffix + ReaperSanction.instance.getConfig().getString("ReaperSanction.Settings.SanctionWaitForApplication").replace("&", "§"));
                            break;
                        case ARMOR_STAND:
                            player.closeInventory();
                            player.chat("/tempban " + cible + " " + ReaperSanction.instance.getConfig().getString("ReaperSanction.Settings.Menu.Bans.ArmorStand.Duration") + " " + ReaperSanction.instance.getConfig().getString("ReaperSanction.Settings.Menu.Bans.ArmorStand.Reason"));
                            player.sendMessage(ReaperSanction.instance.Preffix + ReaperSanction.instance.getConfig().getString("ReaperSanction.Settings.SanctionWaitForApplication").replace("&", "§"));
                            break;
                        case TNT:
                            player.closeInventory();
                            player.chat("/tempban " + cible + " " + ReaperSanction.instance.getConfig().getString("ReaperSanction.Settings.Menu.Bans.Tnt.Duration") + " " + ReaperSanction.instance.getConfig().getString("ReaperSanction.Settings.Menu.Bans.Tnt.Reason"));
                            player.sendMessage(ReaperSanction.instance.Preffix + ReaperSanction.instance.getConfig().getString("ReaperSanction.Settings.SanctionWaitForApplication").replace("&", "§"));
                            break;
                        case LEATHER:
                            player.closeInventory();
                            player.chat("/tempban " + cible + " " + ReaperSanction.instance.getConfig().getString("ReaperSanction.Settings.Menu.Bans.Leather.Duration") + " " + ReaperSanction.instance.getConfig().getString("ReaperSanction.Settings.Menu.Bans.Leather.Reason"));
                            player.sendMessage(ReaperSanction.instance.Preffix + ReaperSanction.instance.getConfig().getString("ReaperSanction.Settings.SanctionWaitForApplication").replace("&", "§"));
                            break;
                        case DIAMOND_CHESTPLATE:
                            player.closeInventory();
                            player.chat("/tempban " + cible + " " + ReaperSanction.instance.getConfig().getString("ReaperSanction.Settings.Menu.Bans.DiamondChestPlate.Duration") + " " + ReaperSanction.instance.getConfig().getString("ReaperSanction.Settings.Menu.Bans.DiamondChestPlate.Reason"));
                            player.sendMessage(ReaperSanction.instance.Preffix + ReaperSanction.instance.getConfig().getString("ReaperSanction.Settings.SanctionWaitForApplication").replace("&", "§"));
                            break;
                        case DIRT:
                            player.closeInventory();
                            player.chat("/tempban " + cible + " " + ReaperSanction.instance.getConfig().getString("ReaperSanction.Settings.Menu.Bans.Dirt.Duration") + " " + ReaperSanction.instance.getConfig().getString("ReaperSanction.Settings.Menu.Bans.Dirt.Reason"));
                            player.sendMessage(ReaperSanction.instance.Preffix + ReaperSanction.instance.getConfig().getString("ReaperSanction.Settings.SanctionWaitForApplication").replace("&", "§"));
                            break;
                        default:
                            RsGui.SsMainGui(player, cible);
                            break;
                    }
                } else {
                    RsGui.SsMainGui(player, cible);
                    player.sendMessage(ReaperSanction.instance.Preffix + ReaperSanction.instance.getConfig().getString("ReaperSanction.Settings.NoPermission").replace("&", "§"));
                }
                break;
            case "§4ReaperSanction Bans-ip":
                event.setCancelled(true);

                if (player.hasPermission("mod+")) {

                    switch (currenttype) {
                        case NAME_TAG:
                            player.closeInventory();
                            player.chat("/ban-ip " + cible + " " + ReaperSanction.instance.getConfig().getString("ReaperSanction.Settings.Menu.Banip.NameTag.Reason"));
                            player.sendMessage(ReaperSanction.instance.Preffix + ReaperSanction.instance.getConfig().getString("ReaperSanction.Settings.SanctionWaitForApplication").replace("&", "§"));
                            break;
                        case CLAY_BALL:
                            player.closeInventory();
                            player.chat("/ban-ip " + cible + " " + ReaperSanction.instance.getConfig().getString("ReaperSanction.Settings.Menu.Banip.ClayBall.Reason"));
                            player.sendMessage(ReaperSanction.instance.Preffix + ReaperSanction.instance.getConfig().getString("ReaperSanction.Settings.SanctionWaitForApplication").replace("&", "§"));
                            break;
                        default:
                            RsGui.SsMainGui(player, cible);
                            break;
                    }
                } else {
                    RsGui.SsMainGui(player, cible);
                    player.sendMessage(ReaperSanction.instance.Preffix + ReaperSanction.instance.getConfig().getString("ReaperSanction.Settings.NoPermission").replace("&", "§"));
                }
                break;
            case "§4ReaperSanction Unbans/Unmutes":
                event.setCancelled(true);

                if (player.hasPermission("mod+")) {

                    switch (currenttype) {
                        case BOW:
                            player.closeInventory();
                            player.chat("/unmute " + cible);
                            player.sendMessage(ReaperSanction.instance.Preffix + ReaperSanction.instance.getConfig().getString("ReaperSanction.Settings.SanctionWaitEnd").replace("&", "§"));
                            break;
                        case DIAMOND_SWORD:
                            player.closeInventory();
                            player.chat("/unban " + cible);
                            player.chat("/unbanip " + cible);
                            player.sendMessage(ReaperSanction.instance.Preffix + ReaperSanction.instance.getConfig().getString("ReaperSanction.Settings.SanctionWaitEnd").replace("&", "§"));
                            break;
                        case ANVIL:
                            player.closeInventory();
                            player.chat("/unmute " + cible);
                            player.chat("/unban " + cible);
                            player.chat("/unbanip " + cible);
                            player.sendMessage(ReaperSanction.instance.Preffix + ReaperSanction.instance.getConfig().getString("ReaperSanction.Settings.SanctionWaitEnd").replace("&", "§"));
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
        player.sendMessage(ReaperSanction.instance.Preffix + ReaperSanction.instance.getConfig().getString("ReaperSanction.Settings.Report.sended").replace("&", "§").replace("%player%", cible).replace("%reason%", ReportReason));
        player.closeInventory();
        for (Player all : Bukkit.getServer().getOnlinePlayers()) {
            bc.append(part).append(" ");
            if (all.hasPermission("reportview")) {
                all.sendMessage(ReaperSanction.instance.getConfig().getString("ReaperSanction.Settings.Report.Obtain").replace("&", "§").replace("%player%", cible).replace("%reason%", ReportReason).replace("%sender%", player.getName()));
                bc.delete(0, 100);
            }
        }
    }

}

