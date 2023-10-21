package fr.farmeurimmo.reapersanction.proxy.velocity;

import com.google.inject.Inject;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.player.KickedFromServerEvent;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.plugin.PluginContainer;
import com.velocitypowered.api.plugin.PluginDescription;
import com.velocitypowered.api.plugin.annotation.DataDirectory;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;
import fr.farmeurimmo.reapersanction.core.Main;
import fr.farmeurimmo.reapersanction.proxy.velocity.cmd.BanCmd;
import net.kyori.adventure.text.Component;
import org.slf4j.Logger;

import java.io.File;
import java.nio.file.Path;
import java.util.Optional;

@Plugin(
        id = "reapersanction",
        name = "ReaperSanction",
        description = "ReaperSanction velocity plugin",
        url = "https://reaper.farmeurimmo.fr",
        authors = "Farmeurimmo",
        version = "1.6.0-RC1"
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

        Main.INSTANCE.sendLogMessage("ReaperSanction is now enabled", 0);

        proxy.getCommandManager().register("ban", new BanCmd());
    }

    @Subscribe
    public void playerGotKick(KickedFromServerEvent e) {
        System.out.println("KICKED");
        Player p = e.getPlayer();
        if (!e.getServerKickReason().isPresent()) return;
        System.out.println("KICKED2");
        Component reason = e.getServerKickReason().get();
        String reasonText = String.valueOf(reason);
        System.out.println(reasonText);
        if (reasonText.contains("RS:IMP:")) {
            System.out.println("KICKED3");
            e.setResult(KickedFromServerEvent.DisconnectPlayer.create(
                    Component.text("§cYou have been kicked from the server for the following reason: §4" + reason)));
        }
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
}
