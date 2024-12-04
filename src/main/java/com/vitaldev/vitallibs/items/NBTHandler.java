package com.vitaldev.vitallibs.items;

import com.vitaldev.vitallibs.VitalLibs;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

public class NBTHandler {
    public VitalLibs plugin;
    public NBTHandler(VitalLibs plugin) {
        this.plugin = plugin;
    }

    public String getKey() {
        return "vitallibs_";
    }

    public void addString(ItemStack item, String key, String value) {
        if (item == null || value == null) return;

        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            NamespacedKey namespacedKey = new NamespacedKey(this.plugin, key);
            meta.getPersistentDataContainer().set(namespacedKey, PersistentDataType.STRING, value);
            item.setItemMeta(meta);
        }
    }

    public void addInteger(ItemStack item, String key, int value) {
        if (item == null) return;

        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            NamespacedKey namespacedKey = new NamespacedKey(this.plugin, key);
            meta.getPersistentDataContainer().set(namespacedKey, PersistentDataType.INTEGER, value);
            item.setItemMeta(meta);
        }
    }

    public void addDouble(ItemStack item, String key, double value) {
        if (item == null) return;

        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            NamespacedKey namespacedKey = new NamespacedKey(this.plugin, key);
            meta.getPersistentDataContainer().set(namespacedKey, PersistentDataType.DOUBLE, value);
            item.setItemMeta(meta);
        }
    }

    public void addBoolean(ItemStack item, String key, boolean value) {
        if (item == null) return;

        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            NamespacedKey namespacedKey = new NamespacedKey(this.plugin, key);
            meta.getPersistentDataContainer().set(namespacedKey, PersistentDataType.BYTE, (byte) (value ? 1 : 0));
            item.setItemMeta(meta);
        }
    }

    public String getString(ItemStack item, String key) {
        if (item == null) return null;

        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            NamespacedKey namespacedKey = new NamespacedKey(this.plugin, key);
            return meta.getPersistentDataContainer().get(namespacedKey, PersistentDataType.STRING);
        }
        return null;
    }

    public Integer getInteger(ItemStack item, String key) {
        if (item == null) return null;

        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            NamespacedKey namespacedKey = new NamespacedKey(this.plugin, key);
            return meta.getPersistentDataContainer().get(namespacedKey, PersistentDataType.INTEGER);
        }
        return null;
    }


    public Double getDouble(ItemStack item, String key) {
        if (item == null) return null;

        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            NamespacedKey namespacedKey = new NamespacedKey(this.plugin, key);
            return meta.getPersistentDataContainer().get(namespacedKey, PersistentDataType.DOUBLE);
        }
        return null;
    }

    public Boolean getBoolean(ItemStack item, String key) {
        if (item == null) return null;

        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            NamespacedKey namespacedKey = new NamespacedKey(this.plugin, key);
            Byte value = meta.getPersistentDataContainer().get(namespacedKey, PersistentDataType.BYTE);
            return value != null && value == 1;
        }
        return null;
    }
}
