package fr.farmeurimmo.reapersanction.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class TimeConverter {

    public static String getFormatTimeWithTZ(Date currentTime) {
        SimpleDateFormat timeZoneDate = new SimpleDateFormat("HH:mm:ss dd/MM/yyyy", Locale.FRANCE);
        return timeZoneDate.format(currentTime);
    }

    public static String replaceArgs(String toReplace, String duration, String player, String sender, String reason, long at, long until) {
        return toReplace.replaceAll("%duration%", duration).replaceAll("%player%", player).replaceAll("%banner%", sender.trim())
                .replaceAll("%reason%", reason.trim()).replaceAll("%date%", getDateFormatted(at))
                .replaceAll("%expiration%", getDateFormatted(until));
    }

    public static String getDateFormatted(long time) {
        if (time < 0) return "Permanent";
        return getFormatTimeWithTZ(new Date(time));
    }
}
