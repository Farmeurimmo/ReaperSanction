package main.java.fr.farmeurimmo.reapersanction.sanctions;

import main.java.fr.farmeurimmo.reapersanction.ReaperSanction;
import main.java.fr.farmeurimmo.reapersanction.storage.MessageManager;
import main.java.fr.farmeurimmo.reapersanction.users.User;
import main.java.fr.farmeurimmo.reapersanction.users.UsersManager;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SanctionRevoker {

    public static SanctionRevoker instance;

    public SanctionRevoker() {
        instance = this;

        Bukkit.getScheduler().runTaskTimerAsynchronously(ReaperSanction.instance, () -> {
            for (User user : UsersManager.instance.users) {
                checkForSanctionExpiration(user);
            }
        }, 0, 20);
    }

    public void checkForSanctionExpiration(User user) {
        if (user.isMuted() && !user.isPermaMuted())
            if (user.getMutedUntil() != -1 && user.getMutedUntil() < System.currentTimeMillis()) revokeMute(user);
        if (user.isBanned() && !user.isPermaBan() && !user.isIpBanned())
            if (user.getBannedUntil() != -1 && user.getBannedUntil() < System.currentTimeMillis()) revokeBan(user);
    }

    public void revokeMute(User user) {
        user.setMutedAt(0);
        user.setMutedDuration("");
        user.setMutedUntil(0);
        user.setMutedReason("");
        user.setMutedBy("");
        user.requestUserUpdate();

        Player player = Bukkit.getPlayer(user.getUuid());
        if (player != null) {
            player.sendMessage(MessageManager.prefix +
                    MessageManager.instance.getMessage("MuteEnded"));
        }
    }

    public void revokeMuteAdmin(User user, CommandSender requester) {
        if (!user.isMuted()) {
            requester.sendMessage(MessageManager.prefix + MessageManager.instance.getMessage("NotMuted"));
            return;
        }
        revokeMute(user);
        requester.sendMessage(MessageManager.prefix +
                MessageManager.instance.getMessage("SuccefullyUnmuted")
                        .replace("%player%", user.getName()));
    }

    public void revokeBan(User user) {
        user.setBannedAt(0);
        user.setBannedDuration("");
        user.setBannedUntil(0);
        user.setBannedReason("");
        user.setBannedBy("");
        user.setIpBanned(false);
        user.requestUserUpdate();

        Player player = Bukkit.getPlayer(user.getUuid());
        if (player != null) {
            player.sendMessage(MessageManager.prefix +
                    MessageManager.instance.getMessage("BanEnded"));
        }
    }

    public void revokeBanAdmin(User user, CommandSender requester) {
        if (!user.isBanned()) {
            requester.sendMessage(MessageManager.prefix + MessageManager.instance.getMessage("NotBanned"));
            return;
        }
        revokeBan(user);
        requester.sendMessage(MessageManager.prefix + MessageManager.instance.getMessage("SuccefullyUnbanned").replace("%player%", user.getName()));
    }
}
