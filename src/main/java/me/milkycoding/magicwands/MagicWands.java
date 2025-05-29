package me.milkycoding.magicwands;

import org.bukkit.plugin.java.JavaPlugin;
import me.milkycoding.magicwands.commands.MagicWandsCommand;
import me.milkycoding.magicwands.listeners.WandListener;

import java.util.Objects;


public final class MagicWands extends JavaPlugin {
    private static MagicWands instance;


    public static MagicWands getInstance() {
        return instance;
    }

    @Override
    public void onEnable() {
        instance = this;
        
        // Регистрация команд и их автодополнения
        registerCommands();
        
        // Регистрация слушателей событий
        registerListeners();
        
        this.saveDefaultConfig();
        
        getLogger().info("MagicWands успешно запущен!");
    }

    @Override
    public void onDisable() {
        getLogger().info("MagicWands выключен!");
    }

    private void registerCommands() {
        MagicWandsCommand commandExecutor = new MagicWandsCommand();
        Objects.requireNonNull(getCommand("magicwands")).setExecutor(commandExecutor);
        Objects.requireNonNull(getCommand("magicwands")).setTabCompleter(commandExecutor);
    }


    private void registerListeners() {
        getServer().getPluginManager().registerEvents(new WandListener(), this);
    }
}
