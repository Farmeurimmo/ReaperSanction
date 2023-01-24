package main.java.fr.farmeurimmo.reapersanction.users;

import main.java.fr.farmeurimmo.reapersanction.storage.FilesManager;

public class Sanction {

    private final int type;
    private final String reason;
    private final String by;
    private final long at;
    private final long until;
    private final boolean isBan;
    private final boolean isIp;
    private final String duration;

    public Sanction(int type, String reason, String by, long at, long until, boolean isBan, boolean isIp, String duration) {
        this.type = type;
        this.reason = reason;
        this.by = by;
        this.at = at;
        this.until = until;
        this.isBan = isBan;
        this.isIp = isIp;
        this.duration = duration;
    }

    public int getType() {
        return type;
    }

    public String getReason() {
        return reason;
    }

    public String getBy() {
        return by;
    }

    public long getAt() {
        return at;
    }

    public long getUntil() {
        return until;
    }

    public boolean isBan() {
        return isBan;
    }

    public boolean isIp() {
        return isIp;
    }

    public String getDuration() {
        return duration;
    }

    public String getDurationType() {
        String duration = this.duration;
        return duration.replaceAll("[^0-9]", "");
    }

    public String getSanctionTypeStr() {
        if (this.type == 0) return FilesManager.instance.getFromConfigFormatted("History.sanctiontype.banip");
        if (this.type == 1) return FilesManager.instance.getFromConfigFormatted("History.sanctiontype.ban");
        if (this.type == 2) return FilesManager.instance.getFromConfigFormatted("History.sanctiontype.tempban");
        if (this.type == 3) return FilesManager.instance.getFromConfigFormatted("History.sanctiontype.mute");
        if (this.type == 4) return FilesManager.instance.getFromConfigFormatted("History.sanctiontype.tempmute");
        return FilesManager.instance.getFromConfigFormatted("History.sanctiontype.unknown");
    }
}
