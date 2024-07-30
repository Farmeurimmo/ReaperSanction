package fr.farmeurimmo.reapersanction.proxy.velocity.cmd;

import com.velocitypowered.api.command.SimpleCommand;
import com.velocitypowered.api.proxy.Player;
import fr.farmeurimmo.reapersanction.core.Main;
import fr.farmeurimmo.reapersanction.core.sanctions.SanctionsManager;
import fr.farmeurimmo.reapersanction.core.storage.MessageManager;
import fr.farmeurimmo.reapersanction.core.storage.SettingsManager;
import fr.farmeurimmo.reapersanction.core.users.Sanction;
import fr.farmeurimmo.reapersanction.proxy.velocity.ReaperSanction;
import fr.farmeurimmo.reapersanction.utils.StrUtils;
import fr.farmeurimmo.reapersanction.utils.TimeConverter;
import net.kyori.adventure.text.Component;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public class BanIpCmd implements SimpleCommand {

    @Override
    public void execute(Invocation invocation) {
        if (invocation.arguments().length == 0) {
            invocation.source().sendMessage(MessageManager.INSTANCE.getComponent("ErrorBanIpArg", true));
            return;
        }
        if (Main.INSTANCE.isProxyMode()) {
            invocation.source().sendMessage(Component.text("§cIn proxy mode, please use this command on the proxy"));
            return;
        }
        Player target = ReaperSanction.INSTANCE.getPlayer(invocation.arguments()[0]);
        if (target == null) {
            invocation.source().sendMessage(MessageManager.INSTANCE.getComponent("InvalidPlayer", true));
            return;
        }
        String reason = MessageManager.INSTANCE.getMessage("UnknownReasonSpecified", false);
        if (invocation.arguments().length != 1) {
            reason = StrUtils.fromArgs(invocation.arguments()).replace(invocation.arguments()[0] + " ", "").trim();
        }
        String banner = (invocation.source() instanceof Player) ? ((Player) invocation.source()).getUsername() : "Console";
        Sanction s = SanctionsManager.INSTANCE.banIp(target.getUniqueId(), target.getUsername(),
                target.getRemoteAddress().getAddress().getHostAddress(), reason, banner);
        if (target.isActive()) {
            target.disconnect(Component.text(SettingsManager.INSTANCE.getSanctionMessage("banip")
                    .replace("%banner%", s.getBy())
                    .replace("%date%", TimeConverter.getDateFormatted(s.getAt()))
                    .replace("%reason%", s.getReason())));
        }

    }

    @Override
    public CompletableFuture<List<String>> suggestAsync(Invocation invocation) {
        return ReaperSanction.INSTANCE.getListCompletableFuture(invocation);
    }

    @Override
    public boolean hasPermission(Invocation invocation) {
        return invocation.source().hasPermission("reapersanction.banip");
    }
}
