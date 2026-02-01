package io.github.sulkywhale.itemmail.objects.inventories;

import net.kyori.adventure.text.Component;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

public class ItemViewInventory extends AdminMailInventory {

    private final OfflinePlayer sender;

    public ItemViewInventory(Player viewer, Inventory inventory, Component name, OfflinePlayer receiver, OfflinePlayer sender) {
        super(viewer, inventory, name, receiver);
        this.sender = sender;
    }

    public OfflinePlayer getSender() {
        return sender;
    }
}
