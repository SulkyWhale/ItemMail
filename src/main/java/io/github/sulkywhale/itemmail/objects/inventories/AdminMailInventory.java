package io.github.sulkywhale.itemmail.objects.inventories;

import net.kyori.adventure.text.Component;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

public class AdminMailInventory extends ItemMailInventory {

    private final OfflinePlayer receiver;

    public AdminMailInventory(Player player, Inventory inventory, Component name, OfflinePlayer receiver) {
        super(player, inventory, name);
        this.receiver = receiver;
    }

    public OfflinePlayer getReceiver() {
        return receiver;
    }
}
