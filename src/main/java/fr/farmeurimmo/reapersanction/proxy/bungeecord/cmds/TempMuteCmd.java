package fr.farmeurimmo.reapersanction.proxy.bungeecord.cmds;

import fr.farmeurimmo.reapersanction.core.sanctions.SanctionsManager;
import fr.farmeurimmo.reapersanction.core.storage.MessageManager;
import fr.farmeurimmo.reapersanction.core.users.Sanction;
import fr.farmeurimmo.reapersanction.core.users.User;
import fr.farmeurimmo.reapersanction.core.users.UsersManager;
import fr.farmeurimmo.reapersanction.proxy.bungeecord.ReaperSanction;
import fr.farmeurimmo.reapersanction.utils.TimeConverter;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class TempMuteCmd extends Command {

    public TempMuteCmd() {
        super("tempmute", "reapersanction.tempmute");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (args.length == 0 || args.length == 1) {
            sender.sendMessage(new TextComponent(MessageManager.INSTANCE.getMessage("ErrorTempMuteArg", true)));
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
                sender.sendMessage(new TextComponent(MessageManager.INSTANCE.getMessage("ErrorTempMuteArg", true)));
                return;
            }
            if (Character.isDigit(c)) cb.append(c);
        }
        if (!(cb.length() > 0 && cb.length() < 6)) {
            sender.sendMessage(new TextComponent(MessageManager.INSTANCE.getMessage("ErrorTempMuteArg", true)));
            return;
        }
        if (!(args[1].contains("sec")) && !(args[1].contains("min")) && !(args[1].contains("day")) && !(args[1].contains("year"))
                && !(args[1].contains("hour"))) {
            sender.sendMessage(new TextComponent(MessageManager.INSTANCE.getMessage("ErrorTempMuteArg", true)));
            return;
        }

        if (user.isPermaMuted()) {
            sender.sendMessage(new TextComponent(MessageManager.INSTANCE.getMessage("AlreadyMuted", true)));
            return;
        }
        String type = args[1].replace(cb.toString(), "").trim();
        String reason = MessageManager.INSTANCE.getMessage("UnknownReasonSpecified", false).trim();
        if (args.length != 2) {
            reason = String.join(" ", args).replace(args[0] + " ", "").replace(args[1] + " ", "").trim();
        }
        String by = sender.getName();

        Sanction s = SanctionsManager.INSTANCE.tempMute(user.getUuid(), user.getName(), reason, by, cb.toString(), type);

        ProxiedPlayer target = ReaperSanction.INSTANCE.getProxy().getPlayer(user.getUuid());
        if (target != null && target.isConnected()) {
            target.sendMessage(new TextComponent(TimeConverter.replaceArgs(MessageManager.INSTANCE.getMessage("MessageToPlayerGotTempMuted", true),
                    s.getDuration(), target.getName(), s.getBy(), s.getReason(), s.getAt(), s.getUntil())));
        }
    }
}
