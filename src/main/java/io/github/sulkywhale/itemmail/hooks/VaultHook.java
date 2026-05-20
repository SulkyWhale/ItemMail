package io.github.sulkywhale.itemmail.hooks;

import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.economy.EconomyResponse;
import org.bukkit.entity.Player;

public class VaultHook {

    private static Economy economy;

    public static void setEconomy(Economy economy) {
        VaultHook.economy = economy;
    }

    public static boolean isEnabled() {
        return economy != null;
    }

    public static EconomyResponse withdraw(Player player, double amount) {
        return economy.withdrawPlayer(player, amount);
    }
}
