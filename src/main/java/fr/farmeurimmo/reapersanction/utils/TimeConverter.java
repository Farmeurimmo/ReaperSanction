package main.java.fr.farmeurimmo.reapersanction.utils;

import main.java.fr.farmeurimmo.reapersanction.ReaperSanction;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class TimeConverter {

    public static String getFormatTimeWithTZ(Date currentTime) {
        SimpleDateFormat timeZoneDate = new SimpleDateFormat("HH:mm:ss dd/MM/yyyy", Locale.FRANCE);
        return timeZoneDate.format(currentTime);
    }

    public static String replaceArgs(String toReplace, String duration, String type, String player, String sender, String reason) {
        toReplace = toReplace.replaceAll("%duration%", duration + " " + type.replaceAll("sec",
                                ReaperSanction.instance.getConfig().getString("ReaperSanction.Settings.Seconds").replaceAll("&", "§"))
                        .replaceAll("min",
                                ReaperSanction.instance.getConfig().getString("ReaperSanction.Settings.Minutes").replaceAll("&", "§"))
                        .replaceAll("hour",
                                ReaperSanction.instance.getConfig().getString("ReaperSanction.Settings.Hours").replaceAll("&", "§"))
                        .replaceAll("day",
                                ReaperSanction.instance.getConfig().getString("ReaperSanction.Settings.Days").replaceAll("&", "§"))
                        .replaceAll("year",
                                ReaperSanction.instance.getConfig().getString("ReaperSanction.Settings.Years").replaceAll("&", "§")))
                .replaceAll("&", "§")
                .replaceAll("%player%", player).replaceAll("%banner%", sender.trim())
                .replaceAll("%reason%", reason.trim());
        return toReplace;
    }
}
