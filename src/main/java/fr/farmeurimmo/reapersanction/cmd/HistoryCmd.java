package fr.farmeurimmo.reapersanction.cmd;

import fr.farmeurimmo.reapersanction.gui.HistoryGui;
import fr.farmeurimmo.reapersanction.storage.MessageManager;
import fr.farmeurimmo.reapersanction.users.User;
import fr.farmeurimmo.reapersanction.users.UsersManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class HistoryCmd implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(MessageManager.prefix + MessageManager.instance.getMessage("NotAvailableInConsole"));
            return false;
        }
        Player player = (Player) sender;

        if (args.length != 1) {
            player.sendMessage(MessageManager.prefix + MessageManager.instance.getMessage("ErrorHistoryArg"));
            return false;
        }

        User targetUser = UsersManager.instance.getUser(args[0]);
        if (targetUser == null) {
            player.sendMessage(MessageManager.prefix + MessageManager.instance.getMessage("InvalidPlayer"));
            return false;
        }
        HistoryGui.instance.openHistoryGui(player, targetUser, 0);
        return false;
    }
}
