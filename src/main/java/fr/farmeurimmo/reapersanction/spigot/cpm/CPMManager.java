package fr.farmeurimmo.reapersanction.spigot.cpm;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import fr.farmeurimmo.reapersanction.core.Main;
import fr.farmeurimmo.reapersanction.core.users.User;
import fr.farmeurimmo.reapersanction.core.users.UsersManager;
import fr.farmeurimmo.reapersanction.spigot.ReaperSanction;
import fr.farmeurimmo.reapersanction.spigot.gui.CustomInventories;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.messaging.PluginMessageListener;

import java.util.UUID;

public class CPMManager implements PluginMessageListener {

    public static CPMManager INSTANCE;
    private final String channel = "reaper:sanction";

    public CPMManager() {
        INSTANCE = this;

        ReaperSanction.INSTANCE.getServer().getMessenger().registerOutgoingPluginChannel(ReaperSanction.INSTANCE, channel);
    }

    @Override
    public void onPluginMessageReceived(String s, Player player, byte[] bytes) {
        String data = new String(bytes).replace(channel, "");
        String subChannel = data.split("\\$")[0].trim();

        if (subChannel.startsWith("openhistorygui")) {
            data = subChannel.replace("openhistorygui", "").replace("�", "");

            try {
                User user = User.getUserFromString(data.substring(data.indexOf("'") + 1, data.lastIndexOf("'") - 1));

                UsersManager.INSTANCE.replaceUser(user);

                CustomInventories.INSTANCE.startInventoryOpenOfHistoryGui(player, user, 0);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return;
        }
        if (subChannel.equals("nowmuted")) {
            String uuid = data.split("\\$")[1];
            try {
                Main.INSTANCE.mutedPlayers.add(UUID.fromString(uuid));
            } catch (Exception e) {
                e.printStackTrace();
            }
            return;
        }
        if (subChannel.equals("unmuted")) {
            String uuid = data.split("\\$")[1];
            try {
                Main.INSTANCE.mutedPlayers.remove(UUID.fromString(uuid));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void requestForMuteds() {
        try {
            sendPluginMessage(ReaperSanction.INSTANCE.getServer().getOnlinePlayers().iterator().next(), "getmuteds");
        } catch (Exception e) {
            Bukkit.getScheduler().runTaskLater(ReaperSanction.INSTANCE, this::requestForMuteds, 20L);
        }
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
