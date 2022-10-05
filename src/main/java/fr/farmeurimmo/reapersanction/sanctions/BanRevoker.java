package main.java.fr.farmeurimmo.reapersanction.sanctions;

import main.java.fr.farmeurimmo.reapersanction.ConfigManager;
import main.java.fr.farmeurimmo.reapersanction.MessageManager;
import main.java.fr.farmeurimmo.reapersanction.ReaperSanction;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;

public class BanRevoker {

    @SuppressWarnings("deprecation")
    public static void CheckForUnban() {
        for (String aa : ConfigManager.instance.getData().getConfigurationSection("").getKeys(false)) {
            if (ConfigManager.instance.getData().getBoolean(aa + ".tempban.istempbanned")) {
                if (ConfigManager.instance.getData().getLong(aa + ".tempban.timemillis") <= System.currentTimeMillis()) {
                    UnTempBan(aa, Bukkit.getConsoleSender());
                }
            }
            if (ConfigManager.instance.getData().getBoolean(aa + ".tempmute.istempmuted")) {
                if (ConfigManager.instance.getData().getLong(aa + ".tempmute.timemillis") <= System.currentTimeMillis()) {
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
        if (ConfigManager.instance.getData().getBoolean(aa + ".ban.isbanned") ||
                ConfigManager.instance.getData().getBoolean(aa + ".tempban.istempbanned")
                || ConfigManager.instance.getData().getBoolean(aa + ".ban.isipbanned")) {
            ConfigManager.instance.getData().set(aa + ".tempban.banner", "");
            ConfigManager.instance.getData().set(aa + ".tempban.reason", "");
            ConfigManager.instance.getData().set(aa + ".tempban.date", "");
            ConfigManager.instance.getData().set(aa + ".tempban.expiration", "");
            ConfigManager.instance.getData().set(aa + ".tempban.duration", "");
            ConfigManager.instance.getData().set(aa + ".tempban.timemillis", "");
            ConfigManager.instance.getData().set(aa + ".tempban.istempbanned", false);

            ConfigManager.instance.getData().set(aa + ".ban.banner", "");
            ConfigManager.instance.getData().set(aa + ".ban.reason", "");
            ConfigManager.instance.getData().set(aa + ".ban.date", "");
            ConfigManager.instance.getData().set(aa + ".ban.isbanned", false);

            ConfigManager.instance.saveData();

            sender.sendMessage(MessageManager.prefix +
                    MessageManager.instance.getMessage("SuccefullyUnbanned")
                            .replace("%player%", aa));
        } else {
            sender.sendMessage(MessageManager.prefix +
                    MessageManager.instance.getMessage("NotBanned"));
        }
    }
}
