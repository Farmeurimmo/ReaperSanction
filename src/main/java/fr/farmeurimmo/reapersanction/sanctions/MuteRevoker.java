package main.java.fr.farmeurimmo.reapersanction.sanctions;

import main.java.fr.farmeurimmo.reapersanction.ConfigManager;
import main.java.fr.farmeurimmo.reapersanction.MessageManager;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;

public class MuteRevoker {

    public static void revokepermamute(String aa, CommandSender sender) {
        if (ConfigManager.instance.getData().getBoolean(aa + ".mute.ismuted") ||
                ConfigManager.instance.getData().getBoolean(aa + ".tempmute.istempmuted")) {
            ConfigManager.instance.getData().set(aa + ".mute.banner", "");
            ConfigManager.instance.getData().set(aa + ".mute.reason", "");
            ConfigManager.instance.getData().set(aa + ".mute.ismuted", false);

            ConfigManager.instance.getData().set(aa + ".tempmute.banner", "");
            ConfigManager.instance.getData().set(aa + ".tempmute.reason", "");
            ConfigManager.instance.getData().set(aa + ".tempmute.timemillis", "");
            ConfigManager.instance.getData().set(aa + ".tempmute.duration", "");
            ConfigManager.instance.getData().set(aa + ".tempmute.istempmuted", false);

            sender.sendMessage(MessageManager.prefix +
                    MessageManager.instance.getMessage("SuccefullyUnmuted")
                            .replace("%player%", aa));

            if (Bukkit.getPlayer(aa) != null) {
                Bukkit.getPlayer(aa).sendMessage(MessageManager.prefix +
                        MessageManager.instance.getMessage("MuteEnded"));
            }

            ConfigManager.instance.saveData();
        } else {
            sender.sendMessage(MessageManager.prefix +
                    MessageManager.instance.getMessage("NotMuted"));
        }
    }
}
