package fr.farmeurimmo.reapersanction.proxy.velocity.cmds;

import com.velocitypowered.api.command.SimpleCommand;
import com.velocitypowered.api.proxy.Player;
import fr.farmeurimmo.reapersanction.core.sanctions.SanctionsManager;
import fr.farmeurimmo.reapersanction.core.storage.MessageManager;
import fr.farmeurimmo.reapersanction.core.storage.SettingsManager;
import fr.farmeurimmo.reapersanction.core.users.Sanction;
import fr.farmeurimmo.reapersanction.proxy.velocity.ReaperSanction;
import fr.farmeurimmo.reapersanction.utils.TimeConverter;
import net.kyori.adventure.text.Component;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public class BanCmd implements SimpleCommand {
    @Override
    public void execute(Invocation invocation) {
        String[] args = invocation.arguments();
        if (args.length == 0) {
            invocation.source().sendMessage(Component.text(MessageManager.INSTANCE.getMessage("ErrorBanArg", true)));
            return;
        }
        Player target = ReaperSanction.INSTANCE.getProxy().getPlayer(args[0]).orElse(null);
        String reason = MessageManager.INSTANCE.getMessage("UnknownReasonSpecified", false);
        if (target == null) {
            invocation.source().sendMessage(Component.text(MessageManager.INSTANCE.getMessage("InvalidPlayer", true)));
            return;
        }
        if (args.length != 1) {
            reason = String.join(" ", args).replace(args[0] + " ", "").trim();
        }
        String by = (invocation.source() instanceof Player) ? ((Player) invocation.source()).getUsername() : "Console";
        Sanction s = SanctionsManager.INSTANCE.ban(target.getUniqueId(), target.getUsername(),
                target.getRemoteAddress().getAddress().getHostAddress(), reason, by);
        if (target.isActive()) target.disconnect(Component.text(SettingsManager.INSTANCE.getSanctionMessage("ban")
                .replace("%banner%", s.getBy())
                .replace("%date%", TimeConverter.getDateFormatted(s.getAt()))
                .replace("%reason%", s.getReason())));
    }

    @Override
    public CompletableFuture<List<String>> suggestAsync(Invocation invocation) {
        return ReaperSanction.INSTANCE.getListCompletableFuture(invocation);
    }

    @Override
    public boolean hasPermission(Invocation invocation) {
        return invocation.source().hasPermission("reapersanction.ban");
    }
}
