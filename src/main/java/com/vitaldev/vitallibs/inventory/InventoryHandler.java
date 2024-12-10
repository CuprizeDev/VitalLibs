package com.vitaldev.vitallibs.inventory;

import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class InventoryHandler {
    private final Map<UUID, Inventory> playerInventories = new HashMap<>();

    public void openInventory(Player player, Inventory inventory) {
        player.openInventory(inventory);
        playerInventories.put(player.getUniqueId(), inventory);
    }

    public Inventory getPlayerInventory(Player player) {
        return playerInventories.get(player.getUniqueId());
    }

    public void closeInventory(Player player) {
        if (playerInventories.containsKey(player.getUniqueId())) {
            player.closeInventory();
            playerInventories.remove(player.getUniqueId());
        }
    }

    public boolean isPlayerInventoryOpen(Player player) {
        return playerInventories.containsKey(player.getUniqueId());
    }

    public void clearAllInventories() {
        playerInventories.clear();
    }
}
