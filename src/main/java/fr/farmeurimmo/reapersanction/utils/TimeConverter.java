package main.java.fr.farmeurimmo.reapersanction.utils;

import main.java.fr.farmeurimmo.reapersanction.storage.FilesManager;

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
                        FilesManager.instance.getFromConfigFormatted("Seconds")).replaceAll("min",
                        FilesManager.instance.getFromConfigFormatted("Minutes")).replaceAll("hour",
                        FilesManager.instance.getFromConfigFormatted("Hours")).replaceAll("day",
                        FilesManager.instance.getFromConfigFormatted("Days")).replaceAll("year",
                        FilesManager.instance.getFromConfigFormatted("Years")))

                .replaceAll("%player%", player).replaceAll("%banner%", sender.trim())
                .replaceAll("%reason%", reason.trim());
        return toReplace;
    }

    public static String getDateFormatted(long time) {
        return getFormatTimeWithTZ(new Date(time));
    }
}
