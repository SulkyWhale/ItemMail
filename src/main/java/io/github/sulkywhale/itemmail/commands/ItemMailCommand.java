package io.github.sulkywhale.itemmail.commands;

import io.github.sulkywhale.itemmail.MailManager;
import io.github.sulkywhale.itemmail.utils.GUIUtil;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;

public class ItemMailCommand implements TabExecutor {

    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (args.length == 1) {
            if ("send".startsWith(args[0])) {
                return List.of("send");
            } else if (sender.hasPermission("itemmail.admin")) {
                return matchPlayers(args[0]);
            }
        }
        if (args.length == 2) {
            return matchPlayers(args[1]);
        }
        return Collections.emptyList();
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage(Component.text("Only players can execute that command.", NamedTextColor.RED));
            return true;
        }

        if (args.length == 0) {
            if (!player.hasPermission("itemmail.mail.gui")) {
                player.sendMessage(Component.text("You do not have permission!", NamedTextColor.RED));
                return true;
            }
            GUIUtil.openMailGUI(player);
            return true;
        }

        if (args[0].equalsIgnoreCase("send")) {
            if (args.length != 2) {
                player.sendMessage(Component.text("Usage: /itemmail send {player}", NamedTextColor.GOLD));
                return true;
            }
            if (!player.hasPermission("itemmail.mail.send")) {
                player.sendMessage(Component.text("You do not have permission!", NamedTextColor.RED));
                return true;
            }
            OfflinePlayer receiver = Bukkit.getOfflinePlayer(args[1]);
            if (!receiver.hasPlayedBefore()) {
                player.sendMessage(Component.text("That player does not exist!", NamedTextColor.RED));
                return true;
            }
            if (receiver.equals(player)) {
                player.sendMessage(Component.text("You cannot send mail to yourself!", NamedTextColor.RED));
                return true;
            }

            ItemStack itemStack = player.getInventory().getItemInMainHand();
            if (itemStack.getType() == Material.AIR) {
                player.sendMessage(Component.text("You are not holding an item in your hand.", NamedTextColor.GOLD));
                return true;
            }
            MailManager.addMail(receiver.getUniqueId(), player.getUniqueId(), itemStack);
            player.getInventory().clear(player.getInventory().getHeldItemSlot());
            player.sendMessage(MiniMessage.miniMessage().deserialize("<gold>Sent item <item> to <player>.", Placeholder.component("item", itemStack.displayName()), Placeholder.unparsed("player", receiver.getName())));
            Player receiverPlayer = receiver.getPlayer();
            if (receiverPlayer != null) {
                receiverPlayer.sendMessage(MiniMessage.miniMessage().deserialize("<gold>You received item mail from <player>. Do /itemmail to get it.", Placeholder.unparsed("player", player.getName())));
            }
            return true;
        }

        if (args.length == 1) {
            if (!player.hasPermission("itemmail.admin")) {
                player.sendMessage(Component.text("You do not have permission!", NamedTextColor.RED));
                return true;
            }
            OfflinePlayer target = Bukkit.getOfflinePlayer(args[0]);
            if (!target.hasPlayedBefore()) {
                player.sendMessage(Component.text("That player does not exist!", NamedTextColor.RED));
                return true;
            }

            GUIUtil.openAdminGUI(player, target);
            return true;
        }

        player.sendMessage(Component.text("Usage: /itemmail send {player}", NamedTextColor.GOLD));
        return true;
    }

    private static List<String> matchPlayers(String arg) {
        return Bukkit.getOnlinePlayers().stream().map(Player::getName).filter(name -> name.toLowerCase().startsWith(arg.toLowerCase())).toList();
    }
}
