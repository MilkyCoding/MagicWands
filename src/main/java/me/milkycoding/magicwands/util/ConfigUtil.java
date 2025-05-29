package me.milkycoding.magicwands.util;

import me.milkycoding.magicwands.MagicWands;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

/**
 * Утилита для работы с конфигурацией плагина.
 * Предоставляет удобные методы для получения настроек.
 */
public final class ConfigUtil {
    private static final String WANDS_PATH = "wands";
    private static final String MESSAGES_PATH = "messages";

    private ConfigUtil() {
        throw new IllegalStateException("Utility class");
    }

    /**
     * Получает конфигурацию плагина.
     *
     * @return Конфигурация плагина
     */
    public static FileConfiguration getConfig() {
        return MagicWands.getInstance().getConfig();
    }

    /**
     * Получает секцию конфигурации для указанного типа палочки.
     *
     * @param wandType Тип палочки
     * @return Секция конфигурации или null, если секция не найдена
     */
    public static ConfigurationSection getWandSection(String wandType) {
        return getConfig().getConfigurationSection(WANDS_PATH + "." + wandType);
    }

    /**
     * Получает название палочки из конфигурации.
     *
     * @param wandType Тип палочки
     * @return Название палочки или значение по умолчанию
     */
    public static String getWandName(String wandType) {
        return ChatUtil.colorize(getConfig().getString(WANDS_PATH + "." + wandType + ".name", "&6Магическая палочка"));
    }

    /**
     * Получает время перезарядки палочки.
     *
     * @param wandType Тип палочки
     * @return Время перезарядки в секундах
     */
    public static int getWandCooldown(String wandType) {
        return getConfig().getInt(WANDS_PATH + "." + wandType + ".cooldown", 5);
    }

    /**
     * Получает дальность действия палочки.
     *
     * @param wandType Тип палочки
     * @return Дальность в блоках
     */
    public static int getWandRange(String wandType) {
        return getConfig().getInt(WANDS_PATH + "." + wandType + ".range", 50);
    }

    /**
     * Получает сообщение для палочки.
     *
     * @param wandType Тип палочки
     * @return Отформатированное сообщение
     */
    public static String getWandMessage(String wandType) {
        return ChatUtil.colorize(getConfig().getString(WANDS_PATH + "." + wandType + ".message", ""));
    }

    /**
     * Получает силу взрыва для палочки взрыва.
     *
     * @return Сила взрыва
     */
    public static double getExplosionPower() {
        return getConfig().getDouble(WANDS_PATH + ".explosion.power", 2.0);
    }

    /**
     * Получает сообщение из конфигурации.
     *
     * @param path Путь к сообщению
     * @return Отформатированное сообщение
     */
    public static String getMessage(String path) {
        return ChatUtil.colorize(getConfig().getString(MESSAGES_PATH + "." + path, ""));
    }

    /**
     * Получает сообщение с префиксом.
     *
     * @param path Путь к сообщению
     * @return Отформатированное сообщение с префиксом
     */
    public static String getPrefixedMessage(String path) {
        String prefix = getConfig().getString(MESSAGES_PATH + ".prefix", "&8[&6MagicWands&8] &r");
        return ChatUtil.colorize(prefix + getConfig().getString(MESSAGES_PATH + "." + path, ""));
    }

    /**
     * Получает сообщение с префиксом и заменяет плейсхолдеры.
     *
     * @param path Путь к сообщению
     * @param replacements Пары "плейсхолдер-значение" для замены
     * @return Отформатированное сообщение с префиксом и замененными плейсхолдерами
     */
    public static String getPrefixedMessage(String path, String... replacements) {
        String message = getPrefixedMessage(path);
        for (int i = 0; i < replacements.length; i += 2) {
            if (i + 1 < replacements.length) {
                message = message.replace(replacements[i], replacements[i + 1]);
            }
        }
        return message;
    }
} 