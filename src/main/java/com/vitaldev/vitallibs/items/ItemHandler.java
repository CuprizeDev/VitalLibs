package com.vitaldev.vitallibs.items;

import com.vitaldev.vitallibs.util.ChatUtil;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

public class ItemHandler {

    public static ItemStack buildItem(Material material, String name, int amount, List<String> lore, boolean glow, boolean hideAttributes) {
        ItemStack itemStack = new ItemStack(material);
        itemStack.setAmount(amount);
        ItemMeta itemMeta = itemStack.getItemMeta();
        Objects.requireNonNull(itemMeta).setDisplayName(ChatUtil.color(name));
        itemMeta.setLore(lore.stream().map(ChatUtil::color).collect(Collectors.toList()));
        if (glow) {
            itemMeta.addEnchant(Enchantment.LURE, 1, true);
            itemMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        }

        if (hideAttributes) {
            itemMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
            itemMeta.addItemFlags(ItemFlag.HIDE_ADDITIONAL_TOOLTIP);
        }

        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }

    public static ItemStack addEnchantments(ItemStack item, Map<Enchantment, Integer> enchantments) {
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            for (Map.Entry<Enchantment, Integer> entry : enchantments.entrySet()) {
                meta.addEnchant(entry.getKey(), entry.getValue(), true);
            }
            item.setItemMeta(meta);
        }
        return item;
    }

    public static ItemStack addEnchantment(ItemStack item, Enchantment enchantment, int level) {
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
                meta.addEnchant(enchantment, level, true);
            item.setItemMeta(meta);
        }
        return item;
    }

    public static ItemStack removeEnchantment(ItemStack item, Enchantment enchantment) {
        ItemMeta meta = item.getItemMeta();
        if (meta != null && meta.hasEnchant(enchantment)) {
            meta.removeEnchant(enchantment);
            item.setItemMeta(meta);
        }
        return item;
    }

    public static boolean isEmpty(ItemStack item) {
        return item == null || item.getType() == Material.AIR;
    }
}
