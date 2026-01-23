package io.github.sulkywhale.itemmail.utils;

import io.github.sulkywhale.itemmail.objects.inventories.ItemViewInventory;
import io.github.sulkywhale.itemmail.objects.Mail;
import io.github.sulkywhale.itemmail.objects.inventories.AdminMailInventory;
import io.github.sulkywhale.itemmail.MailManager;
import io.github.sulkywhale.itemmail.objects.inventories.MailInventory;
import io.papermc.paper.datacomponent.DataComponentTypes;
import io.papermc.paper.datacomponent.item.TooltipDisplay;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.sound.Sound;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.List;

public class GUIUtil {

    public static void openMailGUI(Player player) {
        List<Mail> mails = MailManager.getMail(player.getUniqueId());
        if (mails.isEmpty()) {
            player.sendMessage(Component.text("You do not have any mail.", NamedTextColor.RED));
            return;
        }

        Inventory inventory = createInventory();
        for (Mail mail : mails) {
            ItemStack skull = new ItemStack(Material.PLAYER_HEAD);
            SkullMeta meta = (SkullMeta) skull.getItemMeta();

            OfflinePlayer sender = Bukkit.getOfflinePlayer(mail.sender());
            meta.setOwningPlayer(sender);
            meta.displayName(Component.text(sender.getName(), NamedTextColor.GOLD).decoration(TextDecoration.ITALIC, false));

            skull.setItemMeta(meta);

            TooltipDisplay tooltipDisplay = TooltipDisplay.tooltipDisplay().addHiddenComponents(DataComponentTypes.PROFILE).build();
            skull.setData(DataComponentTypes.TOOLTIP_DISPLAY, tooltipDisplay);

            inventory.addItem(skull);
        }

        new MailInventory(player, inventory, Component.text("Item Mail"));
    }

    public static void openAdminGUI(Player admin, OfflinePlayer receiver) {
        List<Mail> mails = MailManager.getMail(receiver.getUniqueId());
        if (mails.isEmpty()) {
            admin.sendMessage(Component.text(receiver.getName() + " does not have any mail.", NamedTextColor.RED));
            return;
        }

        Inventory inventory = createInventory();
        for (Mail mail : mails) {
            ItemStack skull = new ItemStack(Material.PLAYER_HEAD);
            SkullMeta meta = (SkullMeta) skull.getItemMeta();

            OfflinePlayer sender = Bukkit.getOfflinePlayer(mail.sender());
            meta.setOwningPlayer(sender);
            meta.displayName(Component.text(sender.getName(), NamedTextColor.GOLD).decoration(TextDecoration.ITALIC, false));

            skull.setItemMeta(meta);

            TooltipDisplay tooltipDisplay = TooltipDisplay.tooltipDisplay().addHiddenComponents(DataComponentTypes.PROFILE).build();
            skull.setData(DataComponentTypes.TOOLTIP_DISPLAY, tooltipDisplay);

            inventory.addItem(skull);
        }

        new AdminMailInventory(admin, inventory, Component.text("Inspecting mail of " + receiver.getName()), receiver);
    }

    public static void openItemViewInventory(Player viewer, OfflinePlayer target, OfflinePlayer receiver) {
        List<Mail> mails = MailManager.getMail(receiver.getUniqueId());
        if (mails.isEmpty()) {
            viewer.sendMessage(Component.text(target.getName() + " does not have any mail.", NamedTextColor.RED));
            return;
        }

        Inventory inventory = createInventory();
        mails.stream()
                .filter(mail -> mail.sender().equals(target.getUniqueId()))
                .forEach(mail -> inventory.addItem(mail.itemStack()));

        ItemStack backArrow = new ItemStack(Material.ARROW);
        backArrow.editMeta(meta -> meta.displayName(Component.text("Back").decoration(TextDecoration.ITALIC, false)));
        inventory.setItem(45, backArrow);

        new ItemViewInventory(viewer, inventory, Component.text("Inspecting mail from " + target.getName()), receiver);
    }

    public static void playClickSound(Player player) {
        player.playSound(Sound.sound(Key.key("block.stone_button.click_on"), Sound.Source.PLAYER, 1.0f, 1.0f));
    }

    private static Inventory createInventory() {
        return Bukkit.createInventory(null, 54);
    }
}
