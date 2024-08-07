package fr.farmeurimmo.reapersanction.spigot.gui;

import fr.farmeurimmo.reapersanction.core.Main;
import fr.farmeurimmo.reapersanction.core.storage.MessageManager;
import fr.farmeurimmo.reapersanction.core.users.User;
import fr.farmeurimmo.reapersanction.core.users.UsersManager;
import fr.farmeurimmo.reapersanction.spigot.ReaperSanction;
import fr.farmeurimmo.reapersanction.spigot.cpm.CPMManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class ActionGuiInterpreter {

    public static final String SEPARATOR = "->";
    public static ActionGuiInterpreter INSTANCE;

    public ActionGuiInterpreter() {
        INSTANCE = this;
    }

    public void interpretAction(String action, Player p) {
        int sizeof = action.split(SEPARATOR).length;
        if (sizeof == 0) {
            sendActionNotWorking(action, ActionErrorCodes.NO_SECTION);
            return;
        }
        String where = action.split(SEPARATOR)[0];
        if (where.equals("INT")) {
            if (sizeof == 1) {
                sendActionNotWorking(action, ActionErrorCodes.NO_DECISION);
                return;
            }
            String what = action.split(SEPARATOR)[1];
            if (what.equals("GUI")) {
                if (sizeof == 2) {
                    sendActionNotWorking(action, ActionErrorCodes.WHAT_GUI);
                    return;
                }
                if (sizeof == 3) {
                    sendActionNotWorking(action, ActionErrorCodes.NO_PLAYER_SELECTED);
                    return;
                }
                String player = action.split(SEPARATOR)[3];
                String gui = action.split(SEPARATOR)[2];
                InventoryType it;
                try {
                    it = InventoryType.valueOf(gui);
                } catch (IllegalArgumentException e) {
                    sendActionNotWorking(action, ActionErrorCodes.NO_GUI);
                    return;
                }
                CustomInventories.INSTANCE.startInventoryOpenProcess(p, it, player);
                return;
            }
            if (what.equals("GUI_DYN")) {
                if (sizeof == 2) {
                    sendActionNotWorking(action, ActionErrorCodes.WHAT_GUI);
                    return;
                }
                if (sizeof == 3) {
                    sendActionNotWorking(action, ActionErrorCodes.NO_PLAYER_SELECTED);
                    return;
                }
                String player = action.split(SEPARATOR)[3];
                String gui = action.split(SEPARATOR)[2];
                if (gui.equals(InventoryType.HISTORY.name())) {
                    User user = UsersManager.INSTANCE.getUser(player);
                    if (user == null) {
                        sendActionNotWorking(action, ActionErrorCodes.PLAYER_NOT_FOUND);
                        return;
                    }
                    if (sizeof == 5) {
                        try {
                            int page = Integer.parseInt(action.split(SEPARATOR)[4]);
                            CustomInventories.INSTANCE.startInventoryOpenOfHistoryGui(p, user, page);
                            return;
                        } catch (Exception ignored) {
                        }
                    }
                    if (Main.INSTANCE.isProxyMode()) {
                        CPMManager.INSTANCE.sendPluginMessage(p, "openhistorygui", user.getUuid().toString());
                        return;
                    }
                    CustomInventories.INSTANCE.startInventoryOpenOfHistoryGui(p, user, 0);
                    return;
                }
                sendActionNotWorking(action, ActionErrorCodes.NO_GUI);
                return;
            }

            if (what.equals("MUTE")) {
                if (sizeof == 2) {
                    sendActionNotWorking(action, ActionErrorCodes.NO_PLAYER_SELECTED);
                    return;
                }
                String player = action.split(SEPARATOR)[2];
                String reason = "";
                if (sizeof > 3) reason = action.split(SEPARATOR)[3];

                if (!Main.INSTANCE.isProxyMode()) p.performCommand("mute " + player + " " + reason);
                else CPMManager.INSTANCE.sendPluginMessage(p, "mute", player + " " + reason);
                return;
            }

            if (what.equals("TEMPMUTE")) {
                if (sizeof == 2) {
                    sendActionNotWorking(action, ActionErrorCodes.NO_PLAYER_SELECTED);
                    return;
                }
                String player = action.split(SEPARATOR)[2];
                if (sizeof == 3) {
                    sendActionNotWorking(action, ActionErrorCodes.NO_DURATION_FOR_TEMP);
                    return;
                }
                String duration = action.split(SEPARATOR)[3];
                String reason = "";
                if (sizeof > 4) reason = action.split(SEPARATOR)[4];

                if (!Main.INSTANCE.isProxyMode())
                    p.performCommand("tempmute " + player + " " + duration + " " + reason);
                else CPMManager.INSTANCE.sendPluginMessage(p, "tempmute", player + " " + duration + " " + reason);
                return;
            }

            if (what.equals("BAN")) {
                if (sizeof == 2) {
                    sendActionNotWorking(action, ActionErrorCodes.NO_PLAYER_SELECTED);
                    return;
                }
                String player = action.split(SEPARATOR)[2];
                String reason = "";
                if (sizeof > 3) reason = action.split(SEPARATOR)[3];

                if (!Main.INSTANCE.isProxyMode()) p.performCommand("ban " + player + " " + reason);
                else CPMManager.INSTANCE.sendPluginMessage(p, "ban", player + " " + reason);
                return;
            }

            if (what.equals("TEMPBAN")) {
                if (sizeof == 2) {
                    sendActionNotWorking(action, ActionErrorCodes.NO_PLAYER_SELECTED);
                    return;
                }
                String player = action.split(SEPARATOR)[2];
                if (sizeof == 3) {
                    sendActionNotWorking(action, ActionErrorCodes.NO_DURATION_FOR_TEMP);
                    return;
                }
                String duration = action.split(SEPARATOR)[3];
                String reason = "";
                if (sizeof > 4) reason = action.split(SEPARATOR)[4];

                if (!Main.INSTANCE.isProxyMode()) p.performCommand("tempban " + player + " " + duration + " " + reason);
                else CPMManager.INSTANCE.sendPluginMessage(p, "tempban", player + " " + duration + " " + reason);
                return;
            }

            if (what.equals("KICK")) {
                if (sizeof == 2) {
                    sendActionNotWorking(action, ActionErrorCodes.NO_PLAYER_SELECTED);
                    return;
                }
                String player = action.split(SEPARATOR)[2];
                String reason = "";
                if (sizeof > 3) reason = action.split(SEPARATOR)[3];

                if (!Main.INSTANCE.isProxyMode()) p.performCommand("kick " + player + " " + reason);
                else CPMManager.INSTANCE.sendPluginMessage(p, "kick", player + " " + reason);
                return;
            }

            if (what.equals("BAN_IP")) {
                if (sizeof == 2) {
                    sendActionNotWorking(action, ActionErrorCodes.NO_PLAYER_SELECTED);
                    return;
                }
                String player = action.split(SEPARATOR)[2];
                String reason = "";
                if (sizeof > 3) reason = action.split(SEPARATOR)[3];

                if (!Main.INSTANCE.isProxyMode()) p.performCommand("ban-ip " + player + " " + reason);
                else CPMManager.INSTANCE.sendPluginMessage(p, "ban-ip", player + " " + reason);
                return;
            }

            if (what.equals("UNMUTE")) {
                if (sizeof == 2) {
                    sendActionNotWorking(action, ActionErrorCodes.NO_PLAYER_SELECTED);
                    return;
                }
                String player = action.split(SEPARATOR)[2];

                if (!Main.INSTANCE.isProxyMode()) p.performCommand("unmute " + player);
                else CPMManager.INSTANCE.sendPluginMessage(p, "unmute", player);
                return;
            }

            if (what.equals("UNBAN")) {
                if (sizeof == 2) {
                    sendActionNotWorking(action, ActionErrorCodes.NO_PLAYER_SELECTED);
                    return;
                }
                String player = action.split(SEPARATOR)[2];

                if (!Main.INSTANCE.isProxyMode()) p.performCommand("unban " + player);
                else CPMManager.INSTANCE.sendPluginMessage(p, "unban", player);
                return;
            }

            if (what.equals("UNBAN_IP")) {
                if (sizeof == 2) {
                    sendActionNotWorking(action, ActionErrorCodes.NO_PLAYER_SELECTED);
                    return;
                }
                String player = action.split(SEPARATOR)[2];

                if (!Main.INSTANCE.isProxyMode()) p.performCommand("unban-ip " + player);
                else CPMManager.INSTANCE.sendPluginMessage(p, "unban-ip", player);
                return;
            }

            if (what.equals("CLOSE")) {
                p.closeInventory();
                return;
            }

            if (what.equals("REPORT")) {
                if (sizeof == 2) {
                    sendActionNotWorking(action, ActionErrorCodes.NO_REASON);
                    return;
                }
                String reason = action.split(SEPARATOR)[2];
                if (sizeof == 3) {
                    sendActionNotWorking(action, ActionErrorCodes.NO_PLAYER_SELECTED);
                    return;
                }
                String player = action.split(SEPARATOR)[3];

                if (!Main.INSTANCE.isProxyMode()) sendMessageReported(p, player, reason);
                else CPMManager.INSTANCE.sendPluginMessage(p, "report", player + " " + reason);
                return;
            }

            sendActionNotWorking(action, ActionErrorCodes.NO_INTRUCTION);
            return;
        }
        if (where.equals("EXT")) {
            String cmd = action.split(SEPARATOR)[1];
            p.performCommand(cmd);
            return;
        }
        sendActionNotWorking(action, ActionErrorCodes.NOT_ENOUGH_ARGUMENTS);
    }

    public void sendActionNotWorking(String action, ActionErrorCodes code) {
        ReaperSanction.INSTANCE.getLogger().warning("Action " + action + " is not working. Code: " + code.getCode() + " " + code.name() +
                " (see the wiki for more informations)");
    }

    public void sendMessageReported(Player player, String cible, String ReportReason) {
        player.sendMessage(MessageManager.INSTANCE.getMessage("Report-Sended", true).replace("%player%", cible).replace("%reason%", ReportReason));
        player.closeInventory();
        for (Player all : Bukkit.getServer().getOnlinePlayers()) {
            if (all.hasPermission("reportview"))
                all.sendMessage(MessageManager.INSTANCE.getMessage("Report-Obtain", true).replace("%player%", cible).replace("%reason%", ReportReason).replace("%sender%", player.getName()));
        }
    }
}
