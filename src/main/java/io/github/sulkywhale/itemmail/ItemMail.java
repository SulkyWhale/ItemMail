package io.github.sulkywhale.itemmail;

import io.github.sulkywhale.itemmail.commands.ItemMailCommand;
import io.github.sulkywhale.itemmail.config.Config;
import io.github.sulkywhale.itemmail.data.DataSource;
import io.github.sulkywhale.itemmail.data.FlatFileSource;
import io.github.sulkywhale.itemmail.data.SQLSource;
import io.github.sulkywhale.itemmail.hooks.VaultHook;
import io.github.sulkywhale.itemmail.listeners.InventoryListener;
import io.github.sulkywhale.itemmail.listeners.PlayerListener;
import io.github.sulkywhale.itemmail.objects.exceptions.DatabaseInitException;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.plugin.java.JavaPlugin;

public class ItemMail extends JavaPlugin {

    private static ItemMail plugin;
    private DataSource dataSource;

    public ItemMail() {
        plugin = this;
    }

    public static ItemMail getPlugin() {
        return plugin;
    }

    @Override
    public void onEnable() {
        saveDefaultConfig();
        Config.init(getConfig());
        try {
            initDataSource();
        } catch (DatabaseInitException e) {
            getLogger().severe("Failed to load the data source: " + e);
            getServer().getPluginManager().disablePlugin(this);
        }
        dataSource.loadData();

        getCommand("itemmail").setExecutor(new ItemMailCommand());
        getCommand("itemmail").setTabCompleter(new ItemMailCommand());
        getServer().getPluginManager().registerEvents(new InventoryListener(), this);
        getServer().getPluginManager().registerEvents(new PlayerListener(), this);

        hookEconomy();

        getLogger().info("Plugin enabled.");
    }

    @Override
    public void onDisable() {
        dataSource.saveData();
        getLogger().info("Plugin disabled.");
    }

    private void initDataSource() throws DatabaseInitException {
        dataSource = switch (Config.getDatabaseType().toLowerCase()) {
            case "flatfile" -> new FlatFileSource(this);
            case "mysql" -> new SQLSource(this);
            default -> throw new DatabaseInitException("Invalid database type in config.");
        };
    }

    public void reloadPlugin() throws DatabaseInitException {
        dataSource.saveData();
        reloadConfig();
        Config.init(getConfig());
        initDataSource();
        hookEconomy();
    }

    private void hookEconomy() {
        if (Config.isUsingEconomy() && getServer().getPluginManager().getPlugin("Vault") != null) {
            Economy economy = getServer().getServicesManager().load(Economy.class);
            if (economy != null) {
                VaultHook.setEconomy(economy);
                getLogger().info("Successfully hooked with Vault");
            }
        }
    }
}
