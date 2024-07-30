package fr.farmeurimmo.reapersanction.proxy.velocity.cmds;

import com.velocitypowered.api.command.SimpleCommand;
import com.velocitypowered.api.proxy.Player;
import fr.farmeurimmo.reapersanction.core.storage.MessageManager;
import fr.farmeurimmo.reapersanction.core.users.User;
import fr.farmeurimmo.reapersanction.core.users.UsersManager;
import fr.farmeurimmo.reapersanction.proxy.velocity.ReaperSanction;
import fr.farmeurimmo.reapersanction.proxy.velocity.cpm.CPMManager;
import net.kyori.adventure.text.Component;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public class HistoryCmd implements SimpleCommand {

    @Override
    public void execute(Invocation invocation) {
        String[] args = invocation.arguments();
        if (args.length != 1) {
            invocation.source().sendMessage(Component.text(MessageManager.INSTANCE.getMessage("ErrorHistoryArg", true)));
            return;
        }
        User targetUser = UsersManager.INSTANCE.getUser(args[0]);
        if (targetUser == null) {
            invocation.source().sendMessage(Component.text(MessageManager.INSTANCE.getMessage("InvalidPlayer", true)));
            return;
        }
        if (targetUser.getHistory().isEmpty()) {
            invocation.source().sendMessage(Component.text(MessageManager.INSTANCE.getMessage("PlayerNoHistoryAvailable", true)));
            return;
        }
        if (invocation.source() instanceof Player) {
            Player player = (Player) invocation.source();

            CPMManager.INSTANCE.sendPluginMessage(player, "openhistorygui", targetUser.getUserAsString());
        }
    }

    @Override
    public CompletableFuture<List<String>> suggestAsync(Invocation invocation) {
        return ReaperSanction.INSTANCE.getListCompletableFuture(invocation);
    }

    @Override
    public boolean hasPermission(Invocation invocation) {
        return invocation.source().hasPermission("reapersanction.history");
    }
}
