package io.github.sulkywhale.itemmail.utils;

import io.github.sulkywhale.itemmail.MailManager;
import io.github.sulkywhale.itemmail.objects.Mail;
import io.github.sulkywhale.itemmail.objects.inventories.AdminMailInventory;
import io.github.sulkywhale.itemmail.objects.inventories.ConfirmationInventory;
import io.github.sulkywhale.itemmail.objects.inventories.ItemViewInventory;
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
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class GUIUtil {

    public static void openMailGUI(Player player, boolean notify) {
        List<Mail> mails = MailManager.getInstance().getMail(player.getUniqueId());
        Inventory inventory = attemptToOpenMailInventory(player, player, mails, notify);
        if (inventory == null)
            return;

        populateMailInventory(mails, inventory);
        new MailInventory(player, inventory, Component.text("Item Mail"));
    }

    public static void openAdminGUI(Player admin, OfflinePlayer receiver, boolean notify) {
        List<Mail> mails = MailManager.getInstance().getMail(receiver.getUniqueId());
        Inventory inventory = attemptToOpenMailInventory(admin, receiver, mails, notify);
        if (inventory == null)
            return;

        populateMailInventory(mails, inventory);
        new AdminMailInventory(admin, inventory, Component.text("Inspecting mail of " + receiver.getName()), receiver);
    }

    public static void openItemViewInventory(Player viewer, OfflinePlayer sender, OfflinePlayer receiver) {
        List<Mail> mails = MailManager.getInstance().getMail(receiver.getUniqueId());
        Inventory inventory = attemptToOpenMailInventory(viewer, receiver, mails, false);
        if (inventory == null)
            return;

        mails.stream()
                .filter(mail -> mail.sender().equals(sender.getUniqueId()))
                .forEach(mail -> inventory.addItem(mail.itemStack()));

        ItemStack backArrow = ItemStack.of(Material.ARROW);
        backArrow.editMeta(meta -> meta.customName(Component.text("Back").decoration(TextDecoration.ITALIC, false)));
        inventory.setItem(45, backArrow);

        new ItemViewInventory(viewer, inventory, Component.text("Inspecting mail from " + sender.getName()), receiver, sender);
    }

    public static void openConfirmationInventory(Player viewer, OfflinePlayer receiver, OfflinePlayer sender, ItemStack item) {
        Inventory inventory = createInventory();

        inventory.setItem(13, item);

        ItemStack rejectButton = ItemStack.of(Material.RED_STAINED_GLASS_PANE);
        rejectButton.editMeta(meta -> meta.customName(Component.text("Cancel", NamedTextColor.RED).decoration(TextDecoration.ITALIC, false)));
        inventory.setItem(30, rejectButton);

        ItemStack confirmButton = ItemStack.of(Material.GREEN_STAINED_GLASS_PANE);
        confirmButton.editMeta(meta -> meta.customName(Component.text("Remove mail from " + sender.getName() + " to " + receiver.getName(), NamedTextColor.GREEN).decoration(TextDecoration.ITALIC, false)));
        inventory.setItem(32, confirmButton);

        new ConfirmationInventory(viewer, inventory, Component.text("Would you like to delete mail?"), receiver, sender, item);
    }

    public static void playClickSound(Player player) {
        player.playSound(Sound.sound(Key.key("block.stone_button.click_on"), Sound.Source.PLAYER, 1.0f, 1.0f));
    }

    private static void populateMailInventory(List<Mail> mails, Inventory inventory) {
        for (Mail mail : mails) {
            ItemStack skull = ItemStack.of(Material.PLAYER_HEAD);
            SkullMeta meta = (SkullMeta) skull.getItemMeta();

            OfflinePlayer sender = Bukkit.getOfflinePlayer(mail.sender());
            meta.setOwningPlayer(sender);
            meta.customName(Component.text(sender.getName(), NamedTextColor.GOLD).decoration(TextDecoration.ITALIC, false));

            skull.setItemMeta(meta);

            TooltipDisplay tooltipDisplay = TooltipDisplay.tooltipDisplay().addHiddenComponents(DataComponentTypes.PROFILE).build();
            skull.setData(DataComponentTypes.TOOLTIP_DISPLAY, tooltipDisplay);

            inventory.addItem(skull);
        }
    }

    private static @Nullable Inventory attemptToOpenMailInventory(Player viewer, OfflinePlayer receiver, List<Mail> mails, boolean notify) {
        if (mails.isEmpty()) {
            if (notify) {
                Component text = Component.text(viewer == receiver ? "You do not have any mail." : receiver.getName() + " does not have any mail.", NamedTextColor.RED);
                viewer.sendMessage(text);
            }
            viewer.closeInventory();
            return null;
        }

        return createInventory();
    }

    private static Inventory createInventory() {
        return Bukkit.createInventory(null, 54);
    }
}
