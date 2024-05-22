package fr.farmeurimmo.reapersanction.proxy.velocity.cmd;

import com.velocitypowered.api.command.SimpleCommand;
import com.velocitypowered.api.proxy.Player;
import fr.farmeurimmo.reapersanction.core.sanctions.SanctionsManager;
import fr.farmeurimmo.reapersanction.core.storage.MessageManager;
import fr.farmeurimmo.reapersanction.core.storage.SettingsManager;
import fr.farmeurimmo.reapersanction.core.users.Sanction;
import fr.farmeurimmo.reapersanction.core.users.User;
import fr.farmeurimmo.reapersanction.core.users.UsersManager;
import fr.farmeurimmo.reapersanction.proxy.velocity.ReaperSanction;
import fr.farmeurimmo.reapersanction.utils.TimeConverter;
import net.kyori.adventure.text.Component;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public class TempBanCmd implements SimpleCommand {


    @Override
    public void execute(Invocation invocation) {
        String[] args = invocation.arguments();
        if (args.length == 0 || args.length == 1) {
            invocation.source().sendMessage(Component.text(MessageManager.INSTANCE.getMessage("ErrorTempBanArg", true)));
            return;
        }
        Player target = ReaperSanction.INSTANCE.getProxy().getPlayer(args[0]).orElse(null);
        if (target == null) {
            invocation.source().sendMessage(Component.text(MessageManager.INSTANCE.getMessage("InvalidPlayer", true)));
            return;
        }
        String sample = args[1];
        char[] chars = sample.toCharArray();
        StringBuilder cb = new StringBuilder();
        for (char c : chars) {
            if (c == '-') {
                invocation.source().sendMessage(Component.text(MessageManager.INSTANCE.getMessage("ErrorTempBanArg", true)));
                return;
            }
            if (Character.isDigit(c)) cb.append(c);
        }
        if (!(cb.length() > 0 && cb.length() < 6)) {
            invocation.source().sendMessage(Component.text(MessageManager.INSTANCE.getMessage("ErrorTempBanArg", true)));
            return;
        }
        if (!(args[1].contains("sec") || args[1].contains("min") || args[1].contains("day") || args[1].contains("year")
                || args[1].contains("hour"))) {
            invocation.source().sendMessage(Component.text(MessageManager.INSTANCE.getMessage("ErrorTempBanArg", true)));
        }
        User user = UsersManager.INSTANCE.getUserAndCreateIfNotExists(target.getUniqueId(), target.getUsername());

        if (user.isPermaBan()) {
            invocation.source().sendMessage(Component.text(MessageManager.INSTANCE.getMessage("AlreadyBanned", true)));
            return;
        }
        String type = args[1].replace(cb.toString(), "");
        String reason = MessageManager.INSTANCE.getMessage("UnknownReasonSpecified", false).trim();
        if (args.length != 2) {
            reason = String.join(" ", args).replace(args[0] + " ", "").replace(args[1] + " ", "").trim();
        }
        String by = (invocation.source() instanceof Player) ? ((Player) invocation.source()).getUsername() : "Console";
        Sanction s = SanctionsManager.INSTANCE.tempBan(target.getUniqueId(), target.getUsername(),
                target.getRemoteAddress().getAddress().getHostAddress(), reason, by, cb.toString(), type);
        if (target.isActive()) target.disconnect(Component.text(SettingsManager.INSTANCE.getSanctionMessage("tempban")
                .replace("%banner%", s.getBy())
                .replace("%date%", TimeConverter.getDateFormatted(s.getAt()))
                .replace("%reason%", s.getReason())
                .replace("%until%", TimeConverter.getDateFormatted(s.getUntil()))
                .replace("%duration%", s.getDuration())));
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
        return invocation.source().hasPermission("reapersanction.tempban");
    }
}