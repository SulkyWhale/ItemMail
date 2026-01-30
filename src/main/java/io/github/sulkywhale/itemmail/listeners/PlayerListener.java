package io.github.sulkywhale.itemmail.listeners;

import io.github.sulkywhale.itemmail.ItemMail;
import io.github.sulkywhale.itemmail.MailManager;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerListener implements Listener {

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        if (!MailManager.getMail(player.getUniqueId()).isEmpty()) {
            Bukkit.getGlobalRegionScheduler().runDelayed(
                    ItemMail.getPlugin(),
                    task -> player.sendMessage(Component.text("You have item mail. Do /itemmail to get it.", NamedTextColor.GOLD)),
                    2L
            );
        }
    }
}
