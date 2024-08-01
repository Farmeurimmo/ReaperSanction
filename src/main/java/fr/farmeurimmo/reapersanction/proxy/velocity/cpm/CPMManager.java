package fr.farmeurimmo.reapersanction.proxy.velocity.cpm;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import com.velocitypowered.api.command.CommandManager;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.connection.PluginMessageEvent;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;
import com.velocitypowered.api.proxy.ServerConnection;
import com.velocitypowered.api.proxy.messages.MinecraftChannelIdentifier;
import fr.farmeurimmo.reapersanction.core.storage.MessageManager;
import fr.farmeurimmo.reapersanction.core.users.User;
import fr.farmeurimmo.reapersanction.core.users.UsersManager;
import fr.farmeurimmo.reapersanction.proxy.velocity.ReaperSanction;
import fr.farmeurimmo.reapersanction.utils.StrUtils;
import net.kyori.adventure.text.Component;
import org.slf4j.Logger;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.util.Optional;

public class CPMManager {

    public static CPMManager INSTANCE;
    private final Logger logger;

    MinecraftChannelIdentifier queue = MinecraftChannelIdentifier.from("reaper:sanction");

    public CPMManager(ProxyServer server, Logger logger) {
        INSTANCE = this;
        this.logger = logger;

        server.getChannelRegistrar().register(queue);
    }

    @Subscribe
    public void onPluginMessage(PluginMessageEvent e) {
        if (!e.getIdentifier().getId().equals(queue.getId())) {
            return;
        }
        if (!(e.getSource() instanceof ServerConnection)) {
            return;
        }
        handle(((ServerConnection) e.getSource()).getPlayer(), e.getData());
        e.setResult(PluginMessageEvent.ForwardResult.handled());
    }

    public void handle(Player player, byte[] data) {
        DataInputStream in = new DataInputStream(new ByteArrayInputStream(data));
        try {
            String subChannel = in.readUTF();

            String playerName = subChannel.split(";")[1];
            subChannel = subChannel.split(";")[0];

            if (subChannel.equals("getmuteds")) {
                for (User user : UsersManager.INSTANCE.users) {
                    if (user.isMuted()) {
                        sendPluginMessage(player, "nowmuted", user.getUuid().toString());
                    }
                }
                return;
            }

            if (subChannel.equals("openhistorygui")) {
                User user = UsersManager.INSTANCE.getUser(player.getUniqueId());
                if (user == null) return;
                sendPluginMessage(player, "openhistorygui", user.getUserAsString());
                return;
            }

            String msg = in.readUTF();

            String[] args = msg.split(" ");

            if (subChannel.equals("report")) {
                sendMessageReported(player, args[0], StrUtils.fromArgs(args).replace(args[0] + " ", "").trim());
                return;
            }

            CommandManager cmdManager = ReaperSanction.INSTANCE.getProxy().getCommandManager();

            cmdManager.executeAsync(player, subChannel + " " + msg);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void sendMessageReported(Player player, String cible, String ReportReason) {
        player.sendMessage(Component.text(MessageManager.INSTANCE.getMessage("Report-Sended", true)
                .replace("%player%", cible).replace("%reason%", ReportReason)));
        for (Player p : ReaperSanction.INSTANCE.getProxy().getAllPlayers()) {
            if (p.hasPermission("reportview"))
                p.sendMessage(Component.text(MessageManager.INSTANCE.getMessage("Report-Obtain", true)
                        .replace("%player%", cible).replace("%reason%", ReportReason).replace("%sender%", player.getUsername())));
        }
    }

    public void sendPluginMessage(Player player, String channel, String... data) {
        ByteArrayDataOutput out = ByteStreams.newDataOutput();

        out.writeUTF(channel);

        for (String s : data) {
            out.writeUTF(s);
        }

        Optional<ServerConnection> server = player.getCurrentServer();

        server.ifPresent(serverConnection -> serverConnection.sendPluginMessage(queue, out.toByteArray()));
    }
}
