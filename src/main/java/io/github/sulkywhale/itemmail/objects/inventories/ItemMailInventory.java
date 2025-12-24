package io.github.sulkywhale.itemmail.objects.inventories;

import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.jspecify.annotations.NonNull;

public class ItemMailInventory implements InventoryHolder {

    private final Inventory inventory;

    public ItemMailInventory(Player player, Inventory inventory, Component name) {
        this.inventory = Bukkit.createInventory(this, 54, name);
        this.inventory.setContents(inventory.getContents());
        player.openInventory(this.inventory);
    }

    @Override
    public @NonNull Inventory getInventory() {
        return this.inventory;
    }
}
