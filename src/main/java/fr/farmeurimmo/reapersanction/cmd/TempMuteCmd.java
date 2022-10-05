package main.java.fr.farmeurimmo.reapersanction.cmd;

import main.java.fr.farmeurimmo.reapersanction.MessageManager;
import main.java.fr.farmeurimmo.reapersanction.sanctions.ApplySanction;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class TempMuteCmd implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (args.length == 0 || args.length == 1) {
            sender.sendMessage(MessageManager.prefix +
                    MessageManager.instance.getMessage("ErrorTempMuteArg"));
        } else {
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
            if (cb.length() > 0 && cb.length() < 6) {
                if (args[1].contains("sec") || args[1].contains("min") || args[1].contains("day") || args[1].contains("year")
                        || args[1].contains("hour")) {

                    String type = args[1];
                    String reason;
                    if (args.length == 2) {
                        reason = MessageManager.instance.getMessage("UnkownReasonSpecified").trim();
                    } else {
                        reason = sb.toString().replace(args[0] + " ", "").replace(args[1] + " ", "").trim();
                    }
                    ApplySanction.instance.ApplyTempMute(args[0], reason.trim(),
                            sender, cb.toString(), type.replace(cb, ""));
                } else {
                    sender.sendMessage(MessageManager.prefix +
                            MessageManager.instance.getMessage("ErrorTempMuteArg"));
                }
            } else {
                sender.sendMessage(MessageManager.prefix +
                        MessageManager.instance.getMessage("ErrorTempMuteArg"));
            }
        }
        return false;
    }
}
