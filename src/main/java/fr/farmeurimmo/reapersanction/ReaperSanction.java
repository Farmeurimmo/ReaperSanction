package fr.farmeurimmo.reapersanction;

import fr.farmeurimmo.reapersanction.cmd.*;
import fr.farmeurimmo.reapersanction.gui.GuiListener;
import fr.farmeurimmo.reapersanction.gui.GuiManager;
import fr.farmeurimmo.reapersanction.listeners.ChatEvent;
import fr.farmeurimmo.reapersanction.listeners.JoinLeaveEvent;
import fr.farmeurimmo.reapersanction.sanctions.SanctionApplier;
import fr.farmeurimmo.reapersanction.sanctions.SanctionRevoker;
import fr.farmeurimmo.reapersanction.storage.DatabaseManager;
import fr.farmeurimmo.reapersanction.storage.FilesManager;
import fr.farmeurimmo.reapersanction.storage.LocalStorageManager;
import fr.farmeurimmo.reapersanction.storage.MessageManager;
import fr.farmeurimmo.reapersanction.users.UsersManager;
import fr.mrmicky.fastinv.FastInvManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.TimeZone;

public class ReaperSanction extends JavaPlugin implements Listener {

    public static final ArrayList<Player> vanished = new ArrayList<>();
    public static ReaperSanction instance;
    public static String storageMethod = "YAML";
    public final HashMap<String, String> ipblocked = new HashMap<>();

    @Override
    public void onEnable() {
        saveDefaultConfig();
        instance = this;
        String version = Bukkit.getServer().getBukkitVersion();
        getLogger().info("-----------------------------------------------------------------------------------------------------");
        getLogger().info("This server is using minecraft : " + version);

        getLogger().info("Starting configs files...");
        new FilesManager();
        new LocalStorageManager();

        getLogger().info("Starting users manager...");
        new UsersManager();

        storageMethod = getConfig().getString("storage.method");
        if (storageMethod.equalsIgnoreCase("MYSQL")) {
            getLogger().info("Found MYSQL storage database, trying to connect...");

            getCredentialsAndInitialize();

            try {
                DatabaseManager.instance.startConnection();
                DatabaseManager.instance.loadUsers();
            } catch (Exception e) {
                e.printStackTrace();
                getLogger().severe("§c§lUnable to connect to the database, stopping server...");
                Bukkit.shutdown();
                return;
            }
        } else {
            getLogger().info("Found YAML storage method, starting it...");
            LocalStorageManager.instance.setup();
        }

        getLogger().info("Starting moderation module...");
        new SanctionApplier();
        new SanctionRevoker();
        Vanish();
        UsersManager.instance.checkForOnlinePlayersIfTheyAreUsers();

        getLogger().info("Looking for messages...");
        new MessageManager();

        getLogger().info("Initializing GUIs...");
        FastInvManager.register(instance);
        new GuiManager();

        getLogger().info("Starting listeners...");
        getServer().getPluginManager().registerEvents(new GuiListener(), this);
        getServer().getPluginManager().registerEvents(new JoinLeaveEvent(), this);
        getServer().getPluginManager().registerEvents(new ChatEvent(), this);

        getLogger().info("Starting commands...");
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

        getLogger().info("Trying to get the TimeZone from config");
        try {
            TimeZone.setDefault(TimeZone.getTimeZone(getConfig().getString("TimeZone")));
        } catch (Exception e) {
            getLogger().warning("§c§lUnable to get the TimeZone from config, using default one...");
            TimeZone.setDefault(TimeZone.getTimeZone("Europe/Paris"));
        }

        getLogger().info("[ReaperSanction] Plugin enabled !");
        getLogger().info("Official website : https://reaper.farmeurimmo.fr/reapersanction/");
        getLogger().info("-----------------------------------------------------------------------------------------------------");

        checkForUpdate();
    }

    @Override
    public void onDisable() {
        getLogger().info("-----------------------------------------------------------------------------------------------------");
        getLogger().info("[ReaperSanction] Plugin disabled !");
        getLogger().info("-----------------------------------------------------------------------------------------------------");
    }

    public void reload() {
        FilesManager.instance.reloadData();
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
        new UpdateChecker(this, 89580).getVersion(version -> {
            if (this.getDescription().getVersion().equals(version)) {
                getLogger().info("§6No update found.");
            } else {
                if (version.contains("RC")) {
                    getLogger().warning("§c§lA new RC update is available, you can try it but it may contains bugs !");
                    for (Player player : Bukkit.getOnlinePlayers()) {
                        if (player.hasPermission("reapersanction.rsadmin")) {
                            player.sendMessage("§c§lA new Release candidate update is available, you can try it but it may contains bugs and breaking changes so be careful !");
                        }
                    }
                } else {
                    getLogger().warning("A new update is available please consider updating if you want to receive support !");
                    getLogger().info("Newest version detected at spigot : " + version);
                    getLogger().info("Your version : " + this.getDescription().getVersion());
                    getLogger().info("Download link : https://www.spigotmc.org/resources/reapersanction.89580/");
                    for (Player player : Bukkit.getOnlinePlayers()) {
                        if (player.hasPermission("reapersanction.rsadmin")) {
                            player.sendMessage("§c§lA new update is available please consider updating if you want to receive support !");
                        }
                    }
                    getLogger().info("§4§lA new update is available please consider updating if you want to receive support ! (the spigot api is taking time to update the version)");
                }
            }
        });
        Bukkit.getScheduler().runTaskLater(this, this::checkForUpdate, 20 * 60 * 60);
    }

    public void Vanish() {
        for (Player players : Bukkit.getOnlinePlayers()) {
            for (Player pl : vanished) {
                players.hidePlayer(pl);
            }
        }
        Bukkit.getServer().getScheduler().runTaskLaterAsynchronously(ReaperSanction.instance, this::Vanish, 20);
    }
}