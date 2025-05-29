package me.milkycoding.magicwands;

import org.bukkit.plugin.java.JavaPlugin;
import me.milkycoding.magicwands.commands.MagicWandsCommand;
import me.milkycoding.magicwands.listeners.WandListener;

import java.util.Objects;

/**
 * Главный класс плагина MagicWands.
 * Плагин добавляет в игру магические палочки с различными эффектами.
 *
 * @author MilkyCoding
 * @version 1.0
 */
public final class MagicWands extends JavaPlugin {
    private static MagicWands instance;

    /**
     * Получает экземпляр плагина.
     * Используется для доступа к плагину из других классов.
     *
     * @return Экземпляр плагина
     */
    public static MagicWands getInstance() {
        return instance;
    }

    @Override
    public void onEnable() {
        // Инициализация экземпляра плагина
        instance = this;
        
        // Регистрация команд и их автодополнения
        registerCommands();
        
        // Регистрация слушателей событий
        registerListeners();
        
        // Загрузка конфигурации
        loadConfig();
        
        getLogger().info("MagicWands успешно запущен!");
    }

    @Override
    public void onDisable() {
        getLogger().info("MagicWands выключен!");
    }

    /**
     * Регистрирует команды плагина и их автодополнение.
     */
    private void registerCommands() {
        MagicWandsCommand commandExecutor = new MagicWandsCommand();
        Objects.requireNonNull(getCommand("magicwands")).setExecutor(commandExecutor);
        Objects.requireNonNull(getCommand("magicwands")).setTabCompleter(commandExecutor);
    }

    /**
     * Регистрирует слушатели событий плагина.
     */
    private void registerListeners() {
        getServer().getPluginManager().registerEvents(new WandListener(), this);
    }

    /**
     * Загружает конфигурацию плагина.
     */
    private void loadConfig() {
        saveDefaultConfig();
    }
}
