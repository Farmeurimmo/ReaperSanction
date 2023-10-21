package fr.farmeurimmo.reapersanction.spigot.cmd;

import fr.farmeurimmo.reapersanction.core.sanctions.SanctionsManager;
import fr.farmeurimmo.reapersanction.core.storage.MessageManager;
import fr.farmeurimmo.reapersanction.core.storage.SettingsManager;
import fr.farmeurimmo.reapersanction.core.users.Sanction;
import fr.farmeurimmo.reapersanction.spigot.ReaperSanction;
import fr.farmeurimmo.reapersanction.utils.StrUtils;
import fr.farmeurimmo.reapersanction.utils.TimeConverter;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.Calendar;
import java.util.List;

public class BanCmd implements CommandExecutor, TabCompleter {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        Calendar calendar = Calendar.getInstance();
        if (args.length == 0) {
            sender.sendMessage(MessageManager.INSTANCE.getMessage("ErrorBanArg", true));
            return true;
        }
        Player target = Bukkit.getPlayer(args[0]);
        String reason = MessageManager.INSTANCE.getMessage("UnknownReasonSpecified", false);
        if (target == null) {
            sender.sendMessage(MessageManager.INSTANCE.getMessage("InvalidPlayer", true));
            return true;
        }
        if (args.length != 1) {
            reason = StrUtils.fromArgs(args).replace(args[0] + " ", "").trim();
        }
        Sanction s = SanctionsManager.INSTANCE.ban(target.getUniqueId(), target.getName(), target.getAddress().getAddress().getHostAddress(), reason, sender.getName());
        if (target.isOnline()) target.kickPlayer(SettingsManager.INSTANCE.getSanctionMessage("ban")
                .replace("%banner%", s.getBy())
                .replace("%date%", TimeConverter.getDateFormatted(s.getAt()))
                .replace("%reason%", s.getReason()));
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String commandLabel, String[] args) {
        if (args.length == 1) {
            return ReaperSanction.INSTANCE.getEveryoneExceptMe(sender.getName());
        }
        return null;
    }
}
