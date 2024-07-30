package fr.farmeurimmo.reapersanction.core;

public enum ServerType {

    SPIGOT,
    BUNGEECORD,
    VELOCITY,
    NOT_SUPPORTED;

    public static ServerType getServerType() {
        try {
            Class.forName("org.bukkit.Bukkit");
            return SPIGOT;
        } catch (ClassNotFoundException e) {
            try {
                Class.forName("net.md_5.bungee.api.ProxyServer");
                return BUNGEECORD;
            } catch (ClassNotFoundException e1) {
                try {
                    Class.forName("com.velocitypowered.api.proxy.ProxyServer");
                    return VELOCITY;
                } catch (ClassNotFoundException e2) {
                    return NOT_SUPPORTED;
                }
            }
        }
    }
}
