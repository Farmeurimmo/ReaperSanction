package fr.farmeurimmo.reapersanction.spigot.cpm;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import fr.farmeurimmo.reapersanction.spigot.ReaperSanction;
import org.bukkit.entity.Player;
import org.bukkit.plugin.messaging.PluginMessageListener;

public class CPMManager implements PluginMessageListener {

    public static CPMManager INSTANCE;
    private final String channel = "reapersanction:main";

    public CPMManager() {
        INSTANCE = this;

        ReaperSanction.INSTANCE.getServer().getMessenger().registerOutgoingPluginChannel(ReaperSanction.INSTANCE, channel);
    }

    @Override
    public void onPluginMessageReceived(String s, Player player, byte[] bytes) {

    }

    public void sendMessage(Player player, String subchannel, String data) {

        ByteArrayDataOutput out = ByteStreams.newDataOutput();
        out.writeUTF(subchannel);
        out.writeUTF(data);

        if (!ReaperSanction.INSTANCE.getServer().getMessenger().isOutgoingChannelRegistered(ReaperSanction.INSTANCE, channel)) {
            ReaperSanction.INSTANCE.getServer().getMessenger().registerOutgoingPluginChannel(ReaperSanction.INSTANCE, channel);
        }
        player.sendPluginMessage(ReaperSanction.INSTANCE, channel, out.toByteArray());
    }
}
