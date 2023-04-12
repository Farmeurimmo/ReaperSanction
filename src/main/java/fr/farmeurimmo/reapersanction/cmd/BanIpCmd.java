package fr.farmeurimmo.reapersanction.cmd;

import fr.farmeurimmo.reapersanction.sanctions.SanctionApplier;
import fr.farmeurimmo.reapersanction.storage.FilesManager;
import fr.farmeurimmo.reapersanction.storage.MessageManager;
import fr.farmeurimmo.reapersanction.utils.StrUtils;
import fr.farmeurimmo.reapersanction.utils.TimeConverter;
import org.bukkit.BanList.Type;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Calendar;

public class BanIpCmd implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        Calendar calendar = Calendar.getInstance();
        if (args.length == 0) {
            sender.sendMessage(MessageManager.prefix +
                    MessageManager.instance.getMessage("ErrorBanIpArg"));
            return true;
        }
        if (args.length == 1) {
            Player p = Bukkit.getPlayer(args[0]);
            String reason = MessageManager.instance.getMessage("UnkownReasonSpecified");
            assert p != null;
            if (p.isOnline()) p.kickPlayer(FilesManager.instance.getFromConfigFormatted("BanIp.lines")
                    .replace("%banner%", sender.getName())
                    .replace("%date%", TimeConverter.getFormatTimeWithTZ(calendar.getTime()))
                    .replace("%reason%", reason));
            SanctionApplier.instance.ApplyPermaBanIp(p, reason, sender.getName());
            Bukkit.getBanList(Type.IP).addBan(p.getAddress().getHostName(), reason,
                    null, sender.getName());
            return true;
        }
        Player p = Bukkit.getPlayer(args[0]);
        String reason = StrUtils.fromArgs(args).replace(args[0] + " ", "").trim();
        assert p != null;
        if (p.isOnline()) p.kickPlayer(FilesManager.instance.getFromConfigFormatted("BanIp.lines")
                .replace("%banner%", sender.getName())
                .replace("%date%", TimeConverter.getFormatTimeWithTZ(calendar.getTime()))
                .replace("%reason%", reason));
        SanctionApplier.instance.ApplyPermaBanIp(p, reason, sender.getName());
        return true;
    }

}
