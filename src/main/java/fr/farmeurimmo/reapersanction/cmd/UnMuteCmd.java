package fr.farmeurimmo.reapersanction.cmd;

import fr.farmeurimmo.reapersanction.sanctions.SanctionRevoker;
import fr.farmeurimmo.reapersanction.storage.MessageManager;
import fr.farmeurimmo.reapersanction.users.User;
import fr.farmeurimmo.reapersanction.users.UsersManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class UnMuteCmd implements CommandExecutor, TabCompleter {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (args.length != 1) {
            sender.sendMessage(MessageManager.prefix +
                    MessageManager.INSTANCE.getMessage("ErrorUnMuteArg"));
            return false;
        }
        User user = UsersManager.INSTANCE.getUser(args[0]);
        if (user == null) {
            sender.sendMessage(MessageManager.prefix +
                    MessageManager.INSTANCE.getMessage("InvalidPlayer"));
            return false;
        }
        SanctionRevoker.INSTANCE.revokeMuteAdmin(user, sender);
        return false;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String commandLabel, String[] args) {
        ArrayList<String> subcmd = new ArrayList<>();
        if (cmd.getName().equalsIgnoreCase("unmute")) {
            if (args.length == 1) {
                for (User user : UsersManager.INSTANCE.users) {
                    if (user.isMuted()) {
                        subcmd.add(user.getName());
                    }
                    if (user.isPermaMuted()) {
                        subcmd.add(user.getName());
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
