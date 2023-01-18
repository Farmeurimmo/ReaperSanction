package main.java.fr.farmeurimmo.reapersanction.cmd;

import main.java.fr.farmeurimmo.reapersanction.MessageManager;
import main.java.fr.farmeurimmo.reapersanction.sanctions.SanctionApplier;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Calendar;

public class TempBanCmd implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        Calendar calendar = Calendar.getInstance();
        if (args.length == 0 || args.length == 1) {
            sender.sendMessage(MessageManager.prefix +
                    MessageManager.instance.getMessage("ErrorTempBanArg"));
            return false;
        }
        StringBuilder sb = new StringBuilder();
        for (String s : args) {
            sb.append(s).append(' ');
        }
        String sample = args[1];
        char[] chars = sample.toCharArray();
        StringBuilder cb = new StringBuilder();
        for (char c : chars) {
            if (Character.isDigit(c)) {
                cb.append(c);
            }
        }
        if (!(cb.length() > 0 && cb.length() < 6)) {
            sender.sendMessage(MessageManager.prefix +
                    MessageManager.instance.getMessage("ErrorTempBanArg"));
            return false;
        }
        if (!(args[1].contains("sec") || args[1].contains("min") || args[1].contains("day") || args[1].contains("year")
                || args[1].contains("hour"))) {
            sender.sendMessage(MessageManager.prefix +
                    MessageManager.instance.getMessage("ErrorTempBanArg"));
            return false;
        }
        String type = args[1].replace(cb.toString(), "");
        String reason;
        if (args.length == 2) {
            reason = MessageManager.instance.getMessage("UnkownReasonSpecified");
        } else {
            reason = sb.toString().replace(args[0] + " ", "").replace(args[1] + " ", "").trim();
        }
        Player p = Bukkit.getPlayer(args[0]);
        if (p == null) {
            sender.sendMessage(MessageManager.prefix +
                    MessageManager.instance.getMessage("InvalidPlayer"));
            return false;
        }
        SanctionApplier.instance.ApplyTempBan(p, reason, sender, cb.toString(), type);
        return false;
    }

}
