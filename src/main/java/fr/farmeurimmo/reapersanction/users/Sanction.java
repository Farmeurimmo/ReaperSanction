package main.java.fr.farmeurimmo.reapersanction.users;

public class Sanction {

    private int type;
    private String reason;
    private String by;
    private long at;
    private long until;
    private boolean isBan;
    private boolean isIp;
    private String duration;

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
}
