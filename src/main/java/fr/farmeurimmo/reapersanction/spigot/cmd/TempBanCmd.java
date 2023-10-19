package fr.farmeurimmo.reapersanction.spigot.cmd;

import fr.farmeurimmo.reapersanction.core.sanctions.SanctionApplier;
import fr.farmeurimmo.reapersanction.core.storage.MessageManager;
import fr.farmeurimmo.reapersanction.utils.StrUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TempBanCmd implements CommandExecutor, TabCompleter {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (args.length == 0 || args.length == 1) {
            sender.sendMessage(MessageManager.INSTANCE.getMessage("ErrorTempBanArg", true));
            return false;
        }
        String sample = args[1];
        char[] chars = sample.toCharArray();
        StringBuilder cb = new StringBuilder();
        for (char c : chars) {
            if (c == '-') {
                sender.sendMessage(MessageManager.INSTANCE.getMessage("ErrorTempBanArg", true));
                return false;
            }
            if (Character.isDigit(c)) cb.append(c);
        }
        if (!(cb.length() > 0 && cb.length() < 6)) {
            sender.sendMessage(MessageManager.INSTANCE.getMessage("ErrorTempBanArg", true));
            return false;
        }
        if (!(args[1].contains("sec") || args[1].contains("min") || args[1].contains("day") || args[1].contains("year")
                || args[1].contains("hour"))) {
            sender.sendMessage(MessageManager.INSTANCE.getMessage("ErrorTempBanArg", true));
            return false;
        }
        String type = args[1].replace(cb.toString(), "");
        String reason;
        if (args.length == 2) {
            reason = MessageManager.INSTANCE.getMessage("UnkownReasonSpecified", false).trim();
        } else {
            reason = StrUtils.fromArgs(args).replace(args[0] + " ", "").replace(args[1] + " ", "").trim();
        }
        Player p = Bukkit.getPlayer(args[0]);
        if (p == null) {
            sender.sendMessage(MessageManager.INSTANCE.getMessage("InvalidPlayer", true));
            return false;
        }
        SanctionApplier.INSTANCE.tempBan(p.getUniqueId(), p.getName(), p.getAddress().getAddress().getHostAddress(), reason, sender, cb.toString(), type);
        return false;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String commandLabel, String[] args) {
        List<String> subcmd = new ArrayList<>();
        if (args.length == 1) {
            for (Player player : Bukkit.getOnlinePlayers()) {
                if (player.getName().equalsIgnoreCase(sender.getName())) continue;
                subcmd.add(player.getName());
            }
        } else if (args.length == 2) {
            subcmd.add("10sec");
            subcmd.add("10min");
            subcmd.add("10hour");
            subcmd.add("10day");
            subcmd.add("10year");
        }
        Collections.sort(subcmd);
        return subcmd;
    }

}
