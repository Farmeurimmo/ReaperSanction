package fr.farmeurimmo.reapersanction.cmd;

import fr.farmeurimmo.reapersanction.sanctions.SanctionApplier;
import fr.farmeurimmo.reapersanction.storage.MessageManager;
import fr.farmeurimmo.reapersanction.utils.StrUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class TempMuteCmd implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (args.length == 0 || args.length == 1) {
            sender.sendMessage(MessageManager.prefix + MessageManager.INSTANCE.getMessage("ErrorTempMuteArg"));
            return false;
        }
        String sample = args[1];
        char[] chars = sample.toCharArray();
        StringBuilder cb = new StringBuilder();
        for (char c : chars) {
            if (c == '-') {
                sender.sendMessage(MessageManager.prefix + MessageManager.INSTANCE.getMessage("ErrorTempMuteArg"));
                return false;
            }
            if (Character.isDigit(c)) cb.append(c);
        }
        if (!(cb.length() > 0 && cb.length() < 6)) {
            sender.sendMessage(MessageManager.prefix + MessageManager.INSTANCE.getMessage("ErrorTempMuteArg"));
            return false;
        }
        if (!(args[1].contains("sec") || args[1].contains("min") || args[1].contains("day") || args[1].contains("year")
                || args[1].contains("hour"))) {
            sender.sendMessage(MessageManager.prefix + MessageManager.INSTANCE.getMessage("ErrorTempMuteArg"));
            return false;
        }
        String type = args[1];
        String reason;
        if (args.length == 2) {
            reason = MessageManager.INSTANCE.getMessage("UnkownReasonSpecified").trim();
        } else {
            reason = StrUtils.fromArgs(args).replace(args[0] + " ", "").replace(args[1] + " ", "").trim();
        }
        Player player = Bukkit.getPlayer(args[0]);
        if (player == null) {
            sender.sendMessage(MessageManager.prefix + MessageManager.INSTANCE.getMessage("ErrorPlayerNotFound"));
            return false;
        }
        SanctionApplier.INSTANCE.ApplyTempMute(player, reason.trim(), sender, cb.toString(), type.replace(cb, ""));
        return false;
    }
}
