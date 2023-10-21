package fr.farmeurimmo.reapersanction.spigot.cmd;

import fr.farmeurimmo.reapersanction.core.storage.MessageManager;
import fr.farmeurimmo.reapersanction.core.storage.SettingsManager;
import fr.farmeurimmo.reapersanction.spigot.ReaperSanction;
import fr.farmeurimmo.reapersanction.utils.Parser;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class VanishCmd implements CommandExecutor, TabCompleter {

    //TODO: recode the vanish

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] arg3) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(MessageManager.INSTANCE.getMessage("NotAvailableInConsole", true));
            return true;
        }
        if (!Parser.PARSE_BOOLEAN(SettingsManager.INSTANCE.getSetting("vanish.status"))) {
            sender.sendMessage(MessageManager.INSTANCE.getMessage("Command-Disabled", true));
            return true;
        }
        Player p = (Player) sender;
        if (ReaperSanction.VANISHED.contains(p)) {
            for (Player players : Bukkit.getOnlinePlayers()) {
                players.showPlayer(p);
                p.removePotionEffect(PotionEffectType.INVISIBILITY);
                if (p.getGameMode() == GameMode.ADVENTURE) p.setAllowFlight(false);
                if (p.getGameMode() == GameMode.SURVIVAL) p.setAllowFlight(false);
                if (Parser.PARSE_BOOLEAN(SettingsManager.INSTANCE.getSetting("vanish.exitGamemode"))) {
                    if (Parser.PARSE_INT(SettingsManager.INSTANCE.getSetting("vanish.exitGamemodeType")) == 1) {
                        p.setGameMode(GameMode.CREATIVE);
                        p.setAllowFlight(true);
                        p.setFlying(true);
                    }
                    if (Parser.PARSE_INT(SettingsManager.INSTANCE.getSetting("vanish.exitGamemodeType")) == 2)
                        p.setGameMode(GameMode.ADVENTURE);
                    if (Parser.PARSE_INT(SettingsManager.INSTANCE.getSetting("vanish.exitGamemodeType")) == 3) {
                        p.setGameMode(GameMode.SPECTATOR);
                        p.setAllowFlight(true);
                        p.setFlying(true);
                    }
                    if (Parser.PARSE_INT(SettingsManager.INSTANCE.getSetting("vanish.exitGamemodeType")) == 0)
                        p.setGameMode(GameMode.SURVIVAL);
                }
            }
            ReaperSanction.VANISHED.remove(p);
            p.sendMessage(MessageManager.INSTANCE.getMessage("Vanish-Isoff", false));
            return true;
        }
        for (Player players : Bukkit.getOnlinePlayers()) {
            players.hidePlayer(p);
            p.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 1000000000, 1));
        }
        ReaperSanction.VANISHED.add(p);
        if (Parser.PARSE_BOOLEAN(SettingsManager.INSTANCE.getSetting("vanish.changeGamemode"))) {
            if (Parser.PARSE_INT(SettingsManager.INSTANCE.getSetting("vanish.gamemode")) == 1)
                p.setGameMode(GameMode.CREATIVE);
            if (Parser.PARSE_INT(SettingsManager.INSTANCE.getSetting("vanish.gamemode")) == 2)
                p.setGameMode(GameMode.ADVENTURE);
            if (Parser.PARSE_INT(SettingsManager.INSTANCE.getSetting("vanish.gamemode")) == 3)
                p.setGameMode(GameMode.SPECTATOR);
            if (Parser.PARSE_INT(SettingsManager.INSTANCE.getSetting("vanish.gamemode")) == 0)
                p.setGameMode(GameMode.SURVIVAL);
        }
        if (Parser.PARSE_BOOLEAN(SettingsManager.INSTANCE.getSetting("vanish.fly"))) {
            if (!p.getAllowFlight()) {
                p.setAllowFlight(true);
                p.setFlying(true);
            }
        }
        p.sendMessage(MessageManager.INSTANCE.getMessage("Vanish-Ison", false));
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String commandLabel, String[] args) {
        ArrayList<String> subcmd = new ArrayList<>();
        if (cmd.getName().equalsIgnoreCase("vanish")) {
            subcmd.add("");
            Collections.sort(subcmd);
        }
        return subcmd;
    }
}
