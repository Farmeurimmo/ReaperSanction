package main.java.fr.farmeurimmo.reapersanction.cmd;

import main.java.fr.farmeurimmo.reapersanction.ConfigManager;
import main.java.fr.farmeurimmo.reapersanction.MessageManager;
import main.java.fr.farmeurimmo.reapersanction.sanctions.ApplySanction;
import main.java.fr.farmeurimmo.reapersanction.utils.TimeConverter;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;

public class TempBanCmd implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        Calendar calendar = Calendar.getInstance();
        if (args.length == 0 || args.length == 1) {
            sender.sendMessage(MessageManager.prefix +
                    MessageManager.instance.getMessage("ErrorTempBanArg"));
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

                    String aaa = args[1];
                    String type = args[1].replace(cb.toString(), "");
                    int aaaaaa;
                    LocalDateTime endDate = LocalDateTime.now();
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss dd/MM/yyyy");
                    if (aaa.contains("s")) {
                        aaaaaa = Integer.parseInt(aaa.replace("sec", ""));
                        endDate = LocalDateTime.now().plusSeconds(aaaaaa);
                    } else if (aaa.contains("m")) {
                        aaaaaa = Integer.parseInt(aaa.replace("min", ""));
                        endDate = LocalDateTime.now().plusMinutes(aaaaaa);
                    } else if (aaa.contains("h")) {
                        aaaaaa = Integer.parseInt(aaa.replace("hour", ""));
                        endDate = LocalDateTime.now().plusHours(aaaaaa);
                    } else if (aaa.contains("d")) {
                        aaaaaa = Integer.parseInt(aaa.replace("day", ""));
                        endDate = LocalDateTime.now().plusDays(aaaaaa);
                    } else if (aaa.contains("y")) {
                        aaaaaa = Integer.parseInt(aaa.replace("year", ""));
                        endDate = LocalDateTime.now().plusYears(aaaaaa);
                    }
                    String reason;
                    if (args.length == 2) {
                        reason = MessageManager.instance.getMessage("UnkownReasonSpecified");
                    } else {
                        reason = sb.toString().replace(args[0] + " ", "").replace(args[1] + " ", "").trim();
                    }
                    Player p = Bukkit.getPlayer(args[0]);
                    assert p != null;
                    if (p.isOnline()) {
                        Bukkit.getPlayer(args[0]).kickPlayer(ConfigManager.instance.getFromConfigFormatted("TempBan.lines")
                                .replace("%banner%", sender.getName())
                                .replace("%date%", TimeConverter.getFormatTimeWithTZ(calendar.getTime()))
                                .replace("%reason%", reason)
                                .replace("%expiration%", endDate.format(formatter))
                                .replace("%duration%", args[1].replace("sec", " second(s)").replace("min", " minute(s)")
                                        .replace("day", " day(s)").replace("hour", " hour(s)").replace("year", " year(s)")));
                    }
                    Date Mydate = new Date(System.currentTimeMillis());
                    ApplySanction.instance.ApplyTempBan(args[0], reason, sender,
                            TimeConverter.getFormatTimeWithTZ(Mydate), endDate.format(formatter), args[1].replace(type, ""),
                            type);
                } else {
                    sender.sendMessage(MessageManager.prefix +
                            MessageManager.instance.getMessage("ErrorTempBanArg"));
                }
            } else {
                sender.sendMessage(MessageManager.prefix +
                        MessageManager.instance.getMessage("ErrorTempBanArg"));
            }
        }
        return false;
    }

}
