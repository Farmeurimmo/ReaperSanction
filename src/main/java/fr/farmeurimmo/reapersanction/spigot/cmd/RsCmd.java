package fr.farmeurimmo.reapersanction.spigot.cmd;

import fr.farmeurimmo.reapersanction.core.storage.MessageManager;
import fr.farmeurimmo.reapersanction.spigot.ReaperSanction;
import fr.farmeurimmo.reapersanction.spigot.gui.CustomInventories;
import fr.farmeurimmo.reapersanction.spigot.gui.InventoryType;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.List;

public class RsCmd implements CommandExecutor, TabCompleter {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if (!(sender instanceof Player)) {
            sender.sendMessage(MessageManager.INSTANCE.getMessage("NotAvailableInConsole", true));
            return true;
        }
        Player player = (Player) sender;
        if (args.length != 1) {
            player.sendMessage(MessageManager.INSTANCE.getMessage("ErrorArg", true));
            return true;
        }
        if (Bukkit.getPlayer(args[0]) != null)
            CustomInventories.INSTANCE.startInventoryOpenProcess(player, InventoryType.MAIN, args[0]);
        else player.sendMessage(MessageManager.INSTANCE.getMessage("InvalidPlayer", true));

        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String commandLabel, String[] args) {
        if (args.length == 1) {
            return ReaperSanction.INSTANCE.getEveryoneExceptMe(sender.getName());
        }
        return Collections.emptyList();
    }
}
