package fr.farmeurimmo.reapersanction.spigot.sanctions;

import fr.farmeurimmo.reapersanction.api.storage.MessageManager;
import fr.farmeurimmo.reapersanction.api.users.User;
import fr.farmeurimmo.reapersanction.api.users.UsersManager;
import fr.farmeurimmo.reapersanction.spigot.ReaperSanction;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SanctionRevoker {

    public static SanctionRevoker INSTANCE;

    public SanctionRevoker() {
        INSTANCE = this;

        Bukkit.getScheduler().runTaskTimerAsynchronously(ReaperSanction.INSTANCE, () -> {
            for (User user : UsersManager.INSTANCE.users) {
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
            player.sendMessage(MessageManager.INSTANCE.getMessage("MuteEnded", true));
        }
    }

    public void revokeMuteAdmin(User user, CommandSender requester) {
        if (!user.isMuted()) {
            requester.sendMessage(MessageManager.INSTANCE.getMessage("NotMuted", true));
            return;
        }
        revokeMute(user);
        requester.sendMessage(MessageManager.INSTANCE.getMessage("SuccefullyUnmuted", true)
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
            player.sendMessage(MessageManager.INSTANCE.getMessage("BanEnded", true));
        }
    }

    public void revokeBanAdmin(User user, CommandSender requester) {
        if (!user.isBanned()) {
            requester.sendMessage(MessageManager.INSTANCE.getMessage("NotBanned", true));
            return;
        }
        revokeBan(user);
        requester.sendMessage(MessageManager.INSTANCE.getMessage("SuccefullyUnbanned", true).replace("%player%", user.getName()));
    }
}
