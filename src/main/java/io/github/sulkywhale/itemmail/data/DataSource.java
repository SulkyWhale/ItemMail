package io.github.sulkywhale.itemmail.data;

import io.github.sulkywhale.itemmail.ItemMail;

public abstract class DataSource {

    protected ItemMail plugin;

    public DataSource(ItemMail plugin) {
        this.plugin = plugin;
    }

    public abstract void loadData();

    public abstract void saveData();
}
