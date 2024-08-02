package fr.farmeurimmo.reapersanction.spigot.cmds;

import fr.farmeurimmo.reapersanction.core.Main;
import fr.farmeurimmo.reapersanction.core.storage.MessageManager;
import fr.farmeurimmo.reapersanction.core.storage.SettingsManager;
import fr.farmeurimmo.reapersanction.spigot.ReaperSanction;
import fr.farmeurimmo.reapersanction.spigot.gui.CustomInventories;
import fr.farmeurimmo.reapersanction.spigot.gui.InventoryType;
import fr.farmeurimmo.reapersanction.utils.Parser;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.List;

public class ReportCmd implements CommandExecutor, TabCompleter {

    //TODO: recode the report

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(MessageManager.INSTANCE.getMessage("NotAvailableInConsole", true));
            return false;
        }
        Player player = (Player) sender;
        if (!Parser.PARSE_BOOLEAN(SettingsManager.INSTANCE.getSetting("report.status"))) {
            player.sendMessage(MessageManager.INSTANCE.getMessage("Report-Disabled", true));
            return false;
        }
        if (args.length != 1) {
            player.sendMessage(MessageManager.INSTANCE.getMessage("Report-ErrorArg", true));
            return false;
        }
        Player target = Bukkit.getPlayer(args[0]);
        if (target == null) {
            player.sendMessage(MessageManager.INSTANCE.getMessage("Report-PlayerNotOnline", true));
            return false;
        }
        CustomInventories.INSTANCE.startInventoryOpenProcess(player, InventoryType.REPORT, args[0]);
        return false;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String commandLabel, String[] args) {
        if (args.length == 1) {
            return Main.INSTANCE.filterByStart(ReaperSanction.INSTANCE.getEveryoneExceptMe(sender.getName()), args[0]);
        }
        return Collections.emptyList();
    }
}
