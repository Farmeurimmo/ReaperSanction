package fr.farmeurimmo.reapersanction.spigot.cmd;

import fr.farmeurimmo.reapersanction.api.storage.MessageManager;
import fr.farmeurimmo.reapersanction.api.users.User;
import fr.farmeurimmo.reapersanction.api.users.UsersManager;
import fr.farmeurimmo.reapersanction.spigot.sanctions.SanctionRevoker;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class UnBanCmd implements CommandExecutor, TabCompleter {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (args.length != 1) {
            sender.sendMessage(MessageManager.prefix +
                    MessageManager.INSTANCE.getMessage("ErrorUnBanArg"));
            return false;
        }
        User user = UsersManager.INSTANCE.getUser(args[0]);
        if (user == null) {
            sender.sendMessage(MessageManager.prefix +
                    MessageManager.INSTANCE.getMessage("InvalidPlayer"));
            return false;
        }
        SanctionRevoker.INSTANCE.revokeBanAdmin(user, sender);
        return false;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String commandLabel, String[] args) {
        ArrayList<String> subcmd = new ArrayList<>();
        if (cmd.getName().equalsIgnoreCase("unban")) {
            if (args.length == 1) {
                for (User user : UsersManager.INSTANCE.users) {
                    if (user.isBanned()) {
                        subcmd.add(user.getName());
                    }
                    if (user.isIpBanned()) {
                        subcmd.add(user.getIp());
                    }
                }
            } else if (args.length >= 2) {
                subcmd.add("");
            }
            Collections.sort(subcmd);
        }
        return subcmd;
    }
}
