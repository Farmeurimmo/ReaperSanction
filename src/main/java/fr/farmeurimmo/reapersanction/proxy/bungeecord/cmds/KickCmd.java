package fr.farmeurimmo.reapersanction.proxy.bungeecord.cmds;

import fr.farmeurimmo.reapersanction.core.sanctions.SanctionsManager;
import fr.farmeurimmo.reapersanction.core.storage.MessageManager;
import fr.farmeurimmo.reapersanction.core.storage.SettingsManager;
import fr.farmeurimmo.reapersanction.core.users.Sanction;
import fr.farmeurimmo.reapersanction.proxy.bungeecord.ReaperSanction;
import fr.farmeurimmo.reapersanction.utils.TimeConverter;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class KickCmd extends Command {

    public KickCmd() {
        super("kick", "reapersanction.kick");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (args.length == 0) {
            sender.sendMessage(new TextComponent(MessageManager.INSTANCE.getMessage("ErrorKickArg", true)));
            return;
        }
        ProxiedPlayer player = ReaperSanction.INSTANCE.getPlayer(args[0]);
        if (player == null) {
            sender.sendMessage(new TextComponent(MessageManager.INSTANCE.getMessage("InvalidPlayer", true)));
            return;
        }
        String reason = MessageManager.INSTANCE.getMessage("UnknownReasonSpecified", false);
        if (args.length != 1) {
            reason = String.join(" ", args).replace(args[0] + " ", "").trim();
        }
        String by = sender.getName();
        Sanction s = SanctionsManager.INSTANCE.kick(player.getUniqueId(), player.getName(), reason, by);
        if (player.isConnected())
            player.disconnect(new TextComponent(SettingsManager.INSTANCE.getSanctionMessage("kick")
                    .replace("%banner%", s.getBy())
                    .replace("%date%", TimeConverter.getDateFormatted(s.getAt()))
                    .replace("%reason%", s.getReason())));
    }
}
