package fr.farmeurimmo.reapersanction.proxy.bungeecord.cmds;

import fr.farmeurimmo.reapersanction.core.sanctions.SanctionsManager;
import fr.farmeurimmo.reapersanction.core.storage.MessageManager;
import fr.farmeurimmo.reapersanction.core.users.User;
import fr.farmeurimmo.reapersanction.core.users.UsersManager;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.plugin.Command;


public class UnbanCmd extends Command {

    public UnbanCmd() {
        super("unban", "reapersanction.unban");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (args.length != 1) {
            sender.sendMessage(new TextComponent(MessageManager.INSTANCE.getMessage("ErrorUnbanArg", true)));
            return;
        }
        User user = UsersManager.INSTANCE.getUser(args[0]);
        if (user == null) {
            sender.sendMessage(new TextComponent(MessageManager.INSTANCE.getMessage("InvalidPlayer", true)));
            return;
        }
        if (!user.isPermaBan()) {
            sender.sendMessage(new TextComponent(MessageManager.INSTANCE.getMessage("NotBanned", true)));
            return;
        }

        if (!user.isBanned()) {
            sender.sendMessage(new TextComponent(MessageManager.INSTANCE.getMessage("NotBanned", true)));
            return;
        }
        SanctionsManager.INSTANCE.revokeBan(user);
        sender.sendMessage(new TextComponent(MessageManager.INSTANCE.getMessage("SuccessfullyUnbanned", true).replace("%player%", user.getName())));
    }
}