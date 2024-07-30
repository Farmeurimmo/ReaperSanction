package fr.farmeurimmo.reapersanction.spigot.cmds;

import fr.farmeurimmo.reapersanction.core.Main;
import fr.farmeurimmo.reapersanction.core.sanctions.SanctionsManager;
import fr.farmeurimmo.reapersanction.core.storage.MessageManager;
import fr.farmeurimmo.reapersanction.core.storage.SettingsManager;
import fr.farmeurimmo.reapersanction.spigot.ReaperSanction;
import fr.farmeurimmo.reapersanction.utils.TimeConverter;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.List;

public class KickCmd implements CommandExecutor, TabCompleter {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (args.length == 0) {
            sender.sendMessage(MessageManager.INSTANCE.getMessage("ErrorKickArg", true));
            return false;
        }
        if (Main.INSTANCE.isProxyMode()) {
            sender.sendMessage("§cIn proxy mode, please use this command on the proxy");
            return false;
        }
        Player target = Bukkit.getPlayer(args[0]);
        if (target == null) {
            sender.sendMessage(MessageManager.INSTANCE.getMessage("InvalidPlayer", true));
            return false;
        }
        String reason = "UnknownReasonSpecified";
        if (args.length != 1) {
            reason = String.join(" ", args).replace(args[0] + " ", "");
        }
        SanctionsManager.INSTANCE.kick(target.getUniqueId(), target.getName(), reason, sender.getName());
        if (target.isOnline()) target.kickPlayer(SettingsManager.INSTANCE.getSanctionMessage("kick")
                .replace("%banner%", sender.getName())
                .replace("%date%", TimeConverter.getDateFormatted(System.currentTimeMillis()))
                .replace("%reason%", reason));
        return false;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String s, String[] args) {
        if (args.length == 1 && command.getName().equalsIgnoreCase("kick")) {
            return ReaperSanction.INSTANCE.getEveryoneExceptMe(sender.getName());
        }
        return null;
    }
}
