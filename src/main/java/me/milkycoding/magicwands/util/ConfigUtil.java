package me.milkycoding.magicwands.util;

import me.milkycoding.magicwands.MagicWands;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

public final class ConfigUtil {
    private static final String WANDS_PATH = "wands";
    private static final String MESSAGES_PATH = "messages";

    private ConfigUtil() {
        throw new IllegalStateException("Utility class");
    }

    public static FileConfiguration getConfig() {
        return MagicWands.getInstance().getConfig();
    }

    public static ConfigurationSection getWandSection(String wandType) {
        return getConfig().getConfigurationSection(WANDS_PATH + "." + wandType);
    }

    public static String getWandName(String wandType) {
        return ChatUtil.colorize(getConfig().getString(WANDS_PATH + "." + wandType + ".name", "&6Магическая палочка"));
    }

    public static int getWandCooldown(String wandType) {
        return getConfig().getInt(WANDS_PATH + "." + wandType + ".cooldown", 5);
    }

    public static int getWandRange(String wandType) {
        return getConfig().getInt(WANDS_PATH + "." + wandType + ".range", 50);
    }

    public static String getWandMessage(String wandType) {
        return ChatUtil.colorize(getConfig().getString(WANDS_PATH + "." + wandType + ".message", ""));
    }

    public static double getExplosionPower() {
        return getConfig().getDouble(WANDS_PATH + ".explosion.power", 2.0);
    }

    public static String getMessage(String path) {
        return ChatUtil.colorize(getConfig().getString(MESSAGES_PATH + "." + path, ""));
    }

    public static String getPrefixedMessage(String path) {
        String prefix = getConfig().getString(MESSAGES_PATH + ".prefix", "&8[&6MagicWands&8] &r");
        return ChatUtil.colorize(prefix + getConfig().getString(MESSAGES_PATH + "." + path, ""));
    }

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