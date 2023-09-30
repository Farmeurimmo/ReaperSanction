package fr.farmeurimmo.reapersanction.proxy.velocity;

import com.google.inject.Inject;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.plugin.PluginContainer;
import com.velocitypowered.api.plugin.PluginDescription;
import com.velocitypowered.api.proxy.ProxyServer;
import fr.farmeurimmo.reapersanction.UpdateChecker;
import fr.farmeurimmo.reapersanction.api.Main;
import fr.farmeurimmo.reapersanction.proxy.velocity.cmd.BanCmd;
import org.slf4j.Logger;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;

@Plugin(
        id = "reapersanction",
        name = "ReaperSanction",
        description = "ReaperSanction velocity plugin",
        url = "https://reaper.farmeurimmo.fr",
        authors = "Farmeurimmo"
)
public class ReaperSanction {

    public static ReaperSanction INSTANCE;
    private final ProxyServer proxy;
    private final Logger logger;
    private Main main;

    @Inject
    public ReaperSanction(ProxyServer proxy, Logger logger) {
        INSTANCE = this;

        this.proxy = proxy;
        this.logger = logger;
    }

    @Subscribe
    public void onProxyInitialization(ProxyInitializeEvent e) {
        main = new Main(logger, null, 1);

        Main.INSTANCE.sendLogMessage("ReaperSanction is now enabled", 0);

        proxy.getCommandManager().register("ban", new BanCmd());

        CompletableFuture.runAsync(() -> new UpdateChecker(89580).checkForUpdate(getPluginVersion(), main));
    }

    private String getPluginVersion() {
        Optional<PluginContainer> plugin = proxy.getPluginManager().getPlugin("reapersanction");
        if (plugin.isPresent()) {
            Optional<PluginDescription> description = Optional.ofNullable(plugin.get().getDescription());
            if (description.isPresent()) {
                return description.get().getVersion().orElse("ERROR");
            }
        }
        return "ERROR";
    }
}
