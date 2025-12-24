package io.github.sulkywhale.itemmail;

import io.github.sulkywhale.itemmail.commands.ItemMailCommand;
import io.github.sulkywhale.itemmail.data.DataSource;
import io.github.sulkywhale.itemmail.listeners.InventoryListener;
import io.github.sulkywhale.itemmail.listeners.PlayerListener;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;

public class ItemMail extends JavaPlugin {

    private static ItemMail plugin;

    public ItemMail() {
        plugin = this;
    }

    public static ItemMail getPlugin() {
        return plugin;
    }

    @Override
    public void onEnable() {
        getCommand("itemmail").setExecutor(new ItemMailCommand());
        getCommand("itemmail").setTabCompleter(new ItemMailCommand());
        getServer().getPluginManager().registerEvents(new InventoryListener(), this);
        getServer().getPluginManager().registerEvents(new PlayerListener(), this);

        DataSource.init(this);

        getLogger().info("Plugin enabled.");
    }

    @Override
    public void onDisable() {
        try {
            DataSource.saveData();
        } catch (IOException e) {
            getLogger().severe("Failed to save data: " + e.getMessage());
        }
        getLogger().info("Plugin disabled.");
    }
}
