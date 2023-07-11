package fr.farmeurimmo.reapersanction.cmd;

import fr.farmeurimmo.reapersanction.gui.CustomInventories;
import fr.farmeurimmo.reapersanction.storage.MessageManager;
import fr.farmeurimmo.reapersanction.users.User;
import fr.farmeurimmo.reapersanction.users.UsersManager;
import org.bukkit.Bukkit;
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
            sender.sendMessage(MessageManager.prefix + MessageManager.INSTANCE.getMessage("NotAvailableInConsole"));
            return false;
        }
        Player player = (Player) sender;

        if (args.length != 1) {
            player.sendMessage(MessageManager.prefix + MessageManager.INSTANCE.getMessage("ErrorHistoryArg"));
            return false;
        }

        User targetUser = UsersManager.INSTANCE.getUser(args[0]);
        if (targetUser == null) {
            player.sendMessage(MessageManager.prefix + MessageManager.INSTANCE.getMessage("InvalidPlayer"));
            return false;
        }
        CustomInventories.INSTANCE.startInventoryOpenOfHistoryGui(player, targetUser, 0);
        return false;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String commandLabel, String[] args) {
        ArrayList<String> subcmd = new ArrayList<>();
        if (args.length == 1) {
            for (Player player : Bukkit.getOnlinePlayers()) {
                if (player.getName().equalsIgnoreCase(sender.getName())) continue;
                subcmd.add(player.getName());
            }
        } else if (args.length >= 2) {
            subcmd.add("");
        }
        Collections.sort(subcmd);
        return subcmd;
    }
}
