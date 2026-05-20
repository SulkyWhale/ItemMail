package io.github.sulkywhale.itemmail.config;

import org.bukkit.configuration.file.FileConfiguration;

import java.util.List;

public class Config {

    private static FileConfiguration config;

    public static void init(FileConfiguration config) {
        Config.config = config;
    }

    public static List<String> getItemBlacklist() {
        return config.getStringList("itemmail.blacklist");
    }

    public static boolean isUsingEconomy() {
        return config.getBoolean("itemmail.economy.enabled");
    }

    public static double getMailCost() {
        return config.getDouble("itemmail.economy.mail_cost");
    }

    public static String getDatabaseType() {
        return config.getString("itemmail.database.type");
    }

    public static String getDatabaseHost() {
        return config.getString("itemmail.database.host");
    }

    public static String getDatabasePort() {
        return config.getString("itemmail.database.port");
    }

    public static String getDatabaseName() {
        return config.getString("itemmail.database.database_name");
    }

    public static String getDatabaseTablePrefix() {
        return config.getString("itemmail.database.table_prefix");
    }

    public static String getDatabaseUsername() {
        return config.getString("itemmail.database.username");
    }

    public static String getDatabasePassword() {
        return config.getString("itemmail.database.password");
    }
}
