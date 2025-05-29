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

/**
 * Менеджер магических палочек.
 * Отвечает за создание, проверку и управление магическими палочками.
 */
public final class WandManager {
    private static final NamespacedKey WAND_TYPE_KEY = new NamespacedKey(MagicWands.getInstance(), "wand_type");

    /**
     * Типы доступных магических палочек.
     */
    public enum WandType {
        TELEPORT("teleport"),
        EXPLOSION("explosion"),
        LIGHTNING("lightning");

        private final String configKey;

        WandType(String configKey) {
            this.configKey = configKey;
        }

        /**
         * Получает ключ конфигурации для типа палочки.
         *
         * @return Ключ конфигурации
         */
        public String getConfigKey() {
            return configKey;
        }
    }

    private WandManager() {
        throw new IllegalStateException("Utility class");
    }

    /**
     * Создает магическую палочку указанного типа.
     *
     * @param type Тип палочки
     * @return Созданная палочка или null, если тип не найден в конфигурации
     */
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

    /**
     * Проверяет, является ли предмет магической палочкой.
     *
     * @param item Проверяемый предмет
     * @return true, если предмет является магической палочкой
     */
    public static boolean isWand(ItemStack item) {
        if (item == null || item.getType() != Material.BLAZE_ROD) return false;
        ItemMeta meta = item.getItemMeta();
        return meta != null && meta.getPersistentDataContainer().has(WAND_TYPE_KEY, PersistentDataType.STRING);
    }

    /**
     * Получает тип магической палочки.
     *
     * @param item Предмет палочки
     * @return Тип палочки или null, если предмет не является палочкой
     */
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

    /**
     * Получает время перезарядки для типа палочки.
     *
     * @param type Тип палочки
     * @return Время перезарядки в секундах
     */
    public static int getCooldown(WandType type) {
        return ConfigUtil.getWandCooldown(type.getConfigKey());
    }

    /**
     * Получает дальность действия палочки.
     *
     * @param type Тип палочки
     * @return Дальность в блоках
     */
    public static int getRange(WandType type) {
        return ConfigUtil.getWandRange(type.getConfigKey());
    }

    /**
     * Получает сообщение для типа палочки.
     *
     * @param type Тип палочки
     * @return Отформатированное сообщение
     */
    public static String getMessage(WandType type) {
        return ConfigUtil.getWandMessage(type.getConfigKey());
    }

    /**
     * Получает силу взрыва для палочки взрыва.
     *
     * @return Сила взрыва
     */
    public static double getExplosionPower() {
        return ConfigUtil.getExplosionPower();
    }
} 