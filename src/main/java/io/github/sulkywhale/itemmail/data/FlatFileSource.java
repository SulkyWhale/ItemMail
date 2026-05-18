package io.github.sulkywhale.itemmail.data;

import io.github.sulkywhale.itemmail.ItemMail;
import io.github.sulkywhale.itemmail.MailManager;
import io.github.sulkywhale.itemmail.objects.Mail;
import org.bukkit.configuration.MemorySection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

public class FlatFileSource extends DataSource {

    public FlatFileSource(ItemMail plugin) {
        super(plugin);
    }

    @Override
    public void loadData() {
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
                    MailManager.getInstance().addMail(receiver, sender, itemStack);
                }
            }
        }
    }

    @Override
    public void saveData() {
        File file = new File(plugin.getDataFolder(), "data.yml");
        YamlConfiguration config = new YamlConfiguration();
        for (Map.Entry<UUID, List<Mail>> receiver : MailManager.getInstance().getMailMap().entrySet()) {
            Map<UUID, List<ItemStack>> mailLoad = receiver.getValue().stream()
                    .collect(Collectors.groupingBy(
                            Mail::sender,
                            Collectors.mapping(Mail::itemStack, Collectors.toList())
                    ));
            for (Map.Entry<UUID, List<ItemStack>> mail : mailLoad.entrySet()) {
                config.set(receiver.getKey() + "." + mail.getKey(), mail.getValue());
            }
        }
        try {
            config.save(file);
        } catch (IOException e) {
            plugin.getLogger().severe("Failed to save data: " + e.getMessage());
        }
    }
}
