package com.vitaldev.vitallibs.inventory;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.util.Consumer;

import java.util.*;

public class InventoryBuilder {

    private final Inventory inventory;
    private final Map<Integer, Consumer<InventoryClickEvent>> clickActions = new HashMap<>();

    public InventoryBuilder(int size, String title) {
        this.inventory = Bukkit.createInventory(null, size, title);
    }

    public InventoryBuilder addItem(int slot, ItemStack item, Consumer<InventoryClickEvent> clickAction) {
        inventory.setItem(slot, item);
        if (clickAction != null) {
            clickActions.put(slot, clickAction);
        }
        return this;
    }

    public InventoryBuilder fillWithItem(ItemStack item) {
        for (int i = 0; i < inventory.getSize(); i++) {
            if (inventory.getItem(i) == null || inventory.getItem(i).getType() == Material.AIR) {
                inventory.setItem(i, item);
            }
        }
        return this;
    }

    public InventoryBuilder fillWithCheckeredItem(ItemStack item) {
        for (int i = 0; i < inventory.getSize(); i++) {
            if ((i % 2 == 0) && (inventory.getItem(i) == null || inventory.getItem(i).getType() == Material.AIR)) {
                inventory.setItem(i, item);
            }
        }
        return this;
    }


    public InventoryBuilder fillRowWithItem(ItemStack item, int row) {
        int start = row * 9;
        for (int i = start; i < start + 9; i++) {
            if (inventory.getItem(i) == null || inventory.getItem(i).getType() == Material.AIR) {
                inventory.setItem(i, item);
            }
        }
        return this;
    }

    public InventoryBuilder fillWithBorderItem(ItemStack item) {
        int size = inventory.getSize();
        for (int i = 0; i < size; i++) {
            if (i < 9 || i >= size - 9 || i % 9 == 0 || (i + 1) % 9 == 0) {
                if (inventory.getItem(i) == null || inventory.getItem(i).getType() == Material.AIR) {
                    inventory.setItem(i, item);
                }
            }
        }
        return this;
    }

    public InventoryBuilder fillWithCustomPatternItem(ItemStack item, int[] pattern) {
        for (int slot : pattern) {
            if (slot >= 0 && slot < inventory.getSize()) {
                if (inventory.getItem(slot) == null || inventory.getItem(slot).getType() == Material.AIR) {
                    inventory.setItem(slot, item);
                }
            }
        }
        return this;
    }

    public InventoryBuilder setCloseButton(ItemStack item, Consumer<InventoryClickEvent> clickAction) {
        int size = inventory.getSize();
        int middleBottomSlot = size - 9 + 4; // Center of the last row

        inventory.setItem(middleBottomSlot, item);
        if (clickAction != null) {
            clickActions.put(middleBottomSlot, clickAction);
        }

        return this;
    }

    public InventoryBuilder setItem(int slot, ItemStack item) {
        inventory.setItem(slot, item);
        return this;
    }

    public Inventory build() {
        return inventory;
    }

    public void open(Player player) {
        player.openInventory(inventory);
    }

    public Map<Integer, Consumer<InventoryClickEvent>> getClickActions() {
        return clickActions;
    }

    public static ItemStack createItem(Material material, String name, String... lore) {
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            meta.setDisplayName(name);
            List<String> loreList = new ArrayList<>();
            Collections.addAll(loreList, lore);
            meta.setLore(loreList);
            item.setItemMeta(meta);
        }
        return item;
    }
}
