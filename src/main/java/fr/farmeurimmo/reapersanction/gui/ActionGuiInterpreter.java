package main.java.fr.farmeurimmo.reapersanction.gui;

import main.java.fr.farmeurimmo.reapersanction.ReaperSanction;
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
                CustomInventories.instance.startInventoryOpenProcess(p, it, player);
                return;
            }
            if (what.equals("MUTE")) {

                return;
            }
            if (what.equals("CLOSE")) {
                p.closeInventory();
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
        ReaperSanction.instance.getLogger().warning("Action " + action + " is not working. Code: " + code.getCode() + " " + code.name() +
                " (see the wiki for more informations)");
    }
}
