package fr.farmeurimmo.reapersanction.spigot.cmds;

import fr.farmeurimmo.reapersanction.core.Main;
import fr.farmeurimmo.reapersanction.core.sanctions.SanctionsManager;
import fr.farmeurimmo.reapersanction.core.storage.MessageManager;
import fr.farmeurimmo.reapersanction.core.users.Sanction;
import fr.farmeurimmo.reapersanction.core.users.User;
import fr.farmeurimmo.reapersanction.core.users.UsersManager;
import fr.farmeurimmo.reapersanction.spigot.ReaperSanction;
import fr.farmeurimmo.reapersanction.utils.StrUtils;
import fr.farmeurimmo.reapersanction.utils.TimeConverter;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.List;

public class MuteCmd implements CommandExecutor, TabCompleter {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (args.length == 0) {
            sender.sendMessage(MessageManager.INSTANCE.getMessage("ErrorMuteArg", true));
            return false;
        }
        if (Main.INSTANCE.isProxyMode()) {
            sender.sendMessage("§cIn proxy mode, please use this command on the proxy");
            return false;
        }
        User user = UsersManager.INSTANCE.getUser(args[0]);
        if (user == null) {
            sender.sendMessage(MessageManager.INSTANCE.getMessage("InvalidPlayer", true));
            return false;
        }
        String reason = MessageManager.INSTANCE.getMessage("UnknownReasonSpecified", false);
        if (args.length != 1) {
            reason = StrUtils.fromArgs(args).replace(args[0] + " ", "").trim();
        }
        Sanction s = SanctionsManager.INSTANCE.mute(user.getUuid(), user.getName(), reason, sender.getName());
        Player target = Bukkit.getPlayer(user.getUuid());
        if (target != null)
            target.sendMessage(TimeConverter.replaceArgs(MessageManager.INSTANCE.getMessage("MessageToPlayerGotPermaMuted", true),
                    "null", target.getName(), sender.getName(), reason, s.getAt(), s.getUntil()));
        return false;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String commandLabel, String[] args) {
        if (args.length == 1) {
            return Main.INSTANCE.filterByStart(ReaperSanction.INSTANCE.getEveryoneExceptMe(sender.getName()), args[0]);
        }
        return null;
    }
}