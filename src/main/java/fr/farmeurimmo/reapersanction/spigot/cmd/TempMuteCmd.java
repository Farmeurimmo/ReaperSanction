package fr.farmeurimmo.reapersanction.spigot.cmd;

import fr.farmeurimmo.reapersanction.core.sanctions.SanctionsManager;
import fr.farmeurimmo.reapersanction.core.storage.MessageManager;
import fr.farmeurimmo.reapersanction.core.users.Sanction;
import fr.farmeurimmo.reapersanction.spigot.ReaperSanction;
import fr.farmeurimmo.reapersanction.utils.StrUtils;
import fr.farmeurimmo.reapersanction.utils.TimeConverter;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TempMuteCmd implements CommandExecutor, TabCompleter {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (args.length == 0 || args.length == 1) {
            sender.sendMessage(MessageManager.INSTANCE.getMessage("ErrorTempMuteArg", true));
            return true;
        }
        Player target = Bukkit.getPlayer(args[0]);
        if (target == null) {
            sender.sendMessage(MessageManager.INSTANCE.getMessage("ErrorPlayerNotFound", true));
            return true;
        }
        String sample = args[1];
        char[] chars = sample.toCharArray();
        StringBuilder cb = new StringBuilder();
        for (char c : chars) {
            if (c == '-') {
                sender.sendMessage(MessageManager.INSTANCE.getMessage("ErrorTempMuteArg", true));
                return true;
            }
            if (Character.isDigit(c)) cb.append(c);
        }
        if (!(cb.length() > 0 && cb.length() < 6)) {
            sender.sendMessage(MessageManager.INSTANCE.getMessage("ErrorTempMuteArg", true));
            return true;
        }
        if (!(args[1].contains("sec") || args[1].contains("min") || args[1].contains("day") || args[1].contains("year")
                || args[1].contains("hour"))) {
            sender.sendMessage(MessageManager.INSTANCE.getMessage("ErrorTempMuteArg", true));
            return true;
        }
        String type = args[1];
        String reason = MessageManager.INSTANCE.getMessage("UnknownReasonSpecified", false).trim();
        if (args.length != 2) {
            reason = StrUtils.fromArgs(args).replace(args[0] + " ", "").replace(args[1] + " ", "").trim();
        }
        Sanction s = SanctionsManager.INSTANCE.tempMute(target.getUniqueId(), target.getName(), target.getAddress().getAddress().getHostAddress(),
                reason.trim(), sender, cb.toString(), type.replace(cb, ""));
        target.sendMessage(TimeConverter.replaceArgs(MessageManager.INSTANCE.getMessage("MessageToPlayerGotTempMuted", true),
                s.getDuration(), target.getName(), s.getBy(), s.getReason(), s.getAt(), s.getUntil()));
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String commandLabel, String[] args) {
        if (args.length == 1) {
            return ReaperSanction.INSTANCE.getEveryoneExceptMe(sender.getName());
        } else if (args.length == 2) {
            return new ArrayList<>(Arrays.asList("10sec", "10min", "10hour", "10day", "10year"));
        }
        return null;
    }
}
