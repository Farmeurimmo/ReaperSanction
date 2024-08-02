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

public class MuteCmd extends Command {

    public MuteCmd() {
        super("mute", "reapersanction.mute");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (args.length == 0) {
            sender.sendMessage(new TextComponent(MessageManager.INSTANCE.getMessage("ErrorMuteArg", true)));
            return;
        }
        User user = UsersManager.INSTANCE.getUser(args[0]);
        if (user == null) {
            sender.sendMessage(new TextComponent(MessageManager.INSTANCE.getMessage("InvalidPlayer", true)));
            return;
        }
        String reason = MessageManager.INSTANCE.getMessage("UnknownReasonSpecified", false);
        if (args.length != 1) {
            reason = String.join(" ", args).replace(args[0] + " ", "").trim();
        }
        String by = sender.getName();
        Sanction s = SanctionsManager.INSTANCE.mute(user.getUuid(), user.getName(), reason, by);

        ProxiedPlayer target = ReaperSanction.INSTANCE.getProxy().getPlayer(user.getUuid());
        if (target != null && target.isConnected()) target.sendMessage(new TextComponent(TimeConverter.replaceArgs(
                MessageManager.INSTANCE.getMessage("MessageToPlayerGotPermaMuted", true), "null",
                target.getName(), by, reason, s.getAt(), s.getUntil())));
    }
}
