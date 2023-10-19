package fr.farmeurimmo.reapersanction.proxy.velocity.cmd;

import com.velocitypowered.api.command.SimpleCommand;
import fr.farmeurimmo.reapersanction.core.storage.MessageManager;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public class BanCmd implements SimpleCommand {
    @Override
    public void execute(Invocation invocation) {
        String[] args = invocation.arguments();
        if (args.length == 0) {
            invocation.source().sendMessage(MessageManager.INSTANCE.getComponent("ErrorBanArg", true));
        }
    }

    @Override
    public List<String> suggest(Invocation invocation) {
        return SimpleCommand.super.suggest(invocation);
    }

    @Override
    public CompletableFuture<List<String>> suggestAsync(Invocation invocation) {
        return SimpleCommand.super.suggestAsync(invocation);
    }

    @Override
    public boolean hasPermission(Invocation invocation) {
        return SimpleCommand.super.hasPermission(invocation);
    }
}
