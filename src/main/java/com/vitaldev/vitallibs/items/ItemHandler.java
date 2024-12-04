package com.vitaldev.vitallibs.items;

import com.cryptomorin.xseries.XMaterial;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;
import java.util.Map;

public class ItemHandler {

    public static ItemStack buildItem(Material material, String name, int amount, List<String> lore, boolean glow) {
        ItemStack itemStack = XMaterial.matchXMaterial(material).parseItem();
        itemStack.setAmount(amount);
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName(name);
        itemMeta.setLore(lore);
        if (glow) {
            itemMeta.addEnchant(Enchantment.LURE, 1, true);
            itemMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
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
