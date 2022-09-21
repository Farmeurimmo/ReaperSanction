package main.java.fr.farmeurimmo.reapersanction.cmd;

import main.java.fr.farmeurimmo.reapersanction.ReaperSanction;
import main.java.fr.farmeurimmo.reapersanction.gui.ReportGui;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ReportCmd implements CommandExecutor, TabCompleter {

    @SuppressWarnings("deprecation")
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (args.length == 0) {
                player.sendMessage(ReaperSanction.instance.Preffix +
                        ReaperSanction.instance.getConfig().getString("ReaperSanction.Settings.Report.ErrorArg").replace("&", "§"));
                return true;
            }
            if (args.length >= 2) {
                player.sendMessage(ReaperSanction.instance.Preffix +
                        ReaperSanction.instance.getConfig().getString("ReaperSanction.Settings.Report.ErrorArg").replace("&", "§"));
                return true;
            }
            if (Bukkit.getOfflinePlayer(args[0]).isOnline()) {
                if (ReaperSanction.instance.getConfig().getBoolean("ReaperSanction.Settings.Report.Enabled")) {
                    ReportGui.MakeReportGui(player, args[0]);
                } else {
                    player.sendMessage(ReaperSanction.instance.Preffix +
                            ReaperSanction.instance.getConfig().getString("ReaperSanction.Settings.Report.Disabled").replace("&", "§"));
                }
            } else {
                player.sendMessage(ReaperSanction.instance.Preffix +
                        ReaperSanction.instance.getConfig().getString("ReaperSanction.Settings.Report.PlayerNotonline").replace("&", "§"));
            }
            return true;
        }
        return false;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String commandLabel, String[] args) {
        ArrayList<String> subcmd = new ArrayList<>();
        if (cmd.getName().equalsIgnoreCase("report")) {
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
