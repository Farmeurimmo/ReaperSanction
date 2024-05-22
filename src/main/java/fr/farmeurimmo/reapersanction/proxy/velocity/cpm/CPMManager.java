package fr.farmeurimmo.reapersanction.proxy.velocity.cpm;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;
import com.velocitypowered.api.proxy.ServerConnection;
import com.velocitypowered.api.proxy.messages.MinecraftChannelIdentifier;
import org.slf4j.Logger;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.util.Optional;

public class CPMManager {

    public static CPMManager INSTANCE;
    private final Logger logger;

    MinecraftChannelIdentifier queue = MinecraftChannelIdentifier.from("reapersanction:main");

    public CPMManager(ProxyServer server, Logger logger) {
        INSTANCE = this;
        this.logger = logger;

        server.getChannelRegistrar().register(queue);
    }

    @Subscribe
    public void onPluginMessage(com.velocitypowered.api.event.connection.PluginMessageEvent e) {
        if (!e.getIdentifier().getId().equals(queue.getId())) {
            return;
        }
        if (!(e.getSource() instanceof ServerConnection)) {
            return;
        }
        handle(((ServerConnection) e.getSource()).getPlayer(), e.getData());
    }

    public void handle(Player player, byte[] data) {
        DataInputStream in = new DataInputStream(new ByteArrayInputStream(data));
        try {
            String subchannel = in.readUTF();
            String msg = in.readUTF();
            //logger.info("Received plugin message from " + player.getUsername() + " on subchannel " + subchannel + " with message " + msg);
            String[] args = msg.split(" ");
        } catch (Exception e) {
            e.printStackTrace();
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
