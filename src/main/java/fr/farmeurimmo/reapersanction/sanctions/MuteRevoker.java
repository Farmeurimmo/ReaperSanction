package main.java.fr.farmeurimmo.reapersanction.sanctions;

import main.java.fr.farmeurimmo.reapersanction.ReaperSanction;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;

public class MuteRevoker {

    public static void revokepermamute(String aa, CommandSender sender) {
        if (ReaperSanction.instance.getData().getBoolean(aa + ".mute.ismuted") ||
                ReaperSanction.instance.getData().getBoolean(aa + ".tempmute.istempmuted")) {
            ReaperSanction.instance.getData().set(aa + ".mute.banner", "");
            ReaperSanction.instance.getData().set(aa + ".mute.reason", "");
            ReaperSanction.instance.getData().set(aa + ".mute.ismuted", false);

            ReaperSanction.instance.getData().set(aa + ".tempmute.banner", "");
            ReaperSanction.instance.getData().set(aa + ".tempmute.reason", "");
            ReaperSanction.instance.getData().set(aa + ".tempmute.timemillis", "");
            ReaperSanction.instance.getData().set(aa + ".tempmute.duration", "");
            ReaperSanction.instance.getData().set(aa + ".tempmute.istempmuted", false);

            sender.sendMessage(ReaperSanction.instance.Preffix +
                    ReaperSanction.instance.getConfig().getString("ReaperSanction.Settings.SuccefullyUnmuted").replace("&", "§")
                            .replace("%player%", aa));

            if (Bukkit.getPlayer(aa) != null) {
                Bukkit.getPlayer(aa).sendMessage(ReaperSanction.instance.Preffix +
                        ReaperSanction.instance.getConfig().getString("ReaperSanction.Settings.MuteEnded").replace("&", "§"));
            }

            ReaperSanction.instance.saveData();
        } else {
            sender.sendMessage(ReaperSanction.instance.Preffix +
                    ReaperSanction.instance.getConfig().getString("ReaperSanction.Settings.NotMuted").replace("&", "§"));
        }
    }
}
