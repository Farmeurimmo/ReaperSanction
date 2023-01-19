package main.java.fr.farmeurimmo.reapersanction.cmd;

import main.java.fr.farmeurimmo.reapersanction.sanctions.SanctionRevoker;
import main.java.fr.farmeurimmo.reapersanction.storage.MessageManager;
import main.java.fr.farmeurimmo.reapersanction.users.User;
import main.java.fr.farmeurimmo.reapersanction.users.UsersManager;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class UnBanCmd implements CommandExecutor, TabCompleter {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (args.length != 1) {
            sender.sendMessage(MessageManager.prefix +
                    MessageManager.instance.getMessage("ErrorUnBanArg"));
            return false;
        }
        User user = UsersManager.instance.getUser(args[0]);
        if (user == null) {
            sender.sendMessage(MessageManager.prefix +
                    MessageManager.instance.getMessage("InvalidPlayer"));
            return false;
        }
        SanctionRevoker.instance.revokeBanAdmin(user, sender);
        return false;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String commandLabel, String[] args) {
        ArrayList<String> subcmd = new ArrayList<>();
        if (cmd.getName().equalsIgnoreCase("unban")) {
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
