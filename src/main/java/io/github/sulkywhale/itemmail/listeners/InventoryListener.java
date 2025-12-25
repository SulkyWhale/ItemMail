package io.github.sulkywhale.itemmail.listeners;

import io.github.sulkywhale.itemmail.MailManager;
import io.github.sulkywhale.itemmail.objects.inventories.AdminMailInventory;
import io.github.sulkywhale.itemmail.objects.inventories.ItemMailInventory;
import io.github.sulkywhale.itemmail.objects.inventories.ItemViewInventory;
import io.github.sulkywhale.itemmail.objects.Mail;
import io.github.sulkywhale.itemmail.objects.inventories.MailInventory;
import io.github.sulkywhale.itemmail.utils.GUIUtil;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.Iterator;
import java.util.List;

public class InventoryListener implements Listener {

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (!(event.getInventory().getHolder(false) instanceof ItemMailInventory) || (event.getCurrentItem() == null && event.getHotbarButton() == -1))
            return;

        event.setCancelled(true);

        if (event.getHotbarButton() > -1)
            return;

        Player player = (Player) event.getWhoClicked();
        ItemStack clickedItem = event.getCurrentItem();

        if (event.getInventory().getHolder() instanceof MailInventory) {
            if (clickedItem.getType() == Material.PLAYER_HEAD) {
                GUIUtil.playClickSound(player);
                SkullMeta meta = (SkullMeta) clickedItem.getItemMeta();
                OfflinePlayer sender = meta.getOwningPlayer();
                List<Mail> mails = MailManager.getMail(player.getUniqueId());
                Iterator<Mail> iterator = mails.iterator();
                PlayerInventory inventory = player.getInventory();
                while (iterator.hasNext()) {
                    Mail mail = iterator.next();
                    if (!mail.sender().equals(sender.getUniqueId())) {
                        continue;
                    }
                    for (int i = 0; i < 36; i++) {
                        ItemStack itemSlot = inventory.getItem(i);
                        if (itemSlot == null) {
                            inventory.setItem(i, mail.itemStack());
                            iterator.remove();
                            MailManager.cleanupMail(player.getUniqueId(), mails);
                            player.sendMessage(MiniMessage.miniMessage().deserialize("<gold>Received item <item> from <player>.", Placeholder.component("item", mail.itemStack().displayName()), Placeholder.parsed("player", sender.getName())));
                            if (mails.isEmpty()) {
                                inventory.close();
                            } else {
                                GUIUtil.openMailGUI(player);
                            }
                            break;
                        }
                        if (i == 35) {
                            player.sendMessage(Component.text("You do not have enough space in your inventory! Make space to receive all your item mail.", NamedTextColor.RED));
                            return;
                        }
                    }
                }
            }
        }

        if (event.getInventory().getHolder(false) instanceof AdminMailInventory adminMailInventory) {
            if (clickedItem.getType() == Material.PLAYER_HEAD) {
                GUIUtil.playClickSound(player);
                SkullMeta meta = (SkullMeta) clickedItem.getItemMeta();
                OfflinePlayer target = meta.getOwningPlayer();
                GUIUtil.openItemViewInventory(player, target, adminMailInventory.getReceiver());
            }
        }

        if (event.getInventory().getHolder(false) instanceof ItemViewInventory itemViewInventory) {
            if (clickedItem.getType() == Material.ARROW) {
                GUIUtil.playClickSound(player);
                GUIUtil.openAdminGUI(player, itemViewInventory.getReceiver());
            }
        }
    }
}
