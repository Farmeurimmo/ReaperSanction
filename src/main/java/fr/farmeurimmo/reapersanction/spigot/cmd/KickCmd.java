package fr.farmeurimmo.reapersanction.spigot.cmd;

import fr.farmeurimmo.reapersanction.api.storage.MessageManager;
import fr.farmeurimmo.reapersanction.spigot.sanctions.SanctionApplier;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class KickCmd implements CommandExecutor, TabCompleter {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (args.length == 0) {
            sender.sendMessage(MessageManager.INSTANCE.getMessage("ErrorKickArg", true));
            return false;
        }
        Player target = Bukkit.getPlayer(args[0]);
        if (target == null) {
            sender.sendMessage(MessageManager.INSTANCE.getMessage("InvalidPlayer", true));
            return false;
        }
        if (args.length == 1) {
            SanctionApplier.INSTANCE.kickPlayer(target, MessageManager.INSTANCE.getMessage("UnkownReasonSpecified", false), sender.getName());
            return false;
        }
        String reason = String.join(" ", args).replace(args[0] + " ", "");
        SanctionApplier.INSTANCE.kickPlayer(target, reason, sender.getName());
        return false;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String s, String[] args) {
        if (args.length == 1 && command.getName().equalsIgnoreCase("kick")) {
            List<String> toReturn = new ArrayList<>();
            for (Player p : Bukkit.getOnlinePlayers()) {
                if (p.getName().equalsIgnoreCase(sender.getName())) continue;
                toReturn.add(p.getName());
            }
            Collections.sort(toReturn);
            return toReturn;
        }
        return null;
    }
}
