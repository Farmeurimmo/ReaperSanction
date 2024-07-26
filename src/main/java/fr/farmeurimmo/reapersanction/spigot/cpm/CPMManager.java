package fr.farmeurimmo.reapersanction.spigot.cpm;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import fr.farmeurimmo.reapersanction.spigot.ReaperSanction;
import org.bukkit.entity.Player;
import org.bukkit.plugin.messaging.PluginMessageListener;

public class CPMManager implements PluginMessageListener {

    public static CPMManager INSTANCE;
    private final String channel = "reaper:sanction";

    public CPMManager() {
        INSTANCE = this;

        ReaperSanction.INSTANCE.getServer().getMessenger().registerOutgoingPluginChannel(ReaperSanction.INSTANCE, channel);
    }

    @Override
    public void onPluginMessageReceived(String s, Player player, byte[] bytes) {

    }

    public void sendPluginMessage(Player player, String channel, String... data) {
        ByteArrayDataOutput out = ByteStreams.newDataOutput();

        out.writeUTF(channel);
        for (String s : data) {
            out.writeUTF(s);
        }

        player.sendPluginMessage(ReaperSanction.INSTANCE, this.channel, out.toByteArray());
    }
}
