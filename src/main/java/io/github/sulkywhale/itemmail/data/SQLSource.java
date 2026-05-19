package io.github.sulkywhale.itemmail.data;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import io.github.sulkywhale.itemmail.ItemMail;
import io.github.sulkywhale.itemmail.MailManager;
import io.github.sulkywhale.itemmail.config.Config;
import io.github.sulkywhale.itemmail.objects.Mail;
import org.bukkit.inventory.ItemStack;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

public class SQLSource extends DataSource {

    private final HikariDataSource hikariDataSource;

    public SQLSource(ItemMail plugin) {
        super(plugin);
        HikariConfig hikariConfig = new HikariConfig();
        hikariConfig.setPoolName("ItemMail MySQL");
        hikariConfig.setDriverClassName("com.mysql.cj.jdbc.Driver");
        hikariConfig.setJdbcUrl("jdbc:mysql://" + Config.getDatabaseHost() + ":" + Config.getDatabasePort() + "/" + Config.getDatabaseName());
        hikariConfig.setUsername(Config.getDatabaseUsername());
        hikariConfig.setPassword(Config.getDatabasePassword());

        this.hikariDataSource = new HikariDataSource(hikariConfig);

        initTables();
    }

    @Override
    public void loadData() {
        try (Connection connection = hikariDataSource.getConnection()) {
            PreparedStatement ps = connection.prepareStatement("SELECT * FROM " + Config.getDatabaseTablePrefix() + "mail;");
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                try {
                    UUID receiver = UUID.fromString(rs.getString("receiver"));
                    UUID sender = UUID.fromString(rs.getString("sender"));
                    ItemStack[] itemStacks = ItemStack.deserializeItemsFromBytes(rs.getBytes("itemstack"));
                    for (ItemStack itemStack : itemStacks) {
                        MailManager.getInstance().addMail(receiver, sender, itemStack);
                    }
                } catch (SQLException e) {
                    plugin.getLogger().severe("An error occurred when attempting to fetch item mail receiver from the database." + e);
                }

            }
        } catch (SQLException e) {
            plugin.getLogger().severe("An error occurred when attempting to connect to the database." + e);
        }
    }

    @Override
    public void saveData() {
        try (Connection connection = hikariDataSource.getConnection()) {
            PreparedStatement ps = connection.prepareStatement("REPLACE INTO " + Config.getDatabaseTablePrefix() + "mail (receiver, sender, itemstack) VALUES (?, ?, ?);");
            for (Map.Entry<UUID, List<Mail>> receiver : MailManager.getInstance().getMailMap().entrySet()) {
                Map<UUID, List<ItemStack>> mailLoad = receiver.getValue().stream()
                        .collect(Collectors.groupingBy(
                                Mail::sender,
                                Collectors.mapping(Mail::itemStack, Collectors.toList())
                        ));
                for (Map.Entry<UUID, List<ItemStack>> mail : mailLoad.entrySet()) {
                    ps.setString(1, String.valueOf(receiver.getKey()));
                    ps.setString(2, String.valueOf(mail.getKey()));
                    ps.setBytes(3, ItemStack.serializeItemsAsBytes(mail.getValue()));
                    ps.addBatch();
                }
            }
            ps.executeBatch();
        } catch (SQLException e) {
            plugin.getLogger().severe("An error occurred when attempting to connect to the database." + e);
        }
    }

    private void initTables() {
        try (Connection connection = hikariDataSource.getConnection()) {
            PreparedStatement ps = connection.prepareStatement("CREATE TABLE IF NOT EXISTS " + Config.getDatabaseTablePrefix() + "mail (receiver VARCHAR(36), sender VARCHAR(36), itemstack BLOB, PRIMARY KEY (receiver, sender));");
            ps.executeUpdate();
        } catch (SQLException e) {
            plugin.getLogger().severe("An error occurred when attempting to connect to the database." + e);
        }
    }
}
