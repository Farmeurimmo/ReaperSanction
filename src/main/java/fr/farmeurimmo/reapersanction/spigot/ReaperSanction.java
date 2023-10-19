package fr.farmeurimmo.reapersanction.spigot;

import fr.farmeurimmo.reapersanction.core.Main;
import fr.farmeurimmo.reapersanction.core.sanctions.SanctionApplier;
import fr.farmeurimmo.reapersanction.core.sanctions.SanctionRevoker;
import fr.farmeurimmo.reapersanction.core.users.UsersManager;
import fr.farmeurimmo.reapersanction.spigot.cmd.*;
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
import java.util.HashMap;

public class ReaperSanction extends JavaPlugin implements Listener {

    public static final ArrayList<Player> VANISHED = new ArrayList<>();
    public static ReaperSanction INSTANCE;
    public final HashMap<String, String> ipblocked = new HashMap<>();
    private Main main;

    @Override
    public void onEnable() {
        saveDefaultConfig();
        INSTANCE = this;

        main = new Main(Bukkit.getConsoleSender(), getLogger(), 0, INSTANCE.getDataFolder());

        String version = Bukkit.getServer().getBukkitVersion();
        main.sendLogMessage("§6-----------------------------------------------------------------------------------------------------", 0);
        main.sendLogMessage("§6This server is using minecraft : §b" + version, 0);

        main.sendLogMessage("§6Starting moderation module...", 0);
        new SanctionApplier();
        new SanctionRevoker();
        Vanish();
        UsersManager.INSTANCE.checkForOnlinePlayersIfTheyAreUsers();

        main.sendLogMessage("§6Initializing GUIs...", 0);
        FastInvManager.register(INSTANCE);
        new ActionGuiInterpreter();
        new CustomInventories();

        main.sendLogMessage("§6Starting listeners...", 0);
        getServer().getPluginManager().registerEvents(new ConnectionEvent(), this);
        getServer().getPluginManager().registerEvents(new ChatEvent(), this);

        main.sendLogMessage("§6Starting commands...", 0);
        this.getCommand("vanish").setExecutor(new VanishCmd());
        this.getCommand("report").setExecutor(new ReportCmd());
        this.getCommand("rsadmin").setExecutor(new RsAdminCmd());
        this.getCommand("rs").setExecutor(new RsCmd());
        this.getCommand("ban").setExecutor(new BanCmd());
        this.getCommand("tempban").setExecutor(new TempBanCmd());
        this.getCommand("ban-ip").setExecutor(new BanIpCmd());
        this.getCommand("mute").setExecutor(new MuteCmd());
        this.getCommand("tempmute").setExecutor(new TempMuteCmd());
        this.getCommand("unmute").setExecutor(new UnMuteCmd());
        this.getCommand("unban").setExecutor(new UnBanCmd());
        this.getCommand("history").setExecutor(new HistoryCmd());
        this.getCommand("kick").setExecutor(new KickCmd());

        main.sendLogMessage("§aPlugin enabled !", 0);
        main.sendLogMessage("§eOfficial website : §bhttps://reaper.farmeurimmo.fr/reapersanction/", 0);
        main.sendLogMessage("§6-----------------------------------------------------------------------------------------------------", 0);

        //TODO: detect if the server is behind a proxy like bungeecord or velocity
    }

    @Override
    public void onDisable() {
        main.sendLogMessage("§6-----------------------------------------------------------------------------------------------------", 0);
        main.sendLogMessage("§aPlugin disabled !", 0);
        main.sendLogMessage("§6-----------------------------------------------------------------------------------------------------", 0);
    }

    public void Vanish() {
        for (Player players : Bukkit.getOnlinePlayers()) {
            for (Player pl : VANISHED) {
                players.hidePlayer(pl);
            }
        }
        Bukkit.getServer().getScheduler().runTaskLaterAsynchronously(ReaperSanction.INSTANCE, this::Vanish, 20);
    }

    public String getServerName() {
        return Bukkit.getServerName();
    }
}