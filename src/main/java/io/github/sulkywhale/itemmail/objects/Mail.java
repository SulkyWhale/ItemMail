package io.github.sulkywhale.itemmail.objects;

import org.bukkit.inventory.ItemStack;

import java.util.UUID;

public record Mail(UUID sender, ItemStack itemStack) {

}
