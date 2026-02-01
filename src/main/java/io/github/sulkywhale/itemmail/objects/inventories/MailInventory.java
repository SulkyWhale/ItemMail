package io.github.sulkywhale.itemmail.objects.inventories;

import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

public class MailInventory extends ItemMailInventory {

    public MailInventory(Player viewer, Inventory inventory, Component name) {
        super(viewer, inventory, name);
    }
}
