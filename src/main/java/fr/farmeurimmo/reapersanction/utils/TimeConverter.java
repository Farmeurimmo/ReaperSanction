package main.java.fr.farmeurimmo.reapersanction.utils;

import main.java.fr.farmeurimmo.reapersanction.ConfigManager;

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
                        ConfigManager.instance.getFromConfigFormatted("Seconds")).replaceAll("min",
                        ConfigManager.instance.getFromConfigFormatted("Minutes")).replaceAll("hour",
                        ConfigManager.instance.getFromConfigFormatted("Hours")).replaceAll("day",
                        ConfigManager.instance.getFromConfigFormatted("Days")).replaceAll("year",
                        ConfigManager.instance.getFromConfigFormatted("Years")))

                .replaceAll("%player%", player).replaceAll("%banner%", sender.trim())
                .replaceAll("%reason%", reason.trim());
        return toReplace;
    }
}
