package fr.farmeurimmo.reapersanction.spigot.cmd;

import fr.farmeurimmo.reapersanction.api.storage.FilesManager;
import fr.farmeurimmo.reapersanction.api.storage.MessageManager;
import fr.farmeurimmo.reapersanction.spigot.sanctions.SanctionApplier;
import fr.farmeurimmo.reapersanction.utils.StrUtils;
import fr.farmeurimmo.reapersanction.utils.TimeConverter;
import org.bukkit.BanList.Type;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

public class BanIpCmd implements CommandExecutor, TabCompleter {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        Calendar calendar = Calendar.getInstance();
        if (args.length == 0) {
            sender.sendMessage(MessageManager.INSTANCE.getMessage("ErrorBanIpArg", true));
            return true;
        }
        if (args.length == 1) {
            Player p = Bukkit.getPlayer(args[0]);
            String reason = MessageManager.INSTANCE.getMessage("UnkownReasonSpecified", false);
            assert p != null;
            if (p.isOnline()) p.kickPlayer(FilesManager.INSTANCE.getFromConfigFormatted("BanIp.lines")
                    .replace("%banner%", sender.getName())
                    .replace("%date%", TimeConverter.getFormatTimeWithTZ(calendar.getTime()))
                    .replace("%reason%", reason));
            SanctionApplier.INSTANCE.ApplyPermaBanIp(p, reason, sender.getName());
            Bukkit.getBanList(Type.IP).addBan(p.getAddress().getHostName(), reason,
                    null, sender.getName());
            return true;
        }
        Player p = Bukkit.getPlayer(args[0]);
        String reason = StrUtils.fromArgs(args).replace(args[0] + " ", "").trim();
        assert p != null;
        if (p.isOnline()) p.kickPlayer(FilesManager.INSTANCE.getFromConfigFormatted("BanIp.lines")
                .replace("%banner%", sender.getName())
                .replace("%date%", TimeConverter.getFormatTimeWithTZ(calendar.getTime()))
                .replace("%reason%", reason));
        SanctionApplier.INSTANCE.ApplyPermaBanIp(p, reason, sender.getName());
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String commandLabel, String[] args) {
        List<String> subcmd = new ArrayList<>();
        if (args.length == 1) {
            for (Player player : Bukkit.getOnlinePlayers()) {
                if (player.getName().equalsIgnoreCase(sender.getName())) continue;
                subcmd.add(player.getName());
            }
        }
        Collections.sort(subcmd);
        return subcmd;
    }

}
