package main.java.fr.farmeurimmo.reapersanction.users;

public class Sanction {

    private String reason;
    private String by;
    private long at;
    private long until;
    private boolean isBan;
    private boolean isIp;

    public Sanction(String reason, String by, long at, long until, boolean isBan, boolean isIp) {
        this.reason = reason;
        this.by = by;
        this.at = at;
        this.until = until;
        this.isBan = isBan;
        this.isIp = isIp;
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
}
