package fr.farmeurimmo.reapersanction.server.spigot;

import fr.farmeurimmo.reapersanction.UpdateChecker;
import fr.farmeurimmo.reapersanction.api.Startup;
import fr.farmeurimmo.reapersanction.api.storage.DatabaseManager;
import fr.farmeurimmo.reapersanction.api.storage.FilesManager;
import fr.farmeurimmo.reapersanction.api.storage.LocalStorageManager;
import fr.farmeurimmo.reapersanction.api.storage.MessageManager;
import fr.farmeurimmo.reapersanction.api.users.UsersManager;
import fr.farmeurimmo.reapersanction.server.spigot.cmd.*;
import fr.farmeurimmo.reapersanction.server.spigot.gui.ActionGuiInterpreter;
import fr.farmeurimmo.reapersanction.server.spigot.gui.CustomInventories;
import fr.farmeurimmo.reapersanction.server.spigot.listeners.ChatEvent;
import fr.farmeurimmo.reapersanction.server.spigot.listeners.JoinLeaveEvent;
import fr.farmeurimmo.reapersanction.server.spigot.sanctions.SanctionApplier;
import fr.farmeurimmo.reapersanction.server.spigot.sanctions.SanctionRevoker;
import fr.mrmicky.fastinv.FastInvManager;
import org.bukkit.Bukkit;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.TimeZone;

public class ReaperSanction extends JavaPlugin implements Listener {

    public static final ArrayList<Player> VANISHED = new ArrayList<>();
    public static ReaperSanction INSTANCE;
    public static String STORAGEMETHOD = "YAML";
    public static String DISCORD_WEBHOOK_URL = "";
    public final HashMap<String, String> ipblocked = new HashMap<>();
    private ConsoleCommandSender console;

    @Override
    public void onEnable() {
        saveDefaultConfig();
        INSTANCE = this;
        console = Bukkit.getConsoleSender();

        new Startup(console);

        String version = Bukkit.getServer().getBukkitVersion();
        console.sendMessage("§6-----------------------------------------------------------------------------------------------------");
        console.sendMessage("§6This server is using minecraft : §b" + version);

        if (getVersion().contains("RC"))
            getLogger().warning("This version is a release candidate, bugs may be present !");

        console.sendMessage("§6Starting configs files...");
        new FilesManager();
        new LocalStorageManager();

        console.sendMessage("§6Starting users manager...");
        new UsersManager();

        STORAGEMETHOD = getConfig().getString("storage.method");
        if (STORAGEMETHOD.equalsIgnoreCase("MYSQL")) {
            console.sendMessage("§6Found §bMYSQL §6storage database, trying to connect...");

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
            console.sendMessage("§6Found §bYAML §6storage method, starting it...");
            LocalStorageManager.INSTANCE.setup();
        }

        console.sendMessage("§6Starting moderation module...");
        new SanctionApplier();
        new SanctionRevoker();
        Vanish();
        UsersManager.INSTANCE.checkForOnlinePlayersIfTheyAreUsers();

        console.sendMessage("§6Looking for messages...");
        new MessageManager();

        console.sendMessage("§6Initializing GUIs...");
        FastInvManager.register(INSTANCE);
        new ActionGuiInterpreter();
        new CustomInventories();

        console.sendMessage("§6Starting listeners...");
        getServer().getPluginManager().registerEvents(new JoinLeaveEvent(), this);
        getServer().getPluginManager().registerEvents(new ChatEvent(), this);

        console.sendMessage("§6Starting commands...");
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

        console.sendMessage("§aPlugin enabled !");
        console.sendMessage("§eOfficial website : §bhttps://reaper.farmeurimmo.fr/reapersanction/");
        console.sendMessage("§6-----------------------------------------------------------------------------------------------------");

        checkForUpdate();
    }

    @Override
    public void onDisable() {
        console.sendMessage("§6-----------------------------------------------------------------------------------------------------");
        console.sendMessage("§aPlugin disabled !");
        console.sendMessage("§6-----------------------------------------------------------------------------------------------------");
    }

    public void reload() {
        FilesManager.INSTANCE.reloadData();

        selectTimeZone();
        startDiscordWebhook();
    }

    public void selectTimeZone() {
        console.sendMessage("§6Trying to get the TimeZone from config");
        try {
            TimeZone.setDefault(TimeZone.getTimeZone(getConfig().getString("TimeZone")));
        } catch (Exception e) {
            getLogger().warning("Unable to get the TimeZone from config, using default one...");
            TimeZone.setDefault(TimeZone.getTimeZone("Europe/Paris"));
        }
        console.sendMessage("§6TimeZone set to : §b" + TimeZone.getDefault().getID());
    }

    public void startDiscordWebhook() {
        if (getConfig().getBoolean("Discord.active")) {
            console.sendMessage("§6Starting discord webhook...");
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

    public void checkForUpdate() {
        new UpdateChecker(89580).getVersion(version -> {
            if (this.getDescription().getVersion().equals(version)) {
                console.sendMessage("§6No update found.");
            } else {
                if (version.contains("RC")) {
                    console.sendMessage("§c§lA new Release candidate update is available, you can try it but it may contains bugs and breaking changes so be careful !");
                    for (Player player : Bukkit.getOnlinePlayers()) {
                        if (player.hasPermission("reapersanction.rsadmin")) {
                            player.sendMessage("§c§lA new Release candidate update is available, you can try it but it may contains bugs and breaking changes so be careful !");
                        }
                    }
                } else {
                    getLogger().warning("A new update is available please consider updating if you want to receive support !");
                    console.sendMessage("§cNewest version detected at spigot : §4§l" + version);
                    console.sendMessage("§6Your version : §c" + this.getDescription().getVersion());
                    console.sendMessage("§6Download link : §ahttps://reaper.farmeurimmo.fr/reapersanction/");
                    for (Player player : Bukkit.getOnlinePlayers()) {
                        if (player.hasPermission("reapersanction.rsadmin")) {
                            player.sendMessage("§c§lA new update is available please consider updating if you want to receive support !");
                        }
                    }
                    console.sendMessage("§4§lA new update is available please consider updating if you want to receive support ! (the spigot api is taking time to update the version)");
                }
            }
        });
        Bukkit.getScheduler().runTaskLater(this, this::checkForUpdate, 20 * 60 * 60);
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
}