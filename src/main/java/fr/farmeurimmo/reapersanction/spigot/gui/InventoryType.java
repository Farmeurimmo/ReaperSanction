package fr.farmeurimmo.reapersanction.spigot.gui;

public enum InventoryType {
    MAIN("Main"),
    BAN("Ban"),
    MUTE("Mute"),
    KICK("Kick"),
    BAN_IP("BanIP"),
    END("End"),
    REPORT("Report"),
    HISTORY("History");

    final String name;

    InventoryType(String i) {
        this.name = i;
    }

    public String getName() {
        return name;
    }
}
