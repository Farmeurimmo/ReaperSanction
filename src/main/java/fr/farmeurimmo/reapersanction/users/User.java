package fr.farmeurimmo.reapersanction.users;

import fr.farmeurimmo.reapersanction.ReaperSanction;
import fr.farmeurimmo.reapersanction.storage.DatabaseManager;
import fr.farmeurimmo.reapersanction.storage.LocalStorageManager;

import java.util.LinkedList;
import java.util.UUID;

public class User {

    private final static String separator = "§";
    private static final String separator_G = "§§";

    private final UUID uuid;
    private final String name;
    private long mutedUntil;
    private String mutedReason;
    private String mutedBy;
    private long mutedAt;
    private String mutedDuration;
    private long bannedUntil;
    private String bannedReason;
    private String bannedBy;
    private long bannedAt;
    private boolean isIpBanned;
    private String bannedDuration;
    private String ip;
    private LinkedList<Sanction> history;

    /*
     * -1 means permanent sanction
     * 0 means no sanction
     * > 0 means the sanction will be removed at the given time
     */

    public User(UUID uuid, String name) {
        this.uuid = uuid;
        this.name = name;
        this.mutedUntil = 0;
        this.mutedReason = null;
        this.mutedBy = null;
        this.mutedAt = 0;
        this.mutedDuration = null;
        this.bannedUntil = 0;
        this.bannedReason = null;
        this.bannedBy = null;
        this.bannedAt = 0;
        this.isIpBanned = false;
        this.bannedDuration = null;
        this.ip = null;
        this.history = new LinkedList<>();
    }

    public User(UUID uuid, String name, long mutedUntil, String mutedReason, String mutedBy, long mutedAt, String mutedDuration, long bannedUntil, String bannedReason, String bannedBy, long bannedAt, boolean isIpBanned, String bannedDuration, String ip, LinkedList<Sanction> history) {
        this.uuid = uuid;
        this.name = name;
        this.mutedUntil = mutedUntil;
        this.mutedReason = mutedReason;
        this.mutedBy = mutedBy;
        this.mutedAt = mutedAt;
        this.mutedDuration = mutedDuration;
        this.bannedUntil = bannedUntil;
        this.bannedReason = bannedReason;
        this.bannedBy = bannedBy;
        this.bannedAt = bannedAt;
        this.isIpBanned = isIpBanned;
        this.bannedDuration = bannedDuration;
        this.ip = ip;
        this.history = history;
    }

    public static String getHistoryAsString(LinkedList<Sanction> history) {
        StringBuilder builder = new StringBuilder();
        for (Sanction sanction : history) {
            builder.append(sanctionAsString(sanction)).append(separator_G);
        }
        return builder.toString();
    }

    public static LinkedList<Sanction> getHistoryFromString(String history) {
        LinkedList<Sanction> sanctions = new LinkedList<>();
        String[] args = history.split(separator_G);
        for (String sanction : args) {
            if (sanction == null) continue;
            if (sanction.isEmpty()) continue;
            sanctions.add(sanctionFromString(sanction));
        }
        return sanctions;
    }

    public static String sanctionAsString(Sanction sanction) {
        return sanction.getType() + separator + sanction.getReason() + separator + sanction.getBy() + separator + sanction.getAt() + separator + sanction.getUntil() +
                separator + sanction.isBan() + separator + sanction.isIp() + separator + sanction.getDuration();
    }

    public static Sanction sanctionFromString(String sanction) {
        String[] args = sanction.split(separator);
        return new Sanction(Integer.parseInt(args[0]), args[1], args[2], Long.parseLong(args[3]), Long.parseLong(args[4]), Boolean.parseBoolean(args[5]), Boolean.parseBoolean(args[6]), args[7]);
    }

    public void requestUserUpdate() {
        if (ReaperSanction.STORAGEMETHOD.equalsIgnoreCase("MYSQL")) DatabaseManager.INSTANCE.updatePlayer(this);
        else LocalStorageManager.INSTANCE.saveUser(this, true);
    }

    public UUID getUuid() {
        return uuid;
    }

    public String getName() {
        return name;
    }

    public boolean isPermaBan() {
        return bannedUntil == -1;
    }

    public boolean isPermaMuted() {
        return mutedUntil == -1;
    }

    public long getMutedUntil() {
        return mutedUntil;
    }

    public void setMutedUntil(long mutedUntil) {
        this.mutedUntil = mutedUntil;
    }

    public String getMutedReason() {
        return mutedReason;
    }

    public void setMutedReason(String mutedReason) {
        this.mutedReason = mutedReason;
    }

    public String getMutedBy() {
        return mutedBy;
    }

    public void setMutedBy(String mutedBy) {
        this.mutedBy = mutedBy;
    }

    public long getMutedAt() {
        return mutedAt;
    }

    public void setMutedAt(long mutedAt) {
        this.mutedAt = mutedAt;
    }

    public long getBannedUntil() {
        return bannedUntil;
    }

    public void setBannedUntil(long bannedUntil) {
        this.bannedUntil = bannedUntil;
    }

    public String getBannedReason() {
        return bannedReason;
    }

    public void setBannedReason(String bannedReason) {
        this.bannedReason = bannedReason;
    }

    public String getBannedBy() {
        return bannedBy;
    }

    public void setBannedBy(String bannedBy) {
        this.bannedBy = bannedBy;
    }

    public long getBannedAt() {
        return bannedAt;
    }

    public void setBannedAt(long bannedAt) {
        this.bannedAt = bannedAt;
    }

    public boolean isIpBanned() {
        return isIpBanned;
    }

    public void setIpBanned(boolean isIpBanned) {
        this.isIpBanned = isIpBanned;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public LinkedList<Sanction> getHistory() {
        return history;
    }

    public void setHistory(LinkedList<Sanction> history) {
        this.history = history;
    }

    public void addSanction(Sanction sanction) {
        this.history.add(sanction);
    }

    public void removeSanction(Sanction sanction) {
        this.history.remove(sanction);
    }

    public void removeSanction(int index) {
        this.history.remove(index);
    }

    public Sanction getSanction(int index) {
        return this.history.get(index);
    }

    public int getSanctionAmount() {
        return this.history.size();
    }

    public boolean isMuted() {
        return this.mutedUntil != 0;
    }

    public boolean isBanned() {
        return this.bannedUntil != 0;
    }

    public boolean isSanctioned() {
        return this.isBanned() || this.isMuted();
    }

    public boolean hasHistory() {
        return !this.history.isEmpty();
    }

    public String getMutedDuration() {
        return mutedDuration;
    }

    public void setMutedDuration(String mutedDuration) {
        this.mutedDuration = mutedDuration;
    }

    public String getBannedDuration() {
        return bannedDuration;
    }

    public void setBannedDuration(String bannedDuration) {
        this.bannedDuration = bannedDuration;
    }
}
