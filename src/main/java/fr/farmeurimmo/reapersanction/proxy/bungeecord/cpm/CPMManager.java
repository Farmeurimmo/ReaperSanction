package fr.farmeurimmo.reapersanction.proxy.bungeecord.cpm;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import fr.farmeurimmo.reapersanction.core.storage.MessageManager;
import fr.farmeurimmo.reapersanction.proxy.bungeecord.ReaperSanction;
import fr.farmeurimmo.reapersanction.utils.StrUtils;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.PluginMessageEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;

public class CPMManager implements Listener {

    public static final String CHANNEL = "reaper:sanction";
    public static CPMManager INSTANCE;

    public CPMManager() {
        INSTANCE = this;

        ReaperSanction.INSTANCE.getProxy().registerChannel(CHANNEL);
    }

    @EventHandler
    public void onPluginMessage(PluginMessageEvent e) {
        if (!e.getTag().equals(CHANNEL)) return;

        if (!(e.getReceiver() instanceof ProxiedPlayer)) return;

        DataInputStream in = new DataInputStream(new ByteArrayInputStream(e.getData()));

        try {
            String subChannel = in.readUTF();
            String playerName = subChannel.split(";")[1];
            subChannel = subChannel.split(";")[0];

            ProxiedPlayer player = ReaperSanction.INSTANCE.getPlayer(playerName);
            if (player == null) {
                return;
            }

            String msg = in.readUTF();
            String[] args = msg.split(" ");

            if (subChannel.equals("report")) {
                sendMessageReported(player, args[0], StrUtils.fromArgs(args).replace(args[0] + " ", "").trim());
                return;
            }

            //FIXME: not like velocity we can't force the player to execute a command
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void sendPluginMessage(ProxiedPlayer player, String subChannel, String message) {
        ByteArrayDataOutput out = ByteStreams.newDataOutput();

        out.writeUTF(subChannel);
        out.writeUTF(message);

        player.getServer().sendData(CHANNEL, out.toByteArray());
    }

    public void sendMessageReported(ProxiedPlayer player, String cible, String ReportReason) {
        player.sendMessage(new TextComponent(MessageManager.INSTANCE.getMessage("Report-Sended", true)
                .replace("%player%", cible).replace("%reason%", ReportReason)));
        for (ProxiedPlayer p : ReaperSanction.INSTANCE.getProxy().getPlayers()) {
            if (p.hasPermission("reportview"))
                p.sendMessage(new TextComponent(MessageManager.INSTANCE.getMessage("Report-Obtain", true)
                        .replace("%player%", cible).replace("%reason%", ReportReason).replace("%sender%", player.getName())));
        }
    }
}
