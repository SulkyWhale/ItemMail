package io.github.sulkywhale.itemmail.objects.inventories;

import net.kyori.adventure.text.Component;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

public class ItemViewInventory extends AdminMailInventory {

    public ItemViewInventory(Player player, Inventory inventory, Component name, OfflinePlayer target) {
        super(player, inventory, name, target);
    }
}
