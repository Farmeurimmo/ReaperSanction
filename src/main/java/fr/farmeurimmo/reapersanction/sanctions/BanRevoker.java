package main.java.fr.farmeurimmo.reapersanction.sanctions;

import main.java.fr.farmeurimmo.reapersanction.ReaperSanction;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;

public class BanRevoker {

    @SuppressWarnings("deprecation")
    public static void CheckForUnban() {
        for (String aa : ReaperSanction.instance.getData().getConfigurationSection("").getKeys(false)) {
            if (ReaperSanction.instance.getData().getBoolean(aa + ".tempban.istempbanned")) {
                if (ReaperSanction.instance.getData().getLong(aa + ".tempban.timemillis") <= System.currentTimeMillis()) {
                    UnTempBan(aa, Bukkit.getConsoleSender());
                }
            }
            if (ReaperSanction.instance.getData().getBoolean(aa + ".tempmute.istempmuted")) {
                if (ReaperSanction.instance.getData().getLong(aa + ".tempmute.timemillis") <= System.currentTimeMillis()) {
                    MuteRevoker.revokepermamute(aa, Bukkit.getConsoleSender());
                }
            }
        }
        Bukkit.getServer().getScheduler().scheduleAsyncDelayedTask(ReaperSanction.instance, new Runnable() {
            public void run() {
                CheckForUnban();
            }
        }, 20);
    }

    public static void UnTempBan(String aa, CommandSender sender) {
        if (ReaperSanction.instance.getData().getBoolean(aa + ".ban.isbanned") ||
                ReaperSanction.instance.getData().getBoolean(aa + ".tempban.istempbanned")
                || ReaperSanction.instance.getData().getBoolean(aa + ".ban.isipbanned")) {
            ReaperSanction.instance.getData().set(aa + ".tempban.banner", "");
            ReaperSanction.instance.getData().set(aa + ".tempban.reason", "");
            ReaperSanction.instance.getData().set(aa + ".tempban.date", "");
            ReaperSanction.instance.getData().set(aa + ".tempban.expiration", "");
            ReaperSanction.instance.getData().set(aa + ".tempban.duration", "");
            ReaperSanction.instance.getData().set(aa + ".tempban.timemillis", "");
            ReaperSanction.instance.getData().set(aa + ".tempban.istempbanned", false);

            ReaperSanction.instance.getData().set(aa + ".ban.banner", "");
            ReaperSanction.instance.getData().set(aa + ".ban.reason", "");
            ReaperSanction.instance.getData().set(aa + ".ban.date", "");
            ReaperSanction.instance.getData().set(aa + ".ban.isbanned", false);

            ReaperSanction.instance.saveData();

            sender.sendMessage(ReaperSanction.instance.Preffix +
                    ReaperSanction.instance.getConfig().getString("ReaperSanction.Settings.SuccefullyUnbanned").replace("&", "§")
                            .replace("%player%", aa));
        } else {
            sender.sendMessage(ReaperSanction.instance.Preffix +
                    ReaperSanction.instance.getConfig().getString("ReaperSanction.Settings.NotBanned").replace("&", "§"));
        }
    }
}
