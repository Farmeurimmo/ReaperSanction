package fr.farmeurimmo.reapersanction.proxy.velocity.cmds;

import com.velocitypowered.api.command.SimpleCommand;
import fr.farmeurimmo.reapersanction.core.sanctions.SanctionsManager;
import fr.farmeurimmo.reapersanction.core.storage.MessageManager;
import fr.farmeurimmo.reapersanction.core.users.User;
import fr.farmeurimmo.reapersanction.core.users.UsersManager;
import fr.farmeurimmo.reapersanction.proxy.velocity.ReaperSanction;
import net.kyori.adventure.text.Component;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public class UnBanCmd implements SimpleCommand {


    @Override
    public void execute(Invocation invocation) {
        String[] args = invocation.arguments();
        if (args.length != 1) {
            invocation.source().sendMessage(Component.text(MessageManager.INSTANCE.getMessage("ErrorUnBanArg", true)));
            return;
        }
        User user = UsersManager.INSTANCE.getUser(args[0]);
        if (user == null) {
            invocation.source().sendMessage(Component.text(MessageManager.INSTANCE.getMessage("InvalidPlayer", true)));
            return;
        }

        if (!user.isBanned()) {
            invocation.source().sendMessage(Component.text(MessageManager.INSTANCE.getMessage("NotBanned", true)));
            return;
        }
        SanctionsManager.INSTANCE.revokeBan(user);
        invocation.source().sendMessage(Component.text(MessageManager.INSTANCE.getMessage("SuccessfullyUnbanned", true).replace("%player%", user.getName())));
    }

    @Override
    public boolean hasPermission(Invocation invocation) {
        return invocation.source().hasPermission("reapersanction.unban");
    }

    @Override
    public CompletableFuture<List<String>> suggestAsync(Invocation invocation) {
        return ReaperSanction.INSTANCE.getUsersCompletableFuture(invocation);
    }
}
