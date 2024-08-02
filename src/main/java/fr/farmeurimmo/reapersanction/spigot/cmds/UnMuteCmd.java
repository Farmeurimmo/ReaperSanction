package fr.farmeurimmo.reapersanction.spigot.cmds;

import fr.farmeurimmo.reapersanction.core.Main;
import fr.farmeurimmo.reapersanction.core.sanctions.SanctionsManager;
import fr.farmeurimmo.reapersanction.core.storage.MessageManager;
import fr.farmeurimmo.reapersanction.core.users.User;
import fr.farmeurimmo.reapersanction.core.users.UsersManager;
import fr.farmeurimmo.reapersanction.spigot.cpm.CPMManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class UnMuteCmd implements CommandExecutor, TabCompleter {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (args.length != 1) {
            sender.sendMessage(MessageManager.INSTANCE.getMessage("ErrorUnMuteArg", true));
            return false;
        }
        if (Main.INSTANCE.isProxyMode()) {
            CPMManager.INSTANCE.sendPluginMessage((Player) sender, "unban", args[0]);
            sender.sendMessage("Proxy mode enabled, sending unmute request to proxy, it can fail if the proxy is not running");
            return false;
        }
        User user = UsersManager.INSTANCE.getUser(args[0]);
        if (user == null) {
            sender.sendMessage(MessageManager.INSTANCE.getMessage("InvalidPlayer", true));
            return false;
        }
        SanctionsManager.INSTANCE.revokeMuteAdmin(user, sender);
        return false;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String commandLabel, String[] args) {
        ArrayList<String> subcmd = new ArrayList<>();
        if (args.length == 1) {
            for (User user : UsersManager.INSTANCE.users) {
                if (user.isMuted()) {
                    subcmd.add(user.getName());
                }
                if (user.isPermaMuted()) {
                    subcmd.add(user.getName());
                }
            }
            subcmd = new ArrayList<>(Main.INSTANCE.filterByStart(subcmd, args[0]));
        } else {
            return Collections.emptyList();
        }
        return subcmd;
    }
}
