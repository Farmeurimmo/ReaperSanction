package main.java.fr.farmeurimmo.reapersanction.cmd;

import main.java.fr.farmeurimmo.reapersanction.ReaperSanction;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class RsAdminCmd implements CommandExecutor, TabCompleter {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (args.length == 0) {
            sender.sendMessage(ReaperSanction.instance.Preffix +
                    ReaperSanction.instance.getConfig().getString("ReaperSanction.Settings.ErrorArgAdminCommands").replace("&", "§"));
            sender.sendMessage("Subs commands available: infos, reload, rl");
            return true;
        }
        if (args.length >= 2) {
            sender.sendMessage(ReaperSanction.instance.Preffix +
                    ReaperSanction.instance.getConfig().getString("ReaperSanction.Settings.ErrorArgAdminCommands").replace("&", "§"));
            sender.sendMessage("Subs commands available: infos, reload, rl");
            return true;
        }
        if (args[0].equalsIgnoreCase("infos")) {
            sender.sendMessage("Plugin developper : Farmeurimmo");
            sender.sendMessage("Email : contact@farmeurimmo.fr");
            sender.sendMessage("Website : https://reaper.farmeurimmo.fr/reapersanction");
            sender.sendMessage("Version : " + ReaperSanction.instance.getDescription().getVersion());
            return true;
        }
        if (!sender.hasPermission("reapersanction.rsadmin")) {
            sender.sendMessage(ReaperSanction.instance.Preffix +
                    ReaperSanction.instance.getConfig().getString("ReaperSanction.Settings.NoPermission").replace("&", "§"));
            return true;
        }
        if (args[0].equalsIgnoreCase("reload")) {
            ReaperSanction.instance.reloadConfig();
            sender.sendMessage(ReaperSanction.instance.Preffix +
                    ReaperSanction.instance.getConfig().getString("ReaperSanction.Settings.ReloadMessage").replace("&", "§"));
            return true;
        }
        if (args[0].equalsIgnoreCase("rl")) {
            ReaperSanction.instance.reloadConfig();
            sender.sendMessage(ReaperSanction.instance.Preffix +
                    ReaperSanction.instance.getConfig().getString("ReaperSanction.Settings.ReloadMessage").replace("&", "§"));
            return true;
        }
        return false;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String commandLabel, String[] args) {
        ArrayList<String> subcmd = new ArrayList<>();
        if (cmd.getName().equalsIgnoreCase("rsadmin")) {
            if (args.length == 1) {
                subcmd.add("reload");
                subcmd.add("rl");
                subcmd.add("infos");
            } else if (args.length >= 2) {
                subcmd.add("");
            }
            Collections.sort(subcmd);
        }
        return subcmd;
    }
}
