package com.vitaldev.vitallibs.inventory;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.function.Consumer;

public class InventoryHandler implements Listener {
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

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        InventoryHolder holder = event.getInventory().getHolder();
        if (holder instanceof InventoryBuilder.CustomInventoryHolder customHolder) {
            event.setCancelled(true);

            int slot = event.getRawSlot();

            Map<Integer, Consumer<InventoryClickEvent>> clickActions = customHolder.clickActions();
            Consumer<InventoryClickEvent> clickAction = clickActions.get(slot);

            if (clickAction != null) {
                clickAction.accept(event);
            }
        }
    }
}
