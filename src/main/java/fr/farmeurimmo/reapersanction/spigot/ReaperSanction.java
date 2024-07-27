package fr.farmeurimmo.reapersanction.spigot;

import fr.farmeurimmo.reapersanction.core.Main;
import fr.farmeurimmo.reapersanction.core.users.UsersManager;
import fr.farmeurimmo.reapersanction.spigot.cmd.*;
import fr.farmeurimmo.reapersanction.spigot.cpm.CPMManager;
import fr.farmeurimmo.reapersanction.spigot.gui.ActionGuiInterpreter;
import fr.farmeurimmo.reapersanction.spigot.gui.CustomInventories;
import fr.farmeurimmo.reapersanction.spigot.listeners.ChatEvent;
import fr.farmeurimmo.reapersanction.spigot.listeners.ConnectionEvent;
import fr.mrmicky.fastinv.FastInvManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.UUID;

public class ReaperSanction extends JavaPlugin implements Listener {

    public static ReaperSanction INSTANCE;
    private Main main;

    @Override
    public void onEnable() {
        saveDefaultConfig();
        INSTANCE = this;

        main = new Main(Bukkit.getConsoleSender(), getLogger(), INSTANCE.getDataFolder());

        String version = Bukkit.getServer().getBukkitVersion();
        main.sendLogMessage("§6-----------------------------------------------------------------------------------------------------", 0);
        main.sendLogMessage("§6This server is using minecraft : §b" + version, 0);

        main.sendLogMessage("§6Starting moderation module...", 0);
        UsersManager.INSTANCE.checkForOnlinePlayersIfTheyAreUsers();

        main.sendLogMessage("§6Initializing GUIs...", 0);
        FastInvManager.register(INSTANCE);
        new ActionGuiInterpreter();
        new CustomInventories();

        main.sendLogMessage("§6Starting listeners...", 0);
        getServer().getPluginManager().registerEvents(new ConnectionEvent(), this);
        getServer().getPluginManager().registerEvents(new ChatEvent(), this);
        //register the custom plugin message listener with channel "reapersanction:main"
        getServer().getMessenger().registerIncomingPluginChannel(this, "reaper:sanction", new CPMManager());

        main.sendLogMessage("§6Starting commands...", 0);
        if (!Main.INSTANCE.isProxyMode()) {
            getCommand("ban").setExecutor(new BanCmd());
            getCommand("tempban").setExecutor(new TempBanCmd());
            getCommand("ban-ip").setExecutor(new BanIpCmd());
            getCommand("mute").setExecutor(new MuteCmd());
            getCommand("tempmute").setExecutor(new TempMuteCmd());
            getCommand("unmute").setExecutor(new UnMuteCmd());
            getCommand("unban").setExecutor(new UnBanCmd());
            getCommand("kick").setExecutor(new KickCmd());
        }
        getCommand("report").setExecutor(new ReportCmd());

        getCommand("vanish").setExecutor(new VanishCmd());
        getCommand("rsadmin").setExecutor(new RsAdminCmd());
        getCommand("rs").setExecutor(new RsCmd());
        getCommand("history").setExecutor(new HistoryCmd());

        vanish();

        CPMManager.INSTANCE.requestForMuteds();

        main.endOfStart();
        main.sendLogMessage("§6-----------------------------------------------------------------------------------------------------", 0);
    }

    @Override
    public void onDisable() {
        main.sendLogMessage("§6-----------------------------------------------------------------------------------------------------", 0);

        main.disable();

        main.sendLogMessage("§6-----------------------------------------------------------------------------------------------------", 0);
    }

    public void vanish() {
        for (Player players : Bukkit.getOnlinePlayers()) {
            for (UUID pl : Main.VANISHED) {
                Player plTarget = Bukkit.getPlayer(pl);
                if (plTarget == null) continue;
                players.hidePlayer(plTarget);
            }
        }
        Bukkit.getServer().getScheduler().runTaskLaterAsynchronously(ReaperSanction.INSTANCE, this::vanish, 20);
    }

    public ArrayList<String> getEveryoneExceptMe(String playerName) {
        ArrayList<String> players = new ArrayList<>();

        Bukkit.getOnlinePlayers().stream().filter(p -> !p.getName().equals(playerName)).forEach(p -> players.add(p.getName()));

        return players;
    }

    public String getServerName() {
        return Bukkit.getServerName();
    }
}