package fr.farmeurimmo.reapersanction.api.storage;

import java.util.HashMap;
import java.util.Map;

public class DBCredentialsManager {

    public static DBCredentialsManager INSTANCE;
    private final String method;
    private final String host;
    private final String port;
    private final String username;
    private final String password;

    public DBCredentialsManager() throws Exception {
        INSTANCE = this;

        FilesManager.INSTANCE.getDbCredentials().putAll(getDefaultDbCredentials());

        try {
            method = FilesManager.INSTANCE.getDbCredentials().get("method").toString();

            Map<String, Object> credentials = (Map<String, Object>) FilesManager.INSTANCE.getDbCredentials().get("mysql");
            host = credentials.get("host").toString();
            port = credentials.get("port").toString();
            username = credentials.get("username").toString();
            password = credentials.get("password").toString();

            FilesManager.INSTANCE.getDbCredentials().putAll(getMapOfCredentials());

            FilesManager.INSTANCE.saveDbCredentials();
        } catch (Exception e) {
            throw new Exception("Unable to load database credentials : " + e.getMessage());
        }
    }

    public Map<String, Object> getMapOfCredentials() {
        Map<String, Object> toReturn = new HashMap<>();

        toReturn.put("method", method);

        Map<String, Object> mysql = new HashMap<>();
        mysql.put("host", host);
        mysql.put("port", port);
        mysql.put("username", username);
        mysql.put("password", password);

        toReturn.put("mysql", mysql);

        return toReturn;
    }

    public Map<String, Object> getDefaultDbCredentials() {
        Map<String, Object> toReturn = new HashMap<>();

        toReturn.put("method", "YAML");

        Map<String, Object> mysql = new HashMap<>();
        mysql.put("host", "localhost");
        mysql.put("port", "3306");
        mysql.put("username", "root");
        mysql.put("password", "password");

        toReturn.put("mysql", mysql);

        return toReturn;
    }

    public String getHost() {
        return host;
    }

    public String getPort() {
        return port;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getMethod() {
        return method;
    }

    public boolean isMySQL() {
        return method.equalsIgnoreCase("mysql");
    }
}
