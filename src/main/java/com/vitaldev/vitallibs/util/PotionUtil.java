package com.vitaldev.vitallibs.util;

import org.bukkit.Material;
import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.List;

public class PotionUtil {

    public static ItemStack createPotion(String name, PotionEffectType effect, int duration, int amplifier) {
        PotionMeta potionMeta = (PotionMeta) new ItemStack(Material.POTION).getItemMeta();
        potionMeta.setDisplayName(name);
        potionMeta.addCustomEffect(new PotionEffect(effect, duration, amplifier), true);
        ItemStack potion = new ItemStack(Material.POTION);
        potion.setItemMeta(potionMeta);
        return potion;
    }

    public static void applyPotionEffect(LivingEntity entity, PotionEffectType effect, int duration, int amplifier) {
        entity.addPotionEffect(new PotionEffect(effect, duration, amplifier));
    }

    public static void removePotionEffect(LivingEntity entity, PotionEffectType effect) {
        entity.removePotionEffect(effect);
    }

    public static ItemStack createCustomPotion(String name, List<PotionEffect> effects) {
        ItemStack potion = new ItemStack(Material.POTION);
        PotionMeta potionMeta = (PotionMeta) potion.getItemMeta();
        potionMeta.setDisplayName(name);
        for (PotionEffect effect : effects) {
            potionMeta.addCustomEffect(effect, true);
        }
        potion.setItemMeta(potionMeta);
        return potion;
    }

    public static void applyEffects(LivingEntity entity, List<PotionEffect> effects) {
        for (PotionEffect effect : effects) {
            entity.addPotionEffect(effect);
        }
    }

    public static void removeAllPotionEffects(LivingEntity entity) {
        entity.getActivePotionEffects().forEach(effect -> entity.removePotionEffect(effect.getType()));
    }

    public static String serializePotion(ItemStack potion) {
        PotionMeta meta = (PotionMeta) potion.getItemMeta();
        StringBuilder serialized = new StringBuilder(potion.getType().toString() + ":");
        if (meta != null) {
            serialized.append(meta.getDisplayName()).append(":");
            meta.getCustomEffects().forEach(effect ->
                    serialized.append(effect.getType().getName())
                            .append(",")
                            .append(effect.getDuration())
                            .append(",")
                            .append(effect.getAmplifier())
                            .append(";"));
        }
        return serialized.toString();
    }

    public static ItemStack deserializePotion(String serialized) {
        String[] parts = serialized.split(":");
        Material material = Material.valueOf(parts[0]);
        String name = parts[1];
        List<PotionEffect> effects = new ArrayList<>();
        if (parts.length > 2) {
            String[] effectParts = parts[2].split(";");
            for (String effect : effectParts) {
                String[] effectDetails = effect.split(",");
                PotionEffectType effectType = PotionEffectType.getByName(effectDetails[0]);
                int duration = Integer.parseInt(effectDetails[1]);
                int amplifier = Integer.parseInt(effectDetails[2]);
                effects.add(new PotionEffect(effectType, duration, amplifier));
            }
        }
        ItemStack potion = new ItemStack(material);
        PotionMeta meta = (PotionMeta) potion.getItemMeta();
        meta.setDisplayName(name);
        for (PotionEffect effect : effects) {
            meta.addCustomEffect(effect, true);  // Correct method for adding effects in 1.20
        }
        potion.setItemMeta(meta);
        return potion;
    }
}
