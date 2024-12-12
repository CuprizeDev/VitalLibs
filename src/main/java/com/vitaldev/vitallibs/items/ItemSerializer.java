package com.vitaldev.vitallibs.items;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.Base64;

public class ItemSerializer {

    public static JsonObject serialize(ItemStack item) {
        JsonObject json = new JsonObject();

        if (item == null || item.getType() == Material.AIR) {
            return json;
        }

        json.addProperty("type", item.getType().toString());
        json.addProperty("amount", item.getAmount());
        json.addProperty("durability", item.getDurability());

        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            JsonObject metaJson = new JsonObject();
            if (meta.hasCustomModelData()) {
                metaJson.addProperty("custom_model_data", meta.getCustomModelData());
            }

            PersistentDataContainer container = meta.getPersistentDataContainer();
            JsonObject nbtJson = new JsonObject();
            container.getKeys().forEach(key -> {
                String keyString = key.getNamespace() + ":" + key.getKey();
                nbtJson.addProperty(keyString, container.get(key, PersistentDataType.STRING));
            });

            metaJson.add("nbt", nbtJson);
            json.add("meta", metaJson);
        }

        return json;
    }

    public static ItemStack deserialize(JsonObject json) {
        if (!json.has("type")) {
            return new ItemStack(Material.AIR);
        }

        String typeName = json.get("type").getAsString();
        Material material = Material.matchMaterial(typeName);
        if (material == null) {
            return new ItemStack(Material.AIR);
        }

        int amount = json.has("amount") ? json.get("amount").getAsInt() : 1;
        short durability = json.has("durability") ? json.get("durability").getAsShort() : 0;

        ItemStack item = new ItemStack(material, amount);
        item.setDurability(durability);

        if (json.has("meta")) {
            JsonObject metaJson = json.getAsJsonObject("meta");
            ItemMeta meta = item.getItemMeta();

            if (metaJson.has("custom_model_data")) {
                meta.setCustomModelData(metaJson.get("custom_model_data").getAsInt());
            }

            if (metaJson.has("nbt")) {
                JsonObject nbtJson = metaJson.getAsJsonObject("nbt");
                PersistentDataContainer container = meta.getPersistentDataContainer();
                nbtJson.entrySet().forEach(entry -> {
                    String[] keyParts = entry.getKey().split(":");
                    if (keyParts.length == 2) {
                        NamespacedKey key = new NamespacedKey(keyParts[0], keyParts[1]);
                        container.set(key, PersistentDataType.STRING, entry.getValue().getAsString());
                    }
                });
            }

            item.setItemMeta(meta);
        }

        return item;
    }

    public static String encodeToBase64(ItemStack item) {
        JsonObject json = serialize(item);
        String jsonString = json.toString();
        return Base64.getEncoder().encodeToString(jsonString.getBytes());
    }

    public static ItemStack decodeFromBase64(String base64) {
        byte[] decodedBytes = Base64.getDecoder().decode(base64);
        String jsonString = new String(decodedBytes);
        JsonObject json = JsonParser.parseString(jsonString).getAsJsonObject();
        return deserialize(json);
    }
}