package fr.farmeurimmo.reapersanction.proxy.velocity.cmds;

import com.velocitypowered.api.command.SimpleCommand;
import com.velocitypowered.api.proxy.Player;
import fr.farmeurimmo.reapersanction.core.sanctions.SanctionsManager;
import fr.farmeurimmo.reapersanction.core.storage.MessageManager;
import fr.farmeurimmo.reapersanction.core.users.Sanction;
import fr.farmeurimmo.reapersanction.core.users.User;
import fr.farmeurimmo.reapersanction.core.users.UsersManager;
import fr.farmeurimmo.reapersanction.proxy.velocity.ReaperSanction;
import fr.farmeurimmo.reapersanction.proxy.velocity.cpm.CPMManager;
import fr.farmeurimmo.reapersanction.utils.TimeConverter;
import net.kyori.adventure.text.Component;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public class MuteCmd implements SimpleCommand {

    @Override
    public void execute(Invocation invocation) {
        String[] args = invocation.arguments();
        if (args.length == 0) {
            invocation.source().sendMessage(Component.text(MessageManager.INSTANCE.getMessage("ErrorMuteArg", true)));
            return;
        }
        User user = UsersManager.INSTANCE.getUser(args[0]);
        String reason = MessageManager.INSTANCE.getMessage("UnknownReasonSpecified", false);
        if (user == null) {
            invocation.source().sendMessage(Component.text(MessageManager.INSTANCE.getMessage("InvalidPlayer", true)));
            return;
        }
        if (args.length != 1) {
            reason = String.join(" ", args).replace(args[0] + " ", "").trim();
        }
        String by = (invocation.source() instanceof Player) ? ((Player) invocation.source()).getUsername() : "Console";
        Sanction s = SanctionsManager.INSTANCE.mute(user.getUuid(), user.getName(), reason, by);

        Player target = ReaperSanction.INSTANCE.getProxy().getPlayer(user.getUuid()).orElse(null);

        if (target != null) {
            CPMManager.INSTANCE.sendPluginMessage(target, "nowmuted", target.getUniqueId().toString());

            if (target.isActive())
                target.sendMessage(Component.text(TimeConverter.replaceArgs(MessageManager.INSTANCE.getMessage("MessageToPlayerGotPermaMuted", true),
                        "null", target.getUsername(), by, reason, s.getAt(), s.getUntil())));
        }
    }

    @Override
    public CompletableFuture<List<String>> suggestAsync(Invocation invocation) {
        return ReaperSanction.INSTANCE.getListCompletableFuture(invocation);
    }

    @Override
    public boolean hasPermission(Invocation invocation) {
        return invocation.source().hasPermission("reapersanction.mute");
    }
}
