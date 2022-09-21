package main.java.fr.farmeurimmo.reapersanction.cmd;

import main.java.fr.farmeurimmo.reapersanction.ReaperSanction;
import main.java.fr.farmeurimmo.reapersanction.sanctions.ApplySanction;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MuteCmd implements CommandExecutor, TabCompleter {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (args.length == 0) {
            sender.sendMessage(ReaperSanction.instance.Preffix +
                    ReaperSanction.instance.getConfig().getString("ReaperSanction.Settings.ErrorMuteArg").replace("&", "§"));
        } else if (args.length == 1) {
            if (Bukkit.getPlayer(args[0]) != null) {
                Player p = Bukkit.getPlayer(args[0]);
                String reason = ReaperSanction.instance.getConfig().getString("ReaperSanction.Settings.UnkownReasonSpecified").replace("&", "§");
                ApplySanction.instance.ApplyPermaMute(p.getName(), reason.trim(), sender.getName(), sender);
            } else {
                sender.sendMessage(ReaperSanction.instance.Preffix +
                        ReaperSanction.instance.getConfig().getString("ReaperSanction.Settings.InvalidPlayer").replace("&", "§"));
            }
        } else {
            if (Bukkit.getPlayer(args[0]) != null) {
                Player p = Bukkit.getPlayer(args[0]);
                StringBuilder sb = new StringBuilder();
                for (String s : args) {
                    sb.append(s).append(' ');
                }
                String reason = sb.toString().replace(args[0] + " ", "").trim();
                ApplySanction.instance.ApplyPermaMute(p.getName(), reason, sender.getName(), sender);
            } else {
                sender.sendMessage(ReaperSanction.instance.Preffix +
                        ReaperSanction.instance.getConfig().getString("ReaperSanction.Settings.InvalidPlayer").replace("&", "§"));
            }
        }
        return false;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String commandLabel, String[] args) {
        ArrayList<String> subcmd = new ArrayList<>();
        if (cmd.getName().equalsIgnoreCase("mute")) {
            if (args.length == 1) {
                for (Player player : Bukkit.getOnlinePlayers()) {
                    subcmd.add(player.getName());
                }
            } else if (args.length >= 2) {
                subcmd.add("");
            }
            Collections.sort(subcmd);
        }
        return subcmd;
    }
}