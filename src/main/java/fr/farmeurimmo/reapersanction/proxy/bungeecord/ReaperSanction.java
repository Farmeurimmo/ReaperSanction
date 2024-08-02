package fr.farmeurimmo.reapersanction.proxy.bungeecord;

import fr.farmeurimmo.reapersanction.core.Main;
import fr.farmeurimmo.reapersanction.core.sanctions.SanctionsManager;
import fr.farmeurimmo.reapersanction.core.storage.MessageManager;
import fr.farmeurimmo.reapersanction.core.users.User;
import fr.farmeurimmo.reapersanction.core.users.UsersManager;
import fr.farmeurimmo.reapersanction.proxy.bungeecord.cmds.*;
import fr.farmeurimmo.reapersanction.proxy.bungeecord.cpm.CPMManager;
import fr.farmeurimmo.reapersanction.proxy.bungeecord.listeners.PlayerListener;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Plugin;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class ReaperSanction extends Plugin {

    public static ReaperSanction INSTANCE;
    private Main main;

    @Override
    public void onEnable() {
        INSTANCE = this;
        main = new Main(getLogger(), null, getDataFolder());

        getProxy().getPluginManager().registerListener(INSTANCE, new PlayerListener());
        getProxy().getPluginManager().registerListener(INSTANCE, new CPMManager());

        getProxy().getPluginManager().registerCommand(INSTANCE, new BanCmd());
        getProxy().getPluginManager().registerCommand(INSTANCE, new UnBanCmd());
        getProxy().getPluginManager().registerCommand(INSTANCE, new TempBanCmd());
        getProxy().getPluginManager().registerCommand(INSTANCE, new MuteCmd());
        getProxy().getPluginManager().registerCommand(INSTANCE, new UnMuteCmd());
        getProxy().getPluginManager().registerCommand(INSTANCE, new TempMuteCmd());
        getProxy().getPluginManager().registerCommand(INSTANCE, new HistoryCmd());
        getProxy().getPluginManager().registerCommand(INSTANCE, new KickCmd());
        getProxy().getPluginManager().registerCommand(INSTANCE, new BanIpCmd());

        getProxy().getScheduler().schedule(INSTANCE, () -> SanctionsManager.INSTANCE.checkForUsersExpiration(), 0, 10, TimeUnit.SECONDS);

        Main.INSTANCE.endOfStart();
    }

    @Override
    public void onDisable() {
        main.disable();
    }

    public void sendBungeeCordMessage(String message) {
        getProxy().getConsole().sendMessage(new net.md_5.bungee.api.chat.TextComponent(message));
    }

    public ProxiedPlayer getPlayer(String name) {
        return getProxy().getPlayer(name);
    }

    public String getPluginVersion() {
        return getDescription().getVersion();
    }

    public ProxyServer getProxy() {
        return ProxyServer.getInstance();
    }

    public ArrayList<String> getEveryone() {
        ArrayList<String> list = new ArrayList<>();
        for (User user : UsersManager.INSTANCE.getUsers()) {
            list.add(user.getName());
        }
        return list;
    }

    public ArrayList<String> getEveryoneExceptMe(String me) {
        ArrayList<String> everyone = getEveryone();
        everyone.remove(me);
        return everyone;
    }

    public void sendMuteEnded(ProxiedPlayer player) {
        player.sendMessage(new TextComponent(MessageManager.INSTANCE.getMessage("MuteEnded", true)));
    }
}
