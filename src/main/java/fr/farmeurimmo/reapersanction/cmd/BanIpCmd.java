package main.java.fr.farmeurimmo.reapersanction.cmd;

import main.java.fr.farmeurimmo.reapersanction.ReaperSanction;
import main.java.fr.farmeurimmo.reapersanction.sanctions.ApplySanction;
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
            sender.sendMessage(ReaperSanction.instance.Preffix +
                    ReaperSanction.instance.getConfig().getString("ReaperSanction.Settings.ErrorBanIpArg").replace("&", "§"));
        } else if (args.length == 1) {
            Player p = Bukkit.getPlayer(args[0]);
            StringBuilder sb = new StringBuilder();
            for (String s : args) {
                sb.append(s).append(' ');
            }
            String reason = ReaperSanction.instance.getConfig().getString("ReaperSanction.Settings.UnkownReasonSpecified").replace("&", "§");
            assert p != null;
            if (p != null & p.isOnline()) {
                p.kickPlayer(ReaperSanction.instance.getConfig().getString("ReaperSanction.Settings.BanIp.lines").replace("&", "§")
                        .replace("%banner%", sender.getName())
                        .replace("%date%", TimeConverter.getFormatTimeWithTZ(calendar.getTime()))
                        .replace("%reason%", reason));
            }
            Date Mydate = new Date(System.currentTimeMillis());
            ApplySanction.instance.ApplyPermaBanIp(p, reason, sender.getName(),
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
                p.kickPlayer(ReaperSanction.instance.getConfig().getString("ReaperSanction.Settings.BanIp.lines").replace("&", "§")
                        .replace("%banner%", sender.getName())
                        .replace("%date%", TimeConverter.getFormatTimeWithTZ(calendar.getTime()))
                        .replace("%reason%", reason));
            }
            Date Mydate = new Date(System.currentTimeMillis());
            ApplySanction.instance.ApplyPermaBanIp(p, reason, sender.getName(),
                    TimeConverter.getFormatTimeWithTZ(Mydate));
        }
        return true;
    }

}
