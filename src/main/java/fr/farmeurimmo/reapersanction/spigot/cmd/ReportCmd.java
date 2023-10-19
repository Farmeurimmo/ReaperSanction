package fr.farmeurimmo.reapersanction.spigot.cmd;

import fr.farmeurimmo.reapersanction.core.storage.MessageManager;
import fr.farmeurimmo.reapersanction.core.storage.SettingsManager;
import fr.farmeurimmo.reapersanction.spigot.gui.CustomInventories;
import fr.farmeurimmo.reapersanction.spigot.gui.InventoryType;
import fr.farmeurimmo.reapersanction.utils.Parser;
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

    //TODO: recode the report

    @SuppressWarnings("deprecation")
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(MessageManager.INSTANCE.getMessage("NotAvailableInConsole", true));
            return false;
        }
        Player player = (Player) sender;
        if (args.length != 1) {
            player.sendMessage(MessageManager.INSTANCE.getMessage("Report-ErrorArg", true));
            return true;
        }
        if (Bukkit.getOfflinePlayer(args[0]).isOnline()) {
            if (Parser.PARSE_BOOLEAN(SettingsManager.INSTANCE.getSetting("report.status"))) {
                CustomInventories.INSTANCE.startInventoryOpenProcess(player, InventoryType.REPORT, args[0]);
            } else {
                player.sendMessage(MessageManager.INSTANCE.getMessage("Report-Disabled", true));
            }
            return true;
        }
        player.sendMessage(MessageManager.INSTANCE.getMessage("Report-PlayerNotonline", true));
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String commandLabel, String[] args) {
        ArrayList<String> subcmd = new ArrayList<>();
        if (cmd.getName().equalsIgnoreCase("report")) {
            if (args.length == 1) {
                for (Player player : Bukkit.getOnlinePlayers()) {
                    if (player.getName().equalsIgnoreCase(sender.getName())) continue;
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
