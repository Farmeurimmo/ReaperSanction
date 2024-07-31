package fr.farmeurimmo.reapersanction.proxy.velocity;

import com.google.inject.Inject;
import com.velocitypowered.api.command.SimpleCommand;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.plugin.PluginContainer;
import com.velocitypowered.api.plugin.PluginDescription;
import com.velocitypowered.api.plugin.annotation.DataDirectory;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;
import fr.farmeurimmo.reapersanction.core.Main;
import fr.farmeurimmo.reapersanction.core.sanctions.SanctionsManager;
import fr.farmeurimmo.reapersanction.proxy.velocity.cmds.*;
import fr.farmeurimmo.reapersanction.proxy.velocity.cpm.CPMManager;
import fr.farmeurimmo.reapersanction.proxy.velocity.listeners.PlayerListener;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;

import java.io.File;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

@Plugin(
        id = "reapersanction",
        name = "ReaperSanction",
        description = "ReaperSanction velocity plugin",
        url = "https://farmeurimmo.fr/projects/reapersanction",
        authors = "Farmeurimmo",
        version = "2.0.0-RC1"
)
public class ReaperSanction {

    public static ReaperSanction INSTANCE;
    private final ProxyServer proxy;
    private final Logger logger;
    private final File dataFolder;
    private Main main;

    @Inject
    public ReaperSanction(ProxyServer proxy, Logger logger, @DataDirectory Path dataDirectory) {
        INSTANCE = this;

        this.proxy = proxy;
        this.logger = logger;
        this.dataFolder = dataDirectory.toFile();
    }

    @Subscribe
    public void onProxyInitialization(ProxyInitializeEvent e) {
        main = new Main(logger, null, dataFolder);

        proxy.getCommandManager().register("ban", new BanCmd());
        proxy.getCommandManager().register("tempban", new TempBanCmd());
        proxy.getCommandManager().register("unban", new UnBanCmd());
        proxy.getCommandManager().register("unmute", new UnMuteCmd());
        proxy.getCommandManager().register("kick", new KickCmd());
        proxy.getCommandManager().register("ban-ip", new BanIpCmd());
        proxy.getCommandManager().register("mute", new MuteCmd());
        proxy.getCommandManager().register("tempmute", new TempMuteCmd());
        proxy.getCommandManager().register("history", new HistoryCmd());

        proxy.getEventManager().register(this, new CPMManager(proxy, logger));
        proxy.getEventManager().register(this, new PlayerListener());

        proxy.getScheduler().buildTask(ReaperSanction.INSTANCE, () ->
                SanctionsManager.INSTANCE.checkForUsersExpiration()).repeat(10, TimeUnit.SECONDS).schedule();

        Main.INSTANCE.sendLogMessage("ReaperSanction is now enabled", 0);
    }

    public Player getPlayer(String name) {
        Optional<Player> player = proxy.getPlayer(name);
        return player.orElse(null);
    }

    public String getPluginVersion() {
        Optional<PluginContainer> plugin = proxy.getPluginManager().getPlugin("reapersanction");
        if (plugin.isPresent()) {
            Optional<PluginDescription> description = Optional.ofNullable(plugin.get().getDescription());
            if (description.isPresent()) {
                return description.get().getVersion().orElse("ERROR");
            }
        }
        return "ERROR";
    }

    public ProxyServer getProxy() {
        return proxy;
    }

    public ArrayList<String> getEveryoneExceptMe(String me) {
        ArrayList<String> everyone = getEveryone();
        everyone.remove(me);
        return everyone;
    }

    public ArrayList<String> getEveryone() {
        ArrayList<String> list = new ArrayList<>();
        for (Player p : proxy.getAllPlayers()) {
            list.add(p.getUsername());
        }
        return list;
    }

    @NotNull
    public CompletableFuture<List<String>> getListCompletableFuture(SimpleCommand.Invocation invocation) {
        return CompletableFuture.supplyAsync(() -> {
            if (invocation.arguments().length == 1) {
                if (invocation.source() instanceof Player) {
                    return ReaperSanction.INSTANCE.getEveryoneExceptMe(((Player) invocation.source()).getUsername());
                }
                return Main.INSTANCE.filterByStart(ReaperSanction.INSTANCE.getEveryone(), invocation.arguments()[invocation.arguments().length - 1]);
            }
            return new ArrayList<>();
        });
    }

    @NotNull
    public CompletableFuture<List<String>> getUsersCompletableFuture(SimpleCommand.Invocation invocation) {
        return CompletableFuture.supplyAsync(() -> {
            if (invocation.arguments().length == 1) {
                return handleSingleArgument(invocation);
            }
            return new ArrayList<>();
        });
    }

    @NotNull
    public CompletableFuture<List<String>> getListMuteCompletableFuture(SimpleCommand.Invocation invocation) {
        return CompletableFuture.supplyAsync(() -> {
            if (invocation.arguments().length == 1) {
                return handleSingleArgument(invocation);
            }
            if (invocation.arguments().length == 2) {
                return Main.INSTANCE.filterByStart(new ArrayList<>(Arrays.asList("10sec", "10min", "10hour", "10day", "10year")),
                        invocation.arguments()[invocation.arguments().length - 1]);
            }
            return new ArrayList<>();
        });
    }

    private List<String> handleSingleArgument(SimpleCommand.Invocation invocation) {
        if (invocation.source() instanceof Player) {
            return ReaperSanction.INSTANCE.getEveryoneExceptMe(((Player) invocation.source()).getUsername());
        }
        return Main.INSTANCE.filterByStart(ReaperSanction.INSTANCE.getEveryone(), invocation.arguments()[invocation.arguments().length - 1]);
    }
}
