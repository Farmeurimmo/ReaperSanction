package fr.farmeurimmo.reapersanction.cmd;

import fr.farmeurimmo.reapersanction.sanctions.SanctionApplier;
import fr.farmeurimmo.reapersanction.storage.MessageManager;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class KickCmd implements CommandExecutor, TabCompleter {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (args.length == 0) {
            sender.sendMessage(MessageManager.prefix + MessageManager.INSTANCE.getMessage("ErrorKickArg"));
            return false;
        }
        Player target = Bukkit.getPlayer(args[0]);
        if (target == null) {
            sender.sendMessage(MessageManager.prefix + MessageManager.INSTANCE.getMessage("InvalidPlayer"));
            return false;
        }
        if (args.length == 1) {
            SanctionApplier.INSTANCE.kickPlayer(target, MessageManager.INSTANCE.getMessage("UnkownReasonSpecified"), sender.getName());
            return false;
        }
        String reason = String.join(" ", args).replace(args[0] + " ", "");
        SanctionApplier.INSTANCE.kickPlayer(target, reason, sender.getName());
        return false;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String s, String[] args) {
        if (args.length == 0) {
            List<String> toReturn = new ArrayList<>();
            for (Player p : Bukkit.getOnlinePlayers()) {
                toReturn.add(p.getName());
            }
            return toReturn;
        }
        return null;
    }
}
