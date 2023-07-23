package fr.farmeurimmo.reapersanction;

import fr.farmeurimmo.reapersanction.api.Main;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Scanner;
import java.util.function.Consumer;

public class UpdateChecker {

    private final int resourceId;

    public UpdateChecker(int resourceId) {
        this.resourceId = resourceId;
    }

    public void getVersion(final Consumer<String> consumer) {
        try (InputStream inputStream = new URL("https://api.spigotmc.org/legacy/update.php?resource=" + this.resourceId).openStream(); Scanner scanner = new Scanner(inputStream)) {
            if (scanner.hasNext()) {
                consumer.accept(scanner.next());
            }
        } catch (IOException exception) {
            System.out.println("Auto updater crashed, please go to https://reaper.farmeurimmo.fr/reapersanction and check for updates manually.");
        }
    }

    public void checkForUpdate(String v, Main console) {
        if (v.contains("ERROR")) {
            console.sendLogMessage("§c§lCan't find plugin version to check for update, please check for update manually !", 2);
            return;
        }
        new UpdateChecker(89580).getVersion(version -> {
            if (v.equals(version)) {
                console.sendLogMessage("§6No update found.", 0);
            } else {
                if (version.contains("RC")) {
                    console.sendLogMessage("§c§lA new Release candidate update is available, you can try it but it may contains bugs and " +
                            "breaking changes so be careful !", 0);
                } else {
                    console.sendLogMessage("A new update is available please consider updating if you want to receive support !", 1);
                    console.sendLogMessage("§cNewest version detected at spigot : §4§l" + version, 1);
                    console.sendLogMessage("§6Your version : §c" + v, 1);
                    console.sendLogMessage("§6Download link : §ahttps://reaper.farmeurimmo.fr/reapersanction/", 1);
                    console.sendLogMessage("§4§lA new update is available please consider updating if you want to receive support !" +
                            " (the spigot api is taking time to update the version)", 1);
                }
            }
        });
    }
}
