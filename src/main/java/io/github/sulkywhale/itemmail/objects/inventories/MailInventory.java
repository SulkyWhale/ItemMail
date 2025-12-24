package io.github.sulkywhale.itemmail.objects.inventories;

import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

public class MailInventory extends ItemMailInventory {

    public MailInventory(Player player, Inventory inventory, Component name) {
        super(player, inventory, name);
    }
}
