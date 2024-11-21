package com.vitaldev.vitallibs.util;

import com.cryptomorin.xseries.XMaterial;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

public class ItemUtil {

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

}
