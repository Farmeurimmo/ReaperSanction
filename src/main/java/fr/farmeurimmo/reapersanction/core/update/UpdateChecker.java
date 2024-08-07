package fr.farmeurimmo.reapersanction.core.update;

import fr.farmeurimmo.reapersanction.core.Main;

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
            Main.INSTANCE.sendLogMessage("Auto updater crashed, please go to https://www.spigotmc.org/resources/reapersanction.89580/ and check for updates manually.", 2);
        }
    }

    /*public JSONObject getVersionViaAPI() {
        try {
            HttpURLConnection connection = (HttpURLConnection) new URL("https://api.farmeurimmo.fr/v1/plugins/89580").openConnection();
            connection.setRequestMethod("GET");

            InputStream responseStream = connection.getInputStream();
            Scanner scanner = new Scanner(responseStream);

            String response = scanner.useDelimiter("\\A").next();
            scanner.close();
            responseStream.close();

            return (JSONObject) new JSONParser().parse(response);
        } catch (IOException | ParseException ignored) {
        }
        return new JSONObject();
    }*/

    public void checkForUpdate(String v) {
        //TODO: channel update??

        if (v.contains("ERROR")) {
            Main.INSTANCE.sendLogMessage("§c§lCan't find plugin version to check for update, please check for update manually !", 2);
            return;
        }
        new UpdateChecker(89580).getVersion(version -> {
            boolean latest = isLatest(v, version);
            Main.INSTANCE.sendLogMessage("§aDetected version : §b" + v + "§6, Current production version : §b" + version, 0);
            if (version.equals(v)) {
                Main.INSTANCE.sendLogMessage("§6No update found.", 0);
                return;
            }
            if (version.contains("RC")) {
                if (latest) {
                    Main.INSTANCE.sendLogMessage("§6No update found. §eYou are using a release candidate version, bugs may be present ! §b" + v, 0);
                    return;
                }
                Main.INSTANCE.sendLogMessage("§c§lA Release Candidate version is available, you are currently using a stable version, " +
                        "you can try the new version but bugs may be present !", 2);
                return;
            }
            if (!latest) {
                Main.INSTANCE.sendLogMessage("§cNewest version detected at spigot : §4§l" + version, 1);
                Main.INSTANCE.sendLogMessage("§6Your version : §c" + v, 1);
                Main.INSTANCE.sendLogMessage("§6Download link : §ahttps://farmeurimmo.fr/projects/reapersanction", 1);
                Main.INSTANCE.sendLogMessage("§4§lA new update is available please consider updating if you want to receive support !", 1);
                return;
            }
            Main.INSTANCE.sendLogMessage("§6No major update found.", 0);
        });
    }

    public boolean isLatest(String currentVersion, String target) {
        String[] current = currentVersion.split("\\.");
        String[] targetVersion = target.split("\\.");

        if (current[2].contains("-RC")) { //prevents RC+(number) to be make error while checking for update
            current[2] = current[2].split("-RC")[0];
        }

        for (int i = 0; i < current.length; i++) {
            int c = Integer.parseInt(current[i]);
            int t = Integer.parseInt(targetVersion[i]);
            if (c < t) return false;
            if (c > t) return true;
        }
        return true;
    }
}
