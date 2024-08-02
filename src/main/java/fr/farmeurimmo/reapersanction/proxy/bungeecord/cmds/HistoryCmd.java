package fr.farmeurimmo.reapersanction.proxy.bungeecord.cmds;

import fr.farmeurimmo.reapersanction.core.storage.MessageManager;
import fr.farmeurimmo.reapersanction.core.users.User;
import fr.farmeurimmo.reapersanction.core.users.UsersManager;
import fr.farmeurimmo.reapersanction.proxy.bungeecord.cpm.CPMManager;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class HistoryCmd extends Command {

    public HistoryCmd() {
        super("history", "reapersanction.history");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (args.length != 1) {
            sender.sendMessage(new TextComponent(MessageManager.INSTANCE.getMessage("ErrorHistoryArg", true)));
            return;
        }
        User targetUser = UsersManager.INSTANCE.getUser(args[0]);
        if (targetUser == null) {
            sender.sendMessage(new TextComponent(MessageManager.INSTANCE.getMessage("InvalidPlayer", true)));
            return;
        }
        if (targetUser.getHistory().isEmpty()) {
            sender.sendMessage(new TextComponent(MessageManager.INSTANCE.getMessage("PlayerNoHistoryAvailable", true)));
            return;
        }
        if (sender instanceof ProxiedPlayer) {
            ProxiedPlayer player = (ProxiedPlayer) sender;

            CPMManager.INSTANCE.sendPluginMessage(player, "openhistorygui", targetUser.getUserAsString());
        }
    }
}
