package com.vitaldev.vitallibs.util;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import java.lang.reflect.Field;
import java.util.UUID;

public class SkullUtil {

    public static ItemStack getPlayerSkull(String playerName) {
        ItemStack skull = new ItemStack(Material.PLAYER_HEAD);
        SkullMeta meta = (SkullMeta) skull.getItemMeta();
        if (meta != null) {
            meta.setOwnerProfile(Bukkit.createPlayerProfile(UUID.nameUUIDFromBytes(playerName.getBytes())));
            skull.setItemMeta(meta);
        }
        return skull;
    }

    public static ItemStack getCustomSkull(String textureBase64) {
        ItemStack skull = new ItemStack(Material.PLAYER_HEAD);
        SkullMeta meta = (SkullMeta) skull.getItemMeta();
        if (meta != null) {
            try {
                Object profile = Class.forName("com.mojang.authlib.GameProfile")
                        .getConstructor(UUID.class, String.class)
                        .newInstance(UUID.randomUUID(), null);

                Object propertyMap = profile.getClass().getMethod("getProperties").invoke(profile);
                Object property = Class.forName("com.mojang.authlib.properties.Property")
                        .getConstructor(String.class, String.class)
                        .newInstance("textures", textureBase64);

                propertyMap.getClass().getMethod("put", Object.class, Object.class)
                        .invoke(propertyMap, "textures", property);

                Field profileField = meta.getClass().getDeclaredField("profile");
                profileField.setAccessible(true);
                profileField.set(meta, profile);
                skull.setItemMeta(meta);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return skull;
    }
}
