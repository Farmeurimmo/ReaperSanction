package main.java.fr.farmeurimmo.reapersanction.cmd;

import main.java.fr.farmeurimmo.reapersanction.sanctions.SanctionApplier;
import main.java.fr.farmeurimmo.reapersanction.storage.FilesManager;
import main.java.fr.farmeurimmo.reapersanction.storage.MessageManager;
import main.java.fr.farmeurimmo.reapersanction.utils.TimeConverter;
import org.bukkit.BanList.Type;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Calendar;
import java.util.Date;

public class BanIpCmd implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        Calendar calendar = Calendar.getInstance();
        if (args.length == 0) {
            sender.sendMessage(MessageManager.prefix +
                    MessageManager.instance.getMessage("ErrorBanIpArg"));
        } else if (args.length == 1) {
            Player p = Bukkit.getPlayer(args[0]);
            StringBuilder sb = new StringBuilder();
            for (String s : args) {
                sb.append(s).append(' ');
            }
            String reason = MessageManager.instance.getMessage("UnkownReasonSpecified");
            assert p != null;
            if (p.isOnline()) {
                p.kickPlayer(FilesManager.instance.getFromConfigFormatted("BanIp.lines")
                        .replace("%banner%", sender.getName())
                        .replace("%date%", TimeConverter.getFormatTimeWithTZ(calendar.getTime()))
                        .replace("%reason%", reason));
            }
            Date Mydate = new Date(System.currentTimeMillis());
            SanctionApplier.instance.ApplyPermaBanIp(p, reason, sender.getName(),
                    TimeConverter.getFormatTimeWithTZ(Mydate));
            Bukkit.getBanList(Type.IP).addBan(p.getAddress().getHostName(), reason,
                    null, sender.getName());
        } else {
            Player p = Bukkit.getPlayer(args[0]);
            StringBuilder sb = new StringBuilder();
            for (String s : args) {
                sb.append(s).append(' ');
            }
            String reason = sb.toString().replace(args[0] + " ", "").trim();
            assert p != null;
            if (p.isOnline()) {
                p.kickPlayer(FilesManager.instance.getFromConfigFormatted("BanIp.lines")
                        .replace("%banner%", sender.getName())
                        .replace("%date%", TimeConverter.getFormatTimeWithTZ(calendar.getTime()))
                        .replace("%reason%", reason));
            }
            Date Mydate = new Date(System.currentTimeMillis());
            SanctionApplier.instance.ApplyPermaBanIp(p, reason, sender.getName(),
                    TimeConverter.getFormatTimeWithTZ(Mydate));
        }
        return true;
    }

}
