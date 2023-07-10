package fr.farmeurimmo.reapersanction.cmd;

import fr.farmeurimmo.reapersanction.sanctions.SanctionApplier;
import fr.farmeurimmo.reapersanction.storage.MessageManager;
import fr.farmeurimmo.reapersanction.utils.StrUtils;
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
            sender.sendMessage(MessageManager.prefix +
                    MessageManager.INSTANCE.getMessage("ErrorMuteArg"));
            return false;
        }
        Player p = Bukkit.getPlayer(args[0]);
        if (p == null) {
            sender.sendMessage(MessageManager.prefix +
                    MessageManager.INSTANCE.getMessage("InvalidPlayer"));
            return false;
        }
        if (args.length == 1) {
            String reason = MessageManager.INSTANCE.getMessage("UnkownReasonSpecified");
            SanctionApplier.INSTANCE.ApplyPermaMute(p, reason.trim(), sender.getName(), sender);
            return false;
        }
        String reason = StrUtils.fromArgs(args).replace(args[0] + " ", "").trim();
        SanctionApplier.INSTANCE.ApplyPermaMute(p, reason, sender.getName(), sender);
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