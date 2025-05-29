package me.milkycoding.magicwands.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import me.milkycoding.magicwands.wands.WandManager;
import me.milkycoding.magicwands.wands.WandManager.WandType;
import me.milkycoding.magicwands.util.ConfigUtil;
import me.milkycoding.magicwands.util.ChatUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class MagicWandsCommand implements CommandExecutor, TabCompleter {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ConfigUtil.getPrefixedMessage("player-only"));
            return true;
        }

        Player player = (Player) sender;

        if (!player.hasPermission("magicwands.admin")) {
            player.sendMessage(ConfigUtil.getPrefixedMessage("no-permission"));
            return true;
        }

        if (args.length == 0) {
            sendHelp(player);
            return true;
        }

        switch (args[0].toLowerCase()) {
            case "give":
                handleGiveCommand(player, args);
                break;
            case "help":
                sendHelp(player);
                break;
            default:
                player.sendMessage(ConfigUtil.getPrefixedMessage("unknown-command"));
        }

        return true;
    }

    private void handleGiveCommand(Player player, String[] args) {
        if (args.length < 3) {
            player.sendMessage(ConfigUtil.getPrefixedMessage("give-usage"));
            return;
        }

        Player target = Bukkit.getPlayer(args[1]);
        if (target == null) {
            player.sendMessage(ConfigUtil.getPrefixedMessage("player-not-found"));
            return;
        }

        try {
            WandType type = WandType.valueOf(args[2].toUpperCase());
            target.getInventory().addItem(WandManager.createWand(type));
            player.sendMessage(ConfigUtil.getPrefixedMessage("wand-given", "%player%", target.getName()));
            target.sendMessage(ConfigUtil.getPrefixedMessage("wand-received"));
        } catch (IllegalArgumentException e) {
            player.sendMessage(ConfigUtil.getPrefixedMessage("unknown-wand-type", 
                "%types%", java.util.Arrays.stream(WandType.values())
                    .map(Enum::name)
                    .collect(Collectors.joining(", "))));
        }
    }

    private void sendHelp(Player player) {
        List<String> helpMessages = ConfigUtil.getConfig().getStringList("messages.help");
        for (String message : helpMessages) {
            player.sendMessage(ChatUtil.colorize(message));
        }
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        List<String> completions = new ArrayList<>();

        if (args.length == 1) {
            // Завершение для первой подкоманды
            completions.add("give");
            completions.add("help");
        } else if (args.length == 2 && args[0].equalsIgnoreCase("give")) {
            // Завершение для имени игрока после 'give'
            for (Player player : Bukkit.getOnlinePlayers()) {
                completions.add(player.getName());
            }
        } else if (args.length == 3 && args[0].equalsIgnoreCase("give")) {
            // Завершение для типа палочки после 'give <игрок>'
            for (WandType type : WandType.values()) {
                completions.add(type.name());
            }
        }

        // Фильтруем результаты по введенному тексту
        return completions.stream()
                .filter(s -> s.startsWith(args[args.length - 1]))
                .collect(Collectors.toList());
    }
} 