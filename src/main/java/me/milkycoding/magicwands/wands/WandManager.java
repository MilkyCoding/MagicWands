package me.milkycoding.magicwands.wands;

import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.NamespacedKey;
import me.milkycoding.magicwands.MagicWands;
import me.milkycoding.magicwands.util.ChatUtil;
import me.milkycoding.magicwands.util.ConfigUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


public final class WandManager {
    private static final NamespacedKey WAND_TYPE_KEY = new NamespacedKey(MagicWands.getInstance(), "wand_type");

    public enum WandType {
        TELEPORT("teleport"),
        EXPLOSION("explosion"),
        LIGHTNING("lightning");

        private final String configKey;

        WandType(String configKey) {
            this.configKey = configKey;
        }

        public String getConfigKey() {
            return configKey;
        }
    }

    private WandManager() {
        throw new IllegalStateException("Utility class");
    }

    public static ItemStack createWand(WandType type) {
        ConfigurationSection config = ConfigUtil.getWandSection(type.getConfigKey());
        if (config == null) return null;

        ItemStack wand = new ItemStack(Material.BLAZE_ROD);
        ItemMeta meta = wand.getItemMeta();

        Objects.requireNonNull(meta, "ItemMeta не может быть null для BLAZE_ROD");
        meta.setDisplayName(ConfigUtil.getWandName(type.getConfigKey()));
        
        List<String> lore = new ArrayList<>();
        for (String line : config.getStringList("lore")) {
            lore.add(ChatUtil.colorize(line));
        }
        meta.setLore(lore);
        
        meta.getPersistentDataContainer().set(WAND_TYPE_KEY, PersistentDataType.STRING, type.name());
        
        wand.setItemMeta(meta);
        return wand;
    }


    public static boolean isWand(ItemStack item) {
        if (item == null || item.getType() != Material.BLAZE_ROD) return false;
        ItemMeta meta = item.getItemMeta();
        return meta != null && meta.getPersistentDataContainer().has(WAND_TYPE_KEY, PersistentDataType.STRING);
    }

    public static WandType getWandType(ItemStack item) {
        if (!isWand(item)) return null;
        String typeStr = Objects.requireNonNull(item.getItemMeta())
            .getPersistentDataContainer()
            .get(WAND_TYPE_KEY, PersistentDataType.STRING);
        try {
            return WandType.valueOf(typeStr);
        } catch (IllegalArgumentException e) {
            return null;
        }
    }


    public static int getCooldown(WandType type) {
        return ConfigUtil.getWandCooldown(type.getConfigKey());
    }


    public static int getRange(WandType type) {
        return ConfigUtil.getWandRange(type.getConfigKey());
    }


    public static String getMessage(WandType type) {
        return ConfigUtil.getWandMessage(type.getConfigKey());
    }


    public static double getExplosionPower() {
        return ConfigUtil.getExplosionPower();
    }
} 