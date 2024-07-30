package fr.farmeurimmo.reapersanction.proxy.velocity.cmd;

import com.velocitypowered.api.command.SimpleCommand;
import fr.farmeurimmo.reapersanction.core.sanctions.SanctionsManager;
import fr.farmeurimmo.reapersanction.core.storage.MessageManager;
import fr.farmeurimmo.reapersanction.core.users.User;
import fr.farmeurimmo.reapersanction.core.users.UsersManager;
import fr.farmeurimmo.reapersanction.proxy.velocity.ReaperSanction;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public class UnBanCmd implements SimpleCommand {


    @Override
    public void execute(Invocation invocation) {
        String[] args = invocation.arguments();
        if (args.length == 0) {
            invocation.source().sendMessage(MessageManager.INSTANCE.getComponent("ErrorUnBanArg", true));
            return;
        }
        User user = UsersManager.INSTANCE.getUser(args[0]);
        if (user == null) {
            invocation.source().sendMessage(MessageManager.INSTANCE.getComponent("InvalidPlayer", true));
            return;
        }
        if (!user.isPermaBan()) {
            invocation.source().sendMessage(MessageManager.INSTANCE.getComponent("NotBanned", true));
            return;
        }
        SanctionsManager.INSTANCE.revokeBanAdmin(user, invocation);
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