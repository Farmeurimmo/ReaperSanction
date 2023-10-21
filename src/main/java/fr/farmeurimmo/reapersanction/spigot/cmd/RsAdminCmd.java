package fr.farmeurimmo.reapersanction.spigot.cmd;

import fr.farmeurimmo.reapersanction.core.Main;
import fr.farmeurimmo.reapersanction.core.storage.DatabaseManager;
import fr.farmeurimmo.reapersanction.core.storage.FilesManager;
import fr.farmeurimmo.reapersanction.core.storage.LocalStorageManager;
import fr.farmeurimmo.reapersanction.core.storage.MessageManager;
import fr.farmeurimmo.reapersanction.spigot.ReaperSanction;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class RsAdminCmd implements CommandExecutor, TabCompleter {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (args.length != 1) {
            sender.sendMessage(MessageManager.INSTANCE.getMessage("ErrorArgAdminCommands", true));
            sender.sendMessage("Subs commands available: infos, reload, rl");
            return true;
        }
        if (args[0].equalsIgnoreCase("infos")) {
            sender.sendMessage("Plugin developper : Farmeurimmo");
            sender.sendMessage("Email : contact@farmeurimmo.fr");
            sender.sendMessage("Website : https://reaper.farmeurimmo.fr/reapersanction");
            sender.sendMessage("Version : " + ReaperSanction.INSTANCE.getDescription().getVersion());
            return true;
        }
        if (!sender.hasPermission("reapersanction.rsadmin")) {
            sender.sendMessage(MessageManager.INSTANCE.getMessage("NoPermission", true));
            return true;
        }
        if (args[0].equalsIgnoreCase("reload") || args[0].equalsIgnoreCase("rl")) {
            Main.INSTANCE.reload();
            sender.sendMessage(MessageManager.INSTANCE.getMessage("ReloadMessage", true));
            return true;
        }
        if (args[0].equalsIgnoreCase("migratedb")) {
            if (!Main.INSTANCE.matchRequirementsToMigrateToMYSQL()) {
                sender.sendMessage("§l§cIf you want to upgrade to MYSQL and you have already sanctions on players, you can, just follow those steps !\n" +
                        "§cPlease migrate your db when the server is empty, and make a backup of your old sanctions.yml to prevent data loss if the server crash !\n" +
                        "§l§c1. §7Stop your server\n" +
                        "§l§c2. §7Reset your config.yml or just add the missing part\n" +
                        "§7(you can check the default one here (https://github.com/Reaper-Solutions/Minecraft-ReaperSanction/blob/main/src/main/resources/config.yml)\n" +
                        "§l§c3. §7Place your credentials in the config.yml an change YAML to MYSQL\n" +
                        "§l§c4. §7Start your server\n" +
                        "§l§c5. §7Type this command again");
                return true;
            }
            long start = System.currentTimeMillis();
            sender.sendMessage("§cStarting migration, can take a while depending on how much users you have.");
            CompletableFuture.runAsync(() -> {
                sender.sendMessage("§7Migrating users... (ASYNC)");
                FilesManager.INSTANCE.setupSanctions();
                LocalStorageManager.INSTANCE.loadUsers();
                DatabaseManager.INSTANCE.updateAllUsersFromMigration();
                FilesManager.INSTANCE.deleteAndRecreateSanctionFile();
                sender.sendMessage("§aMigration done ! (took " + (System.currentTimeMillis() - start) + "ms)");
            });
            return true;
        }
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String commandLabel, String[] args) {
        ArrayList<String> subcmd = new ArrayList<>();
        if (args.length == 1) {
            subcmd.add("reload");
            subcmd.add("rl");
            subcmd.add("infos");
            subcmd.add("migratedb");
        } else if (args.length >= 2) {
            subcmd.add("");
        }
        Collections.sort(subcmd);
        return subcmd;
    }
}
