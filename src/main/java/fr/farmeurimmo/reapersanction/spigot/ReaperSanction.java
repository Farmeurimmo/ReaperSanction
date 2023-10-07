package fr.farmeurimmo.reapersanction.spigot;

import fr.farmeurimmo.reapersanction.api.Main;
import fr.farmeurimmo.reapersanction.api.UpdateChecker;
import fr.farmeurimmo.reapersanction.api.sanctions.SanctionApplier;
import fr.farmeurimmo.reapersanction.api.sanctions.SanctionRevoker;
import fr.farmeurimmo.reapersanction.api.storage.DatabaseManager;
import fr.farmeurimmo.reapersanction.api.storage.FilesManager;
import fr.farmeurimmo.reapersanction.api.storage.LocalStorageManager;
import fr.farmeurimmo.reapersanction.api.storage.MessageManager;
import fr.farmeurimmo.reapersanction.api.users.UsersManager;
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
import java.util.TimeZone;
import java.util.concurrent.CompletableFuture;

public class ReaperSanction extends JavaPlugin implements Listener {

    public static final ArrayList<Player> VANISHED = new ArrayList<>();
    public static ReaperSanction INSTANCE;
    public static String STORAGE_METHOD = "YAML";
    public static String DISCORD_WEBHOOK_URL = "";
    public final HashMap<String, String> ipblocked = new HashMap<>();
    private Main main;

    @Override
    public void onEnable() {
        saveDefaultConfig();
        INSTANCE = this;

        main = new Main(Bukkit.getConsoleSender(), getLogger(), 0);

        String version = Bukkit.getServer().getBukkitVersion();
        main.sendLogMessage("§6-----------------------------------------------------------------------------------------------------", 0);
        main.sendLogMessage("§6This server is using minecraft : §b" + version, 0);

        if (getVersion().contains("RC"))
            getLogger().warning("This version is a release candidate, bugs may be present !");

        main.sendLogMessage("§6Starting configs files...", 0);
        new FilesManager();
        new LocalStorageManager();

        main.sendLogMessage("§6Starting users manager...", 0);
        new UsersManager();

        STORAGE_METHOD = getConfig().getString("storage.method");
        if (STORAGE_METHOD.equalsIgnoreCase("MYSQL")) {
            main.sendLogMessage("§6Found §bMYSQL§6 storage database, trying to connect...", 0);

            getCredentialsAndInitialize();

            try {
                DatabaseManager.INSTANCE.startConnection();
                DatabaseManager.INSTANCE.loadUsers();
            } catch (Exception e) {
                e.printStackTrace();
                getLogger().severe("Unable to connect to the database, stopping server...");
                Bukkit.shutdown();
                return;
            }
        } else {
            main.sendLogMessage("§6Found §bYAML§6 storage method, starting it...", 0);
            LocalStorageManager.INSTANCE.setup();
        }

        main.sendLogMessage("§6Starting moderation module...", 0);
        new SanctionApplier();
        new SanctionRevoker();
        Vanish();
        UsersManager.INSTANCE.checkForOnlinePlayersIfTheyAreUsers();

        main.sendLogMessage("§6Looking for messages...", 0);
        new MessageManager();

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

        selectTimeZone();

        startDiscordWebhook();

        main.sendLogMessage("§aPlugin enabled !", 0);
        main.sendLogMessage("§eOfficial website : §bhttps://reaper.farmeurimmo.fr/reapersanction/", 0);
        main.sendLogMessage("§6-----------------------------------------------------------------------------------------------------", 0);

        CompletableFuture.runAsync(() -> new UpdateChecker(89580).checkForUpdate(Main.INSTANCE.getPluginVersion(), main));

        //detect if the server is behind a proxy like bungeecord or velocity

        int proxy = detectProxy();
        if (proxy == 0) {
            main.sendLogMessage("§6-----------------------------------------------------------------------------------------------------", 0);
            main.sendLogMessage("§cYou are using BungeeCord, please use the BungeeCord version of ReaperSanction !", 0);
            main.sendLogMessage("§6-----------------------------------------------------------------------------------------------------", 0);

        } else if (proxy == 1) {
            main.sendLogMessage("§6-----------------------------------------------------------------------------------------------------", 0);
            main.sendLogMessage("§cYou are using Velocity, please use the Velocity version of ReaperSanction !", 0);
            main.sendLogMessage("§6-----------------------------------------------------------------------------------------------------", 0);

        }

    }

    @Override
    public void onDisable() {
        main.sendLogMessage("§6-----------------------------------------------------------------------------------------------------", 0);
        main.sendLogMessage("§aPlugin disabled !", 0);
        main.sendLogMessage("§6-----------------------------------------------------------------------------------------------------", 0);
    }

    public void reload() {
        FilesManager.INSTANCE.reloadData();

        selectTimeZone();
        startDiscordWebhook();
    }

    public void selectTimeZone() {
        main.sendLogMessage("§6Trying to get the TimeZone from config", 0);
        try {
            TimeZone.setDefault(TimeZone.getTimeZone(getConfig().getString("TimeZone")));
        } catch (Exception e) {
            getLogger().warning("Unable to get the TimeZone from config, using default one...");
            TimeZone.setDefault(TimeZone.getTimeZone("Europe/Paris"));
        }
        main.sendLogMessage("§6TimeZone set to : §b" + TimeZone.getDefault().getID(), 0);
    }

    public void startDiscordWebhook() {
        if (getConfig().getBoolean("Discord.active")) {
            main.sendLogMessage("§6Starting discord webhook...", 0);
            try {
                DISCORD_WEBHOOK_URL = getConfig().getString("Discord.webhook_url");
            } catch (Exception e) {
                getLogger().warning("Unable to start discord webhook, disabling it...");
                getConfig().set("discord.active", false);
                saveConfig();
            }
        }
    }

    public void getCredentialsAndInitialize() {
        String db_url = "jdbc:mysql://" + getConfig().getString("storage.MYSQL.host") + ":" + getConfig().getString("storage.MYSQL.port");
        String db_user = getConfig().getString("storage.MYSQL.username");
        String db_password = getConfig().getString("storage.MYSQL.password");

        new DatabaseManager(db_url, db_user, db_password);
    }

    public boolean matchRequirementsToMigrateToMYSQL() {
        return getConfig().getString("storage.method").equalsIgnoreCase("MYSQL");
    }

    public void Vanish() {
        for (Player players : Bukkit.getOnlinePlayers()) {
            for (Player pl : VANISHED) {
                players.hidePlayer(pl);
            }
        }
        Bukkit.getServer().getScheduler().runTaskLaterAsynchronously(ReaperSanction.INSTANCE, this::Vanish, 20);
    }

    public String getVersion() {
        return this.getDescription().getVersion();
    }

    public String getServerName() {
        return Bukkit.getServerName();
    }

    public boolean isDiscordWebhookActive() {
        return DISCORD_WEBHOOK_URL.length() > 0;
    }

    private int detectProxy() {
        return 2;
    }
}