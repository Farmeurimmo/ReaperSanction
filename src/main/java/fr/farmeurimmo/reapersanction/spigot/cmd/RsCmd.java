package fr.farmeurimmo.reapersanction.spigot.cmd;

import fr.farmeurimmo.reapersanction.core.storage.MessageManager;
import fr.farmeurimmo.reapersanction.spigot.gui.CustomInventories;
import fr.farmeurimmo.reapersanction.spigot.gui.InventoryType;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class RsCmd implements CommandExecutor, TabCompleter {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if (!(sender instanceof Player)) {
            sender.sendMessage(MessageManager.INSTANCE.getMessage("NotAvailableInConsole", true));
            return false;
        }
        Player player = (Player) sender;
        if (args.length != 1) {
            player.sendMessage(MessageManager.INSTANCE.getMessage("ErrorArg", true));
            return false;
        }
        if (Bukkit.getPlayer(args[0]) != null)
            CustomInventories.INSTANCE.startInventoryOpenProcess(player, InventoryType.MAIN, args[0]);
        else player.sendMessage(MessageManager.INSTANCE.getMessage("InvalidPlayer", true));

        return false;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String commandLabel, String[] args) {
        ArrayList<String> subcmd = new ArrayList<>();
        if (cmd.getName().equalsIgnoreCase("rs") || cmd.getName().equalsIgnoreCase("reapersanction")) {
            if (args.length == 1) {
                for (Player player : Bukkit.getOnlinePlayers()) {
                    if (player.getName().equalsIgnoreCase(sender.getName())) continue;
                    subcmd.add(player.getName());
                }
            } else if (args.length >= 2) {
                subcmd.add("");
            }
            Collections.sort(subcmd);
        }
        return subcmd;
    }
}
