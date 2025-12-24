package io.github.sulkywhale.itemmail.data;

import io.github.sulkywhale.itemmail.ItemMail;
import io.github.sulkywhale.itemmail.objects.Mail;
import io.github.sulkywhale.itemmail.MailManager;
import org.bukkit.configuration.MemorySection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

public class DataSource {

    private static ItemMail plugin;

    public static void init(ItemMail plugin) {
        DataSource.plugin = plugin;
        plugin.getServer().getScheduler().runTaskAsynchronously(plugin, DataSource::loadData);
    }

    public static void saveData() throws IOException {
        File file = new File(plugin.getDataFolder(), "data.yml");
        YamlConfiguration config = new YamlConfiguration();
        Set<Map.Entry<UUID, List<Mail>>> mails = MailManager.getMailMap().entrySet();
        for (Map.Entry<UUID, List<Mail>> receiver : mails) {
            Map<UUID, List<ItemStack>> mailLoad = receiver.getValue().stream()
                    .collect(Collectors.groupingBy(
                            Mail::sender,
                            Collectors.mapping(Mail::itemStack, Collectors.toList())
                    ));
            for (Map.Entry<UUID, List<ItemStack>> mail : mailLoad.entrySet()) {
                config.set(receiver.getKey() + "." + mail.getKey(), mail.getValue());
            }
        }
        config.save(file);
    }

    public static void loadData() {
        File file = new File(plugin.getDataFolder(), "data.yml");
        YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
        for (Map.Entry<String, Object> value : config.getValues(false).entrySet()) {
            MemorySection memorySection = (MemorySection) value.getValue();
            UUID receiver = UUID.fromString(value.getKey());
            LinkedHashMap<String, Object> map = (LinkedHashMap<String, Object>) memorySection.getValues(false);
            for (String key : map.keySet()) {
                UUID sender = UUID.fromString(key);
                ArrayList<ItemStack> itemStacks = (ArrayList<ItemStack>) config.getList(value.getKey() + "." + key);
                for (ItemStack itemStack : itemStacks) {
                    MailManager.addMail(receiver, sender, itemStack);
                }
            }
        }
    }
}
