package fr.farmeurimmo.reapersanction.spigot.cmd;

import fr.farmeurimmo.reapersanction.core.storage.MessageManager;
import fr.farmeurimmo.reapersanction.core.users.User;
import fr.farmeurimmo.reapersanction.core.users.UsersManager;
import fr.farmeurimmo.reapersanction.spigot.ReaperSanction;
import fr.farmeurimmo.reapersanction.spigot.gui.CustomInventories;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class HistoryCmd implements CommandExecutor, TabCompleter {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(MessageManager.INSTANCE.getMessage("NotAvailableInConsole", true));
            return false;
        }
        Player player = (Player) sender;

        if (args.length != 1) {
            player.sendMessage(MessageManager.INSTANCE.getMessage("ErrorHistoryArg", true));
            return false;
        }

        User targetUser = UsersManager.INSTANCE.getUser(args[0]);
        if (targetUser == null) {
            player.sendMessage(MessageManager.INSTANCE.getMessage("InvalidPlayer", true));
            return false;
        }
        CustomInventories.INSTANCE.startInventoryOpenOfHistoryGui(player, targetUser, 0);
        return false;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String commandLabel, String[] args) {
        ArrayList<String> subcmd = new ArrayList<>();
        if (args.length == 1) {
            return ReaperSanction.INSTANCE.getEveryoneExceptMe(sender.getName());
        } else if (args.length >= 2) {
            subcmd.add("");
        }
        Collections.sort(subcmd);
        return subcmd;
    }
}
