package fr.farmeurimmo.reapersanction.proxy.bungeecord.cmds;

import fr.farmeurimmo.reapersanction.core.sanctions.SanctionsManager;
import fr.farmeurimmo.reapersanction.core.storage.MessageManager;
import fr.farmeurimmo.reapersanction.core.users.User;
import fr.farmeurimmo.reapersanction.core.users.UsersManager;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.plugin.Command;

public class UnMuteCmd extends Command {

    public UnMuteCmd() {
        super("unmute", "reapersanction.unmute");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (args.length == 0) {
            sender.sendMessage(new TextComponent(MessageManager.INSTANCE.getMessage("ErrorUnMuteArg", true)));
            return;
        }
        User user = UsersManager.INSTANCE.getUser(args[0]);

        if (user == null) {
            sender.sendMessage(new TextComponent(MessageManager.INSTANCE.getMessage("InvalidPlayer", true)));
            return;
        }

        if (!user.isMuted()) {
            sender.sendMessage(new TextComponent(MessageManager.INSTANCE.getMessage("NotMuted", true)));
            return;
        }

        if (!user.isMuted()) {
            sender.sendMessage(new TextComponent(MessageManager.INSTANCE.getMessage("NotMuted", true)));
            return;
        }
        SanctionsManager.INSTANCE.revokeMute(user, sender.getName());
        sender.sendMessage(new TextComponent(MessageManager.INSTANCE.getMessage("SuccessfullyUnmuted", true)
                .replace("%player%", user.getName())));
    }
}
