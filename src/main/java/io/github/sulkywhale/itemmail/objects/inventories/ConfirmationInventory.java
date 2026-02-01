package io.github.sulkywhale.itemmail.objects.inventories;

import net.kyori.adventure.text.Component;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class ConfirmationInventory extends ItemViewInventory {

    private final ItemStack item;

    public ConfirmationInventory(Player viewer, Inventory inventory, Component name, OfflinePlayer receiver, OfflinePlayer sender, ItemStack item) {
        super(viewer, inventory, name, receiver, sender);
        this.item = item;
    }

    public ItemStack getItem() {
        return item;
    }
}
