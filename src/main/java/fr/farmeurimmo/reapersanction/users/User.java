package main.java.fr.farmeurimmo.reapersanction.users;

import java.util.LinkedList;
import java.util.UUID;

public class User {

    private final UUID uuid;
    private final String name;
    private long mutedUntil;
    private String mutedReason;
    private String mutedBy;
    private long mutedAt;
    private long bannedUntil;
    private String bannedReason;
    private String bannedBy;
    private long bannedAt;
    private boolean isIpBanned;
    private String ip;
    private LinkedList<Sanction> history;

    /*
     * -1 means permanent sanction
     * 0 means no sanction
     * > 0 means the sanction will be removed at the given time
     */

    public User(UUID uuid, String name, long mutedUntil, String mutedReason, String mutedBy, long mutedAt, long bannedUntil, String bannedReason, String bannedBy, long bannedAt, boolean isIpBanned, String ip, LinkedList<Sanction> history) {
        this.uuid = uuid;
        this.name = name;
        this.mutedUntil = mutedUntil;
        this.mutedReason = mutedReason;
        this.mutedBy = mutedBy;
        this.mutedAt = mutedAt;
        this.bannedUntil = bannedUntil;
        this.bannedReason = bannedReason;
        this.bannedBy = bannedBy;
        this.bannedAt = bannedAt;
        this.isIpBanned = isIpBanned;
        this.ip = ip;
        this.history = history;
    }

    public User(UUID uuid, String name) {
        this(uuid, name, 0, null, null, 0, 0, null, null, 0, false, null, new LinkedList<>());
    }

    public void requestUserUpdate() {

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

    public boolean isIp() {
        return this.ip != null;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public boolean hasHistory() {
        return !this.history.isEmpty();
    }
}
