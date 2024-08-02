package fr.farmeurimmo.reapersanction.proxy.bungeecord.cmds;

import fr.farmeurimmo.reapersanction.core.sanctions.SanctionsManager;
import fr.farmeurimmo.reapersanction.core.storage.MessageManager;
import fr.farmeurimmo.reapersanction.core.storage.SettingsManager;
import fr.farmeurimmo.reapersanction.core.users.Sanction;
import fr.farmeurimmo.reapersanction.core.users.User;
import fr.farmeurimmo.reapersanction.core.users.UsersManager;
import fr.farmeurimmo.reapersanction.proxy.bungeecord.ReaperSanction;
import fr.farmeurimmo.reapersanction.utils.TimeConverter;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class TempBanCmd extends Command {

    public TempBanCmd() {
        super("tempban", "reapersanction.tempban");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (args.length == 0 || args.length == 1) {
            sender.sendMessage(new TextComponent(MessageManager.INSTANCE.getMessage("ErrorTempBanArg", true)));
            return;
        }
        User user = UsersManager.INSTANCE.getUser(args[0]);
        if (user == null) {
            sender.sendMessage(new TextComponent(MessageManager.INSTANCE.getMessage("InvalidPlayer", true)));
            return;
        }
        String sample = args[1];
        char[] chars = sample.toCharArray();
        StringBuilder cb = new StringBuilder();
        for (char c : chars) {
            if (c == '-') {
                sender.sendMessage(new TextComponent(MessageManager.INSTANCE.getMessage("ErrorTempBanArg", true)));
                return;
            }
            if (Character.isDigit(c)) cb.append(c);
        }
        if (!(cb.length() > 0 && cb.length() < 6)) {
            sender.sendMessage(new TextComponent(MessageManager.INSTANCE.getMessage("ErrorTempBanArg", true)));
            return;
        }
        if (!(args[1].contains("sec") || args[1].contains("min") || args[1].contains("day") || args[1].contains("year")
                || args[1].contains("hour"))) {
            sender.sendMessage(new TextComponent(MessageManager.INSTANCE.getMessage("ErrorTempBanArg", true)));
            return;
        }

        if (user.isPermaBan()) {
            sender.sendMessage(new TextComponent(MessageManager.INSTANCE.getMessage("AlreadyBanned", true)));
            return;
        }
        String type = args[1].replace(cb.toString(), "");
        String reason = MessageManager.INSTANCE.getMessage("UnknownReasonSpecified", false).trim();
        if (args.length != 2) {
            reason = String.join(" ", args).replace(args[0] + " " + args[1] + " ", "").trim();
        }
        String by = sender.getName();
        Sanction s = SanctionsManager.INSTANCE.tempBan(user.getUuid(), user.getName(), reason, by, cb.toString(), type);

        ProxiedPlayer target = ReaperSanction.INSTANCE.getProxy().getPlayer(user.getUuid());
        if (target != null && target.isConnected())
            target.disconnect(new TextComponent(SettingsManager.INSTANCE.getSanctionMessage("tempban")
                    .replace("%banner%", s.getBy())
                    .replace("%date%", TimeConverter.getDateFormatted(s.getAt()))
                    .replace("%reason%", s.getReason())
                    .replace("%until%", TimeConverter.getDateFormatted(s.getUntil()))
                    .replace("%duration%", s.getDuration())));
    }
}
