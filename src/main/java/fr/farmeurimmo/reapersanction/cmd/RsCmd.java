package main.java.fr.farmeurimmo.reapersanction.cmd;

import main.java.fr.farmeurimmo.reapersanction.gui.RsGui;
import main.java.fr.farmeurimmo.reapersanction.storage.MessageManager;
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

        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (args.length == 0) {
                player.sendMessage(MessageManager.prefix +
                        MessageManager.instance.getMessage("ErrorArg"));
                return true;
            }
            if (args.length >= 2) {
                player.sendMessage(MessageManager.prefix +
                        MessageManager.instance.getMessage("ErrorArg"));
                return true;
            }
            if (Bukkit.getPlayer(args[0]) != null) {
                RsGui.SsMainGui(player, args[0]);
            } else {
                player.sendMessage(MessageManager.prefix +
                        MessageManager.instance.getMessage("InvalidPlayer"));
            }
            return true;
        }
        return false;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String commandLabel, String[] args) {
        ArrayList<String> subcmd = new ArrayList<>();
        if (cmd.getName().equalsIgnoreCase("rs") || cmd.getName().equalsIgnoreCase("reapersanction")) {
            if (args.length == 1) {
                for (Player player : Bukkit.getOnlinePlayers()) {
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
